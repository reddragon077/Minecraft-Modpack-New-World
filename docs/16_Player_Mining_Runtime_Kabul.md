# Player Mining — dört alanlık toplu kabul

29 Eylül temel kabul: fiziksel terminalle ilk 977 mined/alan/%100 scan/boş Collection eşleşti. Sonraki ekranlarda [-154,11] yeni tarama %7,8 ve EXTRACT N/A; ardından MINING/EXTRACTION, scan %100, 347/%1 çıktı. Replication miktarı 1380→1178→1667, REP toplam aktarımı 4071→4835. Log 11:29:51, 11:44:30, 11:47:01 geçişleri doğruladı; Player Mining hatası yok. Tam buffer parity, GUI yeniden açma, Mining menzil dönüşü ve genişletilmiş durumlar ayrıca açık. Aşağıdaki ilk aday-pending metni tarihsel kurulum kaydıdır.

Aday: `0.5.70.0-alpha-player-mining-view`. On iki otomatik test grubu + gerçek DoctorWhoMod WE fixture geçti. Minecraft runtime kabulü henüz yapılmadı.

## Tek oturumda kontrol

1. Gemi bağlantısı varken Player GUI → MINING aç. Durum, PHASE ve tarama boyutu/merkez chunk görünmeli. Fiziksel Mining Terminal ile karşılaştır; el freni/kalkan gibi mevcut şartlar değişince durum yenilenmeli. Bu ekran kendi başına kazım başlatmaz.
2. Mevcut madenciliği fiziksel kontrollerden normal şekilde çalıştır. SCAN alanı tarama imlecini/toplam blok sayısını, EXTRACT çıkarılan kaynak hedefini/taramada bulunan kaynak hedeflerini göstermeli. Tarama sırasında EXTRACT N/A / SCANNING doğrudur; sıfır/bilinmeyen payda N/A olur. UNKNOWN sayısı araştırma bekleyen hedeflerdir. Hazard temizliği çıkarma yüzdesine dahil değildir; kalan deposit rezervi ölçülmez. Gemi başka chunk/boyuta taşınırsa eski alan LAST SCAN olarak etiketlenir.
3. Collection, AE Transfer ve Replication Feed satırlarını fiziksel bufferlerle karşılaştır. ITEMS toplam eşya, TYPES dolu kaynak türü yuvasıdır (256 tür kapasitesi; genel eşya doluluk yüzdesi değildir). Gerçek boş buffer 0 ITEMS / 0/256 TYPES, bağlantısız NOT BOUND, kaldırılmış MISSING, chunk yüklü değilse UNLOADED, API okunamıyorsa UNAVAILABLE gösterilir. SMART AUTO/FORCE REPLICATION/ROUTING PAUSED fiziksel routing moduyla eşleşmeli. MOVED sayaçları anlık hız değil birikimli aktarım adetleridir.
4. Mining → Overview/Survey/Navigation geçişlerini ve GUI kapat/açmayı aynı oturumda dene. Mining açıkken bağlantı kaybında eski bilgi görünmemeli; bağlantı dönünce güncel veri gelmeli. Bu yeni Mining kabulüdür; önceki Navigation/Ship Link kabullerini yeniden isteme.

Her madde için gerçek sonucu birlikte bildir; bir ekran görüntüsü ve varsa farklı davranış yeterli başlangıç kanıtıdır. Henüz görülmeyen durumlar ayrıca açık kalır.

## Kapsam ve güvenlik

- Salt-okunur: STOP, routing ayarı, Keep, priority, buffer bağlama, tarama başlatma, rota/uçuş veya enerji yazımı yok.
- Owner/Ship Link kontrolü sunucuda tekrar yapılır. İstemcide gemi kimliği/geçerlilik kontrolü ve oturum reseti vardır; kopma devam eden fiziksel madenciliği durdurmaz.
- State map okunur; `state(id)` ile yeni gemi kaydı oluşturulmaz. Buffer okumadan önce yüklü chunk denetlenir; chunk zorla yüklenmez.
- `player-mining.properties`: refresh 20, stale 120, show_scan_area/show_buffers true. Canlı config denemeleri opsiyonel genişletilmiş kontroldür; test sonrası ortak varsayılanlara dön.
- Log öneki: `[NewWorld Player Mining]`. sampling/snapshot/render failure veya decode rejected runtime'da araştırılır; otomatik testte kasıtlı bozuk çerçeve mesajları beklenir.
- Geri dönüş yedeği: `backups/custom-mods/pre-player-mining-20260929-01/{repository,instance}` içindeki `.69.4`; eski sürümü ancak oyun kapalıyken tek aktif core kuralıyla geri al. Yeni config eski sürümde etkisizdir; dünya kaydı göçü yok.
