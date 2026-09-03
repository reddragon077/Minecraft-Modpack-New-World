param(
    [Parameter(Mandatory = $true)]
    [string]$InstanceRoot,
    [Parameter(Mandatory = $true)]
    [string]$WorldName,
    [switch]$Apply
)

$ErrorActionPreference = 'Stop'
$instance = (Resolve-Path -LiteralPath $InstanceRoot).Path
$world = (Resolve-Path -LiteralPath (Join-Path $instance (Join-Path 'saves' $WorldName))).Path
$dataFile = (Resolve-Path -LiteralPath (Join-Path $world 'data\newworld_navigation_discoveries.dat')).Path
if (-not $world.StartsWith($instance, [StringComparison]::OrdinalIgnoreCase)) {
    throw 'Resolved world path escaped the requested instance.'
}

function Expand-Gzip([string]$Path) {
    $input = [IO.File]::OpenRead($Path)
    try {
        $gzip = [IO.Compression.GZipStream]::new($input, [IO.Compression.CompressionMode]::Decompress)
        try {
            $memory = [IO.MemoryStream]::new()
            $gzip.CopyTo($memory)
            return $memory.ToArray()
        } finally {
            $gzip.Dispose()
        }
    } finally {
        $input.Dispose()
    }
}

function Write-Gzip([string]$Path, [byte[]]$Bytes) {
    $output = [IO.File]::Create($Path)
    try {
        $gzip = [IO.Compression.GZipStream]::new(
            $output, [IO.Compression.CompressionLevel]::Optimal, $false)
        try {
            $gzip.Write($Bytes, 0, $Bytes.Length)
        } finally {
            $gzip.Dispose()
        }
    } finally {
        $output.Dispose()
    }
}

function Field-Offset([string]$Text, [string]$Name) {
    $position = $Text.IndexOf($Name, [StringComparison]::Ordinal)
    if ($position -lt 0) { return -1 }
    return $position + $Name.Length
}

function Read-NbtString([byte[]]$Bytes, [string]$Text, [string]$Name) {
    $offset = Field-Offset $Text $Name
    if ($offset -lt 0) { return '' }
    $length = ($Bytes[$offset] -shl 8) -bor $Bytes[$offset + 1]
    return [Text.Encoding]::UTF8.GetString($Bytes, $offset + 2, $length)
}

function Read-NbtInt([byte[]]$Bytes, [string]$Text, [string]$Name) {
    $offset = Field-Offset $Text $Name
    if ($offset -lt 0) { return $null }
    $littleEndian = [byte[]]@(
        $Bytes[$offset + 3], $Bytes[$offset + 2], $Bytes[$offset + 1], $Bytes[$offset]
    )
    return [BitConverter]::ToInt32($littleEndian, 0)
}

function Get-GeologyRecords([byte[]]$Bytes) {
    $text = [Text.Encoding]::Latin1.GetString($Bytes)
    $records = @()
    foreach ($match in [regex]::Matches($text, 'S0_D(\d+)_Kind')) {
        $id = $match.Groups[1].Value
        if ((Read-NbtString $Bytes $text "S0_D${id}_Kind") -ne 'GEOLOGY') { continue }
        $records += [pscustomobject]@{
            Id = [int]$id
            Source = Read-NbtString $Bytes $text "S0_D${id}_Source"
            Analysis = Read-NbtInt $Bytes $text "S0_D${id}_AnalysisLevel"
            Label = Read-NbtString $Bytes $text "S0_D${id}_Label"
            AnalysisOffset = Field-Offset $text "S0_D${id}_AnalysisLevel"
        }
    }
    return $records
}

$bytes = Expand-Gzip $dataFile
$records = @(Get-GeologyRecords $bytes)
$radar = @($records | Where-Object { $_.Source -eq 'RADAR' })
$field = @($records | Where-Object { $_.Source -eq 'FIELD' })
Write-Host "Geology records: $($records.Count); RADAR: $($radar.Count); FIELD: $($field.Count)"
$radar | Group-Object Analysis | Sort-Object Name | ForEach-Object {
    Write-Host "RADAR analysis L$($_.Name): $($_.Count)"
}
$field | Group-Object Analysis | Sort-Object Name | ForEach-Object {
    Write-Host "FIELD analysis L$($_.Name): $($_.Count)"
}

if (-not $Apply) {
    Write-Host 'Audit only; no file was changed.'
    exit 0
}

$javaProcesses = @(Get-Process java, javaw -ErrorAction SilentlyContinue)
if ($javaProcesses.Count -gt 0) {
    throw 'A Java process is running. Close Minecraft before editing discovery data.'
}

$toReset = @($radar | Where-Object { $_.Analysis -gt 0 })
if ($toReset.Count -eq 0) {
    Write-Host 'No non-zero GEOLOGY/RADAR analysis records required a reset.'
    exit 0
}

$backupDirectory = Join-Path $instance ('backups\discovery-data\pre-analysis-reset-' + (Get-Date -Format 'yyyyMMdd-HHmmss'))
if (-not $backupDirectory.StartsWith($instance, [StringComparison]::OrdinalIgnoreCase)) {
    throw 'Resolved backup path escaped the requested instance.'
}
New-Item -ItemType Directory -Path $backupDirectory -Force | Out-Null
$backupFile = Join-Path $backupDirectory 'newworld_navigation_discoveries.dat'
Copy-Item -LiteralPath $dataFile -Destination $backupFile

foreach ($record in $toReset) {
    if ($record.AnalysisOffset -lt 0) { throw "Missing analysis offset for record $($record.Id)" }
    $bytes[$record.AnalysisOffset] = 0
    $bytes[$record.AnalysisOffset + 1] = 0
    $bytes[$record.AnalysisOffset + 2] = 0
    $bytes[$record.AnalysisOffset + 3] = 0
}

$temporaryFile = Join-Path ([IO.Path]::GetDirectoryName($dataFile)) (
    '.newworld_navigation_discoveries.' + [guid]::NewGuid().ToString('N') + '.tmp')
try {
    Write-Gzip $temporaryFile $bytes
    $roundTrip = Expand-Gzip $temporaryFile
    if ($roundTrip.Length -ne $bytes.Length) { throw 'Round-trip NBT length mismatch.' }
    for ($index = 0; $index -lt $bytes.Length; $index++) {
        if ($roundTrip[$index] -ne $bytes[$index]) { throw "Round-trip NBT mismatch at byte $index." }
    }
    $verified = @(Get-GeologyRecords $roundTrip)
    $remaining = @($verified | Where-Object { $_.Source -eq 'RADAR' -and $_.Analysis -ne 0 })
    if ($remaining.Count -ne 0) { throw "$($remaining.Count) RADAR records remained above analysis zero." }
    $verifiedField = @($verified | Where-Object { $_.Source -eq 'FIELD' })
    if ($verifiedField.Count -ne $field.Count) { throw 'FIELD record count changed during reset.' }
    Move-Item -LiteralPath $temporaryFile -Destination $dataFile -Force
} finally {
    if (Test-Path -LiteralPath $temporaryFile) { Remove-Item -LiteralPath $temporaryFile -Force }
}

$backupHash = (Get-FileHash -LiteralPath $backupFile -Algorithm SHA256).Hash.ToLowerInvariant()
$newHash = (Get-FileHash -LiteralPath $dataFile -Algorithm SHA256).Hash.ToLowerInvariant()
Write-Host "Reset $($toReset.Count) GEOLOGY/RADAR analysis records to L0."
Write-Host "Backup: $backupFile"
Write-Host "Backup SHA-256: $backupHash"
Write-Host "Updated SHA-256: $newHash"
