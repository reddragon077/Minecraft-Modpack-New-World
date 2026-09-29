# Değişiklik günlüğü

Bu belge oyuncular için özellik özetidir; tam commit geçmişinin veya test raporlarının yerine geçmez. Proje alpha aşamasındadır.

## 29 Eylül 2026 — güncel durum: 0.5.72.1

### Ship Alerts (.72.0 → .72.1)

- Overview'daki tüm uyarılar ortak kaynaktan Alert History/HUD sistemine bağlandı; düşük FE'nin geçmişte eksik kalması düzeltildi.
- FE/WE eşikleri `overview.properties` üzerinden ortak yönetilir. Eski bağımsız Ship Alerts WE eşikleri kaldırıldı.
- Enerji, matrix, motor, madencilik ve telemetri uyarıları; Collection tür yuvası doluluğu ve mevcut uçuş hedefinin drive ihtiyacı takip edilir.
- Eksik veri arızayı yanlışlıkla çözülmüş saymaz. Geçmiş oturum başına sınırlıdır; GUI yeniden açılışında korunur, çıkışta sıfırlanır.
- Düşük FE/geçmiş/çözülme ekranla, HUD ve GUI kapat-aç kullanıcı bildirimiyle kabul edildi. Önceki derlemede 16 otomatik test grubu ve Doctor portal/WE kontrolleri geçti. Diğer uyarı türlerinin tümü oyun içinde test edildi sayılmaz.

### Emergency (.71.x)

- DISTRESS BEACON kaldırıldı. Yerine doğrudan kendi gemisinin iç ana giriş kapısına, normal portal varış noktasına acil dönüş geldi.
- İki aşamalı onay ve başarılı dönüş sonrası 30 gerçek dakikalık kalıcı bekleme uygulanır. Çevrimdışı süre sayılır; başarısız dönüş yeni süre tüketmez.
- Menzil kurtarmayı engellemez; sahiplik ve güvenli/yüklü giriş denetimleri sürer. Ayrı Teleporter Room hedefi artık kullanılmaz.
- Kapı önü varış, bekleme başlangıcı, geri sayım ve çıkış/giriş kalıcılığı temel oyun kabulünden geçti. Genişletilmiş koruma/çok oyunculu durumlar açık.

### Mining (.70.x)

- Oyuncu paneline durum, tarama alanı, Scan/Extraction ilerlemesi, üç buffer ve routing sayaçları eklendi.
- MINED RESOURCES gerçek çıkarılan blokları alan bazında izler; eski izlenmemiş kazımları ayrı tutar.
- Onaylı STOP MINING yalnız Mining Shield'ı kapatır. Yeniden başlatma ve ayrıntılı yönetim fiziksel terminalde kalır.
- Temel görünüm, kaynaklar, durdurma, aynı alanda kalıcılık ve yeniden başlatma kullanıcı kabulünden geçti. Deposit kalan rezervi henüz hesaplanmıyor.

### Navigation (.69.x)

- Seçili hedef ile mevcut rota ayrı kartlarda; sonraki durak WE tahmini gösterilir.
- Favoriler, konum kaydı, aynı noktada kopya önleme ve SEND TO SHIP hedef gönderimi eklendi.
- Manuel konumların fiziksel terminal Y/mesafe ve son rota yüksekliği düzeltildi. Hedef seçimi mevcut rotayı değiştirmez.
- Temel hedef/favori/kayıt/yeniden giriş/terminal/rota zinciri kabul edildi; genişletilmiş testler ayrı tutuluyor.

### Önceki temel sistemler (.65–.68)

- Discoveries listesi, filtreler, ayrıntılar, favori/hedef/rota eylemleri; Overview telemetrisi ve Ship Link bağlantı durumları tamamlandı veya temel tek oyunculu kapsamda kabul edildi.
- Structure Survey ortak ayarı kullanıcının seçimiyle 20 tick / 1 saniyedir; Geological Survey ayrı 80 tick ayarını korur.

### Belge kapanışı

Oyuncu rehberi, ana sayfa, ayar rehberi, yol haritası, bilinen sınırlar ve devam kayıtları güncellendi. Bu belge güncellemesi yeni JAR, config değeri veya dünya değişikliği içermez. Geliştirme kullanıcının isteğiyle burada duraklatıldı; sonraki başlangıç Aşama 12'nin açık işleri üzerinden değerlendirilir.

[Oyuncu rehberi](docs/19_Oyuncu_Rehberi.md) · [Açık işler](docs/12_Gelistirme_Yol_Haritasi.md) · [Kabul kayıtları](docs/README.md)
