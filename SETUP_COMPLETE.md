# Proje altyapısı ve senkronizasyon durumu

Son güncelleme: 1 Eylül 2026

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
| laptop | Aktif | Geliştirme ve oyun testi |
| desktop | Bekliyor | Klasör yolları ve CurseForge örneği daha sonra kaydedilecek |

## Günlük çalışma

Çalışmaya başlamadan önce GitHub’dan `main` alın. Repo içeriğini yerel CurseForge örneğine uygulayın. Oyun içinde doğrulanan değişiklikleri tekrar repoya aktarın; ardından diff, commit ve push sırasını izleyin.

Teknik komutlar ve güvenlik sınırları [`docs/WORKSPACE_SYNC.md`](docs/WORKSPACE_SYNC.md) içinde açıklanır.

## Mevcut geliştirme kapısı

Altyapı hazırdır. `0.5.58.0-alpha-expanded-geology-deposits` laptopta açılmış; Uraninite radar → rota → fiziksel yatak zinciri doğrulanmıştır. Aktif geliştirme ve kalan kabul sırası [`docs/12_Gelistirme_Yol_Haritasi.md`](docs/12_Gelistirme_Yol_Haritasi.md) belgesindedir.
