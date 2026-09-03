# Temel mekanikler

Aktif runtime-kabul edilen laptop buildi: NewWorldCore `0.5.65.2-alpha-player-discoveries` (Discovery Database şema v3, kademeli Geological Analysis ve Player GUI'de ortak Structure/Geology keşif listesi, ayrıntı görünümü, kategori dengesi ve canlı oyuncu mesafesi geçti)

| Mekanik | Durum | Uygulama |
|---|---|---|
| Oda kontrolü ve matrix | Alpha | Kapalı oda kabukları, modül sayımı, bütünleştirme ve koruma |
| Gemi ağı | Alpha | FE/eşya/sıvı/gaz taşıma, oda bağlantıları, öncelik ve telemetri |
| Mining M1 | Alpha | Scan → Extraction, FE tüketimi, yükseltmeler, derin depolama ve güvenlik koşulları |
| Geological deposits | Alpha | Deterministik worldgen, Accuracy 0/I/II/III kademeli tanımlama, aile bazlı config eşikleri, fiziksel blok doğrulamalı Geological Field Survey ve kalıcı deposit verisi |
| Structure Radar | Alpha | Canlı structure registry üzerinden vanilla/modlu yapı taraması, family filtreleri ve şema-v3 keşif veritabanı |
| Navigasyon | Alpha | Hedef seçimi, geçmiş/favoriler, rota hesabı ve çok duraklı ilerleme |
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
