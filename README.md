# New World

> Bilgi, bu evrendeki en değerli kaynaktır.

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen)
![NeoForge](https://img.shields.io/badge/NeoForge-21.1.235-blue)
![Durum](https://img.shields.io/badge/Durum-Alpha-orange)
![Ana%20Dal](https://img.shields.io/badge/GitHub-main-black)

New World; keşif, araştırma, jeoloji, otomasyon ve yaşayan bir uzay gemisi etrafında şekillenen hikâye odaklı bir Minecraft mod paketidir. Oyuncunun amacı yalnızca daha büyük makineler kurmak değil; bilinmeyen bir evrende bilgi toplayarak gemisini, üretim altyapısını ve seyahat yeteneklerini aşamalı biçimde geri kazanmaktır.

## Güncel teknik temel

- Minecraft `1.21.1`
- NeoForge `21.1.235`
- 267 CurseForge modu, 4 kaynak paketi ve 4 shader paketi
- 274 etkin, 1 bilinçli olarak devre dışı bırakılmış CurseForge öğesi
- Projeye ait iki özel fork: NewWorldCore ve DoctorWhoMod
- Güncel alpha: `0.5.72.1-alpha-unified-ship-alerts` (29 Eylül 2026). Düşük FE uyarısı/geçmiş/çözülme ekranla, HUD bildirimi ve arayüzü kapatıp açınca geçmişin korunması kullanıcı testiyle doğrulandı. Bu temel kabul, tüm uyarı türleri ve çok oyunculu durumların tamamının test edildiği anlamına gelmez. [Kabul kapsamı](docs/18_Ship_Alerts_Runtime_Kabul.md).
- Runtime-kabul edilen `DISCOVERIES` sekmesi ortak Structure/Geology geçmişini, analiz/kaynak/rezerv/son-görülme ayrıntılarını ve canlı oyuncu mesafesini gösterir; kayıtlar favoriye alınabilir, aktif Navigation hedefi yapılabilir ve gerçek TARDIS rota/hop planına bağlanabilir.
- Aktif DoctorWhoMod fork buildi: `1.0.16-NewWorld-EngineTravel-v5.8.19-Tall-Large-XLarge-Swap`

Kesin eklenti sürümleri [`manifest.json`](manifest.json) ve [`pack-lock.json`](pack-lock.json) içinde tutulur. Üçüncü taraf JAR dosyaları GitHub’a eklenmez; yalnızca projeye ait iki fork JAR’ı repoda saklanır.

NewWorldCore çalışma ayarları [`config/newworldcore/`](config/newworldcore/) altında sistemlere ayrılmıştır. Radar hızı dahil çalışan denge ve performans değerleri bu dizinden yönetilir; her ayarın birimi, etkisi ve artırıp azaltmanın sonucu dosya içindeki Türkçe yorumlarda açıklanır.

## Bugün çalışan ana sistemler

İlk kez bakıyorsanız [oyuncu rehberinden](docs/19_Oyuncu_Rehberi.md) başlayın. Son gelişmeler [değişiklik özetinde](CHANGELOG.md); henüz tamamlanmayan işler [bilinen sınırlarda](docs/Known%20Issues.md) açıklanır.

| Sistem | Durum | Kısa açıklama |
|---|---|---|
| Gemi oda/matrix altyapısı | Alpha | Oda kabukları, kontrolcüler, koruma, gemi panel aileleri ve dekor blokları |
| Gemi ağı | Alpha | FE, eşya, sıvı ve gaz modülleri; oda bağlantıları, öncelik ve telemetri |
| Mining M1 | Alpha | İki aşamalı tarama/çıkarma, kalkan ve el freni koşulları, yükseltmeler ve derin depolama |
| Jeoloji | Alpha | Fiziksel deposit worldgen, radar eşleşmesi, kalıcılık ve modlu maden genişletmesi |
| Navigasyon | Alpha | Dinamik vanilla/modlu yapı radarı, keşif veritabanı, geçmiş/favoriler, rota hesaplama ve çok duraklı seyahat |
| Player Ship Interface | Alpha; temel akışlar doğrulandı | Altı sekme: Overview, Survey, Discoveries, Navigation, Mining ve Emergency |
| Overview / Ship Link | Temel tek oyunculu kabulü geçti | FE/WE, motor, kalkan, rota ve Matrix durumu; canlı bağlantı, mesafe ve kayıp/geri dönüş |
| Konumlar ve favoriler | Temel kabulü geçti | Konum kaydetme, kopya önleme, yeniden girişte kalıcılık, SEND TO SHIP; hedef ve mevcut rota ayrı tutulur |
| Player Mining | Temel kabulü geçti; genişletilmiş testler açık | Tarama/çıkarma bilgisi, çıkarılan kaynak sayıları, onaylı STOP MINING; yeniden başlatma fiziksel terminalde |
| Emergency Return | Temel kabulü geçti; genişletilmiş testler açık | Kendi gemisinin iç giriş kapısına dönüş; başarı sonrası 30 gerçek dakika kalıcı bekleme. Beacon yok |
| Ship Alerts | Temel FE/geçmiş/HUD kabulü geçti | Overview uyarıları, Collection/drive kontrolleri, sınırlı oturum geçmişi; çözülenler RESOLVED |
| Replikasyon | Alpha | Doğal kaynak tarama bilgisi, ham madde Matter değerleri ve üretim kısıtları |
| TARDIS/gemi seyahati | Deneysel | DoctorWhoMod fork’u ile fiziksel seyahat ve NewWorldCore rota/engine bağlantıları |
| Araştırma ve görev ilerlemesi | Tasarım/prototip | AStages, Pufferfish Skills ve FTB Quests tabanı mevcut; bütün içerik zinciri tamamlanmadı |
| Production Chamber | Tasarım/prototip | Custom Machinery entegrasyonları ve oda altyapısı hazır; nihai üretim ağacı tamamlanmadı |
| Genetik gelişim | Tasarım | Uzun vadeli ilerleme katmanı; oynanabilir sistem henüz tamamlanmadı |

## Ana oyun döngüsü

1. Bir bölgeyi, yapıyı veya jeolojik kaynağı keşfet.
2. Radar, terminal veya laboratuvar yoluyla veriyi analiz et.
3. Bilgiyi araştırma ve ilerleme kaydı olarak aç.
4. Gerekli kaynağı çıkar, taşı veya replikasyonla yeniden üret.
5. Gemi odalarını, ağı, motoru ve navigasyonu geliştir.
6. Daha uzak ve daha tehlikeli hedeflere ulaş.

Detaylı akış için [`docs/01_Oyun_Döngüsü.md`](docs/01_Oyun_Döngüsü.md) belgesine bakın.

## Depo düzeni

```text
config/          Paylaşılan mod paketi ayarları
defaultconfigs/  Yeni dünyalara uygulanacak sunucu ayarları
kubejs/          Replikasyon ve paket davranış betikleri
mods/            İki özel fork JAR’ı ve teknik analizler
docs/            Vizyon, mekanikler, hikâye ve teknik tasarım
machines/        Geliştirme bilgisayarlarının bağlantı kayıtları
tools/           CurseForge ↔ GitHub senkronizasyon araçları
src-patches/     Özel JAR’lar için yeniden üretilebilir kaynak yamaları
manifest.json    CurseForge içe aktarma manifesti
pack-lock.json   Kesin eklenti/sürüm/hash kaydı
```

## Belgeler

Belge haritası ve durumları için [`docs/README.md`](docs/README.md) dosyasını kullanın. Güncel mod listesi [`mods/00_kullanılan modlar .md`](mods/00_kullan%C4%B1lan%20modlar%20.md) içinde manifestten üretilir.

## Sıradaki geliştirme odağı

Ana sıra [`docs/12_Gelistirme_Yol_Haritasi.md`](docs/12_Gelistirme_Yol_Haritasi.md) belgesidir.

1. Aşama 12 Ship Alerts'in kalan kapsamı: henüz oyun içinde doğrulanmayan uyarı türleri ve yeni Structure/Geological Discovery bildirimleri.
2. Navigation, Mining ve Emergency'nin açık izin/bağlantı/çok oyunculu uç durumlarını takip etmek; kabul edilmiş temel akışları tekrar yapılacak iş gibi listelememek.
3. Yol haritası sırasıyla Aşama 13 deposit üretiminin gelişmiş varyasyonları ve Aşama 14 gerçek rezerv/çıkarma/tükenme entegrasyonu.

`EST RESERVE` tahmini başlangıç bilgisidir; Mining çıkarma yüzdesi kalan deposit rezervi değildir. Gerçek remaining/depletion ve `DEPLETED` bağlantısı henüz tamamlanmadı.

## Proje durumu

Bu repo yayınlanmış son kullanıcı sürümünden çok aktif geliştirme çalışma alanıdır. Dünyalar, kişisel seçenekler, loglar, yedekler ve launcher kimlik bilgileri GitHub’a alınmaz.
