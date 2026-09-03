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
- Aktif runtime-kabul edilen laptop buildi `0.5.65.2-alpha-player-discoveries`. Kademeli Geological Analysis korunurken Player Ship Interface içindeki `DISCOVERIES` sekmesi ortak veritabanından son Structure/Geology kayıtlarını, analiz/kaynak ayrıntılarını ve canlı oyuncu mesafesini gösterir; farklı boyuttaki hedefler açıkça işaretlenir.
- Aktif DoctorWhoMod fork buildi: `1.0.16-NewWorld-EngineTravel-v5.8.19-Tall-Large-XLarge-Swap`

Kesin eklenti sürümleri [`manifest.json`](manifest.json) ve [`pack-lock.json`](pack-lock.json) içinde tutulur. Üçüncü taraf JAR dosyaları GitHub’a eklenmez; yalnızca projeye ait iki fork JAR’ı repoda saklanır.

NewWorldCore çalışma ayarları [`config/newworldcore/`](config/newworldcore/) altında sistemlere ayrılmıştır. Radar hızı dahil çalışan denge ve performans değerleri bu dizinden yönetilir; her ayarın birimi, etkisi ve artırıp azaltmanın sonucu dosya içindeki Türkçe yorumlarda açıklanır.

## Bugün çalışan ana sistemler

| Sistem | Durum | Kısa açıklama |
|---|---|---|
| Gemi oda/matrix altyapısı | Alpha | Oda kabukları, kontrolcüler, koruma, gemi panel aileleri ve dekor blokları |
| Gemi ağı | Alpha | FE, eşya, sıvı ve gaz modülleri; oda bağlantıları, öncelik ve telemetri |
| Mining M1 | Alpha | İki aşamalı tarama/çıkarma, kalkan ve el freni koşulları, yükseltmeler ve derin depolama |
| Jeoloji | Alpha | Fiziksel deposit worldgen, radar eşleşmesi, kalıcılık ve modlu maden genişletmesi |
| Navigasyon | Alpha | Dinamik vanilla/modlu yapı radarı, keşif veritabanı, geçmiş/favoriler, rota hesaplama ve çok duraklı seyahat |
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

1. Player `DISCOVERIES` detaylarında son görülme/tahmini rezerv alanlarını tamamlamak.
2. Discovery kaydından navigasyon hedefi, rota ve favori işlemlerini bağlamak.
3. Player Ship Interface'in Navigation ve Mining panellerini tamamlamak.
4. Deposit extraction, remaining, depletion ve `DEPLETED` entegrasyonunu bitirmek.

## Proje durumu

Bu repo yayınlanmış son kullanıcı sürümünden çok aktif geliştirme çalışma alanıdır. Dünyalar, kişisel seçenekler, loglar, yedekler ve launcher kimlik bilgileri GitHub’a alınmaz.
