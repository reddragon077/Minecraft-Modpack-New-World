# Laptop continuation prompt

Copy the block below into Codex on the laptop after opening the New World repository project.

```text
Kanka bu New World projesine masaüstündeki son noktadan devam edeceğiz. GitHub main ortak ve kanonik kaynaktır.

Önce hiçbir dosyayı değiştirmeden mevcut branch ve çalışma ağacını kontrol et. Yerel değişiklik yoksa origin/main'i fetch edip yalnızca fast-forward pull yap. Yerel değişiklik varsa ezme; bana durumu bildir. Ardından sırasıyla `.codex/project-memory.md`, `.codex/HANDOFF.md`, `.codex/conversations/INDEX.md`, `.codex/conversations/2026-09-02_laptop_geology_filter_layer_repair.md` ve `.codex/conversations/2026-09-02_laptop_config_suite_build.md` dosyalarını tamamen oku.

Repodaki yeni aday build:
`NewWorldCore-1.21.1-NeoForge-0.5.60.2-alpha-config-geology-flush.jar`
SHA-256:
`423a25739e14c7db644c3389e041ad23508c06af6f2d920b33c7effb5e77c158`

Son runtime-taranan `0.5.60.0` laptop kanıtı:
- 102 placement-only görev
- range 5000 blocks
- selected=ALL
- 101 results (101 before active filters), yaklaşık 9,98 saniye
- Geology: 48 deposit, yaklaşık 9,00 saniye

Laptopun CurseForge instance yolunu `machines/laptop.json` kaydından çöz. `0.5.60.2` kurulumu tamamlandı; görsel kabulü başarısız `0.5.60.1`, `backups/custom-mods/pre-apply-20260902-144912` altında korundu. Repo/instance'ta tek NewWorldCore JAR bulunduğunu ve yukarıdaki SHA-256 ile eşleştiğini doğrula. Beklenen kurulum yoksa yalnız oyun kapalıyken `tools/apply-to-instance.ps1` çalıştır; çalışan Java varken JAR değiştirme.

`0.5.60.2` Geology filtre ekran görüntüsü kabulü geçti; popup deposit satırları, koordinatlar, scrollbar ve sarı vurgu katmanlarının tamamen üstünde kaldı.

Kurulumdan sonra sıradaki çalışma:
1. `betterarcheology:archeologist_camp_grassy` yapısını bulma, yerine gidip Structure Field Survey ile `ARCHEOLOGIST CAMP` olarak tanıma ve dinamik filtreye eklenme zincirini tamamlamak.
2. Bu Radar v2 kapanışından sonra yol haritasındaki Discovery Database analysis-level / last-seen / event aşamasına geçmek.

Her doğrulanmış adımda pack-lock, ilgili dokümanlar, `.codex/HANDOFF.md`, `.codex/project-memory.md` ve tarihli conversation kaydını güncelle; test et; commit edip GitHub main'e pushla. Dünya/save/log/cache dosyalarını Git'e ekleme. Bilinen iyi JAR yedeklerini silme ve aynı anda iki NewWorldCore sürümü yükleme.

Önce senkronizasyon ve hash kontrollerini yap, sonucu bana özetle; sonra test ve geliştirmeye devam edelim.
```
