# New World — Aktif geliştirme yol haritası

Son doğrulama: 3 Eylül 2026

Aktif runtime-kabul edilen laptop buildi: NewWorldCore `0.5.65.2-alpha-player-discoveries`

Bu belge, eski **Yeni Geliştirme Yol Haritası** listesinin çalışan JAR, güncel proje dosyaları ve oyun testiyle doğrulanmış hâlidir. Araştırma, Production Chamber ve sonraki progression çalışmaları bu yol haritasının 14 aşaması kapandıktan sonra ele alınacaktır.

Durum işaretleri:

- `[x]` Uygulandı ve dosya/JAR düzeyinde doğrulandı
- `[~]` Altyapısı var; işlev veya kabul testi eksik
- `[ ]` Henüz tamamlanmadı

## Aşama 1 — Radar v2

Durum: **tamamlandı.** Dinamik registry taraması, ortak-placement/geology ayrımı, eski seçili rota temizliği, Structure/Geology filtre katmanları, seçilmeyen ailelerin elenmesi, gerçek 5000 blokta pozitif `CAMPSITE`, laptop `ALL` karma taraması ve 48 blok/80 tick Player Field Survey oyun içinde geçti. `ARCHEOLOGIST CAMP` tanındı ve discovery-driven dinamik filtrede görsel olarak doğrulandı.

- [x] Sabit 13 öğelik kullanıcı filtresi kaldırıldı.
- [x] Filtreler Discovery Database içeriğine göre dinamik üretiliyor.
- [x] Yeni gemi yalnızca `ALL` filtresiyle başlıyor.
- [x] Ziyaret edilip tanımlanan yapı family’si ilgili filtreyi açıyor.
- [x] Aynı yapı ailesinin varyasyonları tek family altında gruplanıyor.
- [x] Bilinmeyen/modlu yapılar canlı structure registry üzerinden otomatik sınıflandırılıyor; çok-aileli placementlar `UNKNOWN STRUCTURE` kalıyor ve jeoloji jigsaw’ları dışlanıyor. `0.5.59.5` oyun kabulü beş gerçek yapıyla geçti.
- [x] Radar GUI’de `STRUCTURES` ve `GEOLOGY` modları bulunuyor.
- [x] Structure ve Geology sonuçları birbirinden ayrıldı.
- [x] Range, Speed ve Accuracy yükseltmeleri iki tarama yoluna bağlandı.

Kapanış testi: `ABANDONED CAMP`, `CAMPSITE` ve `ARCHEOLOGIST CAMP` yerinde tanıma geçti; `CAMPSITE` ve `ARCHEOLOGIST CAMP` dinamik filtreleri, GUI katmanları, seçilmeyen ailelerin elenmesi, gerçek 5000 blokta pozitif sonuç, Geology filtre paneli ve `.4` Field Survey denge testi geçti. Radar v2 kapandı; sıradaki geliştirme Aşama 2 metadata/event alanlarıdır.

## Aşama 2 — Ortak Discovery Database

Durum: **tamamlandı.** Ortak kalıcı veritabanı şema v3'e yükseltildi; eski kayıtlar kayıpsız taşındı ve bütün kayıt yolları ortak event hattına bağlandı.

- [x] Gemi kapsamlı ortak `NavigationDiscoverySavedData` mevcut.
- [x] `STRUCTURE` ve `GEOLOGY` kategorileri mevcut.
- [x] Family/tip, dimension, koordinat, ilk keşif zamanı ve `RADAR`/`FIELD` kaynağı saklanıyor.
- [x] Analiz seviyesi 0-3 aralığında kalıcı discovery alanı olarak eklendi; kaynak başlangıç seviyeleri `discovery.properties` ile ayarlanabilir.
- [x] `lastSeenAt` eklendi ve tekrar keşifte güncellenirken `discoveredAt` ilk keşif zamanı olarak korunuyor.
- [x] Structure Radar filtreleri bu veritabanından üretiliyor.
- [x] Navigation Discoveries aynı veritabanına bağlı.
- [x] Geological deposit kayıtları aynı discovery modeline bağlı.
- [x] Research/Exploration XP tarafından dinlenebilecek ortak `DISCOVERED`, `SEEN` ve `ANALYSIS_UPGRADED` event altyapısı eklendi.

Kapanış testi: mevcut dünya şema `2`den `3`e taşındı. Save dosyasında 449/449 kayıtta `analysisLevel` ve `lastSeenAt` bulundu; eksik alan yoktu. Dağılım 342 seviye-0 ve 107 seviye-1 kayıttır. `ARCHEOLOGIST CAMP` kaydında ilk keşif `307066` korunup son görülme `397848`e ilerledi; `FIELD`, analiz seviye 1 ve visited durumu korundu. Structure Radar 102, Geology 48 sonuçla tamamlandı ve ilgili hata görülmedi.

## Aşama 3 — Player Ship Interface

Durum: arayüz kabuğu ve tuş bağlantısı mevcut.

- [x] Oyuncunun tuşla açabildiği `PlayerShipScreen` mevcut.
- [x] `OVERVIEW`, `SURVEY`, `DISCOVERIES`, `NAVIGATION`, `MINING`, `EMERGENCY` sekmeleri oluşturuldu.
- [x] Tasarım, gemideki ayrıntılı terminallerin yerine geçmeyecek şekilde sınırlandı.
- [~] `SURVEY/STRUCTURE` ve `SURVEY/GEOLOGICAL` işlevseldir; diğer sekmelerin içeriği sonraki aşamalarda tamamlanacak.

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

Durum: **tamamlandı.** Structure ve Geological Survey kısa menzilli yerinde doğrulama yolları oyun içinde geçti.

- [x] Player GUI’de kısa menzilli Structure Scan mevcut.
- [x] Player GUI’de Geological Scan etkinleştirildi; ayrılmış mode-1 ağ yolu fiziksel depozit doğrulamasına bağlandı.
- [x] TARDIS Radar uzun menzilli, Field Survey yakın çevre odaklıdır.
- [x] Yürüyerek bulunan gerçek structure start kayıtları `FIELD` kaynağıyla kaydedilebiliyor; `0.5.59.5` kabulünde beş gerçek yapı bulundu ve `COPPER SULFIDE DEPOSIT` sızıntısı görülmedi.
- [x] Yürüyerek bulunan fiziksel depozit, yüklü chunk'lardaki gerçek şablon blokları eşleştirilerek `GEOLOGY/FIELD`, visited ve analiz seviyesi 2 olarak kaydediliyor.
- [x] Field discovery ortak database üzerinden dinamik structure filtresini açabiliyor.
- [x] Field discovery ortak database üzerinden Navigation’a aktarılabiliyor.

Temel ayrım:

```text
Radar        = Bir hedef bul.
Field Survey = Bulunan hedefi yerinde analiz et ve kaydet.
```

Kapanış testi: `0.5.62.0` laptop testinde ilk boş alan taraması temiz biçimde 0 sonuç verdi. İkinci tarama `TIN-RICH DEPOSIT` merkezini `[-2696, 32, -728]` konumunda 3/4 fiziksel blok eşleşmesiyle 4016 ms'de doğruladı. Save kapanışından sonra aynı kayıt `Source=FIELD`, `Visited=1`, `AnalysisLevel=2` ve korunmuş ilk keşif zamanı ile doğrulandı; ilgili hata görülmedi.

## Aşama 7 — Discovery Analysis seviyesi

Durum: **tamamlandı.** Kalıcı 0-3 model, yükseltme/event yolu, Accuracy tabanlı jeolojik çözümleme, aile bazlı config eşikleri ve Field kanıtının düşürülememesi oyun içinde geçti.

- [~] Ziyaret edilmemiş structure sonuçları `UNKNOWN STRUCTURE` olarak maskeleniyor.
- [x] Kalıcı analysis level modeli
- [x] Geological tanımlama zinciri: anomaly → metallic → resource-rich → gerçek deposit family
- [x] Accuracy ve Field Survey kalitesinin analysis level’a etkisi
- [~] Structure analysis seviyesi: Radar adayı 0, Field doğrulaması 1 olarak çalışıyor; ileri Research seviyeleri bekliyor.
- [x] Eski kaydın daha iyi analizle upgrade edilmesi; seviye, FIELD kanıtı ve visited durumu sonraki düşük seviye Radar kaydıyla düşürülemiyor.

Kapanış testi: `0.5.64.0` ile Accuracy `0/1/2/3` taramaları sırasıyla `24/32/40/48` sonuç kapasitesinde ve yaklaşık dokuzar saniyede tamamlandı. Kullanıcı her kademedeki maskeli/exact yazıları ve temiz foreground filtre panelini görsel olarak doğruladı. Son save denetiminde 47 Radar sonucu L3 exact, daha önce Field Survey ile doğrulanan bir TIN kaydı da `FIELD/L3` olarak korundu; kalan 66 eski/taranmamış Radar kaydı L0 kaldı. İlgili NewWorldCore hatası görülmedi.

## Aşama 8 — Discoveries sekmesi

Durum: Player GUI ortak Discovery Database'e bağlandı; liste/detay çekirdeği kabul edildi, kayıt eylemleri ve kalan detay alanları bekliyor.

- [x] Backend, gemi terminali ve Player GUI Structures/Geology ayrımını destekliyor.
- [x] Player GUI Discoveries listesini doldur.
- [x] Son görülmeye göre sıralanan keşifler ve seçili kayıt detay görünümü
- [x] Kaynak, analiz seviyesi, koordinat, kanıt kaynağı ve canlı oyuncu mesafesi
- [ ] Son görülme ve tahmini rezerv alanlarını detay paneline ekle
- [ ] `SET NAVIGATION TARGET`
- [ ] `ADD TO ROUTE`
- [ ] Player GUI favorite desteği

Ara kabul: `0.5.65.2`, ortak veritabanındaki 464 kayıttan kategori başına en yeni 64 girdiyi senkronladı (`128/464`). Ayrı Structure/Geology kotaları son jeoloji taramalarının yapı geçmişini gizlemesini engelledi. Kullanıcı sekme stili, üç filtre, sayfalama, ayrıntılar ve canlı oyuncu mesafesini doğruladı; dünya/runtime başladıktan sonra Discoveries render, snapshot veya paket hatası görülmedi. Farklı boyuttaki kayıtlar `DIFFERENT DIMENSION` olarak gösterilir.

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

1. Geological Field Survey’i etkinleştir.
2. Analysis level'ın jeolojik anomaly → family çözümleme zincirini tamamla.
3. Sonra Player Ship Interface sekmelerini sırayla tamamla.
