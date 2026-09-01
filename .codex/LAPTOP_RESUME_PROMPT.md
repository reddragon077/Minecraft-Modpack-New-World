# Laptop continuation prompt

Copy the block below into Codex on the laptop after opening the New World repository project.

```text
Kanka bu New World projesine masaüstündeki son noktadan devam edeceğiz. GitHub main ortak ve kanonik kaynaktır.

Önce hiçbir dosyayı değiştirmeden mevcut branch ve çalışma ağacını kontrol et. Yerel değişiklik yoksa origin/main'i fetch edip yalnızca fast-forward pull yap. Yerel değişiklik varsa ezme; bana durumu bildir. Ardından sırasıyla `.codex/project-memory.md`, `.codex/HANDOFF.md`, `.codex/conversations/INDEX.md` ve `.codex/conversations/2026-09-02_desktop_random_spread_coordinate_repair.md` dosyalarını tamamen oku.

Masaüstünde son kabul edilen build:
`NewWorldCore-1.21.1-NeoForge-0.5.59.10-alpha-radar-random-spread.jar`
SHA-256:
`876bc247597c0186bced715b05e843e9a85b00f32d3a9c81c990d6134fd63687`

Masaüstü runtime kabulü geçti. `latest.log` kanıtı:
- 1 placement-only görev
- range 5000 blocks
- selected=CAMPSITE
- 1 results (1 before active filters)

Laptopun CurseForge instance yolunu `machines/laptop.json` kaydından çöz. Oyun kapalıyken `tools/apply-to-instance.ps1` ile repo dosyalarını ve iki projeye özel JAR'ı laptop instance'ına uygula. Önce eski custom JAR'ın yedeklendiğini, sonra repo ve instance'ta tek NewWorldCore JAR bulunduğunu ve yukarıdaki SHA-256 ile eşleştiğini doğrula. Oyun açıksa JAR değiştirme; kapatmamı iste.

Kurulumdan sonra sıradaki çalışma:
1. `ALL` filtresiyle bir Structure Radar taraması yapıp karışık ailelerin geri geldiğini ve taramanın makul sürede tamamlandığını doğrulamak.
2. Sonra `betterarcheology:archeologist_camp_grassy` yapısını bulma, yerine gidip Structure Field Survey ile `ARCHEOLOGIST CAMP` olarak tanıma ve dinamik filtreye eklenme zincirini tamamlamak.
3. Bu Radar v2 kapanışından sonra yol haritasındaki Discovery Database analysis-level / last-seen / event aşamasına geçmek.

Her doğrulanmış adımda pack-lock, ilgili dokümanlar, `.codex/HANDOFF.md`, `.codex/project-memory.md` ve tarihli conversation kaydını güncelle; test et; commit edip GitHub main'e pushla. Dünya/save/log/cache dosyalarını Git'e ekleme. Bilinen iyi JAR yedeklerini silme ve aynı anda iki NewWorldCore sürümü yükleme.

Önce senkronizasyon ve hash kontrollerini yap, sonucu bana özetle; sonra test ve geliştirmeye devam edelim.
```
