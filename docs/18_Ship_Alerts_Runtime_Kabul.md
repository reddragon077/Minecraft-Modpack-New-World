# Ship Alerts — .72.1 toplu kabul

Durum: **temel .72.1 oyun kabulü geçti (29 Eylül 2026).** Kullanıcının ekranında FE LEVEL LOW ve telemetri/matrix uyarıları RESOLVED olarak geçmişte görünüyor; 0 ACTIVE / 0 UNKNOWN ve sayfalama mevcut. Kullanıcı ayrıca HUD bildirimi ve GUI kapat-aç kontrolünün düzeldiğini bildirdi. Bu kanıtlar tüm uyarı türlerinin, çok oyunculu veya yük testlerinin tamamlandığı anlamına gelmez. Aşama 12 kısmi; yeni keşif bildirimleri açık. Kabul edilmiş Navigation/Mining/Emergency temel testleri tekrarlanmaz.

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

## Kabul edilen temel kontrol

Düşük FE/geçmiş/çözülme ekran kanıtıyla; HUD bildirimi ve GUI yeniden açılışı kullanıcı beyanıyla geçti. Son derlemede 16 otomatik grup ve gerçek Doctor portal/WE fixture kontrolleri geçti; bu belge güncellemesinde yeni derleme yapılmadı.

## Kalan genişletilmiş kontrol

1. Diğer uyarı türleri için doğal oyun durumunda Overview/History eşleşmesini karşılaştır; kabul edilen temel FE/HUD/reopen kontrolünü sırf bu liste için tekrarlama.
2. Kontrollü bir Matrix kapatma/açma işlemini normal terminalinden yap; dünya bloklarını kırmak gerekmez. GUI kapalıyken küçük uyarı, geçmişte olay ve düzeltince RESOLVED beklenir. Aynı durumda beklemek spam üretmemeli. Test sonunda önceki terminal ayarını geri getir.
3. WE/Collection eşiğini doğal oyun durumu oluştuğunda fiziksel terminalle karşılaştır. Denemek için dünyayı/enerjiyi zorla değiştirme veya buffer doldurma gerekmez; oluşmayan durumları henüz test edilmedi diye bırak. Drive eksikliği yalnız mevcut uçuş hedefinin dimension gereksiniminde görünür; bu test için uçuş başlatmak gerekmez.
4. Menzil dışına çıkınca geçmiş ayrıntıları gizlenmeli, bağlantı döndüğünde aynı geminin geçmişi gelmeli ve eski olay yeniden bildirilmemeli. Gerçek çıkış/girişte oturum geçmişi sıfırlanır.

Bu kontrollerin tümü otomatik test geçişiyle oyun içinde geçti sayılmaz. Çok oyunculu, mod-protection etkileşimi, diğer HUD uyarı türleri ve gecikme/yük ölçümü ayrıca bekleniyor. Ekran ve ilgili log zamanı birlikte kaydedilir.
