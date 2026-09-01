# New World — Aktif geliştirme yol haritası

Son doğrulama: 1 Eylül 2026

Aktif temel: NewWorldCore `0.5.58.0-alpha-expanded-geology-deposits`

Bu belge, eski **Yeni Geliştirme Yol Haritası** listesinin çalışan JAR, güncel proje dosyaları ve oyun testiyle doğrulanmış hâlidir. Araştırma, Production Chamber ve sonraki progression çalışmaları bu yol haritasının 14 aşaması kapandıktan sonra ele alınacaktır.

Durum işaretleri:

- `[x]` Uygulandı ve dosya/JAR düzeyinde doğrulandı
- `[~]` Altyapısı var; işlev veya kabul testi eksik
- `[ ]` Henüz tamamlanmadı

## Aşama 1 — Radar v2

Durum: büyük ölçüde tamamlandı; modlu family adlandırma kabulü kapanmalı.

- [x] Sabit 13 öğelik kullanıcı filtresi kaldırıldı.
- [x] Filtreler Discovery Database içeriğine göre dinamik üretiliyor.
- [x] Yeni gemi yalnızca `ALL` filtresiyle başlıyor.
- [x] Ziyaret edilip tanımlanan yapı family’si ilgili filtreyi açıyor.
- [x] Aynı yapı ailesinin varyasyonları tek family altında gruplanıyor.
- [~] Bilinmeyen/modlu yapılar güvenli fallback ile çalışıyor; otomatik görünen ad ve family eşlemesi farklı modlu yapılarla kabul testinden geçirilmeli.
- [x] Radar GUI’de `STRUCTURES` ve `GEOLOGY` modları bulunuyor.
- [x] Structure ve Geology sonuçları birbirinden ayrıldı.
- [x] Range, Speed ve Accuracy yükseltmeleri iki tarama yoluna bağlandı.

Kapanış testi: yeni veya ziyaret edilmemiş en az üç modlu yapı ailesiyle dinamik ad/filtre davranışı.

## Aşama 2 — Ortak Discovery Database

Durum: ortak kalıcı veritabanı aktif; metadata şeması tamamlanmalı.

- [x] Gemi kapsamlı ortak `NavigationDiscoverySavedData` mevcut.
- [x] `STRUCTURE` ve `GEOLOGY` kategorileri mevcut.
- [x] Family/tip, dimension, koordinat, ilk keşif zamanı ve `RADAR`/`FIELD` kaynağı saklanıyor.
- [ ] Analiz seviyesi kalıcı discovery alanı olarak eklenmeli.
- [ ] Son görülme zamanı eklenmeli ve tekrar keşifte güncellenmeli.
- [x] Structure Radar filtreleri bu veritabanından üretiliyor.
- [x] Navigation Discoveries aynı veritabanına bağlı.
- [x] Geological deposit kayıtları aynı discovery modeline bağlı.
- [ ] Research/Exploration XP tarafından dinlenebilecek ortak discovery event altyapısı eklenmeli.

## Aşama 3 — Player Ship Interface

Durum: arayüz kabuğu ve tuş bağlantısı mevcut.

- [x] Oyuncunun tuşla açabildiği `PlayerShipScreen` mevcut.
- [x] `OVERVIEW`, `SURVEY`, `DISCOVERIES`, `NAVIGATION`, `MINING`, `EMERGENCY` sekmeleri oluşturuldu.
- [x] Tasarım, gemideki ayrıntılı terminallerin yerine geçmeyecek şekilde sınırlandı.
- [~] Yalnız `SURVEY/STRUCTURE` ilk işlevsel modüldür; diğer sekmelerin içeriği sonraki aşamalarda tamamlanacak.

## Aşama 4 — Overview / Ship Status

Durum: placeholder ekran var; canlı telemetri henüz bağlanmadı.

- [ ] Ship FE mevcut/kapasite ve anlık tüketim
- [ ] Warp Energy mevcut/kapasite
- [ ] Engine ve Handbrake durumu
- [ ] Mining Shield ve Mining durumları
- [ ] Navigation durumları
- [ ] FE, Warp ve Engine Matrix durumları
- [ ] TARDIS dimension ve koordinat
- [ ] `NOMINAL`, `WARNING`, `CRITICAL` genel durum hesabı
- [ ] Son sistem uyarıları

Not: Bu verilerin büyük bölümü fiziksel terminallerde ve ComputerCraft telemetrisinde zaten vardır; Player GUI salt-okunur ortak telemetri katmanına bağlanacaktır.

## Aşama 5 — Ship Link

Durum: Field Survey sırasında sunucu taraflı gemi çözümleme var; sürekli bağlantı modeli yok.

- [ ] Sürekli bağlantı göstergesi
- [ ] `CONNECTED`, `DIMENSIONAL`, `LOST` durumları
- [ ] Gemi dimension bilgisi
- [ ] Aynı dimensionda gemiye uzaklık
- [ ] Link kaybında uzaktan özellik kapıları

## Aşama 6 — Field Survey

Durum: Structure Survey çalışıyor; Geological Survey eksik.

- [x] Player GUI’de kısa menzilli Structure Scan mevcut.
- [ ] Player GUI’de Geological Scan etkinleştirilmeli; mevcut ekranda `OFFLINE // next phase` durumunda.
- [x] TARDIS Radar uzun menzilli, Field Survey yakın çevre odaklıdır.
- [x] Yürüyerek bulunan yapılar `FIELD` kaynağıyla kaydedilebiliyor.
- [ ] Yürüyerek bulunan fiziksel deposit tanımlanıp kaydedilebilmeli.
- [x] Field discovery ortak database üzerinden dinamik structure filtresini açabiliyor.
- [x] Field discovery ortak database üzerinden Navigation’a aktarılabiliyor.

Temel ayrım:

```text
Radar        = Bir hedef bul.
Field Survey = Bulunan hedefi yerinde analiz et ve kaydet.
```

## Aşama 7 — Discovery Analysis seviyesi

Durum: Structure için `UNKNOWN`/ziyaret edilmiş maskelemesi var; genel analiz seviyesi sistemi yok.

- [~] Ziyaret edilmemiş structure sonuçları `UNKNOWN STRUCTURE` olarak maskeleniyor.
- [ ] Kalıcı analysis level modeli
- [ ] Geological tanımlama zinciri: anomaly → metallic → resource-rich → gerçek deposit family
- [ ] Accuracy ve Field Survey kalitesinin analysis level’a etkisi
- [ ] Structure analysis seviyesi
- [ ] Eski kaydın daha iyi analizle upgrade edilmesi

## Aşama 8 — Discoveries sekmesi

Durum: Navigation Terminal V2’de Structures/Deposits listeleri var; kişisel GUI sekmesi tamamlanmadı.

- [~] Backend ve gemi terminali Structures/Geology ayrımını destekliyor.
- [ ] Player GUI Discoveries listesini doldur.
- [ ] Son keşifler ve detay görünümü
- [ ] Kaynak, analiz seviyesi ve tahmini rezerv alanları
- [ ] `SET NAVIGATION TARGET`
- [ ] `ADD TO ROUTE`
- [ ] Player GUI favorite desteği

## Aşama 9 — Player Navigation paneli

Durum: gemi Navigation Terminal’i çalışıyor; hafif uzaktan panel bekliyor.

- [ ] Mevcut hedef, gemiye uzaklık, rota ve sonraki hop
- [ ] Tahmini WE maliyeti
- [ ] Favoriden hedef seçme
- [ ] `SAVE CURRENT LOCATION`
- [ ] `SEND TO SHIP`
- [ ] Discovery’den hedef oluşturma

Gelişmiş rota hesabı ve ayarlar fiziksel Navigation Terminal’de kalacaktır.

## Aşama 10 — Player Mining paneli

Durum: Mining M1 ve fiziksel terminal alpha çalışıyor; Player GUI paneli bekliyor.

- [ ] Mining durumu ve aktif deposit/scan alanı
- [ ] Scan ve Extraction yüzdeleri
- [ ] Collection, AE Transfer ve Replication Feed buffer durumları
- [ ] SMART AUTO
- [ ] En çok çıkarılan kaynaklar
- [ ] Deposit remaining yüzdesi
- [ ] `EMERGENCY STOP MINING`

Routing, priority, Keep ve upgrade yönetimi fiziksel Mining Terminal’de kalacaktır.

## Aşama 11 — Emergency panel

Durum: başlamadı.

- [ ] `EMERGENCY RETURN TO SHIP`
- [ ] Uygunsa Teleporter Room’a dönüş
- [ ] TARDIS, teleporter, WE, cooldown, uçuş ve protected-area kapıları
- [ ] WE maliyeti
- [ ] `DISTRESS BEACON`
- [ ] Oyuncu konumunu acil Navigation hedefi olarak gönderme

## Aşama 12 — Ship Alerts

Durum: terminallerde durum verileri var; ortak alert/toast sistemi yok.

- [ ] Düşük Warp Energy
- [ ] Collection Buffer yüksek/full
- [ ] Engine/Matrix
- [ ] Eksik drive
- [ ] Yeni Structure/Geological Discovery
- [ ] Player GUI uyarı geçmişi
- [ ] Küçük toast/notification katmanı

## Aşama 13 — Deposit Generator

Durum: veri odaklı, deterministik fiziksel deposit sistemi alpha çalışıyor; gelişmiş varyasyonlar eksik.

- [x] Büyük, New World’e özgü fiziksel geological deposit sistemi
- [~] Vanilla trace ores dünyada kalıyor; erken oyun rolleri progression ile resmileştirilmeli.
- [x] Büyük depositler radar/navigation/mining döngüsünün endüstriyel hedefidir.
- [x] Multi-chunk fiziksel template yerleşimi
- [~] Doğal şekilli template’ler var; noise tabanlı şekil üretimi henüz yok.
- [x] Primary, secondary ve byproduct metadata
- [ ] Ayrı rare trace material katmanı
- [x] Depth ve dimension bağımlılığı
- [ ] Biome/geology bağımlılığı
- [x] Yoğunluk, reserve ve rarity metadata
- [ ] Değişken `MASSIVE/MOTHERLODE` varyasyonları

Aktif aileler vanilla yataklara ek olarak Osmium, Tin, Lead, Uranium, Fluorite, Aluminum, Nickel, Silver, Zinc, Platinum, Uraninite ve Certus Quartz’tır.

## Aşama 14 — Deposit entegrasyonu

Durum: keşif ve seyahat zinciri doğrulandı; extraction/depletion zinciri eksik.

- [x] Radar → Geological Scan
- [ ] Field Survey → Geological Scan
- [x] Discovery Database → Deposit kaydı
- [x] Navigation → Deposit hedefi
- [x] TARDIS → Deposit bölgesine seyahat
- [~] Mining Module fiziksel cevherleri çıkarabiliyor; seçili deposit kimliği ve reserve ledger ile bağlanması gerekiyor.
- [ ] Mining GUI → Remaining %
- [ ] Deposit tükenme sistemi
- [ ] Tükenen kaydı `DEPLETED` olarak işaretleme

1 Eylül 2026 oyun testiyle `URANINITE-RICH DEPOSIT` için şu zincir doğrulandı:

```text
Geological Radar → Discovery → Navigation Target → TARDIS Route → Physical Deposit
```

Hedef koordinat ile fiziksel yatak eşleşmiştir ve NewWorldCore kaynaklı hata oluşmamıştır.

## Yol haritası sonrası

Aşağıdaki işler Aşama 14 kapanmadan ana geliştirme odağı yapılmayacaktır:

- [ ] Recipe sistemi
- [ ] Upgrade tarifleri
- [ ] Research unlock’ları
- [ ] Teknoloji progression’ı
- [ ] Enerji/WE maliyet dengesi
- [ ] Deposit rarity/reserve dengesi
- [ ] Yeni özel TARDIS odaları
- [ ] Production Chamber üretim kataloğu

## Şu anki çalışma kapısı

1. Aşama 1 modlu structure family/ad testini kapat.
2. Aşama 2’ye analysis level, last-seen ve discovery event alanlarını ekle.
3. Geological Field Survey’i etkinleştir.
4. Sonra Player Ship Interface sekmelerini sırayla tamamla.
