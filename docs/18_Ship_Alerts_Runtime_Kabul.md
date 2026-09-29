# Ship Alerts — .72.1 toplu kabul

Durum: `.72.0` ekranları bağlantı/geçmiş görünümünü doğruladı; FE 250K/2.25M iken Overview uyarısı ile geçmişin 0 ACTIVE göstermesi eksik kapsamı kanıtladı. Kullanıcı tüm uyarıları istedi. `.72.1` ortak örnekleme düzeltmesinin oyun içi kabulü bekleniyor. Acil dönüşün kabul edilmiş kapı/geri sayım/yeniden giriş testleri tekrarlanmaz. Stage 11 hedef/rota/WE gerçek oyun karşılaştırması ve genişletilmiş vakalar açık kalır.

## Kapsam

- Sunucu sahiplik ve Ship Link menzil/dimension politikasını her örneklemede doğrular. Oyuncu GUI'si kapalıyken de 40 tick varsayılan sorgu çalışır; yalnız veri okur.
- FE ve WE <= %20 uyarı, <= %5 kritik. Ortak eşikler `overview.properties`; eski Ship Alerts WE eşikleri kaldırıldı. Bilinmeyen kapasite LOW sayılmaz, ayrı telemetri uyarısıdır.
- Collection tür slotları >= %80 uyarı, %100 kritik. Bu, mevcut türden daha fazla eşya alınamaz demek değildir. Gerçek Mining `BUFFER_FULL` ayrı olaydır; seviyesi Overview ile aynı WARNING.
- Kayıtlı FE/WARP/ENGINE Matrix offline WARNING, motor broken CRITICAL. Kayıt eksikse UNKNOWN ve ayrı veri uyarısı, yanlış OFFLINE veya sahte RESOLVED değil.
- Overview'daki Mining NO_ENERGY, kalkan açıkken WAITING durumları; FE tüketim sayacı/enerji/matrix/motor/madencilik/navigation/dış konum/kısmi telemetri hataları da kapsanır. Motor UNKNOWN ve motor telemetri hatası tek türe birleşir; WAITING nedenleri tek bekleme türüdür. Üç görünür satır sınırı bu kaynağı kırpmaz. Collection/drive kontrolleri korunur; yeni keşif olayları bu talebin kapsamı değildir.
- Gemi uçuş hedefi başka dimension gerektiriyorsa mevcut Doctor kuralıyla drive kontrolü: gezegensel için slot3 veya slot4, evrenler arası için slot4. Oyuncunun yalnız seçili Navigation hedefi mevcut uçuş hedefinin yerine kullanılmaz.
- Overview → ALERT HISTORY: son 16 durum değişimi, 5 satırlı sayfalar. Sunucu oyuncu oturumu geçmişidir; kalıcı dünya günlüğü değildir. Çıkışta sıfırlanır, GUI kapat-açta korunur. Yeni Structure/Geology keşif bildirimleri bu dilimde yok.
- Vanilla action-bar küçük bildirimi; aynı durum tekrarlanmaz. Aynı tür için 60 saniye, bildirimler arasında 4 saniye varsayılan aralık; kritik yükseliş tekrar süresini beklemez. Kuyruk 3, eskimiş/çözülmüş/bilinmeyen olay bildirimi iptal edilir. Diğer modların action-bar mesajlarıyla aynı alanı paylaşır.
- Config canlı okunur. Save, hedef, rota, uçuş, enerji, madencilik kontrolü veya cooldown yazılmaz.

## Tek oturumda toplu kontrol

1. Gemi bağlantısı varken düşük FE hâlâ varsa Overview → ALERT HISTORY aç: FE LEVEL LOW ve ACTIVE sayısı görünmeli. Eşik üzerindeki normal dolumdan sonra RESOLVED beklenir; test için save/enerji değiştirmek gerekmez. GUI kapat-aç geçmişi korumalı.
2. Kontrollü bir Matrix kapatma/açma işlemini normal terminalinden yap; dünya bloklarını kırmak gerekmez. GUI kapalıyken küçük uyarı, geçmişte olay ve düzeltince RESOLVED beklenir. Aynı durumda beklemek spam üretmemeli. Test sonunda önceki terminal ayarını geri getir.
3. WE/Collection eşiğini doğal oyun durumu oluştuğunda fiziksel terminalle karşılaştır. Denemek için dünyayı/enerjiyi zorla değiştirme veya buffer doldurma gerekmez; oluşmayan durumları henüz test edilmedi diye bırak. Drive eksikliği yalnız mevcut uçuş hedefinin dimension gereksiniminde görünür; bu test için uçuş başlatmak gerekmez.
4. Menzil dışına çıkınca geçmiş ayrıntıları gizlenmeli, bağlantı döndüğünde aynı geminin geçmişi gelmeli ve eski olay yeniden bildirilmemeli. Gerçek çıkış/girişte oturum geçmişi sıfırlanır.

Bu kontrollerin tümü otomatik test geçişiyle oyun içinde geçti sayılmaz. Çok oyunculu, mod-protection etkileşimi, gerçek HUD görünümü ve gecikme/yük ölçümü ayrıca bekleniyor. Ekran ve ilgili log zamanı birlikte kaydedilir.
