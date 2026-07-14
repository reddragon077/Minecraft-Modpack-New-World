# Katkı Rehberi - New World Modpack

## Hoşgeldiniz!

New World modpack projesine katkıda bulunmak istediğiniz için teşekkürler! Bu rehber, projeye nasıl katılabileceğinizi açıklıyor.

## Proje Yapısı

```
├── mods/                 # Mod dosyaları (.jar)
├── config/               # Mod konfigürasyonları
├── kubejs/
│   ├── scripts/         # KubeJS betikleri
│   └── assets/          # Oyun içi varlıklar
├── datapacks/           # Minecraft data pack'leri
├── resourcepacks/       # Texture/UI paketleri
├── structures/          # NBT yapı dosyaları
└── docs/                # Belgeler
```

## Çalışma Alanları

### KubeJS Geliştircileri
- `/kubejs/scripts/` klasöründe KubeJS betikleri yazın
- Her özellik için ayrı dosya oluşturun
- Türkçe yorumlar kullanabilirsiniz

### Java Mod Geliştircileri
- `/mods/` klasörüne derlenmiş mod jar dosyalarını ekleyin
- Konfigürasyonları `/config/` klasörüne yerleştirin

### Data Pack Geliştircileri
- `/datapacks/` klasöründe data pack'ler oluşturun
- Loot tablolar, advancement'lar ve fonksiyonlar ekleyin

### Tasarımcılar
- Yapılar: `/structures/` klasörüne NBT dosyalarını ekleyin
- UI/Texture: `/resourcepacks/` klasöründe resourcepack oluşturun

## Kod Yazma Standartları

### KubeJS
```javascript
// Başında açıklama ekleyin
/**
 * Araştırma sistemi - Teknoloji açma mekanizması
 */

// Mantıklı değişken isimleri kullanın
const researchTechTree = new Map()

// Event'leri düzgün organize edin
ServerEvents.recipes(event => {
  // Tarif tanımlamaları
})
```

## Dallar ve İş Akışı

1. `main` dalından kendi feature dalınızı oluşturun:
   ```bash
   git checkout -b feature/feature-adi
   ```

2. Değişikliklerinizi yapın

3. Commit mesajı:
   ```
   [Feature] Açıklayıcı başlık
   
   Detaylı açıklama
   ```

4. Pull Request oluşturun

## Test Etme

Değişikliklerinizi test etmeden PR açmayın:

1. Minecraft'ı başlatın
2. Modpack'i yükleyin
3. Özelliğinizi test edin
4. Hataları rapor edin

## İletişim

- GitHub Issues: Hata raporları ve özellik talepleri
- Discussions: Fikir alışverişi

---

Sorularınız varsa endişelenmeyin - yardımcı olmaktan mutlu oluruz!
