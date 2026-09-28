# Laptop continuation prompt

Copy the block below into Codex on the laptop after opening the New World repository project.

```text
Kanka bu New World projesine kanonik kayıtlardaki son noktadan devam edeceğiz. GitHub main ortak ve kanonik kaynaktır.

Önce hiçbir dosyayı değiştirmeden mevcut branch ve çalışma ağacını kontrol et. Yerel değişiklik yoksa origin/main'i fetch edip yalnızca fast-forward pull yap. Yerel değişiklik varsa ezme; bana durumu bildir. Ardından sırasıyla `.codex/project-memory.md`, `.codex/HANDOFF.md`, `.codex/conversations/INDEX.md` ve INDEX'teki en yeni ilgili conversation kaydını tamamen oku. Bu prompttan daha yeni doğrulanmış kayıt varsa onu esas al.

28 Eylül itibarıyla repo/laptopta kurulu aday (SAVE CURRENT LOCATION; dokuz test grubu geçti, yeni kaydın oyun kabulü bekliyor):
`NewWorldCore-1.21.1-NeoForge-0.5.69.2-alpha-save-current-location.jar`
SHA-256:
`3253d965abe06cf134de86759c94bade33f53038a60a7a9990ef28a35db358c9`

Stage 8 Player Discoveries tamamlandı. ALL/STRUCTURES/GEOLOGY listesi, ayrıntılar, canlı oyuncu mesafesi, LAST SEEN, EST RESERVE, FAV, TARGET ve ROUTE runtime kabulünden geçti. Archeologist Camp ve Trial Chambers hedef/favori/rota yazımları doğrulandı; Trial Chambers seyahati tek hopta tamamlandı. Discovery TARGET/ROUTE yolunu yeniden geliştirme.

Laptopun CurseForge instance yolunu `machines/laptop.json` kaydından çöz. Repo/instance'ta tek NewWorldCore JAR bulunduğunu ve yukarıdaki SHA-256 ile eşleştiğini doğrula. Beklenen kurulum yoksa yalnız oyun kapalıyken `tools/apply-to-instance.ps1` çalıştır; çalışan Java varken JAR değiştirme. Bilinen iyi JAR yedeklerini koru.

`docs/12_Gelistirme_Yol_Haritasi.md` içindeki 14 aşamalı sayısal liste kanonik geliştirme sırasıdır. Stage 6 Field Survey, jeolojik Analysis zinciri ve Stage 8 Discoveries tamamlandı; eski promptlardaki bu görevleri yeniden başlatma. Stage 3 ve Stage 7 ileri işleri nedeniyle kısmi kalır.

Kurulumdan sonra sıradaki çalışma:
1. Aşama 4 ve Aşama 5 temel tek oyunculu kabulü korunur; `.68.2` yeni oturum ve otomatik geri bağlantı regresyonu geçti. Genişletilmiş multiplayer/timeout testlerini geçmiş sayma.
2. `.69.0` görünüm/hedef güncellemesi ve GUI kapat/aç kabulü korunur. `.69.1` temel favori seçimi de geçti: Trial Chambers hedef olurken eski Carbon rotası/1 hop/52 WE korunuyor; ekran/log kanıtı var. Şimdi `.69.2` gemi dışında NAVIGATION → SAVE CURRENT LOCATION → FAVORITES dene; koordinat/boyut, tekrar kayıtta kopya oluşmaması ve dünya yeniden yüklenince kalıcılığı doğrula. `docs/15_Player_Navigation_Runtime_Kabul.md` içindeki açık genişletilmiş kontroller ayrıca kalır. Otomatik testleri oyun kabulü sayma.
3. Konum kaydı kabulünden sonra Aşama 9 SEND TO SHIP. TARGET/ROUTE motorunu yeniden oluşturma; Aşama 9 henüz kapanmadı.

Ayarlanabilir yeni davranışlarda `.cursor/rules/config-first-development.mdc` standardını uygula. Her doğrulanmış adımda pack-lock, ilgili dokümanlar, `.codex/HANDOFF.md`, `.codex/project-memory.md` ve tarihli conversation kaydını güncelle; test et; commit edip GitHub main'e pushla. Dünya/save/log/cache dosyalarını Git'e ekleme. Bilinen iyi JAR yedeklerini silme ve aynı anda iki NewWorldCore sürümü yükleme.

Önce senkronizasyon ve hash kontrollerini yap, sonucu bana özetle; sonra test ve geliştirmeye devam edelim.
```
