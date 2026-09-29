# Proje altyapısı ve senkronizasyon durumu

Son güncelleme: 29 Eylül 2026

İlk depo kurulumu tamamlanmış ve proje artık iki bilgisayarlı geliştirmeye uygun hale getirilmiştir.

## Tamamlanan altyapı

- GitHub `main` ortak ana kaynak olarak belirlendi.
- Laptop, `machines/laptop.json` ile çalışma/test noktası olarak kaydedildi.
- 275 CurseForge öğesi `manifest.json` ve `pack-lock.json` içinde sabitlendi.
- NewWorldCore ve DoctorWhoMod özel fork JAR’ları GitHub’a eklendi.
- Canlı `config`, `defaultconfigs` ve `kubejs` içeriği repoyla eşitlendi.
- Auth anahtarları, oturum verileri, cache, dünyalar, loglar ve yedekler senkronizasyon dışında bırakıldı.
- CurseForge içe aktarma ve iki yönlü yerel senkronizasyon araçları oluşturuldu.
- Proje belleği ve ajan yönergeleri repoya eklendi.

## Kayıtlı bilgisayarlar

| Makine | Durum | Rol |
|---|---|---|
| laptop | Doğrulanan test noktası | Güncel .72.1 ve temel kullanıcı kabulü; geliştirme duraklatıldı |
| desktop | Kayıtlı | machines/desktop.json; bu kapanışta çalışma ortamı yeniden doğrulanmadı |

## Günlük çalışma

Çalışmaya başlamadan önce yerel değişiklikleri koruyarak temiz `main` üzerinde yalnız fast-forward senkronizasyon yapın. Kurulum farklarını doğrulayın; JAR/config uygulaması gerekiyorsa oyun kapalıyken yedekli ilerleyin. Yalnız belge değişikliği için yeniden kurulum gerekmez. Oyun içinde doğrulanan değişiklikleri tekrar repoya aktarın; ardından diff, commit ve push sırasını izleyin.

Teknik komutlar ve güvenlik sınırları [`docs/WORKSPACE_SYNC.md`](docs/WORKSPACE_SYNC.md) içinde açıklanır.

## Mevcut geliştirme kapısı

Kurulu laptop/repo NewWorldCore `0.5.72.1-alpha-unified-ship-alerts`. Navigation, Mining, ana kapıya Emergency dönüş ve son temel FE/History/HUD kabulü kaydedildi; genişletilmiş kontroller açık. Aşama 12 kısmi, geliştirme kullanıcı isteğiyle duraklatıldı.

275 CurseForge öğesi (267 mod, 4 resourcepack, 4 shaderpack; 274 etkin, 1 devre dışı) sabitlenmiştir; iki özel fork ayrıca repoda tutulur. Bu belge güncellemesi sürüm envanterini veya oyun dosyalarını değiştirmez.

[Oyuncu rehberi](docs/19_Oyuncu_Rehberi.md) · [Değişiklikler](CHANGELOG.md) · [Yol haritası](docs/12_Gelistirme_Yol_Haritasi.md).
