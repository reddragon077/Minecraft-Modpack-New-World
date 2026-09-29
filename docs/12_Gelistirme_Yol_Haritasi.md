# New World — Aktif geliştirme yol haritası

Son kayıt güncellemesi: 29 Eylül 2026

Kurulu build: NewWorldCore `0.5.72.1-alpha-unified-ship-alerts`. Navigation, Mining ve Emergency temel akışları kabul edildi. Son Ship Alerts düşük FE/geçmiş/çözülme ekran kontrolü; HUD bildirimi ve GUI kapat-aç kullanıcı kontrolü geçti. Genişletilmiş vakalar ve yeni keşif bildirimleri açık; Aşama 12 kısmi.

Kullanıcı isteğiyle geliştirme burada duraklatıldı; bu kapanış yalnız dokümantasyon güncellemesidir. [Oyuncu rehberi](19_Oyuncu_Rehberi.md) · [Güncel kabul](18_Ship_Alerts_Runtime_Kabul.md).

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

Kapanış testi: `ABANDONED CAMP`, `CAMPSITE` ve `ARCHEOLOGIST CAMP` yerinde tanıma geçti; `CAMPSITE` ve `ARCHEOLOGIST CAMP` dinamik filtreleri, GUI katmanları, seçilmeyen ailelerin elenmesi, gerçek 5000 blokta pozitif sonuç, Geology filtre paneli ve `.4` Field Survey denge testi geçti. Radar v2 kapandı; ardından Aşama 2 metadata/event alanları tamamlandı. Tarihsel 80 tick Structure ayarı daha sonra kullanıcının seçimiyle 20 tick / 1 saniyeye güncellendi; Geological ayarı 80 tick kalır.

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

Durum: **kısmi.** Altı sekme işlevsel; temel tek oyunculu akışlar kabul edildi. İleri işler ve genişletilmiş kabul ilgili aşamalarda açık.

- [x] Oyuncunun tuşla açabildiği `PlayerShipScreen` mevcut.
- [x] `OVERVIEW`, `SURVEY`, `DISCOVERIES`, `NAVIGATION`, `MINING`, `EMERGENCY` sekmeleri oluşturuldu.
- [x] Tasarım, gemideki ayrıntılı terminallerin yerine geçmeyecek şekilde sınırlandı.
- [x] Survey, Discoveries, Overview, Navigation, Mining ve Emergency temel içerikleri mevcut.
- [~] İleri özellikler ve genişletilmiş uç durum kabulü Aşama 7, 9–14 kapsamında izlenir.

## Aşama 4 — Overview / Ship Status

Durum: **tamamlandı — laptop tek oyunculu kabulü.** Önceki ekran/log kanıtına ek olarak kullanıcı `.67.1` uyarı görünümünü, terminal FE/WE karşılaştırmasını, seyahat/rota/cooldown değişimini, dış konum ve dünya yeniden giriş kontrollerinin tamamını denediğini doğruladı. Bu beyan çok oyunculu veya yapay bağlantı kesintisi testi yapılmış anlamına gelmez.

- [x] Ship FE mevcut/kapasite ve tüketim (`OUT`: gerçek havuz çıkışı, `NET`: net değişim; yenileme aralığı ortalaması)
- [x] Warp Energy mevcut/kapasite
- [x] Engine cooldown→READY ve Handbrake durumu (seyahat değişimi kullanıcı tarafından doğrulandı)
- [x] Mining Shield ve Mining durumları
- [x] Navigation durumları
- [x] FE, Warp ve Engine Matrix durumları
- [x] TARDIS dış dimension ve koordinat
- [x] `NOMINAL`, `WARNING`, `CRITICAL` genel durum hesabı (FE/Mining zinciri runtime doğrulandı)
- [x] Son sistem uyarıları (oyuncunun bağlantı/gemi gözlem oturumu; kalıcı günlük değil)

Kabul testi: [`Overview`](13_Overview_Runtime_Kabul.md). Kullanıcının seçtiği `refresh_ticks=20` ve normale dönen 20%/5% eşikleri korunur; `show_resolved_warnings` çözülen kayıtların görünürlüğünü yönetir.

Not: Player GUI fiziksel terminallerin salt-okunur ortak telemetri katmanını kullanır.

## Aşama 5 — Ship Link

Güncel kabul: `.68.2` yeni oturumda CONNECTED, Overview NOMINAL ve Discoveries 128/471 gösterdi. 16:31:47'de OUT OF RANGE, 16:36:53'te yeniden CONNECTED/NOMINAL loglandı. Kullanıcı dönüşte zaten düzeldiğini, geciken şeyin ekran görüntüsü olduğunu açıkladı; 12 blok ekranı geri bağlantıyı doğruladı. SYNCING regresyon kapısı kapandı; Aşama 9 temel akışı da daha sonra kabul edildi. Aşağıdaki paragraf önceki `.68.0`/`.68.1` kabul geçmişidir; açıklama düzeltmesinin görsel kontrolü de artık geçmiştir.

Durum: **temel tek oyunculu işlev ve açıklama düzeltmesinin görsel kabulü tamamlandı.** Ekran görüntüleri ve sunucu logları ON BOARD, 11770 blokta LOST, 9 blokta yeniden CONNECTED, canlı boyut izni ve gemi dışından FAV/TARGET/ROUTE zincirini doğruladı. Gecikmeli Structure Survey için bağlantı kaybında erken dönüş, zaman çizelgesi ve kaynak koduyla doğrulandı; ayrı NBT karşılaştırması yapılmadı. Geological gecikmeli iptal, çok oyunculu izolasyon ve yapay timeout oyun testleri kapsam dışı/açık; otomatik testler bunların yerine sayılmaz. [Kabul kapsamı ve son görsel kontrol](14_Ship_Link_Runtime_Kabul.md).

- [x] Sürekli bağlantı göstergesi
- [x] `CONNECTED`, `DIMENSIONAL`, `LOST` durumları
- [x] Gemi dimension bilgisi
- [x] Aynı dimensionda gemiye uzaklık
- [x] Link kaybında uzaktan özellik kapıları (temel tek oyunculu kapsam)

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

Durum: **kısmi.** Kalıcı 0-3 model, yükseltme/event yolu, Accuracy tabanlı jeolojik çözümleme, aile bazlı config eşikleri ve Field kanıtının düşürülememesi oyun içinde geçti; ileri Structure analiz seviyeleri Research aşamasını bekliyor.

- [~] Ziyaret edilmemiş structure sonuçları `UNKNOWN STRUCTURE` olarak maskeleniyor.
- [x] Kalıcı analysis level modeli
- [x] Geological tanımlama zinciri: anomaly → metallic → resource-rich → gerçek deposit family
- [x] Accuracy ve Field Survey kalitesinin analysis level’a etkisi
- [~] Structure analysis seviyesi: Radar adayı 0, Field doğrulaması 1 olarak çalışıyor; ileri Research seviyeleri bekliyor.
- [x] Eski kaydın daha iyi analizle upgrade edilmesi; seviye, FIELD kanıtı ve visited durumu sonraki düşük seviye Radar kaydıyla düşürülemiyor.

Kapanış testi: `0.5.64.0` ile Accuracy `0/1/2/3` taramaları sırasıyla `24/32/40/48` sonuç kapasitesinde ve yaklaşık dokuzar saniyede tamamlandı. Kullanıcı her kademedeki maskeli/exact yazıları ve temiz foreground filtre panelini görsel olarak doğruladı. Son save denetiminde 47 Radar sonucu L3 exact, daha önce Field Survey ile doğrulanan bir TIN kaydı da `FIELD/L3` olarak korundu; kalan 66 eski/taranmamış Radar kaydı L0 kaldı. İlgili NewWorldCore hatası görülmedi.

## Aşama 8 — Discoveries sekmesi

Durum: Tamamlandı. Player GUI ortak Discovery Database, Navigation hedefi, rota/hop motoru ve favori durumu ile aynı kalıcı kayıtları kullanıyor.

- [x] Backend, gemi terminali ve Player GUI Structures/Geology ayrımını destekliyor.
- [x] Player GUI Discoveries listesini doldur.
- [x] Son görülmeye göre sıralanan keşifler ve seçili kayıt detay görünümü
- [x] Kaynak, analiz seviyesi, koordinat, kanıt kaynağı ve canlı oyuncu mesafesi
- [x] Son görülme ve tahmini rezerv alanlarını detay paneline ekle
- [x] `SET NAVIGATION TARGET`
- [x] `ADD TO ROUTE`
- [x] Player GUI favorite desteği

Ara kabul: `0.5.65.2`, ortak veritabanındaki 464 kayıttan kategori başına en yeni 64 girdiyi senkronladı (`128/464`). Ayrı Structure/Geology kotaları son jeoloji taramalarının yapı geçmişini gizlemesini engelledi. Kullanıcı sekme stili, üç filtre, sayfalama, ayrıntılar ve canlı oyuncu mesafesini doğruladı; dünya/runtime başladıktan sonra Discoveries render, snapshot veya paket hatası görülmedi. Farklı boyuttaki kayıtlar `DIFFERENT DIMENSION` olarak gösterilir.

Kapanış testi: `0.5.66.1` ile `ARCHEOLOGIST CAMP` ve `TRIAL CHAMBERS` kayıtlarında TARGET/FAV/ROUTE eylemleri sunucu logunda doğrulandı. Favoriler ortak kayda yazıldı; hedef anahtarı Navigation veritabanına bağlandı; rota motoru hedef boyutu ve konumunu TARDIS state'ine dört yazımla uygulayıp tek hop hazırladı. Trial Chambers seyahati `Route complete at hop #1` ile tamamlandı. İlk `.66.0` denemesinde `>=100` eylem kodlarının eski istemci-durum köprüsünde yutulduğu bulundu; `.66.1` çakışmayan negatif C2S aralıklarıyla bu kusuru kapattı. Aşama 8 tamamlandı.

## Aşama 9 — Player Navigation paneli

Durum: **temel iş akışı kabul edildi; genişletilmiş kontroller açık.** `.69.4` ekranları ve 29 Eylül 10:52:13 sunucu kaydı manuel hedef [-2454,63,181], doğru Y=63 ve 11 blok rota sonucunu doğruladı. Yakınlık eşiğinde ROUTE_COMPLETE yeni uçuş kanıtı değildir. Aşama 10 temel paketi de daha sonra kabul edildi; aşağıdaki kenar testleri açık kalır.

- [~] Mevcut hedef, gemiye uzaklık, rota ve sonraki hop (`.69.0` ilk görünüm, hedef/rota değişimi ve kullanıcı bildirimli yeniden açılış geçti; kenar durumları açık)
- [~] Tahmini WE maliyeti (`.69.0` yüklü sonraki hop: Aluminum 48 -> Carbon 52 WE görünümü geçti; genişletilmiş kabul açık)
- [x] Favoriden hedef seçme (`.69.1` SYNC 2/2, Trial Chambers TARGET ve eski Carbon rotasının korunması ekran/log ile geçti; genişletilmiş kenar testleri açık)
- [x] `SAVE CURRENT LOCATION` (`.69.2` iki farklı konum, aynı blokta kopya oluşmaması ekran/log ile; dünyadan çıkıp girişte kalıcılık kullanıcı bildirimiyle geçti. İç-mekân/config/çok oyunculu kenar testleri açık.)
- [x] `SEND TO SHIP` temel akışı (`.69.3` ortak hedef/eski rota koruma; `.69.4` doğru terminal Y/mesafe ve hesaplanan son durak ekran/log ile kabul edildi. Genişletilmiş izin/bekleme kontrolleri açık.)
- [x] Discovery’den hedef oluşturma (`0.5.66.1` TARGET/ROUTE kabulü)

Gelişmiş rota hesabı ve ayarlar fiziksel Navigation Terminal’de kalacaktır.

Navigation telemetri görünümü salt-okunur kalır. FAVORITES → SEND TO SHIP mevcut TARGET yazıcısını kullanır; seçili konum/keşif geminin ortak hedefi olur. SAVE CURRENT LOCATION yalnız oyuncunun dış dünya konumunu favoriye kaydeder. İkisi de rota/uçuş başlatmaz. Temel zincir ve `.69.4` fiziksel terminal → mevcut CALCULATE ROUTE kontrolü geçti. [Kabul kaydı ve açık genişletilmiş kontroller](15_Player_Navigation_Runtime_Kabul.md).

## Aşama 10 — Player Mining paneli

Durum: `.70.0` temel durum/alan/tarama→kazım ve canlı sayaç kabulü ekran/log ile geçti. `.70.1` kaynak görünümü, onaylı STOP, aynı alanda dünyaya tekrar girişte kalıcılık ve fiziksel yeniden başlatma dört maddelik kullanıcı onayıyla geçti; kaynak ekranında raw_iron 120 / eski 761 ayrıca görüldü. 13 otomatik grup + engine fixture geçti. Tam buffer eşleştirmesi ve Mining bağlantı/çok oyunculu/config kenar testleri açık; tüm aşama tamamlandı sayılmaz. Yeniden başlatma/routing fiziksel terminalde kalır.

- [x] Mining durumu ve kayıtlı scan alanı (.70.0 LAST SCAN -> SCAN CENTER [-154,11], bekleme/tarama/kazım ekran/log kabulü; deposit kimliği bağlantısı yok)
- [x] Scan ve Extraction yüzdeleri (.70.0 tarama %7,8 -> %100, çıkarım 347/%1 ekran kabulü; genişletilmiş testler açık)
- [~] Collection, AE Transfer ve Replication Feed buffer durumları (.70.0; eşya toplamı + tür yuvası; eksik/yüklü olmayan ayrı)
- [~] SMART AUTO (.70.0 mevcut routing modu ve aktarım sayaçları, kontrol düğmesi değil)
- [x] En çok çıkarılan kaynaklar — temel akış (.70.1 raw_iron 120 / eski 761 ekran kanıtı; aynı alanda dünya yeniden giriş kalıcılığı ve kazıma devam kullanıcı onayı. Çok kaynaklı sıralama kenarları otomatik testli, ayrıca oyun içi doğrulanmadı.)
- [ ] Deposit remaining yüzdesi (Aşama 14 gerçek rezerv defteri bağımlılığı; tarama kalanı bu yüzde yerine kullanılmaz)
- [x] `EMERGENCY STOP MINING` — temel akış (.70.1 iki onaylı Mining Shield OFF/durma ve fiziksel yeniden başlatma kullanıcı onayıyla geçti; sahiplik/timeout/config/çok oyunculu genişletilmiş oyun testleri ayrıca açık.)

Routing, priority, Keep ve upgrade yönetimi fiziksel Mining Terminal’de kalacaktır.

## Aşama 11 — Emergency panel

Durum: **temel kapı önü dönüş, 30 dakika bekleme başlangıcı, geri sayım ve dünya çıkış/giriş kalıcılığı kabul edildi; genişletilmiş vakalar açık.** Kullanıcı, 25:51 ekranı ve 15:21 varış / 15:24–15:25 kayıt-yeniden giriş logları bunu doğruladı. Hedef/rota/WE gerçek oyun karşılaştırması ayrıca açık.

Beacon iptal edildi. .71.2 chunk kontrol hatası .71.3 ile onarıldı; ayrı Teleporter Room hedefi kullanıcı isteğiyle .71.4'te normal portalın ana iç kapı önü konumu/yönüne değişti. Eski hedef ve bekleyen temel kabul notları artık geçerli değildir. [Güncel Emergency kabulü](17_Player_Emergency_Runtime_Kabul.md).

- [~] `EMERGENCY RETURN TO SHIP` — iki aşamalı tek kullanımlık onay; eski Beacon paketleri reddedilir.
- [~] Normal portalın ana iç kapı önü konumu/yönü; gerçek yüklü kapı, hava/zemin/çarpışma/tehlike/sınır kontrolleri. Güvensizse başka oda veya komşu noktaya yedek ışınlanma yok.
- [~] Sahiplik, canlı/yaya oyuncu, başka TARDIS içini reddetme, uçuş/oda yenileme ve NeoForge seyahat iptal kapıları. Acil dönüş normal uzaktan bağlantı mesafe/dimension sınırından bağımsız.
- [~] Başarılı varıştan sonra 30 gerçek dakika, oyuncu kaydında kalıcı süre; çıkış/giriş/ölüm sıfırlamaz, çevrimdışı süre sayılır. Başarısız dönüş süre tüketmez.
- [~] Navigation hedefi/rotası, gemi uçuşu ve WE korunur. Bu kullanıcı-onaylı paket ayrı WE ücreti eklemez; dengeleme 30 dakika cooldown'dur.
- [x] .71.4 temel tek oyunculu kapı önü dönüşü ve sunucuda 30 dakika bekleme başlangıcı (kullanıcı + log).
- [x] Geri sayım görünümü ve dünya çıkış/girişinde kalıcılık (25:51 ekranı, kullanıcı ve log).
- [ ] Değişmeyen hedef/rota/WE karşılaştırması; uzaktan/dimension/çok oyunculu/koruma/ölüm kenar kabulü.
- İptal: DISTRESS BEACON ve konumu acil Navigation hedefi yapma. Eski kayıtlar silinmez.

29 Eylül devam kararı: kullanıcı dört parçalı Aşama 12 paketini onayladı. Mevcut 15 regresyon paketi yeniden geçti; Emergency adapter incelemesinde Navigation/rota/WE yazımı yok, başarısız/veto edilen dönüş testinde cooldown sıfır kalıyor. Bu kaynak/fixture kanıtıdır; yukarıdaki gerçek oyun karşılaştırması ve genişletilmiş kabul açık kalır. Kabul edilmiş kapı dönüşü ve yeniden giriş testleri tekrarlanmaz.

## Aşama 12 — Ship Alerts

Durum: `.72.1-alpha-unified-ship-alerts` düzeltmesi. `.72.0` ekranında düşük FE'nin geçmişte eksik olduğu doğrulandı; kullanıcı tüm Overview uyarılarını istedi. Kırpılmamış ortak örnekleme, aynı FE/WE eşikleri ve seviyeler; düşük FE, Mining enerji/bekleme ve telemetri uyarıları eklendi. Küçük bildirim vanilla HUD/action-bar, geçmiş oturum başınadır. Düşük FE/geçmiş/çözülme ekran kabulü ve kullanıcı bildirimli HUD/GUI yeniden açılış kabulü geçti. Yeni keşif bildirimleri ve genişletilmiş vakalar açık; aşama kısmi. [Toplu kabul](18_Ship_Alerts_Runtime_Kabul.md).

- [~] Düşük Warp Energy
- [~] Düşük FE ve diğer tüm Overview uyarılarının ortak geçmiş/HUD kapsamı
- [~] Collection Buffer yüksek/full (tür slotu doluluğu ve gerçek BUFFER_FULL ayrı)
- [~] Engine/Matrix
- [~] Eksik drive (mevcut gemi uçuş hedefi gereksinimi)
- [ ] Yeni Structure/Geological Discovery
- [x] Player GUI temel FE/geçmiş/çözülme ve GUI yeniden açılışı kabulü (diğer türler, bağlantı/çıkış kenarları açık)
- [x] Küçük HUD bildiriminin temel görünümü (vanilla action-bar; kullanıcı onayı)
- [~] Tüm uyarı türleri, tekrar sınırları, bağlantı/çıkış, çok oyunculu/koruma/yük oyun kabulü

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
- [x] Field Survey → Geological Scan
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

`0.5.62.0` Geological Field Survey kabulünde fiziksel `TIN-RICH DEPOSIT`, `[-2696, 32, -728]` konumunda gerçek şablon bloklarıyla doğrulandı ve ortak veritabanına `GEOLOGY/FIELD` kanıtı olarak kaydedildi.

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

Geliştirme kullanıcı isteğiyle duraklatıldı. Devam talebi geldiğinde temiz GitHub senkronizasyonu ve kurulu build doğrulaması yapılır; otomatik yeni kurulum veya cooldown sıfırlaması yapılmaz.

1. Aşama 4/5 ve 9/10/11 temel tek oyunculu kabulleri korunur. Tamamlanmış Field Survey/Discoveries veya kabul edilmiş Navigation/Mining/Emergency testleri tekrar başlatılmaz.
2. Aşama 12 temel FE/History/HUD/recovery/reopen kabulü geçti. Kalan uyarı türleri ve bağlantı/oturum/çok oyunculu/koruma/yük kontrolleri ayrı açık; yeni Structure/Geological Discovery bildirimleri sonraki geliştirme dilimidir.
3. Önceki aşamalardaki açık genişletilmiş kabul maddeleri kapanmış sayılmaz. Acil dönüşün hedef/rota/WE gerçek oyun karşılaştırması buna dahildir.
4. Aşama 13 ileri deposit varyasyonları ve Aşama 14 gerçek rezerv/tükenme defteri daha sonra gelir. Mining tarama yüzdesi bu eksikliği kapatmaz.
5. İlişkili işler 3–4 maddelik tutarlı paketlerle geliştirilir/doğrulanır; gerekli oyun kanıtı tek toplu kontrolle istenir. Bu kapanış yeni çalışma başlatmaz.
