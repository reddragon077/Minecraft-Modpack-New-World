# Bilinen sorunlar ve doğrulama listesi

Son güncelleme: 1 Eylül 2026

## Aktif riskler

- NewWorldCore `0.5.59.4-alpha-full-placement-radar` aktif test buildidir; placement tabanlı vanilla/modlu structure taraması, gerçek-yapı Field Survey izolasyonu ve deposit regresyonu birlikte oyun içinde doğrulanmalıdır.
- İlk `0.5.59.0` denemesinde namespace toplu sorgusu 40 saniyeyi aşan server tick'e yol açtı. `0.5.59.1` sorguları tick'lere yaydı fakat tekil locate çağrıları yine 2–8 saniyelik takılmalar üretti. `0.5.59.2`, modlu yapılar için chunk/worldgen yükleyen locate yolunu kaldırıp yalnızca placement koordinat matematiğini kullanır.
- Placement tabanlı radar koordinatı bir yapı için olası üretim noktasıdır; biyom/structure-set seçimi nedeniyle yanlış pozitif ihtimali vardır. Gerçek keşif, oyuncunun yerinde kullandığı Structure Field Survey ile doğrulanır.
- `0.5.59.2` taraması 128 sonuç korumasına ulaştığında ilerleme indeksini artırmadığı için tarama tamamlanmıyordu. `0.5.59.3`, taramayı sonuna kadar ilerletirken yalnızca mesafe olarak en yakın 128 sonucu tutar.
- `0.5.59.3` bitiş filtresi eski 13 vanilla etiketi dışında kalan modlu aileleri sildi ve legacy vanilla locate döngüsü bazı tick'lerde 8–13 saniyelik gecikme üretti. `0.5.59.4`, modlu aileleri bitişte korur ve vanilla yapıları da random-spread/concentric-rings placement verisinden hesaplar.
- `0.5.56.0-alpha-radar-navigation-mining-recovery` konuşma geçmişinde bilinen iyi geri dönüş noktası olarak geçer; yerel binary/source bulunmadan otomatik geri dönüş yapılmamalıdır.
- Reliable EMI (`emixx-neoforge-1.21.1-3.1.2.jar.disabled`) bilinçli olarak devre dışıdır.
- Araştırma, Production Chamber, genetik ve tam hikâye progression’ı henüz tamamlanmış oynanış sistemi değildir.
- NewWorldCore'un tam tarihsel kaynak ağacı henüz bulunamadı. `src-patches/newworldcore/` genişletilmiş jeoloji değişikliğini yeniden üretilebilir tutar; ileride tam kaynak projeye katlanmalıdır.

## Senkronizasyon sınırları

- `manifest.json` üçüncü taraf eklentileri sabitler; iki custom fork `mods/` altında tutulur.
- KubeJS web server auth anahtarı, WorldEdit oturumları, cache, log, dünya ve kişisel seçenekler GitHub’a alınmaz.
- Her CurseForge örneğinde yalnızca bir NewWorldCore ve bir DoctorWhoMod fork JAR’ı bulunmalıdır.
- Masaüstü bilgisayar henüz `machines/desktop.json` ile kaydedilmemiştir.

## Bir sonraki test kapısı

`uraninite_pocket` için Radar → Navigation → TARDIS rota → fiziksel yatak zinciri 1 Eylül 2026'da doğrulandı. Aşağıdaki maddeler tam regresyon kapısı olarak devam eder:

1. Oyun açılışı, FE Matrix kayıt kararlılığı ve mod çakışması kontrolü.
2. Mevcut dünya yükleme ve gemi verisi kalıcılığı.
3. Geological Radar ile fiziksel deposit eşleşmesi.
4. Osmium, Tin, Lead, Uranium ve Fluorite depositleri.
5. Aluminum, ortak Nickel, Silver, Zinc, Platinum, ayrı Uraninite ve Certus Quartz depositleri.
6. Lead/Uranium/Nickel için tek aile ve çoklu mod palette kuralları.
7. Navigation `DEPOSITS`, geçmiş ve yeniden başlatma kalıcılığı.
8. Replication tarama bilgisi ve duplicate mineral kuralları.
9. `explorify:campsite`, `structory:abandoned_camp` ve `betterarcheology:archeologist_camp_grassy` için Radar → UNKNOWN → yerinde Field Survey → family adı zinciri.
10. Structure Field Survey sonucunun hiçbir `GEOLOGY` kaydını structure olarak işaretlemediği kontrolü.
