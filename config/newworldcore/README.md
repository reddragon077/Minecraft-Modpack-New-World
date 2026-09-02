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
- `player.properties`: oyuncu Structure Field Survey menzili ve görünür tarama süresi.
- `gui.properties`: oyuncu arayüzü karartması, canlı Survey bilgi satırı ve filtre katman derinliği.

Güvenli config sınırı: oynanış dengesi, süre, menzil, enerji, kapasite, performans ve görünüm ayarlanabilir;
kayıt şeması, paket/protokol kimlikleri ve registry anahtarları config değildir. Bunların değişmesi dünyayı veya ağ iletişimini bozabilir.

Değişiklikleri iki bilgisayara taşımak için GitHub `main` ve proje senkronizasyon araçları kullanılmalıdır.
