# New World DoctorWhoMod Fork — Güncel JAR Analizi

Bu belge, New World mod paketinde kullanılan DoctorWhoMod fork'unun güncel durumunu açıklar. Eski `1.0.16` taban JAR incelemesinin yerine geçer.

## İncelenen sürüm

| Alan | Değer |
|---|---|
| Dosya | `DoctorWhoMod-1.21.1-NeoForge-1.0.16-NewWorld-EngineTravel-v5.8.19-Tall-Large-XLarge-Swap.jar` |
| SHA-256 | `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356` |
| Minecraft | 1.21.1 |
| Mod yükleyici | NeoForge 21.1+ |
| Mod kimliği | `dwm` |
| Mod metadata sürümü | `1.0.16` |
| New World yapı etiketi | `EngineTravel-v5.8.19-Tall-Large-XLarge-Swap` |
| Lisans | MIT |

> `1.0.16`, upstream modun metadata sürümüdür. Dosya adındaki `v5.8.19`, New World fork geliştirme serisini gösterir.

## Bu fork neden depoda tutuluyor?

Bu JAR sıradan CurseForge dağıtımı değildir. New World için değiştirilmiş DoctorWhoMod sürümüdür ve paket tarafından doğrudan kullanılır. Bu nedenle yalnızca bu proje tarafından üretilen iki özel JAR'dan biri olarak Git deposunda tutulur.

Diğer üçüncü taraf mod JAR'ları Git'e eklenmez; `manifest.json` ve `pack-lock.json` üzerinden CurseForge'dan kurulur.

## Fork'un sağladığı ana sistemler

### 1. TARDIS uçuşu ve fiziksel yolculuk

DoctorWhoMod tarafı TARDIS'in fiziksel varlığını, iç/dış yapı geçişlerini, kalkış ve materialization akışını yönetir. New World geliştirmeleri motor seyahatini üç sınıfa ayırır:

| Yolculuk | Koşul | Gerekli sürücü | Temel maliyet |
|---|---|---|---:|
| Yerel | Aynı boyut içinde | Standart motor sistemi | Rota ve mesafeye göre |
| Gezegenler arası | Aynı namespace, farklı boyut | Planetary veya Universal Drive | 500 WE |
| Evrensel | Farklı namespace / evren sınıfı | Universal Drive | 2.500 WE |

Gerçek yolculukta rota, stabilizasyon, bekleme süresi ve yükseltmeler maliyeti etkileyebilir. Nihai kurallar NewWorldCore tarafındaki navigasyon ve Engine Matrix verileriyle birlikte uygulanır.

### 2. New World başlangıç TARDIS'i

Fork, oyuncuya yalnızca menüsel bir başlangıç vermek yerine fiziksel ve oynanabilir bir başlangıç TARDIS'i sağlar. Gerekli başlangıç motor bileşenleri ilk kurulumda bir kez hazırlanır; yeniden girişte çoğaltılmaması gerekir.

### 3. Ana köprü ve özel oda yapıları

Güncel paket aşağıdaki New World yapılarını içerir:

- Yaklaşık `43 × 36 × 59` ölçülerinde ana köprü yapısı
- Temizlenmiş AE2 / Replication depolama alanları
- CC:Tweaked rol istasyonları ve görev konumları
- Teleporter odası
- Koridor parçaları ve bağlantı yapıları
- Alçak, yüksek, büyük ve ekstra büyük oda varyantları
- Kesin NBT yerleşimine göre hazırlanmış yapı geçişleri

Dosya adındaki `Tall-Large-XLarge-Swap`, güncel oda varyantlarının bu build içinde bulunduğunu gösterir.

### 4. Bilgisayar rol istasyonları

Fork içindeki fiziksel istasyonlar NewWorldCore ağ rollerine bağlanır:

| İstasyon | Ağ rolü |
|---|---|
| Gemi / köprü | `newworld` |
| Madencilik | `newworld_mining` |
| Navigasyon | `newworld_navigation` |

Bu ayrım, ekranların ve terminallerin doğru gemi sistemine bağlanmasını sağlar.

### 5. Dil desteği

New World fork serisi İngilizce ve Türkçe yerelleştirme düzeltmeleri içerir. Yeni ekran veya blok eklenirken iki dil dosyasının birlikte güncellenmesi gerekir.

## DoctorWhoMod ile NewWorldCore görev ayrımı

İki özel JAR birbirinin alternatifi değildir; birlikte çalışır.

| DoctorWhoMod fork | NewWorldCore |
|---|---|
| TARDIS fiziksel gövdesi ve materialization | Oda denetleyicileri ve koruma |
| İç/dış yapı geçişleri | Engine Matrix ve enerji kuralları |
| Ana köprü, odalar ve koridor NBT'leri | Mining M1, derin depolama ve rota çıktıları |
| Uçuşun temel fiziksel akışı | Radar, navigasyon, geçmiş, favoriler ve çoklu rota |
| Başlangıç TARDIS'i | Jeoloji yatakları ve tarama verileri |
| DoctorWhoMod blokları ve içerikleri | CC:Tweaked telemetrisi ve Replication bağlantıları |

Kısaca DoctorWhoMod, geminin fiziksel dünyadaki gövdesini ve seyahat zeminini; NewWorldCore ise New World'e özgü sistem katmanını taşır.

## Doğrulanması gereken bağımlılıklar

- Minecraft `1.21.1`
- NeoForge `21.1+`
- Architectury
- Güncel NewWorldCore fork'u
- Paket manifestinde sabitlenen DoctorWhoMod bağımlılıkları
- Yapılarda kullanılan AE2, CC:Tweaked ve Replication içerikleri

Bir bağımlılığın sürümü değiştirilirse ana köprü ve oda NBT'lerindeki blok kimlikleri ayrıca test edilmelidir.

## Riskler ve bakım notları

1. **Kaynak kodu görünürlüğü:** Depoda şu anda dağıtım JAR'ı ana gerçek kaynaktır. Kaynak proje ayrı yerdeyse GitHub'a bağlanmalı; değilse yeniden üretilebilir build süreci oluşturulmalıdır.
2. **Çift sürüm:** `mods/` klasöründe bu fork ile upstream DoctorWhoMod JAR'ı aynı anda bulunmamalıdır.
3. **Metadata farkı:** Mod menüsünde `1.0.16`, dosyada ise New World `v5.8.19` etiketi görünmesi beklenen durumdur.
4. **NBT kırılması:** Başka modlardaki blok kimliği değişiklikleri yapı yüklemesini bozabilir.
5. **Statik inceleme sınırı:** Bu belge JAR metadata'sı, gömülü değişiklik notları ve paket entegrasyonuna dayanır. Oyun içi davranış her yeni build'de test dünyasında doğrulanmalıdır.

## Sürüm yükseltme kontrol listesi

- [ ] Eski DoctorWhoMod fork JAR'ı kaldırıldı ve klasörde tek sürüm kaldı.
- [ ] SHA-256 değeri `pack-lock.json` ile aynı.
- [ ] Yeni dünya oluşturma ve mevcut dünyayı açma başarılı.
- [ ] Başlangıç TARDIS'i fiziksel olarak oluşuyor.
- [ ] Ana köprü ve dört oda boyutu hatasız yükleniyor.
- [ ] Teleporter ve koridor bağlantıları doğru.
- [ ] Gemi, madencilik ve navigasyon bilgisayarları doğru role bağlanıyor.
- [ ] Yerel, gezegenler arası ve evrensel yolculuk kuralları doğru.
- [ ] Yeniden girişte başlangıç bileşenleri çoğalmıyor.
- [ ] Sunucu yeniden başlatıldıktan sonra TARDIS konumu ve motor durumu korunuyor.

## Sonuç

Bu JAR, New World için aktif ve gerekli DoctorWhoMod fork'udur. Eski belgelerdeki yalnızca oda sistemi prototipi anlatımı artık geçerli değildir; güncel fork başlangıç TARDIS'ini, genişletilmiş yapı setini ve Engine Travel entegrasyonunu birlikte taşır.
