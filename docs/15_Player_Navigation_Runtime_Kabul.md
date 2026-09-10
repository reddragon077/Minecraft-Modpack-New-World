# Player Navigation — ilk görünüm kabulü

Kurulu aday `.69.0-alpha-player-navigation-view`. 10 Eylül oyun testi kısmen geçti; tüm kontrol listesi tamamlanmadı.

## Doğrulanan kapsam

- İlk ekran: LIVE / READ ONLY, Aluminum hedefi `[-2456,40,328]`, gemi mesafesi 154 blok; yüklü durak `[-2456,61,328]`, 152 blok, 1/1 ve 48 WE tahmini. Mevcut enerji 1000 WE.
- Discoveries TARGET/ROUTE sonrasında Carbon hedefi `[-2376,32,376]`, 220 blok; yüklü durak `[-2376,68,376]`, 219 blok, 1/1 ve 52 WE tahmini. Başlıkta oyuncu-gemi mesafesi ayrı olarak 12 blok kaldı. Metinler iki sütunda okunaklı.
- Sunucu logu 17:19:37.088 Aluminum, 17:24:03.242 Carbon TARGET, 17:24:04.011 ROUTE ready=true ve 17:24:04.968 Carbon Navigation snapshot gösterdi. Navigation sampling/render/decode hatası bulunmadı. Genel logda üçüncü taraf EMI/JEI hataları var; tüm log hatasız denmiyor.
- GUI kapat/aç önerildi ancak henüz açık kullanıcı doğrulaması yok. Sonraki mesaj GitHub'a kaydetme isteğidir; yeniden açılış kanıtı sayılmaz.
- Navigation'a özel menzil kaybı/geri dönüş, boyut farkı, boş/tamamlanmış/çok-hop rota, gerçek uçuş tüketimiyle maliyet karşılaştırması ve canlı görünüm config denemesi henüz doğrulanmadı. `.68.2` Ship Link testleri bunların yerine sayılmaz.

## Kontrol listesi

1. Kendi geminin yakınında Player GUI → NAVIGATION aç. Sekme yazısı etkin görünmeli; kısa SYNCING sonrası iki sütun gelmeli.
2. Sol sütundaki seçili hedefi fiziksel Navigation Terminal ile karşılaştır. SHIP DIST, oyuncuya değil geminin dış konumuna göre hesaplanır; hedef başka boyuttaysa DIFFERENT DIMENSION gösterilir. Üst sağdaki LINK mesafesi ise oyuncu-gemi mesafesi olarak kalır.
3. Sağdaki rota durumu ve LOADED HOP değerini fiziksel Route sayfasıyla karşılaştır. Mevcut hazır rotada sonraki koordinat gösterilmeli. Tamamlanmış rotada COMPLETE ve NONE görünmeli; rota yokken NO ROUTE/NO LOADED HOP beklenir.
4. EST ... WE / NEXT HOP yalnız gerçekten motora yüklenmiş durağın motor formülüyle tahminidir. Toplam rota maliyeti veya uçuş izni değildir. Hedef yüklemesi rota noktasıyla uyuşmuyorsa DESTINATION CHANGED; API/veri yoksa NOT AVAILABLE gösterilir. Mevcut WE ayrı görünür. Sırf bu test için enerji harcamak gerekmez.
5. Discoveries üzerinden var olan TARGET/ROUTE düğmeleriyle farklı hedef seç; Navigation'a dön. Sol seçili hedef ile sağ mevcut rota ayrı kaynaklardır; TARGET tek başına yeni rota hesaplamaz. Gerçek gemi terminalindeki durumla eşleşmeli.
6. GUI kapat/aç ve menzil kaybı/geri dönüşte eski Navigation verileri erişilebilir kalmamalı; yeniden bağlantıda güncel veri gelmeli.
7. İstenirse `config/newworldcore/player-navigation.properties` içindeki show_coordinates/show_we_estimate seçeneklerini canlı değiştir; gizleme yalnız sunumu etkiler. refresh_ticks varsayılan 20, stale_after_ticks 120. Test sonrası ortak varsayılanları geri koy.

Log: `[NewWorld Player Navigation]`. `snapshot failed`, `sampling failed`, `render failed`, `decode rejected` gerçek runtime'da incelenmelidir; smoke testindeki kasıtlı bozuk çerçevelerle karıştırılmaz.

Bu sürüm salt-okunurdur: rota hesaplamaz, hedef seçmez, uçuş başlatmaz, enerji harcamaz veya chunk yüklemez. Favori seçme / SAVE CURRENT LOCATION / SEND TO SHIP henüz bu sekmeye eklenmedi. Otomatik testlerin geçmesi oyun kabulü yerine sayılmaz.
