param(
    [Parameter(Mandatory = $true)]
    [string]$InstanceRoot
)

$ErrorActionPreference = 'Stop'
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$instanceRootResolved = (Resolve-Path -LiteralPath $InstanceRoot).Path

if (-not (Test-Path -LiteralPath (Join-Path $repoRoot '.git'))) {
    throw "Repository root is invalid: $repoRoot"
}

if (-not (Test-Path -LiteralPath (Join-Path $instanceRootResolved 'minecraftinstance.json'))) {
    throw "The target is not a CurseForge instance: $instanceRootResolved"
}

foreach ($folder in @('config', 'defaultconfigs', 'kubejs')) {
    $source = Join-Path $repoRoot $folder
    $destination = Join-Path $instanceRootResolved $folder
    if (Test-Path -LiteralPath $source) {
        New-Item -ItemType Directory -Path $destination -Force | Out-Null
        Copy-Item -Path (Join-Path $source '*') -Destination $destination -Recurse -Force
    }
}

$modsDestination = Join-Path $instanceRootResolved 'mods'
New-Item -ItemType Directory -Path $modsDestination -Force | Out-Null
$repositoryMods = @{}
foreach ($pattern in @('NewWorldCore-*.jar', 'DoctorWhoMod-*.jar')) {
    $matches = @(Get-ChildItem -Path (Join-Path (Join-Path $repoRoot 'mods') $pattern) -File)
    if ($matches.Count -ne 1) {
        throw "Expected exactly one repository mod matching $pattern, found $($matches.Count)."
    }
    $repositoryMods[$pattern] = $matches[0]
}

$staleMods = @(foreach ($pattern in $repositoryMods.Keys) {
    $expectedName = $repositoryMods[$pattern].Name
    Get-ChildItem -Path (Join-Path $modsDestination $pattern) -File |
        Where-Object { $_.Name -ne $expectedName }
})

if ($staleMods.Count -gt 0) {
    $backupRoot = Join-Path $instanceRootResolved (
        'backups\custom-mods\pre-apply-' + (Get-Date -Format 'yyyyMMdd-HHmmss')
    )
    New-Item -ItemType Directory -Path $backupRoot -Force | Out-Null
    foreach ($staleMod in $staleMods) {
        Move-Item -LiteralPath $staleMod.FullName -Destination $backupRoot
        Write-Host "Backed up stale custom mod: $($staleMod.Name)"
    }
}

foreach ($repositoryMod in $repositoryMods.Values) {
    Copy-Item -LiteralPath $repositoryMod.FullName -Destination $modsDestination -Force
}

Write-Host "Shared files and custom mods applied to $instanceRootResolved"
Write-Host 'CurseForge-managed addons remain pinned by manifest.json.'

