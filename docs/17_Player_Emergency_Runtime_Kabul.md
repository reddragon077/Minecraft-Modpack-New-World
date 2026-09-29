# Player Emergency .71.1 — yanıt düzeltmesi ve tek oturumluk kabul

Kurulu aday: `0.5.71.1-alpha-emergency-feedback`. .71.0 oyun denemesinde sunucu SENT ve WAIT kaydetti fakat GUI NO RESPONSE gösterdi. Geciken yanıtın kalıcı kaybı otomatik testte üretildi ve düzeltildi; yeni yanıtlar normal ekran yenilemeleriyle tekrar alınabilir, işlem tekrar çalıştırılmaz. Otomatik 14 paket + gerçek Doctor WE formülü testi geçti; yeni oyun kabulü bekleniyor. Mining'in kabul edilen testlerini tekrar etmeyin.

Önce kısa düzeltme kontrolü: dışarıda aynı blokta iki aşamalı DISTRESS gönder -> BEACON SENT mesajını ve cooldown geri sayımını gör -> Navigation'da o konumun hedef olduğunu, eski rotanın korunmasını kontrol et. Gecikme varsa SERVER DELAYED / WAITING FOR RESULT görünür; yanıt geldiğinde sonuç güncellenmeli. Ardından aşağıdaki henüz kabul edilmemiş genişletilmiş kontroller tek oturumda yapılabilir.

1. TARDIS dışında, bağlantı varken daha önce kaydetmediğin bir blokta dur. Mevcut Navigation hedefi/rotasını not et. Emergency aç: READY ve güncel dünya/koordinat gelsin. DISTRESS BEACON'a bas: ilk tık hedefi değiştirmemeli. CONFIRM gelince aynı blokta kalarak iki saniye içinde ikinci kez bas. BEACON SENT beklenir.
2. Navigation'da yeni hedef o konum olmalı, önceki hesaplanmış rota aynı kalmalı. Gemi uçmamalı; oyuncu ışınlanmamalı; bu eylem WE harcamamalı. Favorilerde yeni DISTRESS kaydı görünür. Aynı koordinatta önceki kayıt varsa adı/kanıtı korunur, yeni kopya beklenmez.
3. Hemen tekrar dene: 10 saniyelik sunucu cooldown geri sayımı boyunca yeni işlem gönderilmemeli. Süre dolunca ilk onayı al, GUI'yi kapatıp başka bloğa geç ve yeniden aç: eski onay kalmamalı. Yeni konum için yeni iki tık gerekir. Bağlantı yokken eylem kapalı; geminin içindeyken EXIT SHIP TO SEND BEACON beklenir.
4. GUI ve dünya çıkış/girişinden sonra kaydın ve seçilen hedefin kaldığını doğrula. Otomatik rota hesaplanması veya uçuş bekleme. RETURN TO SHIP bu sürümde açıkça kullanılamaz; güvenli Teleporter Room / WE / koruma kapıları sonraki Aşama 11 paketidir.

Konfigürasyon, kota, çok oyunculu ve gerçek paket gecikmesi kenar testleri ayrıca açıktır. Tam Aşama 11 kabulü veya gerçek dönüş testi sayılmaz. Normal SAVE ve DISTRESS aynı waypoint kotasını kullanır; kota doluysa WAYPOINT LIMIT REACHED ve eski hedef korunur.
