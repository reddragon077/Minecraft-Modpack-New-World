# Laptop continuation prompt

Copy the block below into Codex on the laptop after opening the New World repository project.

```text
Kanka bu New World projesine masaüstündeki son noktadan devam edeceğiz. GitHub main ortak ve kanonik kaynaktır.

Önce hiçbir dosyayı değiştirmeden mevcut branch ve çalışma ağacını kontrol et. Yerel değişiklik yoksa origin/main'i fetch edip yalnızca fast-forward pull yap. Yerel değişiklik varsa ezme; bana durumu bildir. Ardından sırasıyla `.codex/project-memory.md`, `.codex/HANDOFF.md`, `.codex/conversations/INDEX.md` ve `.codex/conversations/2026-09-02_laptop_config_first_gui_network.md` dosyalarını tamamen oku.

Repodaki ve laptopta runtime-kabul edilen build:
`NewWorldCore-1.21.1-NeoForge-0.5.60.4-alpha-config-first-gui-network.jar`
SHA-256:
`171b1cb6a0ca78c243ad81584f820d89fc32829e0269459048d549b462f390e9`

Son runtime-taranan `0.5.60.0` laptop kanıtı:
- 102 placement-only görev
- range 5000 blocks
- selected=ALL
- 101 results (101 before active filters), yaklaşık 9,98 saniye
- Geology: 48 deposit, yaklaşık 9,00 saniye

Laptopun CurseForge instance yolunu `machines/laptop.json` kaydından çöz. `0.5.60.4` kurulumu tamamlandı; `.3`, `backups/custom-mods/pre-apply-20260902-164534` altında korundu. Repo/instance'ta tek NewWorldCore JAR, on `.properties` dosyası bulunduğunu ve yukarıdaki SHA-256 ile eşleştiğini doğrula. Beklenen kurulum yoksa yalnız oyun kapalıyken `tools/apply-to-instance.ps1` çalıştır; çalışan Java varken JAR değiştirme.

`0.5.60.2` Geology filtre ekran görüntüsü kabulü geçti. `.4` Field Survey'i 48 blok/80 tick'e indirir; runtime taraması bir kez ve 4074 ms'de tamamlandı, `ARCHEOLOGIST CAMP` tanındı ve Structure Filters içinde görsel olarak doğrulandı. `.cursor/rules/config-first-development.mdc` bundan sonraki ayarlanabilir özelliklerde config-first standardını zorunlu kılar.

Kurulumdan sonra sıradaki çalışma:
1. `NavigationDiscoverySavedData.Discovery` şemasını ve bütün record/update yollarını incelemek.
2. Save migration ile config-backed analysis-level ve last-seen davranışını eklemek.
3. Structure ve Geology kayıt yollarının ikisinden de ortak discovery event yayımlayıp ilerideki Research/Exploration XP dinleyicilerine hazırlamak.

Her doğrulanmış adımda pack-lock, ilgili dokümanlar, `.codex/HANDOFF.md`, `.codex/project-memory.md` ve tarihli conversation kaydını güncelle; test et; commit edip GitHub main'e pushla. Dünya/save/log/cache dosyalarını Git'e ekleme. Bilinen iyi JAR yedeklerini silme ve aynı anda iki NewWorldCore sürümü yükleme.

Önce senkronizasyon ve hash kontrollerini yap, sonucu bana özetle; sonra test ve geliştirmeye devam edelim.
```
