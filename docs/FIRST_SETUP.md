# New World - İlk Kurulum Rehberi

## ✅ Sistemgereksinimler

- **Java:** JDK 21 veya daha yeni
- **RAM:** En az 6GB (8GB+ önerilen)
- **Disk Alanı:** 5GB+ boş alan
- **OS:** Windows 10+, macOS 10.14+, Linux

## 🚀 Adım 1: Gerekli Yazılımları Yükleyin

### Java 21 Kurulumu

#### Windows
1. [Eclipse Temurin 21](https://adoptium.net) indir
2. Yükleyiciyi çalıştır
3. Path'e ekle seçeneğini işaretle

#### macOS
```bash
brew install temurin@21
```

#### Linux
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

### Launcher Seçimi

#### Prism Launcher (⭐ Önerilen)
1. [prismlauncher.org](https://prismlauncher.org) indir
2. Kur ve açın
3. "Yeni Instance" → "Minecraft 1.21.1 + NeoForge"

#### MultiMC
1. [multimc.org](https://multimc.org) indir
2. Benzer şekilde instance oluştur

#### Vanilla Launcher
1. Minecraft launcher açın
2. NeoForge profili oluşturun
3. Modpack dosyalarını kopyalayın

## 📦 Adım 2: Modpack'i Yükleyin

### Seçenek A: Repository Klonla

```bash
# Terminal/Command Prompt aç
cd <Launcher Games Folder>
git clone https://github.com/reddragon077/Minecraft-Modpack-New-World.git
cd Minecraft-Modpack-New-World
```

### Seçenek B: Dosya İndir

1. GitHub'dan ZIP indir
2. Launcher games klasörüne çıkart
3. Klasörü `minecraft-modpack-new-world` olarak yeniden adlandır

## 🔧 Adım 3: Mod Yüklemesi

### Modrinth ile (Otomatik)

1. [Modrinth](https://modrinth.com) aç
2. "New World" modpack'i ara
3. Launcher'ını seç (Prism/MultiMC)
4. Otomatik yükleme başlayacak

### Manual Mod Yükleme

1. `mods/` klasörü aç
2. Gerekli modları indir:
   - KubeJS
   - Modern Industrialization
   - Applied Energistics 2
   - Ad Astra
   - Create
   - Mekanism
   - Performans modları ([bkz. MODS.md](MODS.md))

3. `.jar` dosyalarını `mods/` klasörüne kopyala

## 🎮 Adım 4: İlk Başlangıç

1. Launcher'da instance seç
2. "Oyunla" tıkla
3. Modlar yüklenecek (ilk kez uzun sürebilir)
4. Oyun açılacak
5. Yeni Dünya oluştur

## ✨ Adım 5: KubeJS Kurulumu

KubeJS ilk oyun başlatıldığında otomatik yapı oluşturacak:

```
.minecraft/kubejs/
├── scripts/
│   └── server.js
├── client_scripts/
└── config/
```

Detaylı kurulum: [KUBEJS_SETUP.md](KUBEJS_SETUP.md)

## 📋 Kurulum Sonrası Kontrol

- [ ] Oyun sorunsuz açılıyor
- [ ] Modlar log'ta yüklenmiş gösteriyor
- [ ] Hiç red screen hatası yok
- [ ] FPS 30+ (test görevinde)
- [ ] KubeJS dosyaları oluşturulmuş

## ⚙️ Performans Ayarlaması

Bkz. [PERFORMANCE.md](PERFORMANCE.md) performans optimizasyonu için.

## 🐛 Sorun Giderme

### "Hata: Could not find valid JVM"
→ Java 21 doğru kurulmuş mu kontrol et

### "ModNotFoundException"
→ İndirilmeyen modu el ile indir

### Oyun çöküyor
→ [latest.log](https://www.minecraft.net) dosyasını kontrol et

### Yavaş performans
→ [PERFORMANCE.md](PERFORMANCE.md) kontrol et

## 📚 Sonraki Adımlar

1. [Katkı Rehberi](CONTRIBUTING.md) oku
2. [Proje Tasarımı](PROJECT_DESIGN.md) öğren
3. Kabiliyetin alanında geliştirme başla

## 🤝 Yardım Gerekirse

- GitHub Issues: Hata bildirimi
- Discussions: Soru ve fikir
- Discord: (Yakında eklenecek)

---

**Sorun mu yaşıyor? İlk kurulum zor olabilir ama biz buradayız!**

Başarılı kurulum!
