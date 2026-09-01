param(
    [string]$RepositoryRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
)

$ErrorActionPreference = 'Stop'
$lockPath = Join-Path $RepositoryRoot 'pack-lock.json'
$outputPath = Join-Path $RepositoryRoot 'mods\00_kullanılan modlar .md'

if (-not (Test-Path -LiteralPath $lockPath)) {
    throw "pack-lock.json bulunamadı: $lockPath"
}

$lock = Get-Content -LiteralPath $lockPath -Raw | ConvertFrom-Json
$addons = @($lock.addons)
$customMods = @($lock.customMods)

function Escape-MarkdownCell([object]$Value) {
    if ($null -eq $Value) { return '' }
    return ([string]$Value).Replace('|', '\|').Replace("`r", ' ').Replace("`n", ' ')
}

function Add-Line([System.Text.StringBuilder]$Builder, [string]$Line = '') {
    [void]$Builder.AppendLine($Line)
}

$enabledCount = @($addons | Where-Object enabled).Count
$disabledCount = $addons.Count - $enabledCount
$modCount = @($addons | Where-Object category -eq 'mods').Count
$resourcePackCount = @($addons | Where-Object category -eq 'resourcepacks').Count
$shaderPackCount = @($addons | Where-Object category -eq 'shaderpacks').Count

$builder = [System.Text.StringBuilder]::new()
Add-Line $builder '# New World — Kullanılan Modlar ve Paket İçeriği'
Add-Line $builder
Add-Line $builder '> Bu dosya `pack-lock.json` üzerinden otomatik üretilir. Elle düzenlemek yerine `tools/update-mod-list.ps1` çalıştırılmalıdır.'
Add-Line $builder
Add-Line $builder '## Paket özeti'
Add-Line $builder
Add-Line $builder '| Alan | Değer |'
Add-Line $builder '|---|---:|'
Add-Line $builder ('| Minecraft | `{0}` |' -f (Escape-MarkdownCell $lock.minecraftVersion))
Add-Line $builder ('| Mod yükleyici | `{0}` |' -f (Escape-MarkdownCell $lock.modLoader))
Add-Line $builder ("| CurseForge mod girdisi | {0} |" -f $modCount)
Add-Line $builder ("| Resource pack | {0} |" -f $resourcePackCount)
Add-Line $builder ("| Shader pack | {0} |" -f $shaderPackCount)
Add-Line $builder ("| CurseForge toplamı | {0} |" -f $addons.Count)
Add-Line $builder ("| Etkin / devre dışı | {0} / {1} |" -f $enabledCount, $disabledCount)
Add-Line $builder ("| Projeye özel fork JAR'ı | {0} |" -f $customMods.Count)
Add-Line $builder ("| Toplam paket girdisi | {0} |" -f ($addons.Count + $customMods.Count))
Add-Line $builder
Add-Line $builder '## Projeye özel fork JAR dosyaları'
Add-Line $builder
Add-Line $builder 'Bu iki dosya CurseForge üzerinden indirilemez; New World için değiştirilmiş build dosyaları oldukları için Git deposunda tutulur.'
Add-Line $builder
Add-Line $builder '| Dosya | Boyut | SHA-256 | Belge |'
Add-Line $builder '|---|---:|---|---|'
foreach ($custom in $customMods) {
    $sizeMiB = [Math]::Round(([double]$custom.size / 1MB), 2)
    $doc = if ($custom.fileName -like 'DoctorWhoMod-*') {
        '[JAR analizi](New_World_DoctorWhoMod_JAR_Analizi.md)'
    } else {
        '[Alpha test rehberi](New_World_Room_Controller_Alpha_Test_Rehberi.md)'
    }
    Add-Line $builder ('| `{0}` | {1} MiB | `{2}` | {3} |' -f (Escape-MarkdownCell $custom.fileName), $sizeMiB, $custom.sha256, $doc)
}
Add-Line $builder
Add-Line $builder '## Durum açıklaması'
Add-Line $builder
Add-Line $builder '- **Etkin:** CurseForge manifestine dahil edilir ve normal kurulumda yüklenir.'
Add-Line $builder '- **Devre dışı:** Lock dosyasında izlenir fakat manifest kurulumu sırasında etkin olarak yüklenmez.'
Add-Line $builder '- Üçüncü taraf JAR/ZIP dosyaları Git deposuna eklenmez; kesin CurseForge proje ve dosya kimlikleri aşağıdaki tablolarda bulunur.'
Add-Line $builder

$categoryTitles = [ordered]@{
    mods = 'Modlar'
    resourcepacks = 'Resource pack dosyaları'
    shaderpacks = 'Shader pack dosyaları'
}

foreach ($category in $categoryTitles.Keys) {
    $items = @($addons | Where-Object category -eq $category | Sort-Object name, fileName)
    Add-Line $builder ("## {0} ({1})" -f $categoryTitles[$category], $items.Count)
    Add-Line $builder
    Add-Line $builder '| Ad | Dosya | CurseForge Project ID | File ID | Durum |'
    Add-Line $builder '|---|---|---:|---:|---|'
    foreach ($item in $items) {
        $status = if ($item.enabled) { 'Etkin' } else { '**Devre dışı**' }
        Add-Line $builder ('| {0} | `{1}` | {2} | {3} | {4} |' -f (Escape-MarkdownCell $item.name), (Escape-MarkdownCell $item.fileName), $item.projectID, $item.fileID, $status)
    }
    Add-Line $builder
}

Add-Line $builder '## Güncelleme kuralı'
Add-Line $builder
Add-Line $builder '1. CurseForge instance güncellendikten sonra `tools/refresh-from-instance.ps1` ile kilit ve manifest yenilenir.'
Add-Line $builder '2. `tools/update-mod-list.ps1` çalıştırılarak bu liste yeniden üretilir.'
Add-Line $builder '3. Değişiklikler test edilip GitHub ana deposuna gönderilir.'
Add-Line $builder
Add-Line $builder 'Paketin makineye uygulanması ve iki bilgisayarlı çalışma düzeni için [Workspace Sync](../docs/WORKSPACE_SYNC.md) belgesine bakın.'

[IO.File]::WriteAllText(
    $outputPath,
    ($builder.ToString().TrimEnd("`r", "`n") + "`n"),
    [Text.UTF8Encoding]::new($false)
)
Write-Host "Mod listesi güncellendi: $outputPath"
