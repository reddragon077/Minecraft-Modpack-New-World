param(
    [Parameter(Mandatory = $true)]
    [string]$BaselineJar,
    [Parameter(Mandatory = $true)]
    [string]$JdkHome,
    [Parameter(Mandatory = $true)]
    [string]$AsmJar,
    [Parameter(Mandatory = $true)]
    [string]$AsmTreeJar,
    [string]$OutputJar
)

$ErrorActionPreference = 'Stop'
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$patchRoot = Join-Path $repoRoot 'src-patches\newworldcore'
$manifestPath = Join-Path $patchRoot 'patch-manifest.json'
$sourceRoot = Join-Path $patchRoot 'java'
$patcherSource = Join-Path $patchRoot 'tools\NewWorldClassPatcher.java'
$resourcesRoot = Join-Path $patchRoot 'resources'
$manifest = Get-Content -LiteralPath $manifestPath -Raw | ConvertFrom-Json
$baseline = (Resolve-Path -LiteralPath $BaselineJar).Path
$jdk = (Resolve-Path -LiteralPath $JdkHome).Path
$javac = Join-Path $jdk 'bin\javac.exe'
$java = Join-Path $jdk 'bin\java.exe'
$jarTool = Join-Path $jdk 'bin\jar.exe'
$asm = (Resolve-Path -LiteralPath $AsmJar).Path
$asmTree = (Resolve-Path -LiteralPath $AsmTreeJar).Path
$utf8 = [Text.UTF8Encoding]::new($false)

if (-not (Test-Path -LiteralPath $javac) -or -not (Test-Path -LiteralPath $java) -or -not (Test-Path -LiteralPath $jarTool)) {
    throw "JDK 21 tools were not found under: $jdk"
}
$actualBaselineHash = (Get-FileHash -LiteralPath $baseline -Algorithm SHA256).Hash.ToLowerInvariant()
if ($actualBaselineHash -ne $manifest.baselineSha256) {
    throw "Baseline SHA-256 mismatch. Expected $($manifest.baselineSha256), got $actualBaselineHash"
}
if ([string]::IsNullOrWhiteSpace($OutputJar)) {
    $OutputJar = Join-Path $repoRoot ("dist\NewWorldCore-1.21.1-NeoForge-{0}.jar" -f $manifest.version)
}
$output = [IO.Path]::GetFullPath($OutputJar)
if ($output -eq $baseline) {
    throw 'Output JAR must not overwrite the verified baseline.'
}
New-Item -ItemType Directory -Path ([IO.Path]::GetDirectoryName($output)) -Force | Out-Null

Add-Type -AssemblyName System.IO.Compression.FileSystem
$workRoot = Join-Path ([IO.Path]::GetTempPath()) ("newworldcore-geology-{0}" -f [guid]::NewGuid().ToString('N'))
$payloadRoot = Join-Path $workRoot 'payload'
$classesRoot = Join-Path $workRoot 'classes'
$patcherClassesRoot = Join-Path $workRoot 'patcher-classes'
New-Item -ItemType Directory -Path $payloadRoot, $classesRoot, $patcherClassesRoot -Force | Out-Null

function Read-ZipText([IO.Compression.ZipArchive]$Zip, [string]$Name) {
    $entry = $Zip.GetEntry($Name)
    if ($null -eq $entry) { throw "Missing baseline JAR entry: $Name" }
    $reader = [IO.StreamReader]::new($entry.Open(), [Text.Encoding]::UTF8)
    try { return $reader.ReadToEnd() } finally { $reader.Dispose() }
}

function Write-Utf8([string]$Path, [string]$Text) {
    New-Item -ItemType Directory -Path ([IO.Path]::GetDirectoryName($Path)) -Force | Out-Null
    [IO.File]::WriteAllText($Path, ($Text.TrimEnd("`r", "`n") + "`n"), $utf8)
}

function Build-Template([IO.Compression.ZipArchive]$Zip, [string]$Seed, [string]$Target, [object[]]$Palette) {
    $text = Read-ZipText $Zip ("data/newworldcore/geology/materializer/{0}.nwdep" -f $Seed)
    $lines = @($text -split "`r?`n")
    if ($lines.Count -lt 3 -or $lines[0] -notmatch '^size=(\d+),(\d+),(\d+)$') { throw "Invalid seed template: $Seed" }
    $sizeX = [int]$Matches[1]
    $sizeY = [int]$Matches[2]
    $sizeZ = [int]$Matches[3]
    $lines[1] = 'palette=' + (($Palette | ForEach-Object { [string]$_ }) -join ',')
    $coordinateLines = @($lines | Select-Object -Skip 2 | Where-Object { $_ -match '^\d+,\d+,\d+,\d+$' })
    Write-Utf8 (Join-Path $payloadRoot ("data\newworldcore\geology\materializer\{0}.nwdep" -f $Target)) ($lines -join "`n")
    return [pscustomobject]@{ SizeX = $sizeX; SizeY = $sizeY; SizeZ = $sizeZ; Reserve = $coordinateLines.Count }
}

try {
    $baselineZip = [IO.Compression.ZipFile]::OpenRead($baseline)
    try {
        $signatureEntries = @($baselineZip.Entries | Where-Object { $_.FullName -match '^META-INF/.*\.(SF|RSA|DSA)$' })
        if ($signatureEntries.Count -gt 0) { throw 'The baseline JAR is signed; refusing to invalidate its signature.' }
        Copy-Item -Path (Join-Path $resourcesRoot '*') -Destination $payloadRoot -Recurse -Force
        $depositIndex = @(Read-ZipText $baselineZip 'data/newworldcore/geology/deposits/index.txt' -split "`r?`n" | Where-Object { -not [string]::IsNullOrWhiteSpace($_) })
        $materializerIndex = @(Read-ZipText $baselineZip 'data/newworldcore/geology/materializer/index.txt' -split "`r?`n" | Where-Object { -not [string]::IsNullOrWhiteSpace($_) })

        foreach ($deposit in $manifest.newDeposits) {
            $template = Build-Template $baselineZip $deposit.seedTemplate $deposit.id $deposit.palette
            if ($template.Reserve -ne [int]$deposit.expectedReserve) { throw "Template reserve mismatch for $($deposit.id): expected $($deposit.expectedReserve), got $($template.Reserve)" }
            $definitionPath = Join-Path $resourcesRoot ("data\newworldcore\geology\deposits\{0}" -f $deposit.definition)
            $definition = Get-Content -LiteralPath $definitionPath -Raw | ConvertFrom-Json
            if ($definition.id -ne ("newworldcore:{0}" -f $deposit.id)) { throw "Definition ID mismatch for $($deposit.id)" }
            if ([int]$definition.min_reserve -ne $template.Reserve -or [int]$definition.max_reserve -ne $template.Reserve) { throw "Definition reserve does not match physical template for $($deposit.id)" }
            $definitionName = [string]$deposit.definition
            if ($depositIndex -notcontains $definitionName) { $depositIndex += $definitionName }
            $materializerLine = '{0}|{1}|{2}|{3}|{4}' -f $deposit.id, $template.SizeX, $template.SizeY, $template.SizeZ, $template.Reserve
            if ($materializerIndex -notcontains $materializerLine) { $materializerIndex += $materializerLine }
        }
        foreach ($override in $manifest.templateOverrides) { [void](Build-Template $baselineZip $override.seedTemplate $override.id $override.palette) }

        Write-Utf8 (Join-Path $payloadRoot 'data\newworldcore\geology\deposits\index.txt') ($depositIndex -join "`n")
        Write-Utf8 (Join-Path $payloadRoot 'data\newworldcore\geology\materializer\index.txt') ($materializerIndex -join "`n")
        $modsToml = Read-ZipText $baselineZip 'META-INF/neoforge.mods.toml'
        $oldVersionLine = 'version = "{0}"' -f $manifest.baselineVersion
        $newVersionLine = 'version = "{0}"' -f $manifest.version
        if (-not $modsToml.Contains($oldVersionLine)) { throw 'Baseline mod version line was not found in neoforge.mods.toml.' }
        Write-Utf8 (Join-Path $payloadRoot 'META-INF\neoforge.mods.toml') ($modsToml.Replace($oldVersionLine, $newVersionLine))
        Copy-Item -LiteralPath $manifestPath -Destination (Join-Path $payloadRoot 'META-INF\newworld-geology-patch.json') -Force
    } finally { $baselineZip.Dispose() }

    $sourceFiles = @(Get-ChildItem -LiteralPath $sourceRoot -Recurse -Filter '*.java' -File | Select-Object -ExpandProperty FullName)
    if ($sourceFiles.Count -eq 0) { throw "No Java patch sources found under: $sourceRoot" }
    & $javac --release 21 -encoding UTF-8 -classpath $baseline -d $classesRoot $sourceFiles
    if ($LASTEXITCODE -ne 0) { throw "javac failed with exit code $LASTEXITCODE" }
    Copy-Item -Path (Join-Path $classesRoot '*') -Destination $payloadRoot -Recurse -Force

    $asmClasspath = $asm + [IO.Path]::PathSeparator + $asmTree
    & $javac --release 21 -encoding UTF-8 -classpath $asmClasspath -d $patcherClassesRoot $patcherSource
    if ($LASTEXITCODE -ne 0) { throw "class patcher javac failed with exit code $LASTEXITCODE" }
    $patcherClasspath = $patcherClassesRoot + [IO.Path]::PathSeparator + $asmClasspath
    & $java -classpath $patcherClasspath NewWorldClassPatcher $baseline $payloadRoot
    if ($LASTEXITCODE -ne 0) { throw "class patcher failed with exit code $LASTEXITCODE" }
    Copy-Item -LiteralPath $baseline -Destination $output -Force
    & $jarTool --update --file $output -C $payloadRoot .
    if ($LASTEXITCODE -ne 0) { throw "jar update failed with exit code $LASTEXITCODE" }

    $smokeSources = @(Get-ChildItem -LiteralPath (Join-Path $patchRoot 'tools') -Filter '*SmokeTest.java' -File | Select-Object -ExpandProperty FullName)
    if ($smokeSources.Count -eq 0) { throw 'No NewWorldCore smoke tests were found.' }
    $smokeRoot = Join-Path $workRoot 'smoke-classes'
    New-Item -ItemType Directory -Path $smokeRoot -Force | Out-Null
    & $javac --release 21 -encoding UTF-8 -classpath $output -d $smokeRoot $smokeSources
    if ($LASTEXITCODE -ne 0) { throw "smoke-test javac failed with exit code $LASTEXITCODE" }
    $smokeClasspath = $smokeRoot + [IO.Path]::PathSeparator + $output
    $configRoot = Join-Path $repoRoot 'config\newworldcore'
    & $java ("-Dnewworldcore.configDir={0}" -f $configRoot) -classpath $smokeClasspath NewWorldConfigSmokeTest
    if ($LASTEXITCODE -ne 0) { throw "config smoke test failed with exit code $LASTEXITCODE" }
    & $java ("-Dnewworldcore.configDir={0}" -f $configRoot) -classpath $smokeClasspath Navigation0610DiscoveryRuntimeSmokeTest
    if ($LASTEXITCODE -ne 0) { throw "discovery smoke test failed with exit code $LASTEXITCODE" }
    & $java ("-Dnewworldcore.configDir={0}" -f $configRoot) -classpath $smokeClasspath PlayerGeologicalFieldSurvey0620SmokeTest
    if ($LASTEXITCODE -ne 0) { throw "geological field-survey smoke test failed with exit code $LASTEXITCODE" }
    & $java ("-Dnewworldcore.configDir={0}" -f $configRoot) -classpath $smokeClasspath Navigation0630GeologyAnalysisSmokeTest
    if ($LASTEXITCODE -ne 0) { throw "geology analysis progression smoke test failed with exit code $LASTEXITCODE" }
    & $java ("-Dnewworldcore.configDir={0}" -f $configRoot) -classpath $smokeClasspath PlayerDiscoveries0650SmokeTest
    if ($LASTEXITCODE -ne 0) { throw "player discoveries smoke test failed with exit code $LASTEXITCODE" }

    $outputHash = (Get-FileHash -LiteralPath $output -Algorithm SHA256).Hash.ToLowerInvariant()
    Write-Host "Built: $output"
    Write-Host "SHA-256: $outputHash"
} finally {
    $tempRoot = [IO.Path]::GetFullPath([IO.Path]::GetTempPath())
    $resolvedWork = [IO.Path]::GetFullPath($workRoot)
    if ($resolvedWork.StartsWith($tempRoot, [StringComparison]::OrdinalIgnoreCase) -and [IO.Path]::GetFileName($resolvedWork).StartsWith('newworldcore-geology-', [StringComparison]::Ordinal)) {
        Remove-Item -LiteralPath $resolvedWork -Recurse -Force -ErrorAction SilentlyContinue
    }
}
