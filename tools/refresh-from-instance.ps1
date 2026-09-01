param(
    [Parameter(Mandatory = $true)]
    [string]$InstanceRoot,

    [Parameter(Mandatory = $true)]
    [string]$MachineName
)

$ErrorActionPreference = 'Stop'
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$instanceRootResolved = (Resolve-Path -LiteralPath $InstanceRoot).Path

if (-not (Test-Path -LiteralPath (Join-Path $repoRoot '.git'))) {
    throw "Repository root is invalid: $repoRoot"
}

$instanceMetadata = Join-Path $instanceRootResolved 'minecraftinstance.json'
if (-not (Test-Path -LiteralPath $instanceMetadata)) {
    throw "CurseForge instance metadata was not found: $instanceMetadata"
}

foreach ($folder in @('config', 'defaultconfigs', 'kubejs')) {
    $source = Join-Path $instanceRootResolved $folder
    $destination = Join-Path $repoRoot $folder
    if (Test-Path -LiteralPath $source) {
        New-Item -ItemType Directory -Path $destination -Force | Out-Null
        Copy-Item -Path (Join-Path $source '*') -Destination $destination -Recurse -Force
    }
}

# Remove generated caches/backups copied from the runtime instance. These are
# machine-local artifacts and are intentionally excluded from the shared pack.
$generatedArtifacts = @(
    (Join-Path $repoRoot 'config\worldedit\.archive-unpack'),
    (Join-Path $repoRoot 'config\emi-accelerator\stack-cache.emic'),
    (Join-Path $repoRoot 'config\embeddium-fingerprint.json'),
    (Join-Path $repoRoot 'config\sodium-fingerprint.json'),
    (Join-Path $repoRoot 'config\buildinggadgets2gui-history.dat'),
    (Join-Path $repoRoot 'config\ars_nouveau\search_index'),
    (Join-Path $repoRoot 'config\emi-accelerator\reload-timings.json'),
    (Join-Path $repoRoot 'config\spark\tmp'),
    (Join-Path $repoRoot 'config\spark\tmp-client'),
    (Join-Path $repoRoot 'config\worldedit\sessions'),
    (Join-Path $repoRoot 'kubejs\config\web_server.json')
)
foreach ($artifact in $generatedArtifacts) {
    if (Test-Path -LiteralPath $artifact) {
        Remove-Item -LiteralPath $artifact -Recurse -Force
    }
}
Get-ChildItem -LiteralPath (Join-Path $repoRoot 'config') -File -Recurse -Force |
    Where-Object { $_.Name -match '\.bak$|_backup\d*$' } |
    Remove-Item -Force

$modsSource = Join-Path $instanceRootResolved 'mods'
$modsDestination = Join-Path $repoRoot 'mods'
New-Item -ItemType Directory -Path $modsDestination -Force | Out-Null

$customPatterns = @('NewWorldCore-*.jar', 'DoctorWhoMod-*.jar')
foreach ($pattern in $customPatterns) {
    $matches = @(Get-ChildItem -Path (Join-Path $modsSource $pattern) -File)
    if ($matches.Count -ne 1) {
        throw "Expected exactly one custom mod matching $pattern, found $($matches.Count)."
    }
    Copy-Item -LiteralPath $matches[0].FullName -Destination $modsDestination -Force
}

$instance = Get-Content -LiteralPath $instanceMetadata -Raw | ConvertFrom-Json
$addons = @($instance.installedAddons | Sort-Object addonID, { $_.installedFile.id })

$manifestFiles = @($addons | ForEach-Object {
    [ordered]@{
        projectID = [int64]$_.addonID
        fileID = [int64]$_.installedFile.id
        required = [bool]$_.isEnabled
    }
})

$manifest = [ordered]@{
    minecraft = [ordered]@{
        version = $instance.gameVersion
        modLoaders = @([ordered]@{
            id = $instance.baseModLoader.name
            primary = $true
        })
    }
    manifestType = 'minecraftModpack'
    manifestVersion = 1
    name = 'New World'
    version = 'development'
    author = 'reddragon077'
    files = $manifestFiles
    overrides = 'overrides'
}

$manifest | ConvertTo-Json -Depth 8 | Set-Content -LiteralPath (Join-Path $repoRoot 'manifest.json') -Encoding utf8

$lockAddons = @($addons | ForEach-Object {
    $sha1 = @($_.installedFile.hashes | Where-Object type -eq 1 | Select-Object -First 1).value
    [ordered]@{
        name = $_.name
        category = $_.categorySection.path
        fileName = $_.fileNameOnDisk
        projectID = [int64]$_.addonID
        fileID = [int64]$_.installedFile.id
        enabled = [bool]$_.isEnabled
        sha1 = $sha1
    }
})

$customMods = @(foreach ($pattern in $customPatterns) {
    $file = Get-ChildItem -Path (Join-Path $modsDestination $pattern) -File | Select-Object -First 1
    [ordered]@{
        fileName = $file.Name
        sha256 = (Get-FileHash -LiteralPath $file.FullName -Algorithm SHA256).Hash.ToLowerInvariant()
        size = $file.Length
    }
})

$lock = [ordered]@{
    schemaVersion = 1
    sourceMachine = $MachineName
    minecraftVersion = $instance.gameVersion
    modLoader = $instance.baseModLoader.name
    addons = $lockAddons
    customMods = $customMods
}

$lock | ConvertTo-Json -Depth 8 | Set-Content -LiteralPath (Join-Path $repoRoot 'pack-lock.json') -Encoding utf8

Write-Host "Repository refreshed from $instanceRootResolved"
Write-Host 'Review changes with git status before committing.'
