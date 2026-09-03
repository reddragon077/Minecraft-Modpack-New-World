# Bilinen sorunlar ve doğrulama listesi

Son güncelleme: 3 Eylül 2026

## Aktif riskler

- Oyuncu Structure Field Survey `0.5.60.2` altında sabit 96 blokta 13x13/169 yüklü chunk konumunu aynı tickte kontrol ederek sekiz yapıyı yaklaşık 38 ms içinde döndürdü. `0.5.60.4` bunu 48 blok (7x7/49 konum) ve 80 tick gecikmeye indirdi. Runtime logunda tek tarama 4074 ms'de tamamlandı, `ARCHEOLOGIST CAMP` tanındı ve dinamik filtrede görsel olarak doğrulandı; sorun kapandı.
- Laptop `0.5.60.0-alpha-config-suite` tarama kabulünde Radar `ALL` 102 görevi yaklaşık 9,98 saniyede tamamlayıp 101 karışık sonuç, Geology ise yaklaşık 9,00 saniyede 48 deposit verdi. Geology filtre paneli açıldığında sonuç satırları, koordinatlar, scrollbar ve sarı vurgu panelin üstüne çizildi. `0.5.60.1` paneli `Z=1000` katmanına taşıdı fakat ertelenmiş GUI buffer'ları sonradan çizildiği için ekran görüntüsü kabulü başarısız oldu. `0.5.60.2-alpha-config-geology-flush` önce sonuç buffer'larını, sonra son katman popup'ını açıkça flush eder; ekran görüntüsü kabulünde panel tamamen temiz kaldı ve sorun kapandı.
- İlk `0.5.59.0` denemesinde namespace toplu sorgusu 40 saniyeyi aşan server tick'e yol açtı. `0.5.59.1` sorguları tick'lere yaydı fakat tekil locate çağrıları yine 2–8 saniyelik takılmalar üretti. `0.5.59.2`, modlu yapılar için chunk/worldgen yükleyen locate yolunu kaldırıp yalnızca placement koordinat matematiğini kullanır.
- Placement tabanlı radar koordinatı bir yapı için olası üretim noktasıdır; biyom/structure-set seçimi nedeniyle yanlış pozitif ihtimali vardır. Gerçek keşif, oyuncunun yerinde kullandığı Structure Field Survey ile doğrulanır.
- `0.5.59.4`te birden fazla structure ailesinin aynı placement kaydını paylaşması gerçek olmayan `MODDED STRUCTURE` etiketi üretiyordu. `0.5.59.5` kabulünde bu kayıt listeden kalktı ve ortak adaylar `UNKNOWN STRUCTURE` olarak göründü.
- `0.5.59.2` taraması 128 sonuç korumasına ulaştığında ilerleme indeksini artırmadığı için tarama tamamlanmıyordu. `0.5.59.3`, taramayı sonuna kadar ilerletirken yalnızca mesafe olarak en yakın 128 sonucu tutar.
- `0.5.59.3` bitiş filtresi eski 13 vanilla etiketi dışında kalan modlu aileleri sildi ve legacy vanilla locate döngüsü bazı tick'lerde 8–13 saniyelik gecikme üretti. `0.5.59.4`, modlu aileleri bitişte korur ve vanilla yapıları da random-spread/concentric-rings placement verisinden hesaplar.
- `0.5.56.0-alpha-radar-navigation-mining-recovery` konuşma geçmişinde bilinen iyi geri dönüş noktası olarak geçer; yerel binary/source bulunmadan otomatik geri dönüş yapılmamalıdır.
- Reliable EMI (`emixx-neoforge-1.21.1-3.1.2.jar.disabled`) bilinçli olarak devre dışıdır.
- Araştırma, Production Chamber, genetik ve tam hikâye progression’ı henüz tamamlanmış oynanış sistemi değildir.
- NewWorldCore'un tam tarihsel kaynak ağacı henüz bulunamadı. `src-patches/newworldcore/` genişletilmiş jeoloji değişikliğini yeniden üretilebilir tutar; ileride tam kaynak projeye katlanmalıdır.

## Doğrulanan Discovery Database v3

- `0.5.61.0`, `analysisLevel` ve `lastSeenAt` alanlarını ekler; global kayıt şeması `NWDiscoverySchema=3`tür.
- Eski kayıtta ilk keşif zamanı korunur, tekrar tarama yalnız son görülmeyi ilerletir. FIELD kaynağı, visited/favorite durumu ve daha yüksek analiz seviyesi sonraki RADAR kaydıyla düşürülemez.
- Laptop save kabulünde 449 kaydın tamamında iki yeni alan vardı: 342 kayıt seviye 0, 107 kayıt seviye 1; eksik metadata yoktu.
- `ARCHEOLOGIST CAMP` tekrar Field Survey kabulünde ilk keşif `307066`, son görülme `397848`, kaynak `FIELD`, analiz seviyesi `1` ve visited `1` olarak doğrulandı.
- Structure Radar 102, Geology 48 sonuçla tamamlandı; `Navigation0610`, discovery, Field Survey veya NewWorldCore kaynaklı ilgili exception görülmedi.

## Doğrulanan Geological Field Survey

- `0.5.62.0`, Player GUI'deki eski çevrimdışı Geological Scan kartını ayrılmış mode-1 ağ yolu üzerinden etkinleştirir.
- Tarama uzun menzilli Radar adayını kanıt saymaz; yalnız yüklü chunk'lardaki deterministik depozit şablonunun gerçek bloklarını doğrular.
- Varsayılan profil `48` yatay, `128` dikey blok, `80` tick gecikme, en fazla `8` sonuç, aday başına `4096` kontrol ve en az `3` eşleşmedir; tamamı `player.properties` içinde Türkçe açıklamalıdır.
- Laptop kabulünde boş alan taraması 0 sonuçla tamamlandı. TIN merkezindeki tarama `[-2696, 32, -728]` konumunu 3/4 blok eşleşmesiyle 4016 ms'de doğruladı.
- Kapanan save dosyasında TIN kaydı `GEOLOGY`, `FIELD`, `Visited=1`, `AnalysisLevel=2` ve güncellenmiş `LastSeenAt` ile bulundu; `discoveredAt` korunmuştur. İlgili hata veya exception yoktur.

## Doğrulanan kademeli Geological Analysis

- `0.5.64.0`, Radar Accuracy seviyesini her sonuç için ayrı uygular. Henüz açılmayan aileler Accuracy boyunca anomaly, metallic ve resource-rich biçiminde maskelenirken, kendi eşiğine ulaşan aile L3 exact adına yükselir.
- `config/newworldcore/discovery.properties` içinde 21 deposit ailesinin her biri için `reveal.required_accuracy.*=0..3` anahtarı bulunur. Tanımsız gelecek aileler güvenli biçimde Accuracy III varsayımını kullanır.
- Varsayılan kümülatif aile dağılımı Accuracy 0/I/II/III için `3/10/18/21`dir. Field Survey bir kaydı erkenden exact açabilir ve kalıcı merge kuralı bu kaydı sonraki Radar gözlemleriyle düşürmez.
- Laptop kabulünde dört tarama `24/32/40/48` sonuç kapasitesiyle yaklaşık dokuzar saniyede tamamlandı. Kullanıcı kademeli yazıları ve filtre panelinin sonuç satırlarının üzerinde temiz kaldığını doğruladı.
- Save denetimi 47 `GEOLOGY/RADAR` L3 ve bir korunmuş `GEOLOGY/FIELD` L3 kaydı gösterdi; 66 taranmamış eski Radar kaydı L0 kaldı. Test sonrasında ilgili NewWorldCore exception görülmedi.

## Doğrulanan radar düzeltmeleri

- Masaüstü oyun ve mevcut dünya `0.5.59.4` ile açıldı.
- Kullanıcı ekranında `Discoveries 101`, `Visited 6` ve dörtten çok erişilebilir satır görüldü; eski dört-kayıt ekran sınırı bu testte doğrulandı.
- Explorify, Better Dungeons ve Structory sınıfları sonuç listesinde görüldü; dinamik modlu adayların bitiş filtresinde kaybolması düzeltildi.
- Birçok `UNKNOWN STRUCTURE` satırı beklenen placement-adayı durumudur; gerçek üretim ve aile adı Field Survey ile ayrıca doğrulanmalıdır.
- `MODDED STRUCTURE` satırları doğrulanmış yapı değildir ve mevcut buildde gerçek hedef gibi kullanılmamalıdır.
- Field Survey gerçek `ABANDONED CAMP` yapısını tanıdı; ancak iki ardışık testte aynı sonuç listesine `COPPER SULFIDE DEPOSIT` de eklendi. Bu, `GEOLOGY` kaydının structure sonucu olarak sızdığını doğrular.
- `0.5.59.5`, NewWorldCore `*_deposit` jigsaw kayıtlarını radar/Field Survey’den dışladı; Field Survey beş gerçek yapı buldu ve jeoloji sızıntısı görülmedi. `0.5.59.6` kabulünde Deposits görünümünde 28 gerçek kayıt ve iki görünür `COPPER-RICH DEPOSIT` satırı korunmuştu.
- `0.5.59.5` temizlik sırasında silinen seçili hedefi `null` yaparak rota varış kontrolünde sürekli `InvocationTargetException` üretti. `0.5.59.6` boş metin sentinelini kullanır; 20 dakikadan uzun oyun oturumunda her iki ilgili hata sayısı da sıfır kaldı.
- `explorify:campsite` gerçek yapısı bulunup yerinde `CAMPSITE` olarak tanındı; dinamik `CAMPSITE` filtresi de ekranda doğrulandı.
- Filtre penceresi açıkken radar sonuçları ve telemetri metinleri panelin üzerine çiziliyordu. `0.5.59.7` paneli ayrı bir ön plan pose katmanında çizdi; takip ekran görüntüleri panel içeriğinin temiz kaldığını doğruladı.
- Yalnız `CAMPSITE` seçiliyken ilk tarama 96 karışık sonuç döndürdü. `0.5.59.8` kesin aile eşleşmesiyle bunu 96 ham adaydan sıfır sonuca indirdi ve seçilmeyen ailelerin elendiğini doğruladı.
- Sıfır sonuç, GUI'nin 5000 blok göstermesine rağmen placement hesabının 100 chunk (~1600 blok) ile sınırlı olduğunu açığa çıkardı; bilinen kamp yaklaşık 3037 blok uzaktaydı. `0.5.59.9` gerçek menzili bağladı ancak random-spread bölge koordinatını Minecraft metoduna zaten bölünmüş halde vererek spacing'i iki kez böldü. `0.5.59.10` her bölgeyi `regionIndex * spacing` chunk koordinatıyla örnekler; oyun kabulünde bir Campsite görevi 5000 blokta bir sonuç döndürdü.
- Laptop `0.5.60.0` `ALL` taraması 102 placement görevini yaklaşık 9,98 saniyede bitirip 101 karışık aile sonucu döndürdü; gönderilen `scan.batch_interval_ticks=8` profilinin yaklaşık yarı-hız hedefi doğrulandı. Geology aynı oturumda 48 depositi yaklaşık 9,00 saniyede tamamladı. `0.5.60.2` yalnız Geology GUI çizim sırasını düzeltir ve scan/config davranışını korur.
- `ARCHEOLOGIST CAMP` zinciri Radar v2'nin kalan aile kabul maddesidir.

## Senkronizasyon sınırları

- `manifest.json` üçüncü taraf eklentileri sabitler; iki custom fork `mods/` altında tutulur.
- KubeJS web server auth anahtarı, WorldEdit oturumları, cache, log, dünya ve kişisel seçenekler GitHub’a alınmaz.
- Her CurseForge örneğinde yalnızca bir NewWorldCore ve bir DoctorWhoMod fork JAR’ı bulunmalıdır.
- Masaüstü bilgisayar `machines/desktop.json` ile kaydedildi; `0.5.59.10` repo/instance tek-JAR ve hash eşleşmesi geçti.

## Bir sonraki test kapısı

`uraninite_pocket` için Radar → Navigation → TARDIS rota → fiziksel yatak zinciri 1 Eylül 2026'da doğrulandı. Aşağıdaki maddeler tam regresyon kapısı olarak devam eder:

1. FE Matrix kayıt kararlılığı ve mod çakışması için tekrar açılış kontrolü.
2. Gemi verisinin yeniden başlatma sonrası kalıcılığı.
3. Geological Radar ile fiziksel deposit eşleşmesi.
4. Osmium, Tin, Lead, Uranium ve Fluorite depositleri.
5. Aluminum, ortak Nickel, Silver, Zinc, Platinum, ayrı Uraninite ve Certus Quartz depositleri.
6. Lead/Uranium/Nickel için tek aile ve çoklu mod palette kuralları.
7. Navigation `DEPOSITS`, geçmiş ve yeniden başlatma kalıcılığı.
8. Replication tarama bilgisi ve duplicate mineral kuralları.
9. Çok-aileli placement sonucunun `MODDED STRUCTURE` adlı sahte hedef yerine doğrulanmamış aday olarak gösterilmesi.
10. Field Survey runtime yolunun `COPPER SULFIDE DEPOSIT` gibi `GEOLOGY` kayıtlarını structure sonucuna eklememesi.
11. `explorify:campsite`, `structory:abandoned_camp` ve `betterarcheology:archeologist_camp_grassy` için Radar → UNKNOWN → yerinde Field Survey → family adı zinciri.
12. Düzeltme sonrası Structure Field Survey sonucunun hiçbir `GEOLOGY` kaydını structure olarak işaretlemediği regresyon kontrolü.
