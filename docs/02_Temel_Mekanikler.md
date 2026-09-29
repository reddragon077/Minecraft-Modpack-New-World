# Temel mekanikler

Güncel alpha: NewWorldCore `0.5.72.1-alpha-unified-ship-alerts` — 29 Eylül 2026. Discovery, Overview/Ship Link, Navigation konum/favori, Mining kaynak/durdurma ve Emergency kapı önü dönüşünün temel tek oyunculu kabulleri geçti. Son FE uyarı geçmişi/çözülme, HUD ve arayüz kapat-aç kontrolü de kabul edildi. Bütün sistemlerin genişletilmiş testi tamamlanmış değildir. [Oyuncu rehberi](19_Oyuncu_Rehberi.md) · [Kabul sınırları](Known%20Issues.md).

| Mekanik | Durum | Uygulama |
|---|---|---|
| Oda kontrolü ve matrix | Alpha | Kapalı oda kabukları, modül sayımı, bütünleştirme ve koruma |
| Gemi ağı | Alpha | FE/eşya/sıvı/gaz taşıma, oda bağlantıları, öncelik ve telemetri |
| Mining M1 | Alpha | Scan → Extraction, FE tüketimi, yükseltmeler, derin depolama ve güvenlik koşulları |
| Geological deposits | Alpha | Deterministik worldgen, Accuracy 0/I/II/III kademeli tanımlama, aile bazlı config eşikleri, fiziksel blok doğrulamalı Geological Field Survey ve kalıcı deposit verisi |
| Structure Radar | Alpha | Canlı structure registry üzerinden vanilla/modlu yapı taraması, family filtreleri ve şema-v3 keşif veritabanı |
| Navigasyon | Alpha | Hedef seçimi, geçmiş/favoriler, rota hesabı ve çok duraklı ilerleme |
| Player Navigation | Temel kabul geçti | Hedef/rota/sonraki hop, WE tahmini, manuel konum kaydı, favoriden SEND TO SHIP |
| Player Mining | Temel kabul geçti | Canlı alan/ilerleme/buffer görünümü, tarama alanına bağlı kalıcı kaynak sayacı, onaylı STOP MINING |
| Emergency | Temel kabul geçti | Normal portalın getirdiği iç kapı önüne dönüş; başarı sonrası 30 gerçek dakika bekleme, çıkış/girişte korunma |
| Ship Alerts | Temel FE/geçmiş/HUD kabulü geçti | Ortak Overview uyarıları ve sınırlı oturum geçmişi; veri bilinmiyorken arıza sahte biçimde çözülmez |
| Replikasyon | Alpha | Doğal kaynak bilgisi, Matter değerleri ve tarama kısıtları |
| Warp/engine | Deneysel | Oda bileşenleri, Engine Matrix ve DoctorWhoMod seyahat bağlantıları |
| Araştırma | Prototip | Stage/skill/quest altyapısı var; nihai içerik ağacı eksik |
| Production Chamber | Prototip | Custom Machinery entegrasyonları var; oda kataloğu ve progression eksik |
| Genetik | Tasarım | Oynanabilir uygulama henüz tamamlanmadı |

## Sistem bağlantıları

```text
Radar/Jeoloji
      ↓
Keşif ve bilgi kaydı
      ↓
Mining veya Replication
      ↓
Gemi ağı ve depolama
      ↓
Oda, motor ve navigasyon yükseltmeleri
      ↓
Daha uzak keşif
```

## Teknik sınırlar

- Gemi verisi sunucu tarafında ve gemi/TARDIS kapsamlı tutulmalıdır.
- İstemci yalnızca görüntü ve kullanıcı girdisi sağlamalıdır.
- Aynı custom modun birden fazla JAR sürümü birlikte yüklenmemelidir.
- Radar, navigasyon, deposit ve replikasyon isimleri ortak kaynak kimlikleri kullanmalıdır.
- Discovery kayıtlarında `discoveredAt` ilk keşfi, `lastSeenAt` son tekrar gözlemini temsil eder; analiz seviyesi 0-3 arasında kalıcıdır ve düşürülemez.
- Alpha özellikler varmış gibi belgelenmeden önce mevcut JAR ve oyun testiyle doğrulanmalıdır.
- Konum kaydetme, hedef seçme, rota hesaplama ve gemiyi uçurma farklı işlemlerdir; tek düğmeyle aynı işlem sayılmazlar.
- Mining kaynak adetleri kazılan blok sayısıdır; düşen eşya sayısı veya gerçek deposit remaining yüzdesi değildir.
- Favori/konum ve acil dönüş beklemesi kalıcıdır; Ship Alerts geçmişi yalnız mevcut oturum içindir.
