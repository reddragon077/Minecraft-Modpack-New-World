# New World Modpack - Teknoloji Ağacı Taslağı

## Temel Teknoloji Ağacı Yapısı

### Seviye 1: Keşif
- Element Analiz Başlangıcı
- Temel Veri Kayıt
- Araştırma Laboratuvarı I

### Seviye 2: Analiz
- Element Katalogı
- Replicator Sistemi
- Gelişmiş Analiz I

### Seviye 3: Üretim
- Modüler Fabrika
- Production Chamber
- Otomasyonu I

### Seviye 4: Enerji
- Enerji Ağı
- Power Generation I
- Batarya Sistemi

### Seviye 5: Veri
- Applied Energistics II Ağ
- Veri Depolaması
- Kablosuz Transfer

### Seviye 6: Mekanik
- Create Sistemleri
- Dişli Kutusu
- Mekanik Kuvvet

### Seviye 7: İleri Teknoloji
- Kimya Endüstrisi (Mekanism)
- Plastik Üretim
- Katı Yakıt

### Seviye 8: Uzay
- Roket Yapımı
- Uzay Yolculuğu
- Gezegen Keşfi

### Seviye 9: Genetik
- DNA Analizi
- Genetik Değişiklik
- Biyoloji Laboratuvarı

### Seviye 10: Oyun Sonu
- Warp Teknolojisi
- Çok Boyutlu Transfer
- Ev Dönüş Kapısı

## Teknoloji Bağlantı Haritası

```
Keşif → Analiz → Üretim → Veri
  ↓                ↓        ↓
Enerji → Mekanik → Endüstri → Uzay
  ↓                                ↓
Genetik → İleri Teknoloji → Oyun Sonu
```

## Öğrenme Kaynakları

| Teknoloji | Kaynak | Detay |
|-----------|--------|-------|
| Temel | Keşif görevleri | %50 progres |
| Intermediate | Veri diskleri | %75 progres |
| İleri | Araştırma merkezi | %90 progres |
| Oyun Sonu | Kafes / Portal | %100 progres |

## KubeJS Implementation Plan

```javascript
// Teknoloji ağacı veri yapısı
const techTree = {
  'tech_id': {
    name: 'İnsan Adı',
    tier: 1,
    requires: ['previous_tech'],
    unlocks: ['next_tech1', 'next_tech2'],
    learnable_from: ['ancient_tome', 'research_station'],
    rewards: ['recipe1', 'recipe2']
  }
}

// KubeJS event'leri
ServerEvents.recipes(event => {
  // Öğrenilmesi gereken teknoloji kontrol
})

ServerEvents.customCommand(event => {
  // /research teknoloji komutu
})
```

---

**Not:** Bu taslak geliştirilmeye devam edecektir. Topluluk geribildirimine açık.
