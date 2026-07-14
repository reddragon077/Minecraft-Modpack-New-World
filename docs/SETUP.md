# New World - Kurulum Rehberi

## Gereksinimler

- Java 21 veya daha yeni
- Minecraft 1.21.1
- NeoForge 21.1.235

## Adımlar

### 1. Repository'yi Klonlayın

```bash
git clone https://github.com/reddragon077/Minecraft-Modpack-New-World.git
cd Minecraft-Modpack-New-World
```

### 2. Minecraft Launcher

#### Prism Launcher (Önerilen)

1. [Prism Launcher](https://prismlauncher.org) indirin
2. Yeni instance oluşturun
3. Version: Minecraft 1.21.1 + NeoForge 21.1.235 seçin
4. Instance klasörüne repo dosyalarını kopyalayın

#### Vanilla Launcher

1. Minecraft launcher'ı açın
2. NeoForge 21.1.235 profili oluşturun
3. Modpack dosyalarını `.minecraft` klasörüne yerleştirin

### 3. KubeJS Setup

KubeJS otomatik olarak yüklenecek ve klasör yapısını oluşturacak.

### 4. Test Etme

```bash
# Minecraft'ı başlatın
# Konsol hatalarını kontrol edin
# /cubejs reload komutu çalışır mı test edin
```

## Sorun Giderme

### Modlar yüklenmedi
- `mods/` klasöründe `.jar` dosyaları var mı kontrol edin
- NeoForge versiyonunu kontrol edin

### KubeJS hatası
- `kubejs_scripts.log` dosyasını kontrol edin
- `/kubejs/scripts/` klasörü boş mu kontrol edin

### Yapılandırma problemi
- `config/` klasöründe mod config dosyaları var mı kontrol edin

---

Sorularınız varsa GitHub Issues açın.
