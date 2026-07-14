# 02_CORE_MECHANICS.md - Temel Mekanikler

## 🧬 1. Araştırma Sistemi (Research System)

### Konsept
Oyuncu hiçbir craft tarifi ile doğmaz. Her tarif öğrenilmelidir.

### Nasıl Çalışır?

#### Teknoloji Öğrenme Kaynakları
1. **Araştırma Laboratuvarı**
   - Elementi analiz et
   - 5-20 dakika zaman
   - Teknoloji açılır

2. **Veri Diskleri**
   - Eski yapılarda bul
   - KubeJS ile okut
   - Teknoloji açılır

3. **Keşif Görevleri**
   - Bölgeyi tamamen keşfet
   - Tüm elementleri bul
   - Teknoloji açılır

4. **Hafıza Kayıtları**
   - Eski yapılarda depolar
   - Hikâye kırıntıları
   - Teknoloji açılır

#### Teknoloji Ağacı
```
Temel Analiz
  ├─ Element Kataloğu
  │   └─ Advanced Analiz
  ├─ Veri Depolaması
  │   └─ Wireless Transfer
  └─ Production Chamber
      └─ Modüler Fabrika
```

### KubeJS İmplementasyonu
```javascript
const techTree = {
  'basic_analysis': {
    name: 'Temel Analiz',
    tier: 1,
    requires: [],
    learnableFrom: ['research_lab'],
    unlocks: ['element_catalog']
  }
}
```

---

## 💰 2. Bilgi Merkezli Kaynaklar

### Konsept
Kaynak = Bilgi, Data, Veri

### Geleneksel Model
```
Demir Cevheri
  ↓
Demir Eritmek
  ↓
Demir Depolamak
  ↓
Demir Kullanmak
```

### New World Model
```
Element Keşfi
  ↓
Element Analizi
  ↓
Bilgiyi Veritabanına Kaydetme
  ↓
Replicator ile Tekrar Üretme
```

### Replicator Sistemi
- AE2 entegrasyon
- Bilgi (Material Science) gerekir
- Her craft'te veri kullanımı
- Sürdürülebilir kaynak
- Sonsuz mültiplier? HAYIR

### KubeJS Kodu
```javascript
ServerEvents.recipes(event => {
  // Replicator tarifi
  // Gerekli: Bilgi + Energy + Pattern
  // Sonuç: Malzeme
})
```

---

## 🏭 3. Modüler Fabrika Sistemi

### Konsept
Oyuncu hazır makine craft etmez. Fabrika tasarlar.

### Production Chamber
```
Dış: Tek blok
  ↓
İçeri Gir → Boyut değiştir (daha büyük)
  ↓
Makine yerleştir, bağla, otomasyonu kur
  ↓
Çık
  ↓
Harici giriş/çıkış hatları ile kontrol
```

### Tasarım Unsurları
1. **Giriş Hatları** - Malzeme gelmesi
2. **İşleme Makineleri** - Craft işlemi
3. **Depo** - Ara ürünler
4. **Çıkış Hatları** - Nihai ürün
5. **Enerji Sistemi** - Güç dağıtımı
6. **Kontrol** - Red stone signals

### Örnek Fabrika
```
Demir Cevheri → Macerator → Demir Toz
                            ↓
                        Ocak
                            ↓
                        Demir Çubuk
                            ↓
                        Depo (Çıkış)
```

### Sınırlamalar
- Maksimum 16x16x16 alan (Production Chamber)
- Oyuncu sınırları tanır
- Yaratıcılık sınırlanmaz

---

## 🚀 4. Gemi Geliştirmesi (Ship Progression)

### Konsept
Ana üs = Uzay Gemisi. İlerledikçe bölümler açılır.

### Gemi Bölümleri (Sırayla Açılış)

| Bölüm | Sıra | Açıldığı Teknoloji | İşlev |
|-------|------|-------------------|-------|
| Reaktör Odası | 1 | Temel Enerji | Güç |
| Araştırma Lab | 2 | Araştırma | Element analizi |
| Üretim Odası | 3 | Production | Craft |
| Biyoloji Lab | 4 | Genetik | DNA geliştirmesi |
| Veri Merkezi | 5 | AE2 Ağ | Depolama |
| Portal Odası | 6 | Boyutlar | Seyahat |
| Otomasyon Odası | 7 | Create/MI | Mekanik |
| Komuta Merkezi | 8 | Oyun Sonu | Kontrol & Warp |

### Bölüm Açılışı
- Teknoloji açıl → Bölüm kilidini aç
- Oyuncu bölüme girebilir
- Bölümü equip etmeyi seç

### Oyuncu Hissi
"Gemi büyüyor! Daha güçlü hale geliyor!"

---

## 🧬 5. Genetik Sistem

### Konsept
İksir yerine DNA geliştirmesi.

### Nasıl Çalışır?

#### Buff Tipi → Gen Tipi
```
Buff                    Gene
Night Vision       →    Eye Enhancement
Haste              →    Muscle Optimization
Fire Resistance    →    Heat Resistance
Strength           →    Bone Reinforcement
Water Breathing    →    Lung Modification
```

#### Gen Yükseltilmesi
1. DNA örneği topla
2. Biyoloji laboratuvarında işle
3. Modifikasyon tasarla
4. Oyuncu DNA'sına uygula
5. Kalıcı yetenek kazanı

#### DNA Kaynakları
- Yaşlı oyuncu kıntıları (hikâye)
- Zararlılar
- Eski yapılar
- Araştırma

### Sınırlamalar
- Max 10 aktif gen
- Bazı genler birbiriyle uyumlu değil
- Kayda değer fiziksel değişiklikler
- Geri dönüşü yok (önemli karar)

---

## 🗺️ 6. Keşif Sistemi

### Konsept
Harita açılmaz. Oyuncu elle keşfeder.

### Mekanik
- Mini-harita yok
- Waypoint sistem opsiyonel
- Konum marker'ı oyuncu yerleştirir
- Bölge adları oyuncu açar

### Keşif Hedefleri
```
Bölge Keşfi
  ├─ Tüm alanları görüntüle
  ├─ Tüm elementleri bul
  ├─ Tüm yapıları keşfet
  └─ Teknoloji aç
```

### Oyuncu Motivasyonu
"Bu bölgede neler gizli olabilir?"

---

## ⚡ 7. Enerji Sistemi

### Kaynak: Create + Mekanism

#### Enerji Üretimi
1. **Başlangıç (Steam)**
   - Ocak + Boiler
   - Mekanik enerji

2. **Orta (Energy)**
   - Güneş panelleri
   - Rüzgar türbini
   - Mekanism elektrik

3. **İleri (Fusion)**
   - Nükleer enerji
   - Fusion reactor
   - Massive güç

#### Enerji Depolaması
- Battery blocks
- AE2 Power cells
- Capacitor banks

### Dengeleme
- Enerji fiyatı önemli
- Yapı kararları enerji temelli
- Verimlilik önemlidir

---

## 🔗 8. Veri Sistemi

### Applied Energistics 2 Entegrasyon

#### Veri Bileşenleri
- Channels (16'lı sistem)
- Grids (depolama ve craft)
- Drives (veri depolaması)

#### Teknoloji Veritabanı
- Her açılan teknoloji = veri
- Veri disklerde sakla
- Başka griddeki oyuncu ile paylaş

### Sistem Genişlemesi
- Wireless transfer (teknoloji)
- Spatial storage (büyük depo)
- Crafting optimization

---

## 🎯 Mekanik İlişkileri

```
Keşif
  ↓ (Element bul)
Araştırma
  ↓ (Teknoloji aç)
Craft Tarifi Aç
  ↓ (Üretim modu)
Fabrika Kur
  ↓ (Otomasyon)
Gemi Geliştirmesi
  ↓ (Yeni gezegen)
Yeni Keşif
```

### Mekaniklerin Bağı
Her mekanik oyunun diğer alanlarını destekler.

Örnek: Araştırma → Fabrika → Gemi → Keşif → Daha Fazla Araştırma

---

## ✨ Özet

**Temel Mekanikler:**
1. Araştırma - Oyuncuyu öğrenmesi gerektiğini bildirir
2. Bilgi Kaynağı - Sürdürülebilir ve tekrar kullanılabilir
3. Modüler Fabrika - Oyuncunun yaratıcılığı
4. Gemi Geliştirmesi - İlerleme hissi
5. Genetik - Kişisel gelişim
6. Keşif - Dünya meraklılığı
7. Enerji - Sistem constraint'i
8. Veri - Depolama ve paylaşım

**Birlikte:** Immersive bilim-kurgu oylanış deneyimi.
