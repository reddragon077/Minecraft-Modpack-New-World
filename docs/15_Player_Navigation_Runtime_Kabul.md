# Player Navigation — ilk görünüm kabulü

29 Eylül güncellemesi: aşağıdaki `.69.4` bekleyen birleşik kontrol tamamlandı. Ekranlarda Y=63, mesafe 11 blok ve son hedef [-2454,63,181]; sunucu 10:52:13.787 aynı rotayı doğruladı. 10:52:13.980 yakınlık eşiğinde tamamlandı; uçuş yapıldığı iddia edilmez. Temel Aşama 9 kabul edildi; genişletilmiş testler açık kalır. Kullanıcı onayıyla Mining görünüm paketine geçilir.

Kurulu aday `.69.4-alpha-waypoint-terminal-route`. Önceki görünüm/GUI kapat-aç, `.69.1` temel favori seçimi ve `.69.2` konum kaydı/tekrar/kalıcılık kabul edildi. `.69.3` ortak hedef yazımı/eski rotayı koruma doğrulandı; fiziksel terminal Y/mesafe hatası için `.69.4` birleşik oyun kontrolü bekliyor. Genişletilmiş liste tamamlanmadı.

## .69.4 — sıradaki tek birleşik kontrol

1. FAVORITES içindeki `LOCATION -2454 63 181` → SEND TO SHIP. Fiziksel Navigation Terminal seçili hedef koordinatı Y=63 olmalı; gemi dış konumu hâlâ [-2464,61,176] ise mesafe yaklaşık 11 blok. Farklı boyut/veri yokluğu sahte 0 olarak gösterilmez.
2. CALCULATE ROUTE öncesi sağdaki eski Carbon rotası korunmalı. Hesapla düğmesinden sonra son nokta [-2454,63,181] olmalı; artık rota değişmesi doğrudur. Route CPU ve Player Navigation ile karşılaştır. Uçuş yapmaya gerek yok.
3. Bu kısa paketin gerçek sonucunu kaydet. On bir otomatik test grubu + gerçek motor WE fixture geçti; bunlar oyun kabulünün yerine geçmez. Çok-hop/alt sınır/sadece dikey hedef otomatik testleri geçti, oyun karşılıkları ayrıca açık.

Yedek: `backups/custom-mods/pre-waypoint-terminal-20260929-01/`. Yapı/maden yüzeye iniş politikası, ara durak seyir yüksekliği, mevcut uçuş/enerji kapıları ve kayıt şeması değişmedi.

## Doğrulanan kapsam

- İlk ekran: LIVE / READ ONLY, Aluminum hedefi `[-2456,40,328]`, gemi mesafesi 154 blok; yüklü durak `[-2456,61,328]`, 152 blok, 1/1 ve 48 WE tahmini. Mevcut enerji 1000 WE.
- Discoveries TARGET/ROUTE sonrasında Carbon hedefi `[-2376,32,376]`, 220 blok; yüklü durak `[-2376,68,376]`, 219 blok, 1/1 ve 52 WE tahmini. Başlıkta oyuncu-gemi mesafesi ayrı olarak 12 blok kaldı. Metinler iki sütunda okunaklı.
- Sunucu logu 17:19:37.088 Aluminum, 17:24:03.242 Carbon TARGET, 17:24:04.011 ROUTE ready=true ve 17:24:04.968 Carbon Navigation snapshot gösterdi. Navigation sampling/render/decode hatası bulunmadı. Genel logda üçüncü taraf EMI/JEI hataları var; tüm log hatasız denmiyor.
- 28 Eylül devamında kullanıcı, istenen GUI kapat/aç kontrolü için “çalışıyor kanka baktım” onayını verdi. Bu kullanıcı bildirimidir; bağımsız yeni log kontrolü değildir. Önceki 10 Eylül kaydındaki bekleyen yeniden açılış maddesini kapatır.
- Navigation'a özel menzil kaybı/geri dönüş, boyut farkı, boş/tamamlanmış/çok-hop rota, gerçek uçuş tüketimiyle maliyet karşılaştırması ve canlı görünüm config denemesi henüz doğrulanmadı. `.68.2` Ship Link testleri bunların yerine sayılmaz.

## Kontrol listesi

1. Kendi geminin yakınında Player GUI → NAVIGATION aç. Sekme yazısı etkin görünmeli; kısa SYNCING sonrası iki sütun gelmeli.
2. Sol sütundaki seçili hedefi fiziksel Navigation Terminal ile karşılaştır. SHIP DIST, oyuncuya değil geminin dış konumuna göre hesaplanır; hedef başka boyuttaysa DIFFERENT DIMENSION gösterilir. Üst sağdaki LINK mesafesi ise oyuncu-gemi mesafesi olarak kalır.
3. Sağdaki rota durumu ve LOADED HOP değerini fiziksel Route sayfasıyla karşılaştır. Mevcut hazır rotada sonraki koordinat gösterilmeli. Tamamlanmış rotada COMPLETE ve NONE görünmeli; rota yokken NO ROUTE/NO LOADED HOP beklenir.
4. EST ... WE / NEXT HOP yalnız gerçekten motora yüklenmiş durağın motor formülüyle tahminidir. Toplam rota maliyeti veya uçuş izni değildir. Hedef yüklemesi rota noktasıyla uyuşmuyorsa DESTINATION CHANGED; API/veri yoksa NOT AVAILABLE gösterilir. Mevcut WE ayrı görünür. Sırf bu test için enerji harcamak gerekmez.
5. Discoveries üzerinden var olan TARGET/ROUTE düğmeleriyle farklı hedef seç; Navigation'a dön. Sol seçili hedef ile sağ mevcut rota ayrı kaynaklardır; TARGET tek başına yeni rota hesaplamaz. Gerçek gemi terminalindeki durumla eşleşmeli.
6. GUI kapat/aç ve menzil kaybı/geri dönüşte eski Navigation verileri erişilebilir kalmamalı; yeniden bağlantıda güncel veri gelmeli.
7. İstenirse `config/newworldcore/player-navigation.properties` içindeki show_coordinates/show_we_estimate seçeneklerini canlı değiştir; gizleme yalnız sunumu etkiler. refresh_ticks varsayılan 20, stale_after_ticks 120. Test sonrası ortak varsayılanları geri koy.

Log: `[NewWorld Player Navigation]`. `snapshot failed`, `sampling failed`, `render failed`, `decode rejected` gerçek runtime'da incelenmelidir; smoke testindeki kasıtlı bozuk çerçevelerle karıştırılmaz.

## .69.1 favori seçici — temel kabul geçti

28 Eylül ekranları: Trial Chambers ve Archeologist Camp, SYNC 2/2; TARGET sonrası sol hedef Trial Chambers `[-2434,-9,150]`, 80 blok. Sağ rota önceki Carbon durağında `[-2376,68,376]`, 1/1 hop, 219 blok, 52 WE kaldı. Kullanıcı “çalıştı” dedi. Log 15:36:28.600 favoritesOnly=true synced=2; 15:36:31.779 Trial Chambers TARGET; 15:36:32.999 yeni hedef/eski READY_HOP1_LOADED görünümü doğruladı. Bu temel seçme/rotayı koruma kabulüdür; aşağıdaki boş liste/silme/config/link kenar testleri ayrıca açık kalır.

1. Discoveries'te iki farklı kaydı FAV ile favoriye al. Navigation → FAVORITES aç; yalnız ortak favoriler görünmeli. Liste boşsa NO FAVORITES açıklaması gelmeli.
2. Bir favoriyi seçip TARGET bas; TARGET SET sonrasında < NAVIGATION ile dön. Sol hedef değişmeli, sağdaki mevcut rota/durak aynı kalmalı. Bu işlem rota hesaplamaz, uçuş başlatmaz veya WE harcamaz. Yeni rota için mevcut Discoveries ROUTE ya da fiziksel terminal kullanılır.
3. Discoveries'te favoriyi kaldır; Navigation favori listesinde REFRESH yapınca kaybolmalı. GUI kapat/aç ve sekme geçişinde normal Discoveries listesi favorilerle sınırlı kalmamalı.
4. Favori seçici açıkken bağlantı kesilirse ortak Link kilidi görünmeli; eski kayıttan TARGET yazımı engellenmeli. Dönüşte Navigation ve yeniden açılan FAVORITES güncel veriyi göstermeli.
5. İsteğe bağlı canlı config testi: `player-navigation.properties` içinde `favorites.enabled=false` seçiciyi kapatmalı; true geri açmalı. `favorites.sync_limit` 16–512 arasında toplam favori aktarım sınırıdır (varsayılan 128). Eski favoriler yakın tarihli keşif kotasının dışında da listelenir; SYNC aktarılan/toplam favoriyi gösterir. Ayar sonrası REFRESH kullan; ortak varsayılanları geri getir.

Sekiz otomatik test grubu favori adayında geçti; bu testler yukarıdaki oyun kanıtından ayrıdır.

## .69.2 SAVE CURRENT LOCATION — temel oyun kabulü geçti

28 Eylül: ilk [-2454,63,181] konumu 16:38:14.955 kaydedildi (3/3); 16:38:19.368 aynı kayıt yeniden seçildi ve sayı 3/3 kaldı. İkinci [-2454,63,189] konumu 16:38:54.240 kaydedildi, 16:38:55.776 liste 4/4 oldu. İki ekran WAYPOINT/MANUAL koordinatlarını doğruladı. Kullanıcı dünyadan çıkıp geri girdiğinde kayıtların durduğunu açıkça doğruladı; bu son adım kullanıcı bildirimidir. Kayıt aralığında ilgili hata bulunmadı. Aşağıdaki iç-mekân/config/çok oyunculu kenar testleri ayrıca açık kalır.

1. Geminin dışında, LINK CONNECTED iken NAVIGATION → SAVE CURRENT LOCATION bas. Altta LOCATION SAVED // OPEN FAVORITES beklenir. Hedef ve mevcut rota değişmemeli.
2. FAVORITES aç; `LOCATION x y z` kaydı oyuncunun bastığı andaki blok koordinatında ve boyutunda olmalı. Ayrıntıda WAYPOINT // MANUAL, SAVED COORDINATES gösterilir; bu bir yapı/maden keşfi değildir. ALL listesinde de görünür, STRUCTURES/GEOLOGY filtrelerinde görünmez.
3. Aynı blokta 2 saniye bekleyip yeniden kaydet; kopya oluşmamalı. O koordinatta gerçek bir keşif zaten varsa onun bilgileri korunup yalnız favoriye eklenir.
4. GUI kapat/aç; ardından dünyadan çıkıp geri girerek favorinin, koordinatın ve WAYPOINT/MANUAL türünün kalıcı olduğunu kontrol et. TARGET ile bu konumu seçebilirsin; kayıt işlemi kendi başına hedef seçmez/rota hazırlamaz/uçuş başlatmaz.
5. TARDIS içindeyken basınca EXIT THE SHIP... beklenir. Bağlantı kaybında ortak erişim kilidi geçerlidir. İsteğe bağlı config: `location.enabled=false` yazımı kapatır; `location.max_per_ship` limit dolunca yeni konumu reddeder, mevcutları silmez; `location.cooldown_ticks` kayıt beklemesidir. Varsayılanlar true/128/40; test sonrası geri getir.

Dokuz otomatik test grubu ve gerçek motor WE testi geçti. Konum testi: tekrar/aynı-koordinat delil koruma, boyut anahtarı, kota, izin/iç-mekân/bekleme politikası, canlı config sınırları, gerçek yamalı metadata save/load ve GUI mesaj sınırları. Yukarıda açıklanan temel oyun kabulü de geçti; Aşama 9 genişletilmiş kapsamı kısmi kalır.

## .69.3 SEND TO SHIP — hedef yazımı/rotayı koruma geçti; genişletilmiş kabul açık

29 Eylül ekranları ve 09:55:02.951 sunucu logu seçili ortak hedefin [-2454,63,181] olduğunu, eski Carbon rotasının değişmediğini doğruladı. Oyun kapandıktan sonra salt-okunur kayıt incelemesinde iki konum da Y=63 olarak duruyor. Fiziksel terminalde Y=-64/mesafe=0 görünmesi kayıt kaybı değil, terminal projeksiyonu ve eski Distance alanının kullanılmasıydı. `.69.4` bu alanları ve kayıtlı konumun son rota yüksekliğini düzeltir. Mesaj/cooldown/config/çok oyunculu kenar durumlarını ayrıca geçmiş sayma.

Favorilerdeki eski TARGET düğmesi geniş SEND TO SHIP düğmesi oldu. Aynı sunucu hedef yazıcısını ve paket isteğini kullanır; yeni rota veya uçuş sistemi değildir. Kayıt zaten ortak veritabanındadır, ikinci kopya üretilmez. Seçili kayıt geminin ortak Navigation hedefi olur; fiziksel terminalden de okunur. Discoveries içindeki TARGET/ROUTE değişmedi. Eski `.69.1` hedef testi bu yeni düğme/mesaj/config kabulünün yerine sayılmaz.

1. Bağlantı açıkken NAVIGATION → FAVORITES aç. Kaydettiğin LOCATION satırlarından birini seç; SEND TO SHIP bas. Altta SENT TO SHIP beklenir.
2. < NAVIGATION ile dön: sol hedef LOCATION ve doğru koordinat/boyut olmalı. Sağdaki mevcut rota/sonraki durak eskisi gibi kalmalı. Fiziksel Navigation Terminal seçili hedefi de aynı kaydı göstermeli. Gemi hareket etmemeli; işlem WE tüketmemeli.
3. Başka konumu seçip gönder. İki saniye içinde ikinci istek olursa SEND WAIT gelir; bekledikten sonra tekrar gönderilebilir. REFRESH beklemeyi sıfırlamaz. Başarılı seçim sonrası GUI kapat/aç güncel hedefi göstermeli.
4. İsteğe bağlı: `player-navigation.properties` içinde `send_to_ship.enabled=false` bu düğmeyi/sunucu yazımını kapatır. `gui.properties` içindeki `player.discoveries.enable_target_action=false` ortak hedef iznini de kapatır. Ortak varsayılanları geri getir. `send_to_ship.cooldown_ticks=40` (20–1200) canlı bekleme ayarıdır.
5. Bağlantı yokken eski favori yazımı engellenmeli; başka gemiye ait eski snapshot kullanılamaz. Genişletilmiş çok oyunculu/boyut/menzil kontrolleri oyun içinde ayrıca açık kalır.

On otomatik test grubu + gerçek DoctorWhoMod WE fixture geçti. Yeni test canlı config/default/sınırlar, ortak hedef izni, cooldown sınırı/oyuncu izolasyonu/refresh, gerçek ortak seçici metodunun hedef yazımı ve rota/enerjiye dokunmaması, düğme/mesaj çizim sınırları ve Discoveries düğmelerinin korunmasını kontrol eder. Tam Minecraft ortamındaki uçtan uca gönderme bu otomatik testlerin kapsamında değildir.
