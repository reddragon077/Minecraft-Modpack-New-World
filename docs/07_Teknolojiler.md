# Teknolojiler ve mod rolleri

Durum: aktif paket mimarisi

## Çekirdek katmanlar

| Katman | Ana araçlar | New World rolü |
|---|---|---|
| Gemi ve seyahat | NewWorldCore, DoctorWhoMod fork | Oda, motor, radar, navigasyon, TARDIS seyahati |
| Enerji ve ağ | New World Ship Network, Mekanism, Powah | FE üretimi/taşıma, acil rezerv ve modüller |
| Depolama | AE2 ve eklentileri | Gemi verisi, eşya ağı ve üretim entegrasyonu |
| Madencilik ve jeoloji | Mining M1, Geological Radar | Fiziksel deposit tarama ve çıkarma |
| Replikasyon | Replication ailesi + KubeJS | Bilgi ve Matter tabanlı ham kaynak üretimi |
| Üretim | Create, Mekanism, Oritech, IE, Custom Machinery | Uzmanlaşmış işleme ve Production Chamber tabanı |
| Araştırma | AStages, Pufferfish Skills, FTB Quests | Erişim kapıları, beceri ve anlatı progression’ı |
| Bilgisayarlar | CC:Tweaked | Gemi, mining ve navigation telemetrisi |
| İnşa | Building Gadgets 2, WorldEdit | Gemi odaları, şemalar ve geliştirme araçları |

## Kaynak kimliği ilkesi

Bir mineral birden fazla modda bulunabilir ancak New World tarafında tek bilgi/deposit ailesine bağlanır:

- Lead: Mekanism + Immersive Engineering
- Nickel: Immersive Engineering + Oritech
- Uranium: aynı gerçek kaynağı temsil eden modlar
- Uraninite: Powah’a özgü ve Uranium’dan ayrı
- Certus Quartz: normal AE2 Certus; Charged Certus otomatik olarak dahil değildir

## Teknoloji açılımı

Teknolojiler yalnız tarif sırasına göre değil; keşif kaydı, araştırma aşaması, gemi odası, enerji kapasitesi ve rota erişimi birlikte değerlendirilerek açılmalıdır.

## Sürüm kaynağı

Bu belgede yazan mod rolleri kavramsaldır. Kesin dosya adı ve sürüm için `pack-lock.json`, CurseForge kimlikleri için `manifest.json` esas alınır.
