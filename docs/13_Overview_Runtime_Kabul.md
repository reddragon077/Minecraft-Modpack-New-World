# Overview .67.0 — Oyun içi kabul

Durum: bekliyor. Otomatik test, oyun içi kabul yerine geçmez.

1. Oyunu açıp Player Ship Interface → OVERVIEW ekranına gir. Yaklaşık 2–4 saniyede SYNCING yerini canlı verilere bırakmalı. Sütunlar birbirine taşmamalı.
2. FE ve Warp depolarını fiziksel terminallerle karşılaştır. K/M/G gösterimi bin/milyon/milyardır; kapasite sıfır/bilinmiyorsa yüzde yerine uyarı beklenir.
3. İlk örnekte SAMPLING normaldir. Bir Radar veya Mining işi çalışırken ekranı tekrar aç: OUT gerçek FE havuz çıkışının, NET depolanan FE değişiminin yenileme aralığı ortalamasıdır. Üretim varsa NET pozitifken OUT da pozitif olabilir. Simülasyonlar OUT'a dahil değildir; dışarı FE aktarımı dahildir.
4. Mevcut fiziksel kontrollerle el freni ve Mining Shield durumunu değiştir; Overview kısa sürede güncellenmeli. Bu ekranın kendisi kontrol eylemi göndermez. Mining durumu çalışan modül terminaliyle uyumlu olmalı.
5. DISCOVERIES içinden TARGET/ROUTE seçip Overview'a dön; Navigation bilgisi ortak rota durumunu göstermeli. Uçuş ve cooldown bilgisi mevcut motor sisteminden gelir.
6. Geminin içindeyken ve dışındayken EXT dimension/koordinat geminin dış konumu olmalı; oyuncunun iç koordinatı olmamalı. Matrix kayıtları ONLINE/OFFLINE/UNKNOWN olarak görünür. Yüklü sahip olunan gemi bulunamazsa başka bir geminin verisi gösterilmez.
7. `overview.properties` içinde geçici olarak `refresh_ticks=100` dene; yenileme yaklaşık 5 saniyeye çıkmalı. `warning_rows=3` üçüncü uyarıyı açar. Ayarları testten sonra geri al. Kritik eşik uyarı eşiğini aşamaz.
8. FE/WE seviyelerini ve eşikleri güvenli test dünyasında karşılaştır: varsayılan <=20% WARNING, <=5% CRITICAL. Bilinmeyen kapasite CRITICAL boş pil değildir. Önceden görülmüş uyarı son uyarılar listesinde kalabilir; genel durum mevcut örneğe aittir.
9. Ekranı kapat/aç ve dünyadan çık/gir. Yeni örnek gelmeden eski verinin güncel görünmemesi gerekir; veri akışı durursa STALE görünür. Survey ve Discoveries ekranlarında regresyon olmamalı.

Sonrasında `latest.log` içinde `[NewWorld Overview]` durum satırları ve `request/snapshot/decode/render failed` hataları kontrol edilir.
Tüm maddeler doğrulanmadan Aşama 4 kapanmaz; sonraki aşama Ship Link'tir.
