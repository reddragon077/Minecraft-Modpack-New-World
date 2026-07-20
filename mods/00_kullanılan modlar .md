---
Belge: 00_Kullanılan_Modlar.md
Sürüm: 1.0
Durum: 🚧 Taslak
Son Güncelleme: 20.07.2026
Minecraft Sürümü: 1.21.1
Mod Yükleyici: NeoForge
Toplam Mod: 274
---

# 🧩 Kullanılan Modlar

> **"Modlar oyunu oluşturmaz. Oyunun kurallarını biz oluştururuz."**

---

# 📖 Belgenin Amacı

Bu belge, **New World** mod paketinde kurulu olan modları kategorilere ayırır ve her modun projedeki kullanım amacını kısa biçimde açıklar.

`00_Temel_Modlar.md`, mod seçim felsefesini ve tasarım kurallarını açıklar.

`00_Kullanılan_Modlar.md` ise pakette gerçekten bulunan modların güncel envanteridir.

Bir modun bu listede bulunması, bütün varsayılan içeriklerinin serbest biçimde kullanılacağı anlamına gelmez. Tarifler, makineler, ekipmanlar ve üretim yöntemleri; araştırma sistemi, hikâye ilerleyişi ve Production Chamber kurallarına göre sınırlandırılabilir.

---

# 🏷️ Durum İşaretleri

| İşaret | Anlamı |
|---|---|
| ✅ | Mod aktif olarak kullanılıyor. |
| 🔗 | Ana modlar arasında entegrasyon sağlıyor. |
| 🧩 | Başka modların çalışması için gereken teknik bağımlılık. |
| 🧪 | Beta sürüm veya geliştirme sırasında ayrıca test edilmesi gereken mod. |
| ⚠️ | Kaldırılması, değiştirilmesi veya uyumluluk testi yapılması gereken mod. |

---

# 📊 Kategori Özeti

| Kategori | Mod Sayısı |
|---|---:|
| Ana Proje Sistemleri | 8 |
| Teknoloji, Enerji ve Üretim | 16 |
| AE2, Depolama ve Dijital Ağlar | 21 |
| Otomasyon, Lojistik ve Programlama | 12 |
| Entegrasyonlar ve Proje Eklentileri | 24 |
| Keşif, Yapılar ve Dünya Üretimi | 31 |
| Savaş, Yaratıklar, Büyü ve Oyuncu Gelişimi | 10 |
| Dekorasyon, İnşaat ve Aydınlatma | 28 |
| Görevler, Takımlar ve Sunucu Yönetimi | 10 |
| Arayüz, Tarif Görüntüleme ve Yaşam Kalitesi | 49 |
| Performans ve Optimizasyon | 14 |
| Atmosfer, Ses ve Görsel | 4 |
| Kütüphaneler ve Teknik Bağımlılıklar | 47 |
| **Toplam** | **274** |

---

# ⚠️ Güncel Kontrol Notları

## CC:Tweaked Sürüm Kuralı

Pakette `CC:Tweaked 1.116.1` kuruludur.

```text
CC:Tweaked 1.113.1  → Create 6 ile uyumsuz
CC:Tweaked 1.116.1+ → Kullanılabilir
```

CC:Tweaked, güncelleme veya yeniden kurulum sırasında `1.116.1` sürümünün altına düşürülmemelidir.

## EMI Temizliği

Projenin güncel tarif görüntüleme kararı **JEI** yönündedir. Buna rağmen mod klasöründe EMI ve EMI eklentileri hâlâ bulunmaktadır.

Kaldırılması veya bağımlılık kontrolü yapılması gereken dosyalar:

- `emi`
- `emi_accelerator`
- `emi_enchanting`
- `emi_loot`
- `emi_ores`
- `emijeicompat`
- `emilink`
- `emixx`
- `custom_machinery_emi`

Bu modlar kaldırılmadan önce başka bir modun zorunlu bağımlılığı olup olmadığı test edilmelidir.

## Önceki Kararlarla Uyumlu Durumlar

- **Ad Astra** mevcut listede bulunmuyor.
- **Modern Industrialization** mevcut listede bulunmuyor.
- **Macaw modları** mevcut listede bulunmuyor.
- **More Culling** mevcut listede bulunmuyor. Sodium uyumsuzluğu nedeniyle yeniden eklenmemelidir.
- **Regions Unexplored**, daha önce yaşanan çökme ihtimali nedeniyle yeni dünya testlerinde ayrıca kontrol edilmelidir.

---

# Ana Proje Sistemleri

New World’ün hikâye, gemi, oda, araştırma ve özel ilerleme sistemlerini doğrudan taşıyan ana modlar.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `[1.21.1] SecurityCraft v1.10.2.1` | Reaktör ve Warp Core gibi kritik gemi alanlarında güvenlik, şifreli kapı ve erişim kontrolü sağlar. | ✅ Kullanılıyor |
| `CustomMachinery-neoforge-1.21.1-0.10.69` | Research Station ve projeye özel makinelerin tanımlanmasını sağlar. | ✅ Kullanılıyor |
| `DoctorWhoMod-1.21.1-neoforge-1.0.16` | TARDIS benzeri boyutsal iç mekân ve gemi oda sistemi için temel altyapı sağlar. | ✅ Kullanılıyor |
| `kubejs-neoforge-2101.7.2-build.368` | Tarifler, araştırma, ilerleme, görev tetikleri ve özel oyun kurallarının ana script altyapısıdır. | ✅ Kullanılıyor |
| `NeoSync-1.21.1-1.2.1` | Klonlama, yeniden doğma ve yaşam kabini temasını destekleyen Sync benzeri sistemi sağlar. | ✅ Kullanılıyor |
| `Replication-1.21.1-1.2.7` | Analiz edilen ham maddelerin enerji ve veri karşılığında yeniden üretilmesini sağlayan temel Replikatör modudur. | ✅ Kullanılıyor |
| `roomopolis-1.21.1-2.8.3` | Boyutsal oda oluşturma ve özelleştirilebilir iç alan sistemini destekler. | ✅ Kullanılıyor |
| `tardis-remote-addon-1.0.0` | TARDIS/gemi sistemlerine uzaktan erişim ve kontrol desteği ekler. | ✅ Kullanılıyor |

---

# Teknoloji, Enerji ve Üretim

Üretim zincirleri, enerji altyapısı, malzeme işleme ve ileri teknoloji aşamalarında kullanılan içerik modları.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `create-1.21.1-6.0.10` | Erken oyun mekanik gücü, hareketli sistemler ve fiziksel üretim hatları sağlar. | ✅ Kullanılıyor |
| `growthacceleratortiers-1.0.4_neoforge_1.21.1` | Tarım üretimini aşamalı hızlandıran gelişim katmanları ekler. | ✅ Kullanılıyor |
| `HostileNeuralNetworks-1.21.1-6.4.2` | Canlı verilerini simülasyona dönüştüren, keşif sonrası otomatik kaynak üretimi sistemi sağlar. | ✅ Kullanılıyor |
| `ImmersiveEngineering-1.21.1-12.4.2-194` | Ağır sanayi, kablo, enerji, çok bloklu makine ve endüstriyel dekor altyapısı sağlar. | ✅ Kullanılıyor |
| `industrial-foregoing-souls-1.21.1-1.10.4` | Industrial Foregoing için ruh tabanlı hızlandırma ve ileri otomasyon desteği sağlar. | ✅ Kullanılıyor |
| `industrialforegoing-1.21-3.6.38` | Kaynak işleme, tarım ve fabrika otomasyonu sağlar; hazır makineleri ilerlemeye göre sınırlandırılacaktır. | ✅ Kullanılıyor |
| `justdirethings-1.5.7` | Başlangıç aletleri ve zamanla geliştirilebilen teknoloji ekipmanları sağlar. | ✅ Kullanılıyor |
| `Mekanism-1.21.1-10.7.19.85` | Gaz, kimya, ileri malzeme işleme ve oyun sonu teknoloji altyapısını sağlar. | ✅ Kullanılıyor |
| `MekanismGenerators-1.21.1-10.7.19.85` | Mekanism için gelişmiş jeneratör, fisyon ve füzyon enerji üretimini sağlar. | ✅ Kullanılıyor |
| `MekanismTools-1.21.1-10.7.19.85` | Mekanism malzemelerinden araç ve ekipmanlar ekler; tarifleri proje dengesine göre sınırlandırılabilir. | ✅ Kullanılıyor |
| `oritech-neoforge-1.21.1-1.2.9` | İleri üretim, enerji ve çok bloklu teknoloji sistemleri ekler. | ✅ Kullanılıyor |
| `pneumaticcraft-repressurized-8.2.20+mc1.21.1` | Basınçlı hava, lojistik, drone ve programlanabilir otomasyon sistemleri sağlar. | ✅ Kullanılıyor |
| `productivemetalworks-1.21.1-1.15.0` | Metal eritme, döküm ve alaşım üretimi için endüstriyel süreçler sağlar. | ✅ Kullanılıyor |
| `rftoolsbase-1.21-6.0.11` | RFTools modlarının ortak enerji ve teknik altyapısını sağlar. | ✅ Kullanılıyor |
| `rftoolsutility-1.21-7.0.12` | Makine kontrolü, ekranlar ve çeşitli teknolojik yardımcı sistemler sağlar. | ✅ Kullanılıyor |
| `torchmaster-neoforge-1.21.1-21.1.9` | Üretim, enerji veya malzeme işleme altyapısına ek özellikler sağlar. | ✅ Kullanılıyor |

---

# AE2, Depolama ve Dijital Ağlar

Merkezi depolama, dijital lojistik, kablosuz erişim ve AE2 tabanlı veri ağı eklentileri.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `AdvancedAE-1.6.11-1.21.1` | AE2 için ileri seviye ağ bileşenleri ve gelişmiş otomasyon seçenekleri ekler. | ✅ Kullanılıyor |
| `AE2 Useful Utilities-1.21.1-1.0.1-NeoForge` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `AE2-Things-1.4.2-beta` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | 🧪 Beta / test |
| `AE2MEGAThings-1.21.1-2.0.4` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `ae2tooltipfix-neoforge-1.0.0+1.21.1` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `ae2wtlib-19.5.0` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `aeinfinitybooster-neoforge-1.21.1-1.0.0.58` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `applied-experienced-1.21.1-1.3.2` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `appliedenergistics2-19.2.17` | Geminin merkezi dijital depolama, otomasyon ve veri ağıdır. | ✅ Kullanılıyor |
| `AppliedFlux-1.21-2.1.5-neoforge` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `appliedsoul-1.21.1-2.0.3` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `ExtendedAE-1.21-2.2.33-neoforge` | AE2 sistemine genişletilmiş cihazlar, ağ araçları ve otomasyon özellikleri ekler. | ✅ Kullanılıyor |
| `functionalstorage-1.21.1-1.5.4` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `megacells-4.11.0` | AE2 depolama kapasitesini büyük hücreler ve ileri bileşenlerle genişletir. | ✅ Kullanılıyor |
| `merequester-neoforge-1.21.1-1.4.3` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `mesoulcard-1.1.1` | AE2 makinelerinde ruh hızlandırma mekaniklerini kullanmaya yardımcı olan yükseltme desteği sağlar. | ✅ Kullanılıyor |
| `polyeng-0.4.1` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `rep_ae2_bridge-1.8.0.0.0-neoforge-1.21.1` | Replication sistemini AE2 ağına bağlayan köprü görevi görür. | ✅ Kullanılıyor |
| `sophisticatedbackpacks-1.21.1-3.25.70.1985` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `sophisticatedstorage-1.21.1-1.5.76.1972` | AE2 veya depolama sistemine ek kapasite, araç ya da entegrasyon sağlar. | ✅ Kullanılıyor |
| `soulplied_energistics-1.0.3` | Industrial Foregoing Souls kaynaklarını AE2 ağı üzerinden taşımayı sağlar. | ✅ Kullanılıyor |

---

# Otomasyon, Lojistik ve Programlama

Eşya, sıvı, gaz ve enerji taşımayı; programlanabilir fabrika kontrolünü ve otomasyonu sağlayan modlar.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `AdvancedPeripherals-1.21.1-0.7.57b` | CC:Tweaked bilgisayarlarının makineler ve çevre birimleriyle iletişim kurmasını sağlar. | ✅ Kullanılıyor |
| `cc-tweaked-1.21.1-forge-1.116.1` | Programlanabilir bilgisayarlar, ağlar ve fabrika kontrol sistemleri sağlar. Minimum sürüm 1.116.1 olmalıdır. | ✅ Kullanılıyor |
| `eio_pressure_conduits-1.0.2-1.21.1` | Ender IO conduit sistemini PneumaticCraft basınç ağıyla entegre eder. | ✅ Kullanılıyor |
| `enderio-8.2.11-beta` | Kompakt kablo, conduit, enerji ve makine altyapısı sağlar. | 🧪 Beta / test |
| `FluxNetworks-1.21.1-8.0.0` | Boyutlar arası kablosuz enerji aktarımı ve merkezi güç ağı sağlar. | ✅ Kullanılıyor |
| `laserio-1.9.11` | Eşya, sıvı, enerji ve redstone aktarımı için kanal tabanlı lojistik sağlar. | ✅ Kullanılıyor |
| `little-big-redstone-1.9.4-1.21.1` | Kompakt ve ayrıntılı redstone devreleri kurmayı sağlar. | ✅ Kullanılıyor |
| `mekanisticrouters-1.2.0` | Mekanism sistemleri için yönlendirme ve lojistik seçenekleri ekler. | ✅ Kullanılıyor |
| `Modern-Dynamics-0.9.6` | Kablo ve boru tabanlı eşya, sıvı ve enerji taşımacılığı sağlar. | ✅ Kullanılıyor |
| `modular-routers-13.2.6+mc1.21.1` | Programlanabilir yönlendiricilerle eşya ve blok etkileşimi otomasyonu sağlar. | ✅ Kullanılıyor |
| `pipez-neoforge-1.21.1-1.2.31` | Basit ve yükseltilebilir borularla eşya, sıvı, gaz ve enerji aktarımı sağlar. | ✅ Kullanılıyor |
| `Super Factory Manager (SFM)-MC1.21.1-4.34.0` | Production Chamber içindeki karmaşık fabrika lojistiğini kod benzeri kurallarla yönetir. | ✅ Kullanılıyor |

---

# Entegrasyonlar ve Proje Eklentileri

Ana modların KubeJS, Custom Machinery, JEI, AE2 veya diğer sistemlerle birlikte çalışmasını sağlayan köprüler.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `Applied-Mekanistics-1.6.3` | Mekanism kimyasallarını AE2 ağı üzerinden depolama ve taşıma desteği sağlar. | 🔗 Entegrasyon |
| `Applied-Replicatics-21.1-1.0.4` | Replication sistemini AE2 ağı ve otomasyonuyla birleştirir. | 🔗 Entegrasyon |
| `appliedjs-1.21.1-1.0.0` | AE2 içeriklerinin KubeJS ile özelleştirilmesini sağlar. | 🔗 Entegrasyon |
| `appliedpneumatics-1.21.1-neoforge-1.0.8` | PneumaticCraft sistemlerini AE2 ağına bağlayan entegrasyon sağlar. | 🔗 Entegrasyon |
| `arseng-2.1.1-beta` | İki veya daha fazla mod arasında tarif, veri ya da mekanik uyumluluğu sağlar. | 🧪 Beta / test |
| `custom_machinery_ars-1.2.5` | Custom Machinery ile Ars Nouveau kaynaklarını birleştirir. | 🔗 Entegrasyon |
| `CustomMachineryCreate-1.21.1-1.2.7` | Erken oyun mekanik gücü, hareketli sistemler ve fiziksel üretim hatları sağlar. | 🔗 Entegrasyon |
| `CustomMachineryMekanism-1.21.1-1.4.16` | Gaz, kimya, ileri malzeme işleme ve oyun sonu teknoloji altyapısını sağlar. | 🔗 Entegrasyon |
| `CustomMachineryPneumaticCraft-1.21.1-1.0.7` | Basınçlı hava, lojistik, drone ve programlanabilir otomasyon sistemleri sağlar. | 🔗 Entegrasyon |
| `ddfabfm-1.2.0-neoforge-1.21.1` | Mobilya ve dekoratif işlev blokları ekler. | 🔗 Entegrasyon |
| `extra-mod-integrations-all-neoforge-1.0.3+1.21.1` | İki veya daha fazla mod arasında tarif, veri ya da mekanik uyumluluğu sağlar. | 🔗 Entegrasyon |
| `ftb-xmod-compat-neoforge-21.1.9` | İki veya daha fazla mod arasında tarif, veri ya da mekanik uyumluluğu sağlar. | 🔗 Entegrasyon |
| `GeOre_Nouveau-1.21.1-0.5.9` | İki veya daha fazla mod arasında tarif, veri ya da mekanik uyumluluğu sağlar. | 🔗 Entegrasyon |
| `immersive_engineering_js-2101.1.1` | Immersive Engineering tarif ve sistemlerinin KubeJS ile düzenlenmesini sağlar. | 🔗 Entegrasyon |
| `jdtkubejs-1.0.0` | Just Dire Things içeriklerinin KubeJS ile özelleştirilmesini sağlar. | 🔗 Entegrasyon |
| `kjsigrm-1.4.1` | KubeJS ve Custom Machinery tariflerini oyun içinden oluşturup dışa aktarmaya yardımcı olur. | 🔗 Entegrasyon |
| `kubejs-mekanism-neoforge-2101.1.7-build.18` | Gaz, kimya, ileri malzeme işleme ve oyun sonu teknoloji altyapısını sağlar. | 🔗 Entegrasyon |
| `kubejs_curios_neoforge_1.21.1-1.0.4` | Curios slot ve eşyalarının KubeJS ile kontrol edilmesini sağlar. | 🔗 Entegrasyon |
| `kubejs_oritech-neoforge-1.21.1-0.4.4` | İleri üretim, enerji ve çok bloklu teknoloji sistemleri ekler. | 🔗 Entegrasyon |
| `lootjs-neoforge-1.21.1-3.7.0` | İki veya daha fazla mod arasında tarif, veri ya da mekanik uyumluluğu sağlar. | 🔗 Entegrasyon |
| `mifa-neoforge-1.21.x-2.1.0` | Industrial Foregoing için ek hız, verimlilik ve işleme yükseltmeleri sağlar. | 🔗 Entegrasyon |
| `productive-metalworks-kubejs-addon-1.0.0` | Productive Metalworks tariflerini KubeJS üzerinden kontrol etmeyi sağlar. | 🔗 Entegrasyon |
| `rep_up-1.0.0.0.1-neoforge-1.21.1` | Replication sistemine ek yükseltme veya yardımcı özellikler sağlar. | 🔗 Entegrasyon |
| `replicatemekanism-1.0.7 - release` | Gaz, kimya, ileri malzeme işleme ve oyun sonu teknoloji altyapısını sağlar. | 🔗 Entegrasyon |

---

# Keşif, Yapılar ve Dünya Üretimi

Dünyaları, biyomları, mağaraları, yapıları, arkeolojiyi ve keşif içeriğini çeşitlendiren modlar.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `[Neoforge]ctov-3.6.3` | Köylerin yapısını ve mimari çeşitliliğini genişletir. | ✅ Kullanılıyor |
| `betterarcheology-neoforge-1.21.1-1.3.4` | Arkeoloji, kalıntı ve keşif odaklı yapılar ile araştırma içeriği ekler. | ✅ Kullanılıyor |
| `ecologics-1.21.1-2.3.7` | Mevcut biyomlara yeni canlılar, bitkiler ve çevresel ayrıntılar ekler. | ✅ Kullanılıyor |
| `Explorify v1.6.5.mod` | Dünyaya keşfedilebilir küçük yapılar ve ilgi noktaları ekler. | ✅ Kullanılıyor |
| `GeOre-1.21.1-6.2.2` | Jeolojik kaynak üretimi ekler; kaynak çıktıları New World dengesine göre sınırlandırılacaktır. | ✅ Kullanılıyor |
| `graveyards-1.0.0` | Mezarlık ve karanlık keşif yapıları ekler. | ✅ Kullanılıyor |
| `industrialhellscape-1.0.3` | Dünya üretimini, keşfi, yapıları veya ganimet çeşitliliğini geliştirir. | ✅ Kullanılıyor |
| `lithostitched-1.7.13-neoforge-21.1` | Dünya üretimini, keşfi, yapıları veya ganimet çeşitliliğini geliştirir. | ✅ Kullanılıyor |
| `lootintegration_wda-1.8` | Belirli yapı veya yaratık modlarını ortak ganimet sistemine bağlar. | ✅ Kullanılıyor |
| `lootintegrations-1.21.1-4.7` | Farklı modların ganimet tablolarını birbiriyle uyumlu hâle getirir. | ✅ Kullanılıyor |
| `lootintegrations_betterarcheology-1.2` | Arkeoloji, kalıntı ve keşif odaklı yapılar ile araştırma içeriği ekler. | ✅ Kullanılıyor |
| `lootintegrations_borninchaos-1.0` | Farklı modların ganimet tablolarını birbiriyle uyumlu hâle getirir. | ✅ Kullanılıyor |
| `lootintegrations_ctov-1.4` | Farklı modların ganimet tablolarını birbiriyle uyumlu hâle getirir. | ✅ Kullanılıyor |
| `naturalist-1.0.2-neoforge-1.21.1` | Dünyaya daha fazla doğal hayvan ve ekosistem içeriği ekler. | ✅ Kullanılıyor |
| `natures_spirit-2.2.5-1.21.1` | Doğal biyom ve bitki çeşitliliğini artırır. | ✅ Kullanılıyor |
| `regions-unexplored-0.6.2-neoforge-21.1` | Yeni biyomlar ve dünya çeşitliliği ekler; mevcut crash geçmişi nedeniyle ayrıca test edilmelidir. | ⚠️ Uyumluluk testi |
| `Structory_26.2_v1.3.7` | Keşif sırasında karşılaşılabilecek yeni yapılar ekler. | ✅ Kullanılıyor |
| `t_and_t-neoforge-fabric-1.13.9+1.21.1` | Köy ve kule benzeri yapı çeşitliliğini artırır. | ✅ Kullanılıyor |
| `TerraBlender-neoforge-1.21.1-4.1.0.8` | Dünya üretimini, keşfi, yapıları veya ganimet çeşitliliğini geliştirir. | ✅ Kullanılıyor |
| `YungsBetterCaves-1.21.1-NeoForge-3.1.4` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterDesertTemples-1.21.1-NeoForge-4.1.5` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterDungeons-1.21.1-NeoForge-5.1.4` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterEndIsland-1.21.1-NeoForge-3.1.2` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterJungleTemples-1.21.1-NeoForge-3.1.2` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterMineshafts-1.21.1-NeoForge-5.1.1` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterNetherFortresses-1.21.1-NeoForge-3.1.5` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterOceanMonuments-1.21.1-NeoForge-4.1.2` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterStrongholds-1.21.1-NeoForge-5.1.3` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsBetterWitchHuts-1.21.1-NeoForge-4.1.1` | Vanilla yapılarını daha büyük ve keşfe değer hâle getirir. | ✅ Kullanılıyor |
| `YungsCaveBiomes-1.21.1-NeoForge-3.1.1` | Mağaralara yeni biyom ve çevre çeşitliliği ekler. | ✅ Kullanılıyor |
| `YungsExtras-1.21.1-NeoForge-5.1.1` | Dünya üretimine küçük yapılar ve çevresel ayrıntılar ekler. | ✅ Kullanılıyor |

---

# Savaş, Yaratıklar, Büyü ve Oyuncu Gelişimi

Yeni düşmanlar, canlılar, büyü sistemleri, boss içerikleri ve oyuncu gelişimi sağlayan modlar.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `alexsmobs-1.22.17` | Yeni canlılar, ekolojik karşılaşmalar ve biyolojik araştırma örnekleri sağlar. | ✅ Kullanılıyor |
| `ApothicAttributes-1.21.1-2.9.1` | Oyuncu ve eşya niteliklerini genişleten gelişmiş özellik altyapısı sağlar. | ✅ Kullanılıyor |
| `ars_nouveau-1.21.1-5.12.1` | Bilimsel olarak açıklanacak büyü sistemleri ve kaynakları sağlar. | ✅ Kullanılıyor |
| `artifacts-neoforge-13.2.1` | Keşifle bulunan özel aksesuarlar ve oyuncu yetenekleri ekler. | ✅ Kullanılıyor |
| `born_in_chaos_[Neoforge]_1.21.1_1.7.6` | Daha tehlikeli yaratıklar ve hayatta kalma baskısı ekler. | ✅ Kullanılıyor |
| `GatewaysToEternity-1.21.1-5.1.0` | Dalga tabanlı düşman karşılaşmaları ve savaş görevleri oluşturur. | ✅ Kullanılıyor |
| `irons_spellbooks-1.21.1-3.16.2` | Savaş odaklı büyü, büyü kitapları ve yetenekler ekler. | ✅ Kullanılıyor |
| `L_Ender's Cataclysm 1.21.1-3.32` | Gelişmiş boss savaşları, yapılar ve oyun sonu mücadeleleri ekler. | ✅ Kullanılıyor |
| `relics-1.21.1-0.10.7.8` | Nadir, geliştirilebilir ve keşif odaklı ekipmanlar ekler. | ✅ Kullanılıyor |
| `ScorchedGuns-1.5` | Bilim kurgu/ateşli silah içerikleri ekler; denge ve hikâye uyumu ayrıca ayarlanmalıdır. | ✅ Kullanılıyor |

---

# Dekorasyon, İnşaat ve Aydınlatma

Uzay gemisi, Production Chamber ve dünya yapılarının görsel tasarımı için kullanılan blok ve inşaat araçları.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `bf_blockpack-neoforge-1.21.1-1.0.11` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `buildinggadgets2-1.3.9` | Büyük yapıların hızlı ve kontrollü biçimde inşa edilmesini sağlar. | ✅ Kullanılıyor |
| `buildinggadgets2gui-0.2.0-beta.1` | Büyük yapıların hızlı ve kontrollü biçimde inşa edilmesini sağlar. | 🧪 Beta / test |
| `chipped-neoforge-1.21.1-4.0.2` | Yapı bloklarına çok sayıda dekoratif görünüm seçeneği ekler. | ✅ Kullanılıyor |
| `chisel_chipped_integration-v1.3.9-1.21.1` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `colors-1.21.1-1.4.0` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `ConstructionSticks-1.21.1-1.4.1` | Tekrarlayan blok yerleştirmelerini kolaylaştıran inşaat araçları sağlar. | ✅ Kullanılıyor |
| `create_chipped-1.2.0-neoforge-1.21.1` | Yapı bloklarına çok sayıda dekoratif görünüm seçeneği ekler. | ✅ Kullanılıyor |
| `DecorativeBlocks-Reborn-neoforge-1.21.1-6.0.2` | Yapılar için dekoratif blok ve mimari parçalar ekler. | ✅ Kullanılıyor |
| `decorblocks-neo-1.0.1` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `Design-n-Decor-1.21.1-2.2b` | Endüstriyel ve modern dekorasyon seçenekleri ekler. | ✅ Kullanılıyor |
| `elevatorid-neoforge-1.21.1-1.11.4` | Gemi katları ve üretim alanları arasında asansör ulaşımı sağlar. | ✅ Kullanılıyor |
| `framedbg2support-0.1.0` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `FramedBlocks-10.6.1` | İstenen blok dokusunu alabilen şekilli yapı parçaları sağlar. | ✅ Kullanılıyor |
| `handcrafted-neoforge-1.21.1-4.0.3` | Mobilya ve iç mekân dekorasyonu ekler. | ✅ Kullanılıyor |
| `iden_decor-neoforge-1.21.1-2.2.4` | Bilim kurgu ve iç mekân dekorasyonunda kullanılabilecek bloklar ekler. | ✅ Kullanılıyor |
| `immersive_engineer_decor_controls_tool_reforged-1.1.41-reconstructed` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `industrially_plated-1.0.0` | Metal plaka ve endüstriyel yüzey dekorları sağlar. | ✅ Kullanılıyor |
| `neoncraft-1.1.1-neoforge-1.21.1` | Neon ve bilim kurgu temalı aydınlatma/dekor öğeleri ekler. | ✅ Kullanılıyor |
| `Night Lights NeoForge v1.2.0 [1.21.1]` | Gece ve iç mekân kullanımı için dekoratif ışıklar ekler. | ✅ Kullanılıyor |
| `placers-1.21.1-1.3.1` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `rechiseled-1.2.5-neoforge-mc1.21` | Blokların farklı dekoratif varyantlarını üretmeyi sağlar. | ✅ Kullanılıyor |
| `rechiseledae2-neoforge-1.21-1.21.1-1.0.0` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `simplylight-1.5.3+1.21.1-b4` | Kompakt ve farklı biçimlerde aydınlatma blokları sağlar. | ✅ Kullanılıyor |
| `supplementaries-neoforge-1.21.1-3.8.0` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `suppsquared-neoforge-1.21-1.2.18` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |
| `worldedit-mod-7.3.8` | Gemi ve yapı tasarımında hızlı harita düzenleme araçları sağlar. | ✅ Kullanılıyor |
| `XeKr's Decoration-1.21.1-NeoForge-1.1.8` | Yapı tasarımı için dekoratif blok, araç veya aydınlatma seçeneği sağlar. | ✅ Kullanılıyor |

---

# Görevler, Takımlar ve Sunucu Yönetimi

Görev kitabı, takım sistemi, chunk yönetimi, yedekleme ve çok oyunculu yardımcı sistemler.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `ExtraQuests-1.7.0-1.21.1-NeoForge` | FTB Quests sistemine ek görev özellikleri sağlar. | ✅ Kullanılıyor |
| `ftb-chunks-neoforge-2101.1.20` | Chunk talep etme, koruma ve harita tabanlı bölge yönetimi sağlar. | ✅ Kullanılıyor |
| `ftb-essentials-neoforge-2101.1.10` | Görev, takım, sunucu veya çok oyunculu yönetim işlevi sağlar. | ✅ Kullanılıyor |
| `ftb-filter-system-neoforge-21.1.4` | Görev, takım, sunucu veya çok oyunculu yönetim işlevi sağlar. | ✅ Kullanılıyor |
| `ftb-quests-neoforge-2101.1.27` | Ana görev kitabı, araştırma ilerleyişi ve hikâye görevlerini yönetir. | ✅ Kullanılıyor |
| `ftb-stuff-things-21.1.18` | Görev, takım, sunucu veya çok oyunculu yönetim işlevi sağlar. | ✅ Kullanılıyor |
| `ftb-team-bases-21.1.15` | Takımlar için başlangıç üssü veya ayrı üs alanları oluşturmayı destekler. | ✅ Kullanılıyor |
| `ftb-teams-neoforge-2101.1.10` | Takım ve çok oyunculu ilerleme altyapısını sağlar. | ✅ Kullanılıyor |
| `ftbbackups2-neoforge-1.21-1.0.28` | Dünyanın otomatik yedeklerini oluşturur. | ✅ Kullanılıyor |
| `lootr-neoforge-1.21.1-1.11.37.122` | Çok oyunculuda ganimet sandıklarını oyuncuya özel hâle getirir. | ✅ Kullanılıyor |

---

# Arayüz, Tarif Görüntüleme ve Yaşam Kalitesi

Tarif görüntüleme, bilgi ekranları, harita, envanter kullanımı ve genel kullanıcı deneyimi iyileştirmeleri.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `ae2jeiintegration-1.2.1` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `angel_utilities-1.0.9-neoforge-1.21.1` | İnşaat ve boşlukta çalışma için yardımcı araçlar sağlar. | ✅ Kullanılıyor |
| `appleskin-neoforge-mc1.21-3.0.9` | Açlık, doygunluk ve yiyecek değerlerini arayüzde gösterir. | ✅ Kullanılıyor |
| `BetterAdvancements-NeoForge-1.21.1-0.4.3.21` | İlerleme ekranını daha okunabilir ve kullanışlı hâle getirir. | ✅ Kullanılıyor |
| `configured-neoforge-1.21.1-2.6.3` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `Controlling-neoforge-1.21.1-19.0.5` | Tuş atamalarını aramayı ve çakışmaları bulmayı kolaylaştırır. | ✅ Kullanılıyor |
| `createjeicompat-1.0.2` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `custom_machinery_emi-1.0.0` | Custom Machinery tariflerini EMI üzerinde gösterir; JEI kararı nedeniyle kaldırılma adayıdır. | ⚠️ Kaldırma adayı |
| `distraction_free_recipes-neoforge-1.2.2-1.21.1` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `emi-1.1.24+1.21.1+neoforge` | Alternatif tarif görüntüleme sistemi; proje JEI tercih ettiği için kaldırılma adayıdır. | ⚠️ Kaldırma adayı |
| `emi_accelerator-1.1.4` | EMI için ek tarif ve bilgi modülü; JEI kararı nedeniyle kontrol edilmelidir. | ⚠️ Kaldırma adayı |
| `emi_enchanting-0.1.2+1.21+neoforge` | EMI için ek tarif ve bilgi modülü; JEI kararı nedeniyle kontrol edilmelidir. | ⚠️ Kaldırma adayı |
| `emi_loot-0.7.9+1.21+neoforge` | EMI için ek tarif ve bilgi modülü; JEI kararı nedeniyle kontrol edilmelidir. | ⚠️ Kaldırma adayı |
| `emi_ores-1.3+1.21.1+neoforge` | EMI için ek tarif ve bilgi modülü; JEI kararı nedeniyle kontrol edilmelidir. | ⚠️ Kaldırma adayı |
| `emijeicompat-1.0.4` | EMI ile JEI eklentileri arasında uyumluluk sağlar; EMI kaldırılırsa gereksizdir. | ⚠️ Kaldırma adayı |
| `emilink-1.1.13` | EMI bağlantı/entegrasyon özelliği sağlar; EMI kaldırılırsa gereksizdir. | ⚠️ Kaldırma adayı |
| `emixx-neoforge-1.21.1-3.1.2` | EMI için ek arayüz özellikleri sağlar; EMI kaldırılırsa gereksizdir. | ⚠️ Kaldırma adayı |
| `ftb-jei-extras-21.1.7` | Tarifleri, kullanım alanlarını ve üretim süreçlerini görüntüler. | ✅ Kullanılıyor |
| `ftb-ultimine-neoforge-2101.1.15` | Bağlantılı blokları toplu kırmayı sağlar; kullanım sınırları proje dengesine göre ayarlanmalıdır. | ✅ Kullanılıyor |
| `geodejei-1.1` | Tarifleri, kullanım alanlarını ve üretim süreçlerini görüntüler. | ✅ Kullanılıyor |
| `guideme-21.1.17` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `inventoryhud.neoforged.1.21.1-3.4.28` | Envanter, zırh ve durum bilgilerini oyun ekranında gösterir. | ✅ Kullanılıyor |
| `Jade-1.21.1-NeoForge-15.10.5` | Bakılan blok, makine ve canlı hakkında ekranda bilgi gösterir. | ✅ Kullanılıyor |
| `JadeAddons-1.21.1-NeoForge-6.1.0` | Jade bilgi paneline diğer modlar için ek veri sağlar. | ✅ Kullanılıyor |
| `jearchaeology-1.21.1-1.2.0` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `jei-1.21.1-neoforge-19.39.0.368` | Tarifleri, kullanım alanlarını ve üretim süreçlerini görüntüler. | ✅ Kullanılıyor |
| `jei-quickcraft-1.21.1-neoforge-1.0` | Tarifleri, kullanım alanlarını ve üretim süreçlerini görüntüler. | ✅ Kullanılıyor |
| `jei_enhancements-1.0.0` | JEI için ek tarif, yapı veya bilgi görüntüleme desteği sağlar. | ✅ Kullanılıyor |
| `jei_gateways-1.1.2+mc1.21.1` | Dalga tabanlı düşman karşılaşmaları ve savaş görevleri oluşturur. | ✅ Kullanılıyor |
| `jei_structures-1.21.1-1.4` | JEI için ek tarif, yapı veya bilgi görüntüleme desteği sağlar. | ✅ Kullanılıyor |
| `jeimultiblocks-1.21.1-1.0.6` | Çok bloklu makinelerin yapılarını JEI içinde görüntüler. | ✅ Kullanılıyor |
| `jeiworldgen-neoforge-1.21.1-1.4.0` | Dünya üretimi ve kaynak oluşumu bilgilerini JEI içinde gösterir. | ✅ Kullanılıyor |
| `journeymap-neoforge-1.21.1-6.0.0` | Harita, işaret noktası ve keşif takibi sağlar. | ✅ Kullanılıyor |
| `justenoughbreeding-neoforge-1.21.1-3.1.0` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `JustEnoughMekanismMultiblocks-1.21.1-7.15` | Mekanism çok bloklu yapılarını JEI üzerinden gösterir. | ✅ Kullanılıyor |
| `JustEnoughProfessions-neoforge-1.21.1-4.0.5` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `JustEnoughResources-NeoForge-1.21.1-1.6.0.17` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `kubejei-1.0.0` | Tarifleri, kullanım alanlarını ve üretim süreçlerini görüntüler. | ✅ Kullanılıyor |
| `mekagenjei-1.2` | Tarifleri, kullanım alanlarını ve üretim süreçlerini görüntüler. | ✅ Kullanılıyor |
| `mekajadeupgrade-1.3` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `moreoverlays-1.24.2-mc1.21.1-neoforge` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `MouseTweaks-neoforge-mc1.21-2.26.1` | Envanterde fare ile eşya taşıma ve dağıtma işlemlerini geliştirir. | ✅ Kullanılıyor |
| `polymorph-neoforge-1.1.0+1.21.1` | Birbiriyle çakışan crafting tarifleri arasında seçim yapılmasını sağlar. | ✅ Kullanılıyor |
| `Quark-4.1-482` | Vanilla oynanışı çok sayıda küçük özellik ve yaşam kalitesi geliştirmesiyle genişletir. | ✅ Kullanılıyor |
| `shulkerboxtooltip-neoforge-5.1.9+1.21.1` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `smithingtemplateviewer-1.0.4` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `sophisticated_jei_index-1.1.0-neoforge-1.21.1` | JEI için ek tarif, yapı veya bilgi görüntüleme desteği sağlar. | ✅ Kullanılıyor |
| `trashslot-neoforge-1.21.1-21.1.11` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |
| `VisualWorkbench-v21.1.1-1.21.1-NeoForge` | Arayüzü, bilgi erişimini veya günlük oynanış kullanımını kolaylaştırır. | ✅ Kullanılıyor |

---

# Performans ve Optimizasyon

İstemci, sunucu, bellek, yapay zekâ, render, redstone ve ağ performansını iyileştiren modlar.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `AI-Improvements-1.21-0.5.3` | Canlı yapay zekâ hesaplamalarının işlemci yükünü azaltır. | ✅ Kullanılıyor |
| `alltheleaks-1.1.10+1.21.1-neoforge` | Bellek sızıntılarını ve uzun oturumlarda oluşan performans sorunlarını azaltır. | ✅ Kullanılıyor |
| `alternate_current-mc1.21-1.9.0` | Redstone ağlarının hesaplama performansını iyileştirir. | ✅ Kullanılıyor |
| `BadOptimizations-2.4.1-1.21.1` | Çeşitli istemci ve oyun hesaplamalarını optimize eder. | ✅ Kullanılıyor |
| `chloride-NEOFORGE-mc1.21.1-v1.8.1` | Render ayarları ve grafik performansı için optimizasyon desteği sağlar. | ✅ Kullanılıyor |
| `Clumps-neoforge-1.21.1-19.0.0.1` | Deneyim kürelerini birleştirerek varlık yükünü azaltır. | ✅ Kullanılıyor |
| `entityculling-neoforge-1.10.5-mc1.21.1` | Görünmeyen varlık ve blok varlıklarının çizilmesini engeller. | ✅ Kullanılıyor |
| `fastboot-1.21.x-v1.3neo` | Oyunun açılış ve yeniden yükleme süresini azaltmaya yardımcı olur. | ✅ Kullanılıyor |
| `ferritecore-7.0.3-neoforge` | Bellek kullanımını azaltır. | ✅ Kullanılıyor |
| `FTBQuestsOptimizer-neoforge-3.2.0-1.21.1` | FTB Quests kaynaklı görev kontrolü ve veri yükünü azaltır. | ✅ Kullanılıyor |
| `ImmediatelyFast-NeoForge-1.6.11+1.21.1` | Arayüz ve render işlemlerini hızlandırır. | ✅ Kullanılıyor |
| `krypton_fnp-neoforge-1.21.1-0.2.28.1-1.21.1` | Ağ iletişimi ve paket işleme performansını iyileştirir. | ✅ Kullanılıyor |
| `modernfix-neoforge-5.27.20+mc1.21.1` | Bellek, yükleme, veri paketi ve genel oyun performansına yönelik kapsamlı düzeltmeler sağlar. | ✅ Kullanılıyor |
| `sodium-neoforge-0.8.12+mc1.21.1` | Render motorunu optimize ederek FPS ve görsel performansı artırır. | ✅ Kullanılıyor |

---

# Atmosfer, Ses ve Görsel

Ses ortamını, ayak seslerini, çevresel efektleri ve dünyanın atmosferini güçlendiren modlar.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `AmbientSounds_NEOFORGE_v6.3.8_mc1.21.1` | Biyom ve çevreye göre zengin ortam sesleri ekler. | ✅ Kullanılıyor |
| `Falling Leaves-1.0` | Ağaçlardan düşen yaprak parçacıklarıyla çevresel atmosferi güçlendirir. | ✅ Kullanılıyor |
| `PresenceFootsteps-1.21.1-1.12.0-beta.1-1.21NeoForge` | Zemine ve ortama göre ayrıntılı ayak sesleri oluşturur. | 🧪 Beta / test |
| `sound-physics-remastered-neoforge-1.21.1-1.5.1` | Yankı, duvar arkası ses ve mekânsal ses fiziği ekler. | ✅ Kullanılıyor |

---

# Kütüphaneler ve Teknik Bağımlılıklar

Diğer modların çalışması için gereken API, çekirdek, yapılandırma ve kod kütüphaneleri.

| Mod / Dosya | Kısa Kullanım Amacı | Durum |
|---|---|---|
| `architectury-13.0.8-neoforge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `athena-neoforge-1.21.1-4.0.6` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `balm-neoforge-1.21.1-21.0.63` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `bblcore-1.21-1.3.20` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `citadel-1.21.1-2.7.6` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `cloth-config-15.0.140-neoforge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `CreativeCore_NEOFORGE_v2.13.41_mc1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `cristellib-neoforge-1.21.1-3.1.7` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `CTM-1.21-1.2.1+3` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `cupboard-1.21.1-3.8` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `curios-neoforge-9.5.1+1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `experiencelib-1.21.1-1.2.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `ExtraLib-3.0.11-1.21.1-NeoForge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `ForgeConfigAPIPort-v21.1.6-1.21.1-NeoForge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `forgified-fabric-api-0.116.14+2.3.0+1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `framework-neoforge-1.21.1-0.13.11` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `ftb-library-neoforge-2101.1.33` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `fusion-1.3.5-neoforge-mc1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `fzzy_config-0.7.6+1.21+neoforge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `geckolib-neoforge-1.21.1-4.9.2` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `Glodium-1.21-2.2-neoforge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `gtbcs_spell_lib-1.5.0-1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `irons_lib-1.21.1-2.1.0` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `Kiwi-1.21.1-NeoForge-15.8.7` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `konkrete_neoforge_1.9.9_MC_1.21` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `kotlinforforge-5.12.0-all` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `libIPN-neoforge-1.21.1-6.6.3` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `lionfishapi-3.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `mcjtylib-1.21-9.0.21` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `moonlight-neoforge-1.21.1-3.1.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `OctoLib-NEOFORGE-0.6.2+1.21` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `Placebo-1.21.1-9.9.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `player-animation-lib-forge-2.0.4+1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `polylib-2100.1.0-build.183-neoforge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `PuzzlesLib-v21.1.52-1.21.1-NeoForge` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `resourcefulconfig-neoforge-1.21-3.0.11` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `resourcefullib-neoforge-1.21-3.0.12` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `rhino-2101.2.7-build.85` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `Searchables-neoforge-1.21.1-1.0.2` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `sophisticatedcore-1.21.1-1.4.73.2151` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `supermartijn642configlib-1.1.8-neoforge-mc1.21` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `supermartijn642corelib-1.1.21-neoforge-mc1.21` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `tesseract-api-neoforge-1.12.8-1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `titanium-1.21-4.0.43` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `txnilib-neoforge-1.0.24-1.21.1` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `YungsApi-1.21.1-NeoForge-5.1.6` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |
| `Zeta-1.1-40` | Diğer modların çalışması için gereken teknik kütüphane veya API’dir. | 🧩 Bağımlılık |

---

# 📜 Genel Kullanım Kuralları

Modların projede kullanımı aşağıdaki temel kurallara bağlıdır:

1. Hazır makineler, Production Chamber içerisinde kurulması planlanan bir üretim sürecinin yerini almamalıdır.
2. Aynı işi yapan modlar arasında görev ayrımı yapılmalı; gereksiz tekrar engellenmelidir.
3. Replikatör yalnızca ham materyal üretmeli, hazır parça veya son ürün vermemelidir.
4. Gelişmiş teknoloji ve ekipmanlar araştırma ilerleyişine bağlanmalıdır.
5. Büyü sistemleri, lore içerisinde henüz açıklanmamış bilimsel prensipler olarak ele alınmalıdır.
6. Keşif modları yalnızca ganimet değil; araştırma verisi, hikâye ve yeni mekanik sunmalıdır.
7. Performans modları her büyük mod güncellemesinden sonra yeniden uyumluluk testine alınmalıdır.
8. Beta durumundaki modlar, kararlı sürüme geçilmeden önce ayrı test dünyasında denenmelidir.

---

# 📌 Sonuç

Bu liste yalnızca kurulu modların envanteri değildir.

Her modun **New World** içerisindeki rolünü, hangi sistemlere hizmet ettiğini ve hangi içeriklerin sınırlandırılması gerektiğini takip etmek için kullanılacak yaşayan bir proje belgesidir.

Mod listesi değiştiğinde;

- Toplam mod sayısı,
- Kategori tabloları,
- Uyumluluk notları,
- Kaldırma adayları,
- Minimum sürüm kuralları

birlikte güncellenmelidir.

---

> **"Bir modun değeri, eklediği içerik miktarıyla değil; oyunun tasarımına ne kadar hizmet ettiğiyle ölçülür."**