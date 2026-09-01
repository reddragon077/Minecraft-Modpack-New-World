# Warp ve gemi seyahati

Durum: DoctorWhoMod fork + NewWorldCore üzerinde deneysel alpha

## Seyahat sınıfları

- **Yerel:** aynı dimension içinde mesafeye bağlı seyahat; Range Coil sınırı uygulanır.
- **Gezegensel:** aynı namespace içindeki farklı dimension geçişi; Planetary veya Universal Drive gerektirir.
- **Evrensel:** farklı dimension namespace geçişi; Universal Drive gerektirir.

Fork notlarına göre gezegensel temel maliyet 500 WE, evrensel temel maliyet 2500 WE’dir; verimlilik modülleri maliyeti etkiler. Bu değerler alpha denge değerleridir ve oyun testiyle doğrulanmalıdır.

## Aktif bileşenler

- Engine Room Controller
- Range Coil, Efficiency Module ve Stabilizer
- Dimensional/Universal Drive bileşenleri
- Warp Room Controller
- Warp Capacitor, Converter, Efficiency Coil ve Catalyst Chamber kademeleri
- Engine Matrix menzil bağlantısı
- Route Calculation Computer ve çok duraklı rota planı

## Enerji ve stabilizasyon

Warp üretimi saniyelik döngüyle ele alınır; temel Converter 1 WE/sn üretir. Başarılı seyahat sonrası stabilizasyon/cooldown gemi verisinde kalıcı tutulur. Düşük enerji hesap veya taramayı ilerleme kaybetmeden duraklatmalıdır.

## Navigasyon bağlantısı

Radar hedefi doğrudan uçuş başlatmaz. Hedef seçilir, Route CPU rota hesaplar, Engine Matrix menzili değerlendirilir ve gerekirse ara duraklar oluşturulur. Gerçek dış gövde varışı bir sonraki hop’a geçişi tetikler.

## DoctorWhoMod fork rolü

Fork; fiziksel starter davranışı, merkezi engine travel kuralları, başlangıç motor bileşenleri, New World ana köprüsü, koridor/oda yapıları ve TARDIS flight/materialization altyapısını sağlar.

## Test kapısı

Yerel, gezegensel ve evrensel seyahat ayrı ayrı; enerji düşümü, sürücü gereksinimi, cooldown kalıcılığı, güvenli iniş ve sunucu yeniden başlatma senaryolarıyla doğrulanmalıdır.
