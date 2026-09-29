# Player Emergency .71.4 — gemi içi kapı önü dönüş

29 Eylül kabul güncellemesi: kullanıcı kapı önü dönüşünün çalıştığını doğruladı. 15:21:30.375 sunucu kaydı varış `[0,128,-22]`, `cooldown_ms=1800000`; 15:21:30.386 istemci başarı yanıtı. Temel varış geçti, tekrar istenmez. Geri sayım görünümü/çıkış-giriş kalıcılığı ve hedef/rota/WE karşılaştırması bu denemede ayrıca doğrulanmadı; genişletilmiş kontroller açık. Aşağıdaki kurulum zamanı "bekleniyor" ifadesi bu temel varış için geçersizdir.

Kullanıcı ayrı Teleporter Room yerine geminin içindeki ana kapının önünü, normal portalın getirdiği yeri istedi. .71.3 gerçek dönüşü 29 Eylül 14:42:25 loguyla doğrulandı; yeni hedefin oyun kabulü henüz bekleniyor. Beacon iptal; eski DISTRESS kayıtları korunur.

Güncel aday `0.5.71.4-alpha-emergency-entrance`. Doctor portalıyla aynı `getEntrancePosition().relative(getEntranceFacing())` hücresi, taban merkezi ve kapı yönü kullanılır. Ana kapı gerçekten yüklü olmalı; varışta hava, sağlam zemin, sıvı/tehlike/sınır/çarpışma denetlenir. Güvensizse başka oda/komşu noktaya göndermez; `NO SAFE LOADED ENTRANCE / NO COOLDOWN` verir. Kapıları açmaz, dünya bloklarını değiştirmez.

15 otomatik paket, gerçek Doctor portal konum/yön sözleşmesi ve WE testi geçti. Dört yön/taşınmış kapı, tam koordinat/yön, eksik-yüklü olmayan-engelli-tehlikeli-çarpışmalı giriş, yedek nokta kullanmama, seyahat iptali ve kalıcı süre test edildi. Bunlar gerçek oyun kabulünün yerine geçmez.

Kabul listesi (1. maddenin kapı önü varışı tamamlandı; kalanlar toplu doğrulanabilir):

1. Kullanıcının isteğiyle önceki bekleme yalnız bu test için yedek alınarak sıfırlandı. Dışarıda, gemi sabitken Navigation hedefini/rotasını not et. Emergency → RETURN → iki saniye içinde CONFIRM. Normal portal girişinde geldiğin gemi içi kapı önüne, içeri bakarak gelmelisin; gemi uçmamalı.
2. Yaklaşık 30:00 bekleme başlamalı. Navigation hedef/rota, WE ve eski kayıtlar korunmalı. GUI'yi yeniden açınca geri sayım sürmeli.
3. Çıkış/giriş beklemeyi sıfırlamamalı. Çevrimdışı gerçek süre sayılır; bu güncelleme de mevcut beklemeyi silmez. Süre dolmadan yeniden dönüş olmamalı.
4. Genişletilmiş kabul: uzak mesafe/başka dış dimension, seyahat korumasının reddi, uçuş/oda yenileme, engelli veya yüklü olmayan giriş. Başarısız dönüş yeni süre başlatmamalı. Çalışan dünyayı tahrip etme; engel/tehlike testlerini kopya dünyada yap.

Sahiplik, canlı/yaya oyuncu, başka TARDIS içini reddetme, zaten kendi gemisindeyken dönüşü kapatma korunur. Önceden kabul edilen Mining/Navigation testleri tekrarlanmaz. Aşama 12'ye geçilmedi.
