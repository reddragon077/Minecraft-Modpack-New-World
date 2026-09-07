# Laptop continuation prompt

Copy the block below into Codex on the laptop after opening the New World repository project.

```text
Kanka bu New World projesine masaüstündeki son noktadan devam edeceğiz. GitHub main ortak ve kanonik kaynaktır.

Önce hiçbir dosyayı değiştirmeden mevcut branch ve çalışma ağacını kontrol et. Yerel değişiklik yoksa origin/main'i fetch edip yalnızca fast-forward pull yap. Yerel değişiklik varsa ezme; bana durumu bildir. Ardından sırasıyla `.codex/project-memory.md`, `.codex/HANDOFF.md`, `.codex/conversations/INDEX.md`, `.codex/conversations/2026-09-05_laptop_roadmap_reconciliation.md`, `.codex/conversations/2026-09-03_laptop_player_discoveries_actions.md` ve `.codex/conversations/2026-09-03_laptop_player_discoveries.md` dosyalarını tamamen oku.

Repodaki ve laptopta runtime-kabul edilen build:
`NewWorldCore-1.21.1-NeoForge-0.5.66.1-alpha-player-discovery-actions.jar`
SHA-256:
`889900f7e2519b8e07e604431260e28e0b6f8932d3d087bc5b17f8551fda059c`

Stage 8 Player Discoveries tamamlandı. ALL/STRUCTURES/GEOLOGY listesi, ayrıntılar, canlı oyuncu mesafesi, LAST SEEN, EST RESERVE, FAV, TARGET ve ROUTE runtime kabulünden geçti. Archeologist Camp ve Trial Chambers hedef/favori/rota yazımları doğrulandı; Trial Chambers seyahati tek hopta tamamlandı. Discovery TARGET/ROUTE yolunu yeniden geliştirme.

Laptopun CurseForge instance yolunu `machines/laptop.json` kaydından çöz. Repo/instance'ta tek NewWorldCore JAR bulunduğunu ve yukarıdaki SHA-256 ile eşleştiğini doğrula. Beklenen kurulum yoksa yalnız oyun kapalıyken `tools/apply-to-instance.ps1` çalıştır; çalışan Java varken JAR değiştirme. Bilinen iyi JAR yedeklerini koru.

`docs/12_Gelistirme_Yol_Haritasi.md` içindeki 14 aşamalı sayısal liste kanonik geliştirme sırasıdır. Stage 6 Field Survey, jeolojik Analysis zinciri ve Stage 8 Discoveries tamamlandı; eski promptlardaki bu görevleri yeniden başlatma. Stage 3 ve Stage 7 ileri işleri nedeniyle kısmi kalır.

Kurulumdan sonra sıradaki çalışma:
1. Aşama 4 — Overview / Ship Status.
2. Aşama 5 — Ship Link.
3. Ardından Aşama 9 — Player Navigation panelinin kalan maddeleri.

Ayarlanabilir yeni davranışlarda `.cursor/rules/config-first-development.mdc` standardını uygula. Her doğrulanmış adımda pack-lock, ilgili dokümanlar, `.codex/HANDOFF.md`, `.codex/project-memory.md` ve tarihli conversation kaydını güncelle; test et; commit edip GitHub main'e pushla. Dünya/save/log/cache dosyalarını Git'e ekleme. Bilinen iyi JAR yedeklerini silme ve aynı anda iki NewWorldCore sürümü yükleme.

Önce senkronizasyon ve hash kontrollerini yap, sonucu bana özetle; sonra test ve geliştirmeye devam edelim.
```
