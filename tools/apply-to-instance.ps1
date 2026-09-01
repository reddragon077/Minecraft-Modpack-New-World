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
foreach ($pattern in @('NewWorldCore-*.jar', 'DoctorWhoMod-*.jar')) {
    $matches = @(Get-ChildItem -Path (Join-Path (Join-Path $repoRoot 'mods') $pattern) -File)
    if ($matches.Count -ne 1) {
        throw "Expected exactly one repository mod matching $pattern, found $($matches.Count)."
    }
    Copy-Item -LiteralPath $matches[0].FullName -Destination $modsDestination -Force
}

Write-Host "Shared files and custom mods applied to $instanceRootResolved"
Write-Host 'CurseForge-managed addons remain pinned by manifest.json.'

