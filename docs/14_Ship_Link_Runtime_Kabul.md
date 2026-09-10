# Ship Link .68.0 — Oyun kabulü

Durum: aday kuruldu, yedi otomatik test geçti; aşağıdaki oyun testleri henüz yapılmadı.

1. Kendi geminin içinde Player GUI aç: sağ üstte `LINK CONNECTED // ON BOARD`, altında geminin dış boyutu görünmeli. Sekme değiştirirken bağlantı göstergesi kalmalı; Overview ve Discoveries çalışmalı.
2. Gemiden çık, aynı boyutta yürüyerek uzaklaş: `CONNECTED // ... BLK` oyuncu ile geminin dış konumu arasındaki 3B mesafeyi göstermeli; yüksekliği de hesaba katar.
3. Güvenli test için `config/newworldcore/ship-link.properties` dosyasını yedekle, `range_blocks=32` yap. Geminin dışındayken 32 bloktan uzaklaş: LOST; Survey/Discoveries içeriği kilitlenmeli. Gemiye yaklaşınca otomatik açılmalı ve Discovery listesi yenilenmeli. Sonunda 5000'e geri al.
4. Sahip olduğun gemi yüklüyken başka boyuta geç: varsayılan `allow_dimensional_link=true` ile DIMENSIONAL ve geminin boyutu; boyutlar arasında sahte blok mesafesi görünmemeli. Ayarı false yapınca LOST, true yapınca tekrar DIMENSIONAL. Kendi geminin içi bu ayardan etkilenmez.
5. Menzil içinde gecikmeli Field Survey başlatıp tamamlanmadan menzil dışına çık: yeni keşif yazılmamalı; link kaybı bildirilmeli. Tekrar menzilde Structure/Geological Survey regresyonunu dene.
6. Bağlıyken Discoveries FAV/TARGET/ROUTE işlemlerini geminin dışından da kontrol et. Rota, bağlantının ait olduğu gemiye gitmeli. LOST iken hiçbir yeni uzaktan işlem kabul edilmemeli; önceden çalışan gemi Mining/rota işi bu özellik tarafından durdurulmaz.
7. Ekranı kapat/aç, dünyadan çık/gir: bağlantı yeniden doğrulanana kadar eski kayıt erişimi kapalı kalmalı. Varsayılan yenileme 20 tick (~1 sn); eski veri eşiği 120 tick (~6 sn). Sunucu süreleri istemciye gönderilir.

Sahipsiz oyuncu, başka oyuncunun gemisi ve yapay ağ kesintisi kontrolleri mümkünse ayrı test dünyası/sunucuda yapılır. Otomatik sahiplik, bilinmeyen veri, timeout ve paket testleri geçti; bu gerçek çok oyunculu kabulün yerine geçmez.

Log işaretleri: `[NewWorld Ship Link] state=...`, `snapshot failed`, `decode rejected`, `render failed`, `dispatch failed`. Göstergenin yenilenmesi yalnız GUI açıkken sorgulanır; chunk yüklemez. Test için enerji deposunu boşaltmaya gerek yoktur.
