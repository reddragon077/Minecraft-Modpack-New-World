# NewWorldCore sistem ayarları

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
