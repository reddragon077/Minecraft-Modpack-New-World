# Player Emergency .71.0 — tek oturumluk kabul

Kurulu aday: `0.5.71.0-alpha-emergency-beacon`. Otomatik 14 paket + gerçek Doctor WE formülü testi geçti; aşağıdaki oyun kontrolleri henüz geçmedi. Mining'in kabul edilen testlerini tekrar etmeyin.

1. TARDIS dışında, bağlantı varken daha önce kaydetmediğin bir blokta dur. Mevcut Navigation hedefi/rotasını not et. Emergency aç: READY ve güncel dünya/koordinat gelsin. DISTRESS BEACON'a bas: ilk tık hedefi değiştirmemeli. CONFIRM gelince aynı blokta kalarak iki saniye içinde ikinci kez bas. BEACON SENT beklenir.
2. Navigation'da yeni hedef o konum olmalı, önceki hesaplanmış rota aynı kalmalı. Gemi uçmamalı; oyuncu ışınlanmamalı; bu eylem WE harcamamalı. Favorilerde yeni DISTRESS kaydı görünür. Aynı koordinatta önceki kayıt varsa adı/kanıtı korunur, yeni kopya beklenmez.
3. Hemen tekrar dene: 10 saniyelik sunucu cooldown mesajı beklenir. Süre dolunca ilk onayı al, GUI'yi kapatıp başka bloğa geç ve yeniden aç: eski onay kalmamalı. Yeni konum için yeni iki tık gerekir. Bağlantı yokken eylem kapalı; geminin içindeyken EXIT SHIP TO SEND BEACON beklenir.
4. GUI ve dünya çıkış/girişinden sonra kaydın ve seçilen hedefin kaldığını doğrula. Otomatik rota hesaplanması veya uçuş bekleme. RETURN TO SHIP bu sürümde açıkça kullanılamaz; güvenli Teleporter Room / WE / koruma kapıları sonraki Aşama 11 paketidir.

Konfigürasyon, kota, çok oyunculu ve gerçek paket gecikmesi kenar testleri ayrıca açıktır. Tam Aşama 11 kabulü veya gerçek dönüş testi sayılmaz. Normal SAVE ve DISTRESS aynı waypoint kotasını kullanır; kota doluysa WAYPOINT LIMIT REACHED ve eski hedef korunur.
