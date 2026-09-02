# Temel mekanikler

Aktif runtime-kabul edilen laptop buildi: NewWorldCore `0.5.60.4-alpha-config-first-gui-network` (Field Survey 48 blok/80 tick, canlı GUI bilgisi, `ARCHEOLOGIST CAMP` dinamik filtresi ve config-first ağ ayarları geçti)

| Mekanik | Durum | Uygulama |
|---|---|---|
| Oda kontrolü ve matrix | Alpha | Kapalı oda kabukları, modül sayımı, bütünleştirme ve koruma |
| Gemi ağı | Alpha | FE/eşya/sıvı/gaz taşıma, oda bağlantıları, öncelik ve telemetri |
| Mining M1 | Alpha | Scan → Extraction, FE tüketimi, yükseltmeler, derin depolama ve güvenlik koşulları |
| Geological deposits | Alpha | Deterministik worldgen, radar sonuçları ve kalıcı deposit verisi |
| Structure Radar | Alpha | Canlı structure registry üzerinden vanilla/modlu yapı taraması, family filtreleri ve keşif veritabanı |
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
- Alpha özellikler varmış gibi belgelenmeden önce mevcut JAR ve oyun testiyle doğrulanmalıdır.
