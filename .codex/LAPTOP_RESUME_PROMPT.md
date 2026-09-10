# Laptop continuation prompt

Copy the block below into Codex on the laptop after opening the New World repository project.

```text
Kanka bu New World projesine kanonik kayıtlardaki son noktadan devam edeceğiz. GitHub main ortak ve kanonik kaynaktır.

Önce hiçbir dosyayı değiştirmeden mevcut branch ve çalışma ağacını kontrol et. Yerel değişiklik yoksa origin/main'i fetch edip yalnızca fast-forward pull yap. Yerel değişiklik varsa ezme; bana durumu bildir. Ardından sırasıyla `.codex/project-memory.md`, `.codex/HANDOFF.md`, `.codex/conversations/INDEX.md` ve INDEX'teki en yeni ilgili conversation kaydını tamamen oku. Bu prompttan daha yeni doğrulanmış kayıt varsa onu esas al.

10 Eylül itibarıyla repo/laptopta kurulu aday (Player Navigation görünümü oyun kabulünü bekler):
`NewWorldCore-1.21.1-NeoForge-0.5.69.0-alpha-player-navigation-view.jar`
SHA-256:
`af77b62a691a18bf6340c7371647b0c90e07b7643773cf8576958aae29c8427f`

Stage 8 Player Discoveries tamamlandı. ALL/STRUCTURES/GEOLOGY listesi, ayrıntılar, canlı oyuncu mesafesi, LAST SEEN, EST RESERVE, FAV, TARGET ve ROUTE runtime kabulünden geçti. Archeologist Camp ve Trial Chambers hedef/favori/rota yazımları doğrulandı; Trial Chambers seyahati tek hopta tamamlandı. Discovery TARGET/ROUTE yolunu yeniden geliştirme.

Laptopun CurseForge instance yolunu `machines/laptop.json` kaydından çöz. Repo/instance'ta tek NewWorldCore JAR bulunduğunu ve yukarıdaki SHA-256 ile eşleştiğini doğrula. Beklenen kurulum yoksa yalnız oyun kapalıyken `tools/apply-to-instance.ps1` çalıştır; çalışan Java varken JAR değiştirme. Bilinen iyi JAR yedeklerini koru.

`docs/12_Gelistirme_Yol_Haritasi.md` içindeki 14 aşamalı sayısal liste kanonik geliştirme sırasıdır. Stage 6 Field Survey, jeolojik Analysis zinciri ve Stage 8 Discoveries tamamlandı; eski promptlardaki bu görevleri yeniden başlatma. Stage 3 ve Stage 7 ileri işleri nedeniyle kısmi kalır.

Kurulumdan sonra sıradaki çalışma:
1. Aşama 4 ve Aşama 5 temel tek oyunculu kabulü korunur; `.68.2` yeni oturum ve otomatik geri bağlantı regresyonu geçti. Genişletilmiş multiplayer/timeout testlerini geçmiş sayma.
2. Önce `.69.0` Player Navigation salt-okunur görünümünü `docs/15_Player_Navigation_Runtime_Kabul.md` ile doğrula. Mevcut hedef, gemi mesafesi, rota/loaded hop ve sonraki hop WE tahmini fiziksel terminalle eşleşmeli. Uçuş/rota yazımı eklenmedi.
3. Sonra Aşama 9 favori seçimi, SAVE CURRENT LOCATION ve SEND TO SHIP kalan maddeleri. TARGET/ROUTE motorunu yeniden oluşturma.

Ayarlanabilir yeni davranışlarda `.cursor/rules/config-first-development.mdc` standardını uygula. Her doğrulanmış adımda pack-lock, ilgili dokümanlar, `.codex/HANDOFF.md`, `.codex/project-memory.md` ve tarihli conversation kaydını güncelle; test et; commit edip GitHub main'e pushla. Dünya/save/log/cache dosyalarını Git'e ekleme. Bilinen iyi JAR yedeklerini silme ve aynı anda iki NewWorldCore sürümü yükleme.

Önce senkronizasyon ve hash kontrollerini yap, sonucu bana özetle; sonra test ve geliştirmeye devam edelim.
```
