# New World — Oyuncu rehberi

29 Eylül 2026 • NewWorldCore `0.5.72.1-alpha-unified-ship-alerts` • Minecraft 1.21.1 / NeoForge 21.1.235

New World deneysel bir alpha modpack'tir. Aşağıdaki temel akışlar laptopta tek oyunculu oyun testleriyle doğrulandı; bu, bütün mod etkileşimlerinin veya çok oyunculu kullanımın sorunsuz olduğu anlamına gelmez. Güncellemeden önce dünyanı yedekle. [Proje](../README.md) · [Değişiklikler](../CHANGELOG.md) · [Bilinen sınırlar](Known%20Issues.md)

## Oyuncu panelini açma

Minecraft Kontroller ekranından Player Ship Interface tuş atamasını bul. Panel, gemi terminallerinin taşınabilir özetidir; ayrıntılı motor, routing ve upgrade yönetimi fiziksel terminallerde kalır.

Üstteki Ship Link satırı bağlantıyı gösterir: `ON BOARD`, `CONNECTED`, `DIMENSIONAL` veya `LOST`. Varsayılan aynı-boyut menzili 5000 bloktur; boyutlar arası bağlantı config politikasına bağlıdır. Bağlantı kaybında güncel gemi verileri kullanılamayabilir ve uzaktan eylemler engellenir. Acil dönüşün ayrı güvenlik kuralları vardır.

## Overview ve uyarılar

`OVERVIEW` enerji, motor, el freni, Mining Shield, rota, matrix ve dış konumu özetler. `OUT` enerji çıkışını, `NET` net değişimi gösterir.

`ALERT HISTORY` ortak Overview uyarılarını ve Collection/drive kontrollerini izler. Varsayılan FE/WE eşikleri %20 uyarı, %5 kritiktir. Collection yüzdesi eşya sayısını değil kullanılan tür yuvalarını ölçer. Eksik veri, sağlıklı durum veya çözülmüş arıza sayılmaz.

Geçmiş varsayılan son 16 durum değişimini saklar ve sayfa başına 5 satır gösterir. GUI'yi kapatıp açınca korunur; dünyadan/sunucudan çıkınca sıfırlanır. `RESOLVED`, ilgili uyarının sona erdiğini gösterir. Küçük HUD bildirimleri aynı durumu sürekli tekrarlamaz; diğer modlarla action-bar alanını paylaşır. Yeni keşif bildirimleri henüz bu sisteme eklenmedi.

## Survey ve Discoveries

Radar uzak adayları bulur; `SURVEY` bulunduğun çevrede yerinde doğrulama yapar. Structure Survey varsayılan 48 blok / 20 tick (normal hızda 1 saniye); Geological Survey 48 yatay, 128 dikey blok / 80 tick (4 saniye) kullanır. Sunucu yavaşsa gerçek süre uzayabilir.

`DISCOVERIES` içinden `ALL`, `STRUCTURES`, `GEOLOGY` filtrelerini kullan. Ayrıntılarda analiz seviyesi, koordinat, kaynak ve son görülme bulunur. `UNKNOWN STRUCTURE` henüz kesin aile tanımı olmayan aday olabilir; hata olduğunu tek başına göstermez.

- `FAV`: kaydı favorilere ekler/çıkarır.
- `TARGET`: ortak Navigation hedefini seçer; mevcut rotayı değiştirmez.
- `ROUTE`: seçilen kayıt için mevcut rota hazırlama yolunu kullanır; tek başına uçuş başlatmak değildir.

`EST RESERVE` tahmini rezervdir. Kazımdan sonra gerçekten kalan kaynak miktarı değildir.

## Konum kaydetme ve Navigation

1. Geminin dışında istediğin noktada `NAVIGATION → SAVE CURRENT LOCATION` kullan.
2. `FAVORITES` listesini aç. Aynı bloktaki tekrar kayıt kopya oluşturmaz; farklı konum ayrı kayıt olur. Konumlar dünyadan çıkıp girdikten sonra da kalır.
3. Bir favoriyi seçip `SEND TO SHIP` kullan. Yalnız ortak hedef değişir; kayıt yapmak veya hedef göndermek gemiyi uçurmaz, WE harcamaz.
4. Yeni rota gerekiyorsa fiziksel Navigation Terminal'de `CALCULATE ROUTE` kullan. Discoveries kayıtlarında `ROUTE` da kullanılabilir. Uçuşun normal enerji ve güvenlik koşulları ayrıca geçerlidir.

Sol kart **seçili hedef**, sağ kart **mevcut rota** olduğu için farklı yerleri göstermeleri normal olabilir. Yeni hedef seçmek eski rotayı otomatik silmez. `NEXT HOP` WE tahmini yalnız yüklenmiş sonraki durak içindir, toplam rota maliyeti değildir. Manuel konumun Y değeri son rota noktasında korunur.

## Mining

`MINING` durum, tarama alanı, Scan/Extraction ilerlemesi, Collection/AE Transfer/Replication Feed buffer'ları ve aktarım sayaçlarını gösterir. `WAITING FOR HANDBRAKE` el freni koşulunu beklediğini belirtir; ayrıntılı kontrol fiziksel terminaldedir.

`MINED RESOURCES` çıkarılan **blokları** sayar; düşen eşya/stack sayısı değildir. Eski, izlenmemiş kazımlar ayrıca gösterilir ve kaynaklara uydurma dağıtılmaz. Sayaçlar aynı tarama alanında kaydedilir; yeni alanla sıfırlanır. Scan ve Extraction yüzdeleri bir depositin kalan rezerv yüzdesi değildir.

`STOP MINING` iki tıklamayla onaylanır ve Mining Shield'ı kapatır. Yeniden başlatma, routing, öncelik, Keep ve upgrade ayarları fiziksel terminaldedir.

## Emergency: gemiye acil dönüş

`EMERGENCY RETURN TO SHIP` ve ardından onay, seni **kendi geminin içinde, normal portalın getirdiği ana kapı önüne** döndürür. Ayrı Teleporter Room'a göndermez. Eski DISTRESS BEACON işlevi kaldırıldı; geçmiş kayıtlar silinmedi.

Başarılı dönüş varsayılan **30 gerçek dakika** bekleme başlatır. Çıkış/giriş süreyi sıfırlamaz, çevrimdışı zaman da sayılır. Başarısız veya engellenmiş dönüş yeni bekleme tüketmez. Navigation hedefi, rota, gemi uçuşu ve WE değiştirilmez.

Normal Ship Link menzili dışında da kurtarma isteği yapılabilir; ancak sahiplik, canlı/yaya oyuncu, gemi durumu ve yüklü/güvenli giriş kontrolleri devam eder. Zaten kendi gemindeyken veya başka TARDIS içindeyken dönüş reddedilir. Güvensiz girişte başka noktaya rastgele ışınlanma yapılmaz.

## Henüz tamamlanmayanlar

Yeni keşif HUD bildirimleri, ileri depozit çeşitliliği ve gerçek rezerv/tükenme defteri sonraki işlerdir. Multiplayer, koruma modları, yük/gecikme ve bazı bağlantı/config sınır testleri ayrıca açıktır. [Yol haritası](12_Gelistirme_Yol_Haritasi.md), kabul edilen temel akışları ve kalan kontrolleri ayrı gösterir.
