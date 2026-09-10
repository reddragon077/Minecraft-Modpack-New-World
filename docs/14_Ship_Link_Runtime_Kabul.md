# Ship Link — Oyun kabulü ve .68.2 SYNCING regresyonu

## Güncel kabul — 10 Eylül, 16:48 sonrası

`.68.2` yeni oturumda 16:19:17 CONNECTED/NOMINAL, 16:19:36 Discoveries 128/471; 16:31:47 OUT OF RANGE (5998 blok), 16:36:53 otomatik CONNECTED/NOMINAL olarak doğrulandı. Kullanıcı 12 blok geri bağlantı ekranını paylaştı ve dönüşte zaten düzeldiğini, önceki ekran görüntüsünü geç gönderdiğini açıkladı. Kalıcı SYNCING/geri dönüş regresyon kapısı kapandı; Aşama 9'a geçilebilir. Ayrı zorlanmış GUI yaşam döngüsü, çok oyunculu ve yapay timeout kabulü iddia edilmiyor. Aşağıdaki bekleyen aday metinleri tarihsel kurulum planıdır.

Durum (10 Eylül 2026): `.68.0` önceki oturumdaki temel kontrolleri geçti; `.68.1` yeni oturumda sürekli SYNCING ile başarısız oldu. Aşama 5 kabul kapısı yeniden açık. Kurulu `.68.2` otomatik testleri geçti; oyun kabulü bekliyor.

## Önce yapılacak .68.2 kontrolü

Canlı tanıda aynı isim/parametreli iki `Minecraft.getConnection()` bulundu: `Connection` döndüren null, `ClientPacketListener` döndüren doluydu. Önceki kod yanlış metodu seçtiği için hiç veri isteği göndermiyordu. `.68.2` dönüş türünü de eşleştirir; ayar, sahiplik veya erişim sınırı değiştirmez.

1. Oyunu tamamen yeni oturumla aç, TARDIS yakınında Player GUI → OVERVIEW: birkaç saniyede SYNCING yerine CONNECTED ve güncel telemetri gelmeli.
2. DISCOVERIES listesi yüklenmeli; GUI kapat/aç sonrası yeniden veri gelmeli.
3. Ardından aşağıdaki OUT OF RANGE / geri yaklaşma ve isteğe bağlı boyut izni kontrollerini yap. `.68.1` neden düzeltmesi korunmuştur.

Yedi otomatik test paketi geçti. Yeni JVM düzeyindeki çakışma testi iki metot sırasını, doğru bağlantının dolu/boş durumunu ve yanlış dönüş türüne geri düşmeme davranışını sınar. Gerçek oyun sonucu henüz doğrulanmadı.

## Doğrulanan sonuçlar

- 13:37:02 ON BOARD; 14:24:02 menzil dışı LOST (ekranda 11770 blok); 14:24:40 gemiye dönüşte CONNECTED (ekranda 9 blok) ve Overview verileri geri geldi.
- 14:26:28 Nether'da DIMENSIONAL; gemi Overworld'de, boyutlar arasında sahte mesafe yok. 14:28:10 canlı `allow_dimensional_link=false` ile LOST / DIMENSION LINK DISABLED ve Discoveries kilidi; 14:28:37 true ile liste geri geldi (`synced=128 total=471`).
- 14:32:14 favori, 14:32:16 hedef, 14:32:17 hazır rota aynı gemiye ve `minecraft:overworld|-2456|40|328` kaydına uygulandı. Fiziksel Navigation Terminal ekranı aynı ALUMINUM hedefini ve READY_HOP1_LOADED / 1 hop gösterdi. Bu test rota hazırlama aktarımıdır; uçuş tamamlandı iddiası değildir.
- İlk 80-tick Survey 14:35:17'de tamamlandı, bağlantı 14:35:20'de koptu; bu bir iptal kanıtı değildir. İkinci Structure Survey 14:37:34.652'de 400 tick ile sıraya alındı; 14:37:46.037'de boyut izni kapandı; 14:37:54.660'taki yürütmede bağlantı kontrolü erken döndü. Kaynakta `requireLink` keşif arama/yazmadan öncedir. Sarmalayıcının `completed` logu kayıt yazıldı anlamına gelmez. Kanıt log sırası + kaynak kontrolüdür; öncesi/sonrası NBT denetimi yapılmadı.
- Test ayarları yedeklenip geri alındı: range=5000, dimensional=true, refresh=20, stale=120; Structure Survey delay=80 tick. Test-config yedeği: laptop `backups/config-tests/ship-link-restore-20260910-144210/`.

## Şimdi yapılacak kısa .68.1 kontrolü

Overview daha önce bütün başarısızlıkları `NO OWNED SHIP / LOADED INTERIOR REQUIRED` diye gösteriyordu. Artık aynı sunucu çözümlemesindeki gerçek neden korunur; erişim yokken eski telemetri yine gizlenir.

1. Güvenli şekilde menzil dışına çıkıp OVERVIEW aç: `SHIP LINK LOST // OUT OF RANGE` görünmeli.
2. İsteğe bağlı boyut kontrolü: Nether'dayken canlı dimensional iznini false yap; OVERVIEW `SHIP LINK LOST // DIMENSION LINK DISABLED` göstermeli. Sonunda true'ya geri al.
3. Bağlantıyı geri kur: Overview verileri ve NOMINAL/gerçek sağlık durumu dönmeli. Yeni görsel sonuç kullanıcı tarafından doğrulanana kadar bu kontrol açık kalır.

Sahipsiz/yüklenmeyen gemi, bilinmeyen konum/veri dahil altı nedenin üretimi, telemetri gizliliği, wire round-trip ve headless GUI metin çizimi otomatik testte geçti. Bu, oyun ekranının görsel kabulü değildir.

## Kapsam sınırı ve yeniden kullanılabilir kontrol listesi

Aşağıdaki liste tüm kontrollerin bu oturumda yapıldığı iddiası değildir. Ayrı Geological gecikmeli iptal, yeni kapılarla pozitif yerinde Survey regresyonu, yeniden giriş/sekme yaşam döngüsünün zorlanmış durumları, başka oyuncu izolasyonu ve yapay ağ timeout'u oyun içinde ayrıca doğrulanmalıdır.

1. Kendi geminin içinde Player GUI aç: sağ üstte `LINK CONNECTED // ON BOARD`, altında geminin dış boyutu görünmeli. Sekme değiştirirken bağlantı göstergesi kalmalı; Overview ve Discoveries çalışmalı.
2. Gemiden çık, aynı boyutta yürüyerek uzaklaş: `CONNECTED // ... BLK` oyuncu ile geminin dış konumu arasındaki 3B mesafeyi göstermeli; yüksekliği de hesaba katar.
3. Güvenli test için `config/newworldcore/ship-link.properties` dosyasını yedekle, `range_blocks=32` yap. Geminin dışındayken 32 bloktan uzaklaş: LOST; Survey/Discoveries içeriği kilitlenmeli. Gemiye yaklaşınca otomatik açılmalı ve Discovery listesi yenilenmeli. Sonunda 5000'e geri al.
4. Sahip olduğun gemi yüklüyken başka boyuta geç: varsayılan `allow_dimensional_link=true` ile DIMENSIONAL ve geminin boyutu; boyutlar arasında sahte blok mesafesi görünmemeli. Ayarı false yapınca LOST, true yapınca tekrar DIMENSIONAL. Kendi geminin içi bu ayardan etkilenmez.
5. Menzil içinde gecikmeli Field Survey başlatıp tamamlanmadan menzil dışına çık: yeni keşif yazılmamalı; link kaybı bildirilmeli. Tekrar menzilde Structure/Geological Survey regresyonunu dene.
6. Bağlıyken Discoveries FAV/TARGET/ROUTE işlemlerini geminin dışından da kontrol et. Rota, bağlantının ait olduğu gemiye gitmeli. LOST iken hiçbir yeni uzaktan işlem kabul edilmemeli; önceden çalışan gemi Mining/rota işi bu özellik tarafından durdurulmaz.
7. Ekranı kapat/aç, dünyadan çık/gir: bağlantı yeniden doğrulanana kadar eski kayıt erişimi kapalı kalmalı. Varsayılan yenileme 20 tick (~1 sn); eski veri eşiği 120 tick (~6 sn). Sunucu süreleri istemciye gönderilir.

Sahipsiz oyuncu, başka oyuncunun gemisi ve yapay ağ kesintisi kontrolleri mümkünse ayrı test dünyası/sunucuda yapılır. Otomatik sahiplik, bilinmeyen veri, timeout ve paket testleri geçti; bu gerçek çok oyunculu kabulün yerine geçmez.

Log işaretleri: `[NewWorld Ship Link] state=...`, `snapshot failed`, `decode rejected`, `render failed`, `dispatch failed`. Göstergenin yenilenmesi yalnız GUI açıkken sorgulanır; chunk yüklemez. Test için enerji deposunu boşaltmaya gerek yoktur.
