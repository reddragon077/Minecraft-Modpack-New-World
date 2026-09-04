# New World Özel JAR'lar — Alpha Test Rehberi

Bu belge, eski `0.1.0-alpha` Room Controller denemesinin yerine güncel New World özel JAR ikilisinin test rehberidir.

## Test edilen build'ler

| Bileşen | Dosya | SHA-256 |
|---|---|---|
| NewWorldCore | `NewWorldCore-1.21.1-NeoForge-0.5.66.1-alpha-player-discovery-actions.jar` | `889900f7e2519b8e07e604431260e28e0b6f8932d3d087bc5b17f8551fda059c` |
| DoctorWhoMod fork | `DoctorWhoMod-1.21.1-NeoForge-1.0.16-NewWorld-EngineTravel-v5.8.19-Tall-Large-XLarge-Swap.jar` | `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356` |

## Testten önce

1. Dünya klasörünü yedekle.
2. `mods/` içinde her özel JAR'ın yalnızca bir sürümü bulunduğunu doğrula.
3. Upstream DoctorWhoMod ile New World DoctorWhoMod fork'unun birlikte bulunmadığını kontrol et.
4. `manifest.json` / `pack-lock.json` kurulumu ile yerel instance'ın aynı sürümde olduğundan emin ol.
5. İlk testi üretim dünyası yerine ayrı bir test dünyasında yap.

## Hızlı açılış testi

### 1. Mod yükleme

- Oyun ana menüye ulaşıyor.
- Mod listesinde `dwm`, `newworldcore`, `newworldmatrix`, `newworldshipdecor` ve `newworldnetwork` yükleniyor.
- Eksik bağımlılık, duplicate mod veya registry hatası oluşmuyor.
- `latest.log` içinde özel JAR'lara ait fatal hata bulunmuyor.

### 2. DoctorWhoMod yapıları

- Başlangıç TARDIS'i fiziksel olarak oluşuyor.
- Ana köprü yaklaşık `43 × 36 × 59` yapısıyla eksiksiz yükleniyor.
- Alçak, yüksek, büyük ve ekstra büyük oda varyantları doğru yapıyla değiştirilebiliyor.
- Koridor ve teleporter odası bağlantıları kopuk blok bırakmıyor.
- AE2 / Replication alanlarında eski veya istenmeyen depolama blokları oluşmuyor.
- Oyundan çıkıp girince başlangıç motor parçaları ikinci kez eklenmiyor.

### 3. Oda denetleyicisi ve gemi ağı

- Room Controller odayı doğru sınırlar içinde tanıyor.
- Oda koruması yetkisiz kırma / yerleştirme işlemlerini beklenen biçimde engelliyor.
- Oda değişimi sonrasında eski sınır ve kayıtlar temizleniyor.
- Engine Matrix gemiye doğru bağlanıyor ve enerji değerleri tutarlı görünüyor.
- Network ve CC:Tweaked ekranları yeniden başlatma sonrasında aynı gemiyi buluyor.
- Acil FE rezervini yalnızca `Priority 100` çıkışlar tüketebiliyor.

## Sistem testleri

### 4. Mining M1

Madencilik döngüsü iki ayrı aşamada test edilmelidir:

1. **Scan:** Bölge taranır ve hedef aileleri gösterilir.
2. **Extraction:** Handbrake ve Mining Shield koşulları sağlanınca çıkarım başlar.

Kontrol listesi:

- [ ] Handbrake kapalıyken extraction başlamıyor.
- [ ] Mining Shield etkin değilken extraction başlamıyor.
- [ ] FE maliyeti doğru düşüyor.
- [ ] Derin depolama tamponları doluyor.
- [ ] Aynı cevher ailesi içindeki bloklar doğru birlikte işleniyor.
- [ ] 0.5.56.2 strict-family guard farklı cevher ailesine taşmıyor.
- [ ] Yükseltmeler hız, maliyet veya kapasiteyi beklenen yönde etkiliyor.
- [ ] Çıktı AE2 / Replication yönüne aktarılabiliyor.
- [ ] `newworld_mining` bilgisayarı terminal ve dashboard verilerini gösteriyor.

### 5. Yapı radarı ve navigasyon

- Structure Radar canlı registry üzerinden vanilla ve modlu yakın yapıları buluyor.
- `explorify:campsite`, `structory:abandoned_camp` ve `betterarcheology:archeologist_camp_grassy` radar sonucuna giriyor.
- Radar taraması sırasında oyun/server tick'i saniyeler boyunca durmuyor; logda placement-only görev kuyruğu görülüyor.
- Radarın modlu yapı koordinatı olası yerleşim noktasıdır; yerinde Structure Field Survey gerçek structure start kaydıyla sonucu doğruluyor.
- Çok-aileli placement adayı `MODDED STRUCTURE` adlı sahte hedef üretmiyor; doğrulanana kadar `UNKNOWN STRUCTURE` kalıyor.
- Aynı family'nin mod/biome varyantları ortak filtre adı altında gruplanıyor.
- Yerinde Structure Field Survey yalnızca gerçek structure start kayıtlarını tanımlıyor; deposit kayıtlarını structure olarak işaretlemiyor.
- Önceki yanlış `MODDED STRUCTURE` ve `... DEPOSIT`/`STRUCTURE` kayıtları temizlenirken gerçek `GEOLOGY` kayıtları korunuyor.
- Görülmüş yapılar veritabanına kaydoluyor.
- Geçmiş ve favoriler yeniden açılışta korunuyor.
- Tek rota ve çok duraklı rota hesaplaması sonuç üretiyor.
- `newworld_navigation` istasyonu doğru verileri gösteriyor.
- `DEPOSITS` sekmesi jeoloji yataklarını listeliyor.
- Boyut değişiminde eski radar sonuçları yeni konumla karışmıyor.
- Player GUI `DISCOVERIES` sekmesinde Structure ve Geology kayıtları ayrı filtreleniyor; kategori başına senkron kotası bir türün diğerini gizlemesini önlüyor.
- Seçili keşif aynı boyuttaysa canlı oyuncu mesafesi hareketle güncelleniyor, farklı boyuttaysa `DIFFERENT DIMENSION` gösteriliyor.

### 6. Jeoloji yatakları

Vanilla yatak aileleri için en az bir örnek doğrulanmalıdır:

- Overworld: demir, bakır, karbon, altın, redstone, lapis, elmas, zümrüt
- Nether: quartz

`0.5.59.5-alpha-radar-survey-isolation` test buildinde şu modlu yatak aileleri tanımlıdır:

| Yatak | Dahili aile | Kaynak/palette kuralı |
|---|---|---|
| Osmium-rich | `osmium_strata` | Mekanism |
| Tin-rich | `tin_lode` | Mekanism |
| Lead-rich | `lead_galena` | Mekanism + Immersive Engineering, tek aile |
| Uranium-rich | `uranium_pitchblende` | Mekanism + IE + Oritech, tek aile |
| Fluorite-rich | `fluorite_crystal` | Mekanism |
| Aluminum-rich | `bauxite_strata` | Immersive Engineering |
| Nickel-rich | `nickel_sulfide` | IE + Oritech, tek aile |
| Silver-rich | `silver_vein` | Immersive Engineering |
| Zinc-rich | `zinc_lode` | Create |
| Platinum-rich | `platinum_intrusion` | Oritech |
| Uraninite-rich | `uraninite_pocket` | Powah; Uranium'dan ayrı |
| Certus Quartz | `certus_quartz_matrix` | AE2; Charged Certus hariç |

Lead ve Uranium yataklarında negatif Y seviyelerindeki deepslate varyantları ayrıca kontrol edilmelidir.

> Bu aileler JAR, template ve kurulu registry kimlikleri düzeyinde doğrulanmıştır. `uraninite_pocket` için radar → Navigation → TARDIS rota → fiziksel yatak zinciri 1 Eylül 2026'da oyun içinde doğrulandı. Diğer ailelerin toplu kabulü, Mining/depletion ve denge testleri tamamlanana kadar build test sürümü olarak kalır.

Her yatak için:

- [ ] Dünya üretiminde fiziksel bloklar oluşuyor.
- [ ] Radar aynı yatağı doğru aile adıyla görüyor.
- [ ] Mining M1 aynı aileyi tarayıp çıkarabiliyor.
- [ ] Dünya yeniden açıldığında işaretçi ve keşif kaydı korunuyor.

### 7. Replication bağlantısı

Aktif KubeJS kurallarıyla birlikte test et:

- [ ] Matter değeri olan sıradan eşyalar parçalanabiliyor.
- [ ] Yalnızca izin verilen doğal kaynaklar taranabiliyor.
- [ ] Terminal yalnızca öğrenilmiş kaynakları üretiyor.
- [ ] Raw ore kaynaklarının özel Matter değerleri uygulanıyor.
- [ ] Normal Certus Quartz çalışıyor, Charged Certus üretim zincirine girmiyor.
- [ ] Flint taranamıyor, üretilemiyor ve parçalanamıyor.
- [ ] Ortak mineral etiketleri aynı ailede kopya bilgi oluşturmuyor.
- [ ] Replication Chip Storage sökülüp takılınca bilgi geri yükleniyor.

### 8. Motor ve yolculuk

| Senaryo | Beklenen sonuç |
|---|---|
| Aynı boyutta yerel rota | Standart yolculuk başlar |
| Aynı namespace'te farklı boyut | Planetary veya Universal Drive gerekir; taban 500 WE |
| Farklı namespace / evren sınıfı | Universal Drive gerekir; taban 2.500 WE |

Ayrıca Converter'ın temel üretiminin saniyede `1 WE` olduğu, stabilizasyon ve cooldown değerlerinin panelde güncellendiği doğrulanmalıdır.

### 9. Kalıcılık testi

Önemli her testin ardından oyundan tamamen çık, instance'ı yeniden aç ve şunları kontrol et:

- TARDIS iç/dış konumu
- Oda sınırları ve sahiplik
- Engine Matrix bağlantısı ve enerji
- Radar geçmişi ve favoriler
- Navigasyon rotaları
- Jeoloji keşifleri
- Mining M1 kuyruk ve tamponları
- Replication öğrenilmiş kaynakları
- CC:Tweaked bilgisayar rol bağlantıları

## Güncel kapsam

### Bu build'de bulunan sistemler

- Room Controller ve oda koruması
- Engine Matrix, gemi ağı ve öncelikli enerji çıkışları
- Mining M1 tarama / çıkarım döngüsü
- Structure Radar ve navigasyon
- Vanilla ile on iki modlu jeoloji yatak ailesi
- CC:Tweaked terminal ve telemetri bağlantıları
- Replication bilgi ve kaynak akışı entegrasyonu
- DoctorWhoMod başlangıç TARDIS'i, özel köprü ve oda yapıları
- Yerel, gezegenler arası ve evrensel yolculuk sınıfları

### Hâlâ genişletilecek veya tam doğrulanacak alanlar

- Deposit sıklığı, reserve, yoğunluk ve rarity değerlerinin oyun içi dengelemesi
- Tam araştırma / teknoloji ağacı
- Production Chamber'ın nihai üretim tarifleri
- Genetik sisteminin son oynanış döngüsü
- Uzun süreli sunucu ve çok oyunculu dayanıklılık testleri

## Hata raporu için toplanacaklar

Bir sorun çıktığında şu dosya ve bilgileri birlikte kaydet:

- `logs/latest.log`
- Varsa crash report
- Kullanılan iki özel JAR'ın tam dosya adları ve SHA-256 değerleri
- Dünya adı, boyut ve koordinat
- Hatanın oluşması için gereken kısa adımlar
- Yeniden başlatma sonrası sürüp sürmediği
- Mümkünse ekran görüntüsü veya kısa video

## Başarı ölçütü

Build, açılış testinden geçip ana köprü ve oda yapılarını yüklediğinde; Mining M1, radar, navigasyon, jeoloji, Replication ve üç yolculuk sınıfı yeniden başlatma sonrasında veri kaybetmeden çalıştığında alpha kabul testini geçmiş sayılır.
