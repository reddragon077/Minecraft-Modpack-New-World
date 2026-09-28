# Player Navigation — ilk görünüm kabulü

Kurulu aday `.69.2-alpha-save-current-location`. Önceki görünüm/GUI kapat-aç ve `.69.1` temel favori seçimi kabul edildi. Yeni SAVE CURRENT LOCATION oyun testi bekliyor; genişletilmiş kontrol listesi tamamlanmadı.

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

## .69.2 SAVE CURRENT LOCATION — oyun kabulü bekliyor

1. Geminin dışında, LINK CONNECTED iken NAVIGATION → SAVE CURRENT LOCATION bas. Altta LOCATION SAVED // OPEN FAVORITES beklenir. Hedef ve mevcut rota değişmemeli.
2. FAVORITES aç; `LOCATION x y z` kaydı oyuncunun bastığı andaki blok koordinatında ve boyutunda olmalı. Ayrıntıda WAYPOINT // MANUAL, SAVED COORDINATES gösterilir; bu bir yapı/maden keşfi değildir. ALL listesinde de görünür, STRUCTURES/GEOLOGY filtrelerinde görünmez.
3. Aynı blokta 2 saniye bekleyip yeniden kaydet; kopya oluşmamalı. O koordinatta gerçek bir keşif zaten varsa onun bilgileri korunup yalnız favoriye eklenir.
4. GUI kapat/aç; ardından dünyadan çıkıp geri girerek favorinin, koordinatın ve WAYPOINT/MANUAL türünün kalıcı olduğunu kontrol et. TARGET ile bu konumu seçebilirsin; kayıt işlemi kendi başına hedef seçmez/rota hazırlamaz/uçuş başlatmaz.
5. TARDIS içindeyken basınca EXIT THE SHIP... beklenir. Bağlantı kaybında ortak erişim kilidi geçerlidir. İsteğe bağlı config: `location.enabled=false` yazımı kapatır; `location.max_per_ship` limit dolunca yeni konumu reddeder, mevcutları silmez; `location.cooldown_ticks` kayıt beklemesidir. Varsayılanlar true/128/40; test sonrası geri getir.

Dokuz otomatik test grubu ve gerçek motor WE testi geçti. Konum testi: tekrar/aynı-koordinat delil koruma, boyut anahtarı, kota, izin/iç-mekân/bekleme politikası, canlı config sınırları, gerçek yamalı metadata save/load ve GUI mesaj sınırları. Henüz `.69.2` oyun testi yapılmadı. SEND TO SHIP sonraki iştir; Aşama 9 kısmi kalır.
