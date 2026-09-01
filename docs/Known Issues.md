# Bilinen sorunlar ve doğrulama listesi

Son güncelleme: 1 Eylül 2026

## Aktif riskler

- NewWorldCore `0.5.58.0-alpha-expanded-geology-deposits` aktif test buildidir; yeni depositler ve eski radar/navigation davranışları birlikte oyun içinde regresyon testinden geçirilmelidir.
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

1. Oyun açılışı ve mod çakışması kontrolü.
2. Mevcut dünya yükleme ve gemi verisi kalıcılığı.
3. Geological Radar ile fiziksel deposit eşleşmesi.
4. Osmium, Tin, Lead, Uranium ve Fluorite depositleri.
5. Aluminum, ortak Nickel, Silver, Zinc, Platinum, ayrı Uraninite ve Certus Quartz depositleri.
6. Lead/Uranium/Nickel için tek aile ve çoklu mod palette kuralları.
7. Navigation `DEPOSITS`, geçmiş ve yeniden başlatma kalıcılığı.
8. Replication tarama bilgisi ve duplicate mineral kuralları.
