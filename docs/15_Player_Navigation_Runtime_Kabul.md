# Player Navigation — ilk görünüm kabulü

Kurulu aday `.69.0-alpha-player-navigation-view`. Oyun kabulü henüz yapılmadı.

1. Kendi geminin yakınında Player GUI → NAVIGATION aç. Sekme yazısı etkin görünmeli; kısa SYNCING sonrası iki sütun gelmeli.
2. Sol sütundaki seçili hedefi fiziksel Navigation Terminal ile karşılaştır. SHIP DIST, oyuncuya değil geminin dış konumuna göre hesaplanır; hedef başka boyuttaysa DIFFERENT DIMENSION gösterilir. Üst sağdaki LINK mesafesi ise oyuncu-gemi mesafesi olarak kalır.
3. Sağdaki rota durumu ve LOADED HOP değerini fiziksel Route sayfasıyla karşılaştır. Mevcut hazır rotada sonraki koordinat gösterilmeli. Tamamlanmış rotada COMPLETE ve NONE görünmeli; rota yokken NO ROUTE/NO LOADED HOP beklenir.
4. EST ... WE / NEXT HOP yalnız gerçekten motora yüklenmiş durağın motor formülüyle tahminidir. Toplam rota maliyeti veya uçuş izni değildir. Hedef yüklemesi rota noktasıyla uyuşmuyorsa DESTINATION CHANGED; API/veri yoksa NOT AVAILABLE gösterilir. Mevcut WE ayrı görünür. Sırf bu test için enerji harcamak gerekmez.
5. Discoveries üzerinden var olan TARGET/ROUTE düğmeleriyle farklı hedef seç; Navigation'a dön. Sol seçili hedef ile sağ mevcut rota ayrı kaynaklardır; TARGET tek başına yeni rota hesaplamaz. Gerçek gemi terminalindeki durumla eşleşmeli.
6. GUI kapat/aç ve menzil kaybı/geri dönüşte eski Navigation verileri erişilebilir kalmamalı; yeniden bağlantıda güncel veri gelmeli.
7. İstenirse `config/newworldcore/player-navigation.properties` içindeki show_coordinates/show_we_estimate seçeneklerini canlı değiştir; gizleme yalnız sunumu etkiler. refresh_ticks varsayılan 20, stale_after_ticks 120. Test sonrası ortak varsayılanları geri koy.

Log: `[NewWorld Player Navigation]`. `snapshot failed`, `sampling failed`, `render failed`, `decode rejected` gerçek runtime'da incelenmelidir; smoke testindeki kasıtlı bozuk çerçevelerle karıştırılmaz.

Bu sürüm salt-okunurdur: rota hesaplamaz, hedef seçmez, uçuş başlatmaz, enerji harcamaz veya chunk yüklemez. Favori seçme / SAVE CURRENT LOCATION / SEND TO SHIP henüz bu sekmeye eklenmedi. Otomatik testlerin geçmesi oyun kabulü yerine sayılmaz.
