# New World — DoctorWhoMod 1.0.16 JAR Teknik Analizi

**İncelenen dosya:** `DoctorWhoMod-1.21.1-neoforge-1.0.16(1).jar`  
**SHA-256:** `5fa8234f4f0d124cdfd8756dfdfb63111feddcdd9cef594ed8f1fff55afeb5df`  
**Analiz türü:** Statik JAR, kaynak yapısı ve Java bytecode analizi  
**Not:** Bu rapor oyunu çalıştırarak yapılan dinamik testleri içermez.

---

## 1. Kimlik ve teknik temel

JAR içindeki `META-INF/neoforge.mods.toml` bilgileri:

- Mod kimliği: `dwm`
- Görünen ad: `DoctorWhoMod`
- Sürüm: `1.0.16`
- Yapımcı: `DrGmes`
- Lisans: `MIT`
- Minecraft: tam olarak `1.21.1`
- NeoForge: `21.1+`
- Architectury API: `13.0.8+`
- Ana sınıfların classfile sürümü: Java 21 (`major 65`)

JAR içinde **428 Java sınıfı** bulunuyor. Kod isimleri karartılmamış; sınıf, alan ve metot adları okunabilir. Bu nedenle kaynak deposu üzerinden fork yapmak teknik olarak oldukça elverişli.

Başlıca dağılım:

- 131 TARDIS blok sınıfı
- 37 TARDIS ortak sistem sınıfı
- 21 istemci ağ paketi
- 17 sunucu ağ paketi
- 49 dekoratif blok sınıfı

---

## 2. Merkezi TARDIS veri yapısı

Ana sınıf:

`net.drgmes.dwm.common.tardis.TardisStateManager`

Bu sınıf `SavedData` üzerinden geminin kalıcı durumunu NBT'ye kaydediyor. Şunları yönetiyor:

- Sahip UUID'si
- Mevcut, önceki ve hedef dimension
- Mevcut, önceki ve hedef koordinat
- Hedef yön
- Konsol odası ve dış kabuk tipi
- Kapılar, ışıklar, kalkanlar ve el freni
- Sistem bileşenleri
- Yakıt ve enerji kapasitesi/miktarı
- Uçuş, materialization ve araştırma sistemlerinin NBT verileri

Kayıtlı ana sistemler:

1. `TardisSystemConsoleRoom`
2. `TardisSystemResearch`
3. `TardisSystemMaterialization`
4. `TardisSystemFlight`
5. `TardisSystemShields`

Bu mimari New World için uygundur; enerji, seyahat, araştırma ve oda mekanikleri birbirinden ayrılmış durumda.

---

## 3. Kritik enerji hataları

Enerji/yakıt alanları gerçekten mevcut ve NBT'ye kaydediliyor. Ancak getter metotlarında açık bir hata var:

- `getFuelAmount()` gerçek `fuelAmount` yerine `fuelCapacity` döndürüyor.
- `getEnergyAmount()` gerçek `energyAmount` yerine `energyCapacity` döndürüyor.
- `isFuelHarvesting()` kayıtlı alan yerine her zaman `true` döndürüyor.
- `isEnergyHarvesting()` kayıtlı alan yerine her zaman `true` döndürüyor.

Sonuç:

- Konsol ve API enerji deposunu sürekli dolu görebilir.
- Enerji çıkarma/ekleme hesapları yanlış miktar üzerinden ilerleyebilir.
- Yakıt ve enerji harvesting anahtarlarının gerçek durumu dikkate alınmaz.
- TARDIS anahtarı uçuş başarısız olduğunda “yakıt yok” kontrolünü güvenilir biçimde yapamaz.

Ayrıca setter'lar değerleri `0..capacity` aralığına sıkıştırmıyor. Negatif veya kapasite üstü değerlerin yazılması teorik olarak mümkün.

### İlk düzeltme

`TardisStateManager` içinde:

- Getter'lar doğru alanı döndürmeli.
- Setter'lar clamp uygulamalı.
- `tryConsumeEnergy(int amount)` gibi atomik bir işlem eklenmeli.
- `addEnergy(int amount)` güvenli aralıkta çalışmalı.
- Harvesting getter'ları kayıtlı boolean alanları döndürmeli.

Mevcut varsayılanlar:

- Yakıt kapasitesi: `100`
- Başlangıç yakıtı: `0`
- Enerji kapasitesi: `1,000,000`
- Başlangıç enerjisi: `0`

`tick()` içinde yalnızca yakıt üretimi görülüyor: harvesting açıkken her 40 tick'te +1 yakıt. Dahili enerji yenilemesi bulunmuyor; enerji NeoForge capability üzerinden dış makinelerden alınmaya uygun tasarlanmış.

---

## 4. NeoForge enerji capability'si

Ana sınıf:

`net.drgmes.dwm.neoforge.common.TardisEnergyStorage`

NeoForge `IEnergyStorage` uygulanmış. Şu anda:

- `getEnergyStored()` → `TardisStateManager.getEnergyAmount()`
- `getMaxEnergyStored()` → kapasite
- `receiveEnergy()` ve `extractEnergy()` → StateManager miktarını değiştiriyor
- Enerji değişince konsol güncelleniyor

Altyapı var; yeniden enerji sistemi yazmak gerekmiyor. Önce getter/setter hataları düzeltilmeli, ardından warp maliyeti aynı depodan güvenli biçimde düşülmeli.

New World için iki seçenek var:

1. Depoda gerçek FE kullanmak.
2. Depoda FE tutup arayüzde “Warp Enerjisi” olarak normalize edilmiş 0–100 göstermek.

İkinci seçenek oyuncuya daha anlaşılır bir arayüz sunar fakat tek bir dönüşüm oranı tanımlanmalıdır.

---

## 5. Uçuş sistemi

Ana sınıf:

`net.drgmes.dwm.common.tardis.systems.TardisSystemFlight`

Sistem ayrı bir durum makinesi kullanıyor:

- `INITED`
- Dematerialization bekleme
- `PROCESSING`
- İniş/materialization

`init(true, playerUuid)` şu anda yalnızca şunları kontrol ediyor:

- Uçuş sistemi kurulu mu?
- Zaten uçuş var mı?
- Materialization işlemi sürüyor mu?

Şunların hiçbiri kontrol edilmiyor:

- Enerji maliyeti
- Mesafe
- Hedef dimension izni
- Araştırma/AStage seviyesi
- Uçuş sonrası cooldown
- Hedefin güvenilir kaynaktan gelip gelmediği
- Oyuncunun gemiye erişim yetkisi

### Sabit uçuş süresi

`getFlightDuration()` doğrudan `32 tick` döndürüyor; bu yaklaşık **1,6 saniye**.

Materialization tarafında ayrıca yaklaşık 240 ve 170 tick'lik fazlar bulunuyor. TARDIS anahtarının mevcut çağırma cooldown'ı `410 + flightDuration`, yani yaklaşık **22,1 saniye**.

### En doğru müdahale noktası

Bütün hedef seçme yolları sonunda `TardisSystemFlight.init(true, UUID)` çağırdığı için asıl doğrulama burada veya buranın çağırdığı yeni bir merkezi servis içinde yapılmalı.

Öneri:

`TardisTravelRules.validateAndPrepare(...)`

Kontroller:

1. Oyuncu erişimi
2. Sistem durumu
3. Cooldown
4. Hedef dimension stage'i
5. Aynı dimension mesafesi
6. Hedef türü: manuel/waypoint/beacon/görev/recall
7. Enerji maliyeti
8. Güvenli iniş uygunluğu

Enerji yalnız bütün kontroller başarılı olduktan sonra düşülmeli. Dematerialization veya iniş başarısız olursa geri ödeme politikası belirlenmeli.

---

## 6. Materialization ve güvenli iniş

Ana sınıf:

`net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization`

Mevcut sistem şunları zaten yapıyor:

- Dematerialization ve rematerialization fazları
- Dış kabuğu kaldırma/yerleştirme
- Dikey güvenli konum taraması
- Güvenli blok kontrolü
- Uygun konum arama
- Başka bir TARDIS'e iniş kontrolü
- Callback tabanlı başarı/başarısızlık akışı

Bu nedenle Ship Summon ve normal gemi sıçraması için yeni iniş motoru yazmamalıyız. Mevcut `findSafePosition` / `getSafePosition` akışı kullanılmalı ve New World kuralları öncesine eklenmeli.

---

## 7. Hedef koordinatına giden bütün yollar

Hedef dimension/koordinat yalnız konsoldan değişmiyor. Aşağıdaki yollar bulundu:

- Konsol X/Y/Z kontrolleri
- Dimension önceki/sonraki kontrolleri
- Rastgele hedef kontrolü
- Flight history paketi
- Waypoint paketi
- Telepathic interface konum seçimi
- Harita banner seçimi
- Sonic Device TARDIS modu
- TARDIS anahtarı
- Immersive Portals uyumluluğu

Bu yüzden yalnız GUI butonlarını kilitlemek güvenli değildir. Sunucu tarafında uçuş başlamadan hemen önce merkezî doğrulama zorunludur.

Başlangıç progression'ında ayrıca şu kontroller UI seviyesinde gizlenebilir/devre dışı bırakılabilir:

- Serbest X/Y/Z girişi
- Dimension değiştirme
- Randomizer
- Telepathic arbitrary-location seçimi

Ancak UI kısıtı yalnız kullanıcı deneyimidir; güvenlik kontrolü değildir.

---

## 8. Mevcut TARDIS anahtarı: Ship Summon için hazır temel

Sınıf:

`net.drgmes.dwm.items.tardis.keys.TardisKeyItem`

Shift + kullanımda anahtar:

- Bağlı TARDIS'i buluyor.
- Oyuncunun bulunduğu dimension ve koordinatı hedef yapıyor.
- TARDIS uçuşunu başlatıyor.
- Yaklaşık 22 saniyelik item cooldown koyuyor.
- Uçuş callback'iyle başarı/başarısızlığı işliyor.

Yani daha önce tasarladığımız **Ship Summon** özelliğinin temel davranışı modda zaten mevcut. Sıfırdan yazmak yerine şunları eklemeliyiz:

- Stage kontrolü
- Maksimum menzil
- Warp enerji maliyeti
- Suit/oyuncu bağlantı kontrolü
- Beacon zorunluluğu (erken oyun)
- Güvenli hedef sınırlaması
- Gemiye ait global stabilizasyon cooldown'ı

Mevcut cooldown item bazlıdır. Aynı TARDIS'e bağlı ikinci bir anahtar cooldown'ı aşabilir. Bu yüzden gerçek cooldown TARDIS'in kalıcı verisinde tutulmalıdır.

TARDIS anahtarında config ile “yalnız operatör recall yapabilir” seçeneği bulunuyor; ancak New World için sahiplik + yetkili mürettebat modeli ayrıca tasarlanmalı.

---

## 9. Sonic Device üzerinden gemi konumu seçme

Sınıf:

`net.drgmes.dwm.common.sonicdevice.modes.tardis.SonicDeviceTardisMode`

Oyuncu dış dünyada bir bloğa tıklayınca bağlı TARDIS için:

- Hedef dimension'ı oyuncunun bulunduğu dimension yapıyor.
- Hedef koordinatı tıklanan bloğun üstü yapıyor.
- Hedef yönünü oyuncuya göre ayarlıyor.

Ancak doğrudan uçuş başlatmıyor; hedefi hazırlıyor. New World progression'ında bu özellik erken oyunda kapalı olmalı veya yalnız Beacon/araştırma stage'iyle açılmalı.

---

## 10. Waypoint ve history sistemi

Uçuş sistemi NBT'de ayrı listeler tutuyor:

- `TardisFlightHistoryEntry`
- `TardisFlightWaypointEntry`

History en fazla 100 kayıt tutuyor. Waypoint oluşturma, güncelleme, silme ve uygulama için hazır ağ paketleri bulunuyor.

Bu yapı New World Teleport Beacon sistemi için iyi bir temel sağlar. Waypoint kayıtlarına ileride şu metadata eklenebilir:

- Hedef kaynağı: `MANUAL`, `DISCOVERED`, `BEACON`, `MISSION`
- Beacon UUID/ID
- Sahip takım
- Son doğrulama zamanı
- Hasarlı/çevrimdışı durumu
- Gerekli navigasyon seviyesi

### Ağ güvenliği bulgusu

İncelenen waypoint/history paketlerinde istemciden gelen `tardisId` ile ilgili TARDIS world'ü bulunuyor ve hedef koordinatlar uygulanıyor. Handler içinde açık bir:

- `checkAccess(...)`
- sahiplik kontrolü
- oyuncu TARDIS içinde mi kontrolü
- konsola yakınlık kontrolü
- gönderilen waypoint gerçekten sunucu listesindeki kayıt mı kontrolü

görülmedi.

Bu, kötü niyetli istemcinin bildiği bir TARDIS ID'sine paket göndermesi açısından risk oluşturabilir. Fork'ta:

1. Her sunucu packet handler erişim kontrolü yapmalı.
2. İstemciden tam waypoint nesnesi kabul edilmemeli; yalnız sunucu tarafındaki kayıt ID'si gönderilmeli.
3. Son savunma olarak `TardisSystemFlight` merkezi doğrulaması hedefi tekrar kontrol etmeli.

---

## 11. Araştırma sistemi beklenenden daha kullanışlı

Ana sınıf:

`net.drgmes.dwm.common.tardis.systems.TardisSystemResearch`

Mod zaten kalıcı olarak şunları kaydediyor:

- Ziyaret edilen dimension'lar
- Keşfedilen biome'lar
- Keşfedilen structure'lar

İlk kez ekleme metotları `boolean` döndürüyor:

- `addVisitedWorld(...)`
- `addVisitedBiome(...)`
- `addVisitedStructure(...)`

Kayıt daha önce varsa `false`, ilk kez ekleniyorsa `true` döndürüyor. Uçuş başarıyla tamamlandıktan sonra sistem otomatik olarak yeni world, biome ve structure bilgilerini güncelliyor.

Bu tam olarak araştırma tecrübesi için istediğimiz kancadır:

- İlk dimension keşfi → Research XP
- İlk biome keşfi → düşük/orta Research XP
- İlk structure keşfi → yapı tipine göre Research XP

En temiz çözüm, ilk eklemenin başarılı olduğu noktada New World entegrasyon callback/event'i tetiklemektir. Pufferfish Skills komutunu doğrudan her sınıfa gömmek yerine bir entegrasyon katmanı kullanılmalı.

Mevcut config seçenekleri:

- Dimension'ları her zaman açık tutma
- Biome'ları her zaman açık tutma
- Structure'ları her zaman açık tutma
- Dimension blacklist
- End'i koşullu gizleme
- Structure listesini ziyaret edilen biome'a göre filtreleme

Bu sistem araştırma ağacıyla çok iyi birleşir.

---

## 12. Oda ve ARS sistemi: varsayılan odaları kapatabiliriz

Kaynak yükleyici:

`net.drgmes.dwm.setup.ModResourcePacks`

Mod şu klasörleri resource/data pack üzerinden yüklüyor:

- `data/<namespace>/tardis/console_rooms`
- `data/<namespace>/tardis/ars`

JAR içeriğinde:

- **1.780 ARS JSON dosyası**
- **6 konsol odası JSON dosyası**
- **39 TARDIS structure NBT dosyası**

Hem console room hem ARS loader, JSON içinde:

```json
{
  "disable": true
}
```

değerini destekliyor. `disable` true ise kayıt atlanıyor.

Bu nedenle varsayılan odaları kaldırmak için Java fork şart değil. Daha yüksek öncelikli New World datapack içinde aynı ResourceLocation/path ile `disable: true` override'ı oluşturabiliriz.

Ardından yalnız kendi:

- oda kategorilerimizi,
- structure NBT'lerimizi,
- başlık/çeviri anahtarlarımızı,
- substitute bloklarımızı

ekleyebiliriz.

Console room JSON şeması ayrıca şunları destekliyor:

- `title`
- `center`
- `entrance`
- `spawnChance`
- `structure`
- `hidden`
- `repair_to`
- `doors_block`
- `decorator_block`
- `teleporter_room`
- `image`

Abandoned → repaired room dönüşümü `repair_to` ile hazır olarak destekleniyor. Bu, “oda fiziksel olarak var ama bozuk; araştırmayla onarılıyor” tasarımımıza doğrudan uyuyor.

### Harici görsel URL'leri

Konsol oda JSON'larında GitHub'a giden uzak `image` URL'leri var. Modpack'in çevrimdışı ve uzun ömürlü çalışması için bunlar yerel texture/resource sistemiyle değiştirilmelidir veya özel New World ekranında yerel önizleme kullanılmalıdır.

---

## 13. Quantum Suit yaşam desteği bu JAR'ın parçası olmamalı

JAR içinde genel Quantum/Nano Suit yaşam desteği altyapısı bulunmuyor. Yalnız titanium armor sınıfları var.

Bu nedenle önceki mimari kararı doğru:

### DoctorWhoMod fork'u

- Warp enerjisi
- Uçuş maliyeti
- Menzil
- Cooldown
- Dimension izni
- Waypoint/beacon hedefleri
- Ship Summon
- Güvenli materialization

### Ayrı `New World Core` modu

- Quantum Suit yaşam desteği
- Dışarıda kalma süresi
- Dimension hazard çarpanları
- Gemi içinde şarj
- Personal Recall
- Android uyarıları
- Pufferfish Skills / AStages entegrasyonu
- Research Station olayları

Bu ayrım upstream güncellemelerini birleştirirken çatışmayı azaltır.

---

## 14. Config eksikleri

`ModConfig.Server` boş. Mevcut ayarlar Common config altında.

New World seyahat kuralları server-authoritative bir config'e taşınmalı:

- Warp enerji kapasitesi
- Pasif enerji üretimi
- Sabit uçuş maliyeti
- Blok başına maliyet
- Dimension geçiş çarpanı
- Maksimum menzil seviyeleri
- Stabilizasyon cooldown'ı
- Recall/Ship Summon maliyetleri
- Güvenli iniş arama yarıçapı
- Başlangıçta serbest koordinat izni
- Başlangıçta dimension seçimi izni
- Başarısız uçuşta refund oranı

Bu değerler Java koduna sabit gömülmemeli.

---

## 15. Mixin ve bakım riski

Common mixin'ler:

- `MixinEntity`
- `MixinLivingEntity`
- `MixinPortal`
- `MixinServerTeleportationManager`
- İstemci: `MixinMinecraftClient`

Ayrıca MinecraftServer'ın bazı dahili alanları access transformer ile açılmış.

Bu durum 1.21.1 içinde sorun değildir fakat NeoForge/Minecraft sürümü yükseltilirken mixin hedefleri ve açılan alanlar kırılabilir. New World projesi 1.21.1'de sabit kalacağı için kısa vadede yönetilebilir.

---

## 16. Önerilen ilk kod sprinti

### Adım 1 — Kaynak deposunu temiz derlemek

- JAR'a doğrudan patch uygulamak yerine 1.0.16 ile eşleşen kaynak branch/tag kullanılmalı.
- Orijinal proje değiştirilmeden build alınmalı.
- Üretilen JAR'ın mevcut sürümle temel davranışı karşılaştırılmalı.
- MIT lisans ve telif bildirimleri korunmalı.

### Adım 2 — Enerji doğruluğu

`TardisStateManager`:

- dört hatalı getter düzeltilir,
- setter clamp eklenir,
- güvenli consume/add metotları yazılır.

`TardisEnergyStorage`:

- yeni güvenli metotları kullanacak şekilde gözden geçirilir.

### Adım 3 — Server config

Yeni seyahat config alanları eklenir:

- aynı dimension başlangıç menzili: 512 blok
- stabilizasyon: 30 saniye
- temel enerji maliyeti
- blok başı maliyet
- dimension seyahati başlangıçta kapalı

### Adım 4 — Merkezi seyahat doğrulaması

Yeni sınıf önerisi:

`net.drgmes.dwm.common.tardis.travel.TardisTravelRules`

`TardisSystemFlight.init(true, UUID)` öncesinde:

- erişim,
- stage,
- mesafe,
- dimension,
- cooldown,
- enerji

kontrol edilir.

### Adım 5 — Kalıcı cooldown

Cooldown item üzerinde değil TARDIS NBT'sinde tutulur:

- `stabilizationUntilGameTime`
- gerekiyorsa `lastTravelCost`
- gerekiyorsa `pendingTravelTransaction`

Sunucu yeniden başlasa bile doğru davranması için kalan tick yerine server/game time tabanlı kayıt tercih edilmeli.

### Adım 6 — İlk oyun testi

Başarı ölçütü:

- 512 blok içi aynı-dimension uçuş çalışıyor.
- 512 blok dışı reddediliyor.
- Farklı dimension reddediliyor.
- Enerji gerçekten azalıyor.
- Yetersiz enerji uçuşu engelliyor.
- İkinci uçuş 30 saniye boyunca engelleniyor.
- Başarısız iniş enerji/konum bozulmasına yol açmıyor.
- Konsol, key, sonic ve waypoint yolları aynı merkezi kurallara tabi.

---

## 17. Nihai değerlendirme

Bu JAR New World fork'u için **uygun bir temel**.

En güçlü tarafları:

- Sistemler iyi ayrılmış.
- Uçuş/materialization callback yapısı var.
- Enerji capability altyapısı hazır.
- Research sistemi ilk keşifleri zaten kaydediyor.
- Waypoint/history hazır.
- Ship Summon'a yakın TARDIS key davranışı hazır.
- Oda/ARS sistemi datapack tabanlı ve devre dışı bırakılabilir.

En kritik sorunları:

1. Enerji/yakıt getter'ları hatalı.
2. Uçuşta gerçek enerji, menzil ve cooldown kontrolü yok.
3. Hedef giriş yolları çok; kontrol tek UI'da yapılamaz.
4. Bazı sunucu paketlerinde sahiplik/yakınlık doğrulaması görünmüyor.
5. Item cooldown global TARDIS cooldown yerine kullanılıyor.
6. Server seyahat config'i bulunmuyor.

**Karar:** Fork mantıklı. İlk iş GUI veya Quantum Suit değil; kaynak projeyi temiz derledikten sonra enerji doğruluğu ve merkezi uçuş doğrulamasını kurmak olmalı.
