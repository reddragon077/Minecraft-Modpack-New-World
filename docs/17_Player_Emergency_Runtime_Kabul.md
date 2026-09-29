# Player Emergency .71.2 — doğrudan acil dönüş

Kullanıcı Beacon'ı iptal etti. Kurulu aday `0.5.71.2-alpha-emergency-return`: iki tıkla kendi gemisinin güvenli Teleporter Room noktasına dönüş; başarıdan sonra 30 gerçek dakika bekleme. Eski DISTRESS kayıtları korunur, yeni kayıt/hedef/rota yazılmaz. .71.1 Beacon yanıt testi başarılıydı ama ürün gereksinimi değildi.

15 otomatik paket + gerçek Doctor oda şablonu/yerleşim sözleşmesi + WE formülü geçti. Oyun içi kabul **bekleniyor**; aşağıdaki dört kontrol tek oturumda yapılmalı. Önceden kabul edilen Mining/Navigation testlerini tekrar etmeyin.

1. Dışarıda, gemi sabitken Navigation hedefini/rotasını not et. Emergency → EMERGENCY RETURN TO SHIP → CONFIRM RETURN TO SHIP. Aynı blokta kalıp ikinci tıklamayı iki saniye içinde yap. Oyuncu konsol tarafı Teleporter Room'da, duvar/floor içinde olmadan gelmeli; gemi uçmamalı.
2. Emergency ekranını yeniden aç: yaklaşık 30:00 geri sayım görünmeli. Navigation hedefi/rotası aynı kalmalı, yeni DISTRESS oluşmamalı. WE değişimi bu işlemden kaynaklanmamalı.
3. Gemiden çıkıp tekrar bas: süre bitmeden dönüş olmamalı. Dünya çıkış/girişinden sonra kalan süre korunmalı, sıfırlanmamalı; çevrimdışı geçen gerçek süre düşer.
4. Ayrı genişletilmiş kabul: süre dolduktan sonra normal bağlantı menzilinin dışından veya başka dış dimension'dan dönüş. Güvensiz/engelli oda, yüklü olmayan iç dünya, uçuş/oda yenileme veya seyahat korumasının reddi açık başarısızlık vermeli ve yeni cooldown başlamamalı. Çalışan odaları test için tahrip etmeyin; güvenlik senaryoları kontrollü kopya dünyada denenir.

Hedef yalnız Doctor'un gerçek oda şablonundan hesaplanır ve gerçek Teleporter bloğu doğrulanır; girişe veya rastgele konuma yedek dönüş yok. Başka TARDIS içinden, araç üzerinde, ölü/uyuyan oyuncuyla ve zaten kendi gemisindeyken dönüş kapalıdır. Oda bulunamazsa NO SAFE LOADED TELEPORTER ROOM gösterilir. Sunucu oyun zamanı değil gerçek saat kullanır; bekleme oyuncu başınadır. Oyun/çok oyunculu kabulü otomatik testlerden ayrı tutulur; Aşama 12'ye geçilmedi.
