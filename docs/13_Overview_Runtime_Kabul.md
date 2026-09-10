# Overview .67.1 — Oyun içi kabul

Durum: kısmi. `.67.0` canlı veriler, fren/kalkan/Mining, cooldown→READY, FE yük değişimi, CRITICAL→WARNING→NOMINAL, GUI yeniden açılışı ve config yenilemesi geçti. `.67.1` aşağıdaki yeni uyarı görünümü testini bekliyor; otomatik test görsel kabul değildir.

## Öncelikli .67.1 testi — enerji tüketmeden

1. Gemi sabitken ve el freni RELEASED durumundayken Mining Shield'ı aç. Mining fren beklemeli; `[ACTIVE] MINING WAITING FOR HANDBRAKE` sarı görünmeli.
2. Shield'ı kapat. Başka uyarı yoksa başlık NOMINAL; aynı geçmiş kayıt artık gri `[RESOLVED]` olmalı. Test için kazım başlatmaya veya depoyu boşaltmaya gerek yok.
3. `show_resolved_warnings=false` kaydet: çözülmüş satırlar gizlenmeli, aktifler kalmalı. Tekrar true yap. `refresh_ticks=20` tercihini koru.
4. Oturum başında geçmişin boş olması normaldir; bu kalıcı dünya günlüğü değildir. Kırmızı `[CRITICAL]` ve sıralama otomatik testte geçti; yeni sürümde gerçek kritik görünüm ayrıca doğrulanmış sayılmaz.

## Genel regresyon listesi

1. Oyunu açıp Player Ship Interface → OVERVIEW ekranına gir. 20 tick ayarında yaklaşık 1–2 saniyede SYNCING yerini canlı verilere bırakmalı. Sütunlar birbirine taşmamalı.
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
