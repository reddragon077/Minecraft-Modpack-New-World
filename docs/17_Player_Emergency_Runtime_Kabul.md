# Player Emergency — güncel kabul ve sınırlar

29 Eylül 2026 • .71.4 ile gelen ana giriş varışı, kurulu .72.1 içinde korunur.

## Davranış

DISTRESS BEACON kaldırıldı; eski kayıtlar silinmez. İki tıklamalı onay oyuncuyu kendi gemisinin içinde, normal portalın getirdiği ana kapı önüne döndürür. Ayrı Teleporter Room kullanılmaz. Doctor portalıyla aynı `getEntrancePosition().relative(getEntranceFacing())`, taban merkezi ve kapı yönü kullanılır.

Gerçek kapı yüklü ve kayıtlı olmalı; hava, sağlam zemin, sıvı/tehlike, sınır ve çarpışma kontrol edilir. Güvensizse başka oda/komşu hücreye yedek varış yoktur. Dünya blokları ve kapılar değiştirilmez. Sahiplik, canlı/yaya oyuncu, başka TARDIS içini reddetme, zaten kendi gemisinde olmama, uçuş/oda yenileme ve seyahat iptal kapıları korunur. Normal Ship Link mesafe/dimension politikası kurtarmayı engellemez.

Başarı sonrası varsayılan 30 gerçek dakika bekleme oyuncu kaydında saklanır; çevrimdışı zaman sayılır. Başarısız/engellenmiş dönüş yeni bekleme tüketmez. Hedef, rota, uçuş ve WE yazımı yoktur.

## Geçen temel oyun kabulü

- Kapı önü varış kullanıcı tarafından doğrulandı. 29 Eylül 15:21:30.375 sunucu logu varış `[0,128,-22]`, `cooldown_ms=1800000`; 15:21:30.386 istemci başarı yanıtı.
- Geri sayım ve dünya çıkış/girişinde kalıcılık kullanıcı, ekran ve logla doğrulandı. Ekran `RETURN COOLDOWN / 25:51`; 15:24:48–49 kayıt/duruş, 15:25:21 yeniden giriş.
- Bu temel kontroller yeniden istenmez.

## Otomatik kanıt ve açık oyun testleri

15 Emergency dönemi test grubu, gerçek Doctor portal konum/yön sözleşmesi ve WE fixture geçti. Dört yön/taşınmış kapı, engelli/yüklü olmayan/tehlikeli giriş, yedek varış kullanmama, seyahat iptali ve süre kalıcılığı otomatik test kapsamındadır. Son .72.1 derlemesinde toplam 16 grup geçti.

Gerçek oyun içinde hedef/rota/WE değişmezliğinin karşılaştırması; uzak mesafe/başka dış dimension, çok oyunculu, koruma reddi, ölüm, uçuş/oda yenileme ve engelli giriş kenar kabulü ayrıca açıktır. Otomatik kontroller bu testlerin yerine geçmez. Tahrip edici engel/tehlike senaryoları çalışan dünyada değil kopyasında denenmelidir.

## Tarihsel düzeltmeler

.71.2 yüklü chunk kontrolündeki hata .71.3 ile onarıldı. .71.3 ayrı oda varışı çalıştı ancak kullanıcı istediği yerin normal giriş kapısı olduğunu açıkladı; .71.4 bunu değiştirdi. Test için açık kullanıcı isteğiyle bir kez yedekli cooldown sıfırlaması yapıldı; normal özellik veya tekrar yapılacak işlem değildir.

Aşama 11 genişletilmiş kabul nedeniyle kısmi kalır. Kullanıcı onayıyla Aşama 12'ye geçildi; [Ship Alerts temel kabulü](18_Ship_Alerts_Runtime_Kabul.md) de kaydedildi. Geliştirme şu an duraklatılmıştır.
