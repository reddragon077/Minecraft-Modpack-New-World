# New World - KubeJS Kurulum Rehberi

## Dosya Yapısı

```
kubejs/
├── scripts/
│   ├── server.js         # Sunucu-tarafı betikleri
│   └── startup/          # Sunucu başlatıldığında çalış
├── client_scripts/       # İstemci-tarafı betikleri
├── assets/               # Özel kaynaklar
├── data/                 # Data paketi dosyaları
└── config/               # KubeJS konfigürasyonu
```

## Kurulum Adımları

### 1. Mod Yükleme
KubeJS jar dosyasını `mods/` klasörüne ekleyin.

### 2. Oyunu İlk Kez Çalıştırın
- Oyunu açın ve kapatın
- KubeJS otomatik olarak klasör yapısını oluşturacak

### 3. Betik Yazma Başlayın
`kubejs/scripts/server.js` dosyasını açın ve kodlamaya başlayın.

## Temel Event'ler

### Server Events

```javascript
// Sunucu başlatıldığında
ServerEvents.init(event => {
  console.log('[New World] Server initialized')
})

// Craft ve recipe yönetimi
ServerEvents.recipes(event => {
  event.remove({ output: 'minecraft:gold_ingot' })
})

// Her tick'te çalış
ServerEvents.tick(event => {
  // Oyun mekanikleri
})

// Oyuncu olayları
ServerEvents.entityEvent(event => {
  // Oyuncu hareketi, hit, vb.
})
```

### Client Events

```javascript
// İstemci başlatıldığında
ClientEvents.init(event => {
  console.log('[New World] Client ready')
})

// Ekran açıldığında
ClientEvents.screenOpen(event => {
  // UI işlemleri
})
```

## Özel Sistemler Örneği

### Araştırma Teknolojileri

```javascript
// kubejs/scripts/research_system.js

const ResearchTech = {
  'basic_analysis': {
    name: 'Temel Analiz',
    requires: [],
    unlocks: ['element_catalog']
  },
  'element_catalog': {
    name: 'Element Katalogu',
    requires: ['basic_analysis'],
    unlocks: ['advanced_analysis']
  }
}

global.researchTech = ResearchTech
```

### Craft Kuralları

```javascript
ServerEvents.recipes(event => {
  // Özel craft tarifleri
  event.shaped('minecraft:diamond', [
    'AAA',
    'ABA',
    'AAA'
  ], {
    A: 'minecraft:coal',
    B: 'minecraft:cobblestone'
  })
})
```

## Console Komutları

Oyun içinde KubeJS komutlarını test edin:

```
/kubejs reload         # Betikleri yeniden yükle
/kubejs errors         # Hataları göster
/kubejs open_folder    # KubeJS klasörünü aç
```

## Debugging

### Hata Alma
```
kubejs_scripts.log dosyası: .minecraft/logs/kubejs_scripts.log
```

### Console Logging
```javascript
console.log('[New World] Test message')
console.warn('[New World] Warning')
console.error('[New World] Error!')
```

## Kaynaklar

- [KubeJS Resmi Docs](https://kubejs.com/)
- [Event Referansı](https://kubejs.com/wiki/server-events/)
- [Recipe API](https://kubejs.com/wiki/recipes/)

---

**Not:** KubeJS 2000+ satırlık betikleri destekler. Modüler geliştirme için betikleri dosyalara ayırın.
