# NewWorldCore sistem ayarları

`ship-alerts.properties`: salt-okunur Aşama 12 uyarıları. `enabled`, `refresh_ticks` (20–1200), `history_limit` (4–32), `collection.warning_percent`, `notifications.enabled`, `notifications.repeat_seconds` (5–3600), `notifications.spacing_seconds` (3–30) canlı okunur. `.72.1` ile tüm Overview uyarıları kırpılmamış ortak örneklemeden alınır. FE/WE eşikleri yalnız `overview.properties` içindeki `warning_percent` / `critical_percent` değerleridir; eski `warp.warning_percent` / `warp.critical_percent` artık kullanılmaz. Matrix offline ve BUFFER_FULL seviyeleri Overview ile aynı WARNING; bozuk motor/NO_ENERGY CRITICAL. Eksik veri ayrıca telemetri uyarısı üretir, önceki arızayı çözülmüş saymaz. Collection yüzdesi eşya değil tür slotu doluluğudur. Eksik drive yalnız geminin mevcut uçuş hedefi farklı dimension gerektirdiğinde değerlendirilir. Geçmiş sunucu oturumu boyunca tutulur; çıkışta sıfırlanır. Küçük bildirim vanilla action-bar alanını paylaşır.

Bu dizindeki `.properties` dosyaları NewWorldCore'un çalışan denge ve performans ayarlarıdır.
Değerler en geç bir saniye içinde yeniden okunur; Radar yeni tarama başlarken tüm önbelleği yeniler.
Hatalı veya güvenli sınırın dışındaki değerler kod içindeki güvenli sınıra çekilir.
Her ayarın üstünde birimi, etkisi ve artırıp azaltmanın sonucu Türkçe yorumlarla açıklanmıştır.

Genel okuma kuralları:

- `tick`: Minecraft zaman birimi; 20 tick yaklaşık 1 saniyedir.
- `FE`: Forge Energy, `FE/t`: tick başına enerji aktarımıdır.
- `WE`: Warp Energy'dir.
- `level_0`: ilgili yükseltme takılı değilken kullanılan değer; `level_1/2/3` yükseltme seviyeleridir.
- `multiplier`: çarpan; `1.0` değişiklik yok, `1.5` yüzde 50 artış, `0.5` yarıya düşüş demektir.
- `percent`: yüzde; enerji verimliliğinde daha düşük değer genellikle daha düşük FE tüketimidir.
- Performans ayarlarında daha hızlı/daha büyük değerler tek tickte daha fazla çalışma ve daha fazla takılma riski oluşturabilir.
- Büyük değişikliklerden önce dosyanın bir kopyasını alın; sorun olursa repodaki varsayılan değere dönün.

Önemli Radar hız örneği:

- `scan.batch_interval_ticks=4`: önceki Radar paket hızı.
- `scan.batch_interval_ticks=8`: yaklaşık yarı hız; pakette gönderilen değer budur.
- `scan.batch_interval_ticks=16`: yaklaşık çeyrek hız.

Dosyalar:

- `player-mining.properties`: Mining telemetrisi ve onaylı durdurma; `refresh_ticks=20` (20-1200), `stale_after_ticks=120` (40-3600, en az iki yenileme), `show_scan_area=true`, `show_buffers=true`. Canlı uygulanır. İlerleme kayıtlı tarama alanınındır, deposit rezervi değildir. Bufferlar eşya toplamı ve kaynak türü/256 yuva olarak gösterilir; SMART AUTO mevcut yönlendirme durumudur, kontrol değildir. Eksik/yüklü olmayan buffer sahte sıfır yerine açık durum gösterir. `.70.1`: `top_resources.rows=5` (1-5), `stop.enabled=true`, `stop.confirm_ticks=100` (40-200; istemci en fazla 2 saniyelik onay düğmesi), `stop.cooldown_ticks=40` (20-1200). Sunucu her istekte sahiplik/bağlantı/gemi/config kontrolü yapar. STOP yalnız Mining Shield OFF; yeniden başlatma, el freni ve routing kontrolü yok. Kaynak sayaçları gerçek başarılı kazılan blokları mevcut tarama alanı için saklar; stack adedi veya geçmişe dönük tahmin değildir.

- `player-navigation.properties`: Navigation telemetrisi; `refresh_ticks=20`, `stale_after_ticks=120`, `show_coordinates=true`, `show_we_estimate=true`. WE yalnız yüklü sonraki hop içindir. `favorites.enabled=true` ve `favorites.sync_limit=128` (16-512), ortak favori seçiciyi yönetir. `send_to_ship.enabled=true`, `send_to_ship.cooldown_ticks=40` (20-1200) seçili favorinin mevcut TARGET yazıcısıyla gemiye gönderilmesini yönetir; rota/uçuş başlatmaz veya WE harcamaz. Discoveries hedef izni ayrıca uygulanır; normal Discoveries TARGET/ROUTE değişmez. Liste son keşif kotasından bağımsızdır; REFRESH ile yenilenir ve SYNC gelen/toplam sayısını gösterir. `location.enabled=true`, `location.max_per_ship=128` (1-1024), `location.cooldown_ticks=40` (20-1200), SAVE CURRENT LOCATION işlemini yönetir. Sunucu oyuncu konumunu WAYPOINT/MANUAL favorisi olarak kaydeder; TARDIS iç mekânı reddedilir, aynı koordinat çoğaltılmaz, var olan keşif ezilmez, limit dolunca eski kayıt silinmez. Konum kaydı hedef/rota/WE değiştirmez; bütün seçenekler canlıdır.

- `radar.properties`: yapı Radar'ı, navigasyon yükseltmeleri, CPU ve tarama FE maliyeti.
- `mining.properties`: Mining Matrix tarama miktarı, kazım aralığı ve FE maliyeti.
- `matrix.properties`: FE/Warp Matrix kapasitesi, aktarım, tier ağırlıkları ve üretim.
- `travel.properties`: motor modüllerinin azami ışınlanma menzili.
- `geology.properties`: jeoloji taramasının FE dengesi.
- `replication.properties`: replikasyon besleme aralığı ve parti büyüklüğü.
- `rooms.properties`: oda koruma sistemi anahtarı.
- `network.properties`: acil enerji rezervi ile FE/item/fluid/gas düğüm hız ve kapasite çarpanları.
- `player.properties`: oyuncu Structure/Geological Field Survey menzilleri, gecikmeleri ve fiziksel depozit doğrulama sınırları.
- `gui.properties`: oyuncu arayüzü karartması, canlı Survey bilgi satırı ve filtre katman derinliği.
- `discovery.properties`: Structure/Geology Radar ve Field Survey kaynaklarının kalıcı başlangıç analiz seviyeleri.
- `overview.properties`: salt-okunur Gemi Durumu ekranı; `refresh_ticks` (20, kullanıcının seçimi), `stale_after_ticks` (120), `warning_percent` (20), `critical_percent` (5), `warning_rows` (2), `show_resolved_warnings` (true).
- `ship-link.properties`: sürekli gemi bağlantısı; `range_blocks` (5000), `allow_dimensional_link` (true), `refresh_ticks` (20), `stale_after_ticks` (120). Sunucu ayarları yetkilidir; istemci yenileme/geçerlilik sürelerini sunucudan alır.

Ship Link bütün Player GUI sekmelerinin başlığında görünür. Kendi geminin içinde `CONNECTED // ON BOARD`,
aynı boyutta 3B menzil içinde `CONNECTED`, farklı boyutta izin varsa `DIMENSIONAL` gösterilir.
Menzil dışı, kapalı boyut bağlantısı, sahip olunan yüklü gemi bulunamaması veya eskimiş veri `LOST` olur.
Survey ve Discoveries işlemleri istemcide ve sunucuda kilitlenir; gecikmeli Survey tamamlanırken tekrar denetlenir.
Devam eden gemi Mining/rota işi durdurulmaz; sadece Player GUI üzerinden yeni uzaktan işlemler engellenir.
Yeniden bağlantıda Discovery listesi tazelenir; önceki gemiye ait seçimin başka gemiye uygulanmasına izin verilmez.

Overview FE satırındaki `OUT`, ortak FE havuzundan gerçekten çekilen enerjinin tick başına ortalamasıdır;
simülasyon çağrıları sayılmaz, dışarı enerji aktarımı da dahildir. `NET`, iki örnek arasındaki depolanan FE farkıdır;
üretim ve tüketimin birlikte etkisidir. İkisi de yenileme aralığının ortalamasıdır; ilk örnek `SAMPLING` gösterir.
Doğrudan eski enerji aynasına yapılan yönetici atamaları OUT tüketimi sayılmaz; sayaç dünyaya kaydedilmez.
Matrix kaydı yoksa `UNKNOWN`, ayrılmışsa `OFFLINE` görünür; bilinmeyen değerler sıfır enerji gibi yorumlanmaz.
Son uyarılar yalnız mevcut oyuncu bağlantısı/gemi gözlem oturumu içindir, kalıcı olay günlüğü değildir.
Aktif uyarılar sarı `[ACTIVE]`, aktif kritik durumlar kırmızı `[CRITICAL]`, çözülenler gri `[RESOLVED]` görünür.
Sunucu her kaydın durumunu ayrı belirler; kritik/aktif kayıtlar geçmiş kayıtlarından önce gösterilir.
`show_resolved_warnings=false` çözülenleri gizler; bu görünüm ayarı canlı yenilenir, aktif uyarıları gizlemez.
Sunucu uyarı eşiklerini, istemci satır sayısını ve bayat veri süresini kendi configinden okur.

Güvenli config sınırı: oynanış dengesi, süre, menzil, enerji, kapasite, performans ve görünüm ayarlanabilir;
kayıt şeması, paket/protokol kimlikleri ve registry anahtarları config değildir. Bunların değişmesi dünyayı veya ağ iletişimini bozabilir.

Değişiklikleri iki bilgisayara taşımak için GitHub `main` ve proje senkronizasyon araçları kullanılmalıdır.

### Player Emergency (.71.2)

`player-emergency.properties`: `return.enabled=true`, `return.cooldown_minutes=30` (1–1440), `return.confirm_ticks=100` (40–200), `refresh_ticks=20` (20–1200), `stale_after_ticks=120` (40–3600, en az 2x refresh). Canlı ayarlar. Bekleme gerçek zamanlıdır; çevrimdışı süre sayılır, yeni değer yalnız sonraki başarılı dönüşe uygulanır. `PlayerPersisted/NewWorldEmergencyReturnUntilV1` oyuncu kaydı çıkış/giriş ve ölümde korunur.

Beacon kaldırıldı; eski beacon.* anahtarları kullanılmaz ve eski DISTRESS kayıtları silinmez. İki aşamalı tek kullanımlık onay kendi geminin yüklü ve güvenli konsol tarafı Teleporter Room noktasına oyuncuyu döndürür. Normal bağlantı mesafe/dimension politikası kurtarmayı engellemez; sahiplik, yüklü iç dünya, canlı/yaya oyuncu, uçuş/oda yenileme ve gerçek blok/çarpışma güvenliği yeniden kontrol edilir. Başka TARDIS içinden ve zaten kendi gemisindeyken dönüş reddedilir. Başarısız/engellenmiş dönüş bekleme tüketmez. Navigation hedefi/rotası, WE ve gemi uçuşu değişmez. Oyun kabulü docs/17 içindedir.
