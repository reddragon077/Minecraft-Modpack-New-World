param(
    [string]$OutputDirectory = (Join-Path $PSScriptRoot '..\dist')
)

$ErrorActionPreference = 'Stop'
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$manifestPath = Join-Path $repoRoot 'manifest.json'

if (-not (Test-Path -LiteralPath $manifestPath)) {
    throw 'manifest.json is missing. Run refresh-from-instance.ps1 first.'
}

$staging = Join-Path ([System.IO.Path]::GetTempPath()) ('new-world-pack-' + [guid]::NewGuid().ToString('N'))
$overrides = Join-Path $staging 'overrides'
New-Item -ItemType Directory -Path $overrides -Force | Out-Null

try {
    Copy-Item -LiteralPath $manifestPath -Destination $staging
    foreach ($folder in @('config', 'defaultconfigs', 'kubejs')) {
        $source = Join-Path $repoRoot $folder
        if (Test-Path -LiteralPath $source) {
            Copy-Item -LiteralPath $source -Destination $overrides -Recurse -Force
        }
    }

    $overrideMods = Join-Path $overrides 'mods'
    New-Item -ItemType Directory -Path $overrideMods -Force | Out-Null
    foreach ($pattern in @('NewWorldCore-*.jar', 'DoctorWhoMod-*.jar')) {
        $matches = @(Get-ChildItem -Path (Join-Path (Join-Path $repoRoot 'mods') $pattern) -File)
        if ($matches.Count -ne 1) {
            throw "Expected exactly one custom mod matching $pattern, found $($matches.Count)."
        }
        Copy-Item -LiteralPath $matches[0].FullName -Destination $overrideMods
    }

    New-Item -ItemType Directory -Path $OutputDirectory -Force | Out-Null
    $outputRoot = (Resolve-Path -LiteralPath $OutputDirectory).Path
    $output = Join-Path $outputRoot 'New-World-development.zip'
    Compress-Archive -Path (Join-Path $staging '*') -DestinationPath $output -Force
    Write-Host "CurseForge package created: $output"
}
finally {
    if (Test-Path -LiteralPath $staging) {
        Remove-Item -LiteralPath $staging -Recurse -Force
    }
}
