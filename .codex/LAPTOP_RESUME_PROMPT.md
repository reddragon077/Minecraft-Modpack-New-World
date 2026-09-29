# Laptop continuation prompt

Copy the block below into Codex on the laptop after opening the New World repository project.

```text
Kanka bu New World projesine kanonik kayıtlardaki son noktadan devam edeceğiz. GitHub main ortak ve kanonik kaynaktır.

Önce hiçbir dosyayı değiştirmeden mevcut branch ve çalışma ağacını kontrol et. Yerel değişiklik yoksa origin/main'i fetch edip yalnızca fast-forward pull yap. Yerel değişiklik varsa ezme; bana durumu bildir. Ardından sırasıyla `.codex/project-memory.md`, `.codex/HANDOFF.md`, `.codex/conversations/INDEX.md` ve INDEX'teki en yeni ilgili conversation kaydını tamamen oku. Bu prompttan daha yeni doğrulanmış kayıt varsa onu esas al.

29 Eylül itibarıyla repo/laptopta kurulu aday (kayıtlı konum terminal/rota düzeltmesi; on bir test grubu geçti, birleşik oyun kabulü bekliyor):
`NewWorldCore-1.21.1-NeoForge-0.5.69.4-alpha-waypoint-terminal-route.jar`
SHA-256:
`4a68d8e660cd7e85a40109a16c5e1ec1068a92aefe45efbfccd19127339fc4e8`

Stage 8 Player Discoveries tamamlandı. ALL/STRUCTURES/GEOLOGY listesi, ayrıntılar, canlı oyuncu mesafesi, LAST SEEN, EST RESERVE, FAV, TARGET ve ROUTE runtime kabulünden geçti. Archeologist Camp ve Trial Chambers hedef/favori/rota yazımları doğrulandı; Trial Chambers seyahati tek hopta tamamlandı. Discovery TARGET/ROUTE yolunu yeniden geliştirme.

Laptopun CurseForge instance yolunu `machines/laptop.json` kaydından çöz. Repo/instance'ta tek NewWorldCore JAR bulunduğunu ve yukarıdaki SHA-256 ile eşleştiğini doğrula. Beklenen kurulum yoksa yalnız oyun kapalıyken `tools/apply-to-instance.ps1` çalıştır; çalışan Java varken JAR değiştirme. Bilinen iyi JAR yedeklerini koru.

`docs/12_Gelistirme_Yol_Haritasi.md` içindeki 14 aşamalı sayısal liste kanonik geliştirme sırasıdır. Stage 6 Field Survey, jeolojik Analysis zinciri ve Stage 8 Discoveries tamamlandı; eski promptlardaki bu görevleri yeniden başlatma. Stage 3 ve Stage 7 ileri işleri nedeniyle kısmi kalır.

Kurulumdan sonra sıradaki çalışma:
1. Aşama 4 ve Aşama 5 temel tek oyunculu kabulü korunur; `.68.2` yeni oturum ve otomatik geri bağlantı regresyonu geçti. Genişletilmiş multiplayer/timeout testlerini geçmiş sayma.
2. `.69.0` görünüm/hedef güncellemesi ve GUI kapat/aç, `.69.1` temel favori seçimi ve rotayı koruma, `.69.2` iki konum kaydı/tekrar kopya önleme/yeniden girişte kalıcılık kabulü korunur. Eski testleri yeniden isteme.
3. `.69.3` ortak hedef yazımı ve eski rotanın korunması ekran/log/kayıtla doğrulandı; fiziksel terminal Y/mesafe hatası `.69.4` ile düzeltildi. Tek birleşik test: kayıtlı [-2454,63,181] → SEND TO SHIP → fiziksel terminal Y=63 ve gemiye göre güncel mesafe (~11) → CALCULATE ROUTE → son koordinatlar [-2454,63,181]. Hesap öncesi eski rota korunur; hesap sonrası değişmesi beklenir. Uçuş yapmaya gerek yok. Config/izin ve genişletilmiş Stage 9 kontrolleri açık; otomatik testleri oyun kabulü sayma.
4. Kullanıcı daha hızlı, ilişkili işleri toplu ilerletmek istiyor: geliştirme/test/yedek/kurulum/kayıt paketini birlikte yap; gerekli karar veya oyun kanıtında tek birleşik kontrol iste. Kabul edilmiş testleri yeniden isteme.

Ayarlanabilir yeni davranışlarda `.cursor/rules/config-first-development.mdc` standardını uygula. Her doğrulanmış adımda pack-lock, ilgili dokümanlar, `.codex/HANDOFF.md`, `.codex/project-memory.md` ve tarihli conversation kaydını güncelle; test et; commit edip GitHub main'e pushla. Dünya/save/log/cache dosyalarını Git'e ekleme. Bilinen iyi JAR yedeklerini silme ve aynı anda iki NewWorldCore sürümü yükleme.

Önce senkronizasyon ve hash kontrollerini yap, sonucu bana özetle; sonra test ve geliştirmeye devam edelim.
```
