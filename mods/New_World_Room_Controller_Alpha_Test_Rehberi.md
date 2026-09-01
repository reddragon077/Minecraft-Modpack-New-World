# New World Room Controller — Alpha Test Rehberi

## Kurulacak JAR dosyaları

1. `DoctorWhoMod-1.21.1-NeoForge-1.0.16-NewWorld-RoomSystems-v0.1.jar`
2. `NewWorldCore-1.21.1-NeoForge-0.1.0-alpha.jar`

`mods` klasöründe başka bir DoctorWhoMod JAR'ı bulunmamalıdır. Architectury API kurulu kalmalıdır.

## İlk test

Dünyaya girdikten sonra Creative modda aşağıdaki komutları kullanın:

```mcfunction
/give @s newworldcore:fe_room_controller
/give @s newworldcore:fe_storage_cell 4
/give @s newworldcore:fe_io_module 1
/give @s newworldcore:fe_provider 2
/give @s newworldcore:fe_emergency_capacitor 1
```

Controller'ın tarama alanı sabit olarak controller merkezli **15 × 9 × 15** alandır:

- X: controller'dan -7 ile +7
- Y: controller'dan -1 ile +7
- Z: controller'dan -7 ile +7

Modülleri bu alanın içine yerleştirin.

Controller'a sağ tıklayın:

1. `TARA`
2. Yapı geçerliyse `BUTUNLESTIR`
3. Odadaki bir modülü kırmayı ve yeni blok koymayı deneyin; işlem engellenmelidir.
4. Controller GUI'sinden `AYIR` seçin.
5. Modüller tekrar kırılabilir ve yerleştirilebilir olmalıdır.

FE odasının geçerli olması için en az:

- 1 × FE Storage Cell
- 1 × FE I/O Module

gerekir.

FE kapasitesi şu şekilde hesaplanır:

- Temel kapasite: 1.000.000 FE
- Her Storage Cell: +1.000.000 FE
- Her Emergency Capacitor: +250.000 FE

Örnek olarak 4 Storage Cell ve 1 Emergency Capacitor ile toplam kapasite **5.250.000 FE** olmalıdır. TARDIS Features ekranındaki enerji kapasitesinden kontrol edin.

## Warp Room test komutları

```mcfunction
/give @s newworldcore:warp_room_controller
/give @s newworldcore:warp_capacitor 2
/give @s newworldcore:warp_converter 1
/give @s newworldcore:warp_efficiency_coil 2
/give @s newworldcore:warp_catalyst_chamber 1
```

Geçerlilik için en az:

- 1 × Warp Capacitor
- 1 × Warp Converter

gerekir.

Bu alpha sürümünde Warp odası taranır, bütünleştirilir ve korunur; gerçek FE → WE dönüşümü henüz aktif değildir.

## Engine Room test komutları

```mcfunction
/give @s newworldcore:engine_room_controller
/give @s newworldcore:engine_range_coil 2
/give @s newworldcore:engine_efficiency_module 1
/give @s newworldcore:engine_stabilizer 1
/give @s newworldcore:engine_dimensional_drive 1
/give @s newworldcore:engine_universal_drive 1
```

Geçerlilik için en az:

- 1 × Engine Range Coil
- 1 × Engine Efficiency Module

gerekir.

Bu alpha sürümünde Engine odası taranır, bütünleştirilir ve korunur; menzil ve dimension uçuş hesapları henüz TARDIS Flight sistemine bağlanmamıştır.

## Bu alpha sürümünde çalışanlar

- Üç farklı Room Controller bloğu
- ARS Creator tarzında kırmızı, beyaz ve turuncu controller görselleri
- Controller GUI
- Tara / Bütünleştir / Ayır işlemleri
- Sabit oda alanında modül sayımı
- Bütünleştirilmiş odada oyuncu kırma ve blok koyma koruması
- Oda durumunun dünya kaydında saklanması
- FE odasının TARDIS enerji kapasitesini değiştirmesi
- Önceki DoctorWhoMod worldgen düzeltmesi
- Gerçek FE miktarı getter/setter düzeltmeleri
- TARDIS Features ekranındaki enerji göstergesi

## Henüz eklenmeyenler

- Patlama, piston, sıvı, yangın ve WorldEdit koruması
- Serbest boyutlu oda sınırı / Room Anchor sistemi
- Warp Energy deposu ve FE → WE dönüşümü
- Catalyst envanteri
- Engine Room menzil ve uçuş maliyeti etkileri
- Dimension ve evren sürüşünün gerçek Flight bağlantısı
- Özel Creative Tab

## Hata durumunda

Oyun açılmazsa veya controller'a sağ tıklayınca çökerse şu dosyaları gönderin:

- `logs/latest.log`
- varsa `crash-reports` klasöründeki en yeni rapor

Bu sürüm gerçek oyun ortamında henüz çalıştırılmadığı için ilk test özellikle mod yükleme ve NeoForge API bağlantısını doğrulamak içindir.
