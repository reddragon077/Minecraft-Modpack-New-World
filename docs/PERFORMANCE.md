# New World - Performans Konfigürasyonu

## Önerilen Performans Ayarları

### Video Ayarları (options.txt)

```
graphicsMode:2
viewDistance:12
simulation_distance:8
maxFramerate:120
biomeBlendRadius:2
entityDistanceScaling:1.0
```

### JVM Argümanları (Başlangıç Ayarları)

```
-Xmx6G -Xms4G -XX:+UseG1GC -XX:MaxGCPauseMillis=130 -XX:+ParallelRefProcEnabled -XX:+UnlockExperimentalVMOptions -XX:G1NewCollectionHeuristicPercent=20 -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:SurvivorRatio=20 -XX:G1HeapRegionSize=16M -XX:MinMetaspaceExpansion=21M
```

### Embeddium Ayarları (embeddium.properties)

```
# Rendering Engine
use_async_path_rendering=true
async_chunk_rebuilds=true
enable_block_face_culling=true

# Shadows ve Lighting
use_block_shadow=true
enable_smart_cull=true
```

### FerriteCore Ayarları

Genellikle konfigürasyona gerek yok. Otomatik olarak optimize eder.

### Starlight Ayarları

```properties
# starlight.properties
enable_threaded_lighting=true
use_val_lite=true
```

## Modpack Sunucusu Ayarları

### server.properties

```properties
# Server başlangıç
server-port=25565
max-players=20
max-tick-time=60000
simulation-distance=8
view-distance=10

# Performans
network-compression-threshold=256
spawn-protection=0
pvp=true
```

## Bellek Yönetimi

Farklı sistem kapasitelerine göre:

| RAM | Xmx | Xms |
|-----|-----|-----|
| 4GB | -Xmx2G | -Xms1G |
| 8GB | -Xmx6G | -Xms4G |
| 16GB | -Xmx12G | -Xms8G |
| 32GB+ | -Xmx24G | -Xms16G |

## Performans İpuçları

1. **Chunk Loading**
   - View distance: 10-12 (render)
   - Simulation distance: 6-8 (oyun)

2. **Rendering**
   - Fancy Graphics yerine Fast kullanın
   - Clouds: OFF
   - Particles: Minimum
   - Smooth Lighting: OFF

3. **Mod Konfigürasyonu**
   - Değersiz animasyonları kapat
   - Parçacık efektlerini azalt
   - Sesler değiştirilmemiş

## Debugging Performans Sorunları

### Debug Screen (F3)

- **FPS:** 60+ ideal
- **Memory:** 50-80% kullanım normal
- **TPS:** 20 = tam hız

### Log Analizi

```bash
# Game log dosyasından hata ara
tail -f .minecraft/logs/latest.log | grep ERROR
```

## Optimizasyon Kontrol Listesi

- [ ] JVM argümanları ayarlandı
- [ ] Performans modları yüklendi
- [ ] Video ayarları optimize edildi
- [ ] Mod konfigurasyonları kontrol edildi
- [ ] FPS test edildi (hedef: 60+)
- [ ] Hata log'ları kontrol edildi

---

**Notlar:**
- Her bilgisayar farklı olduğundan ayarları kişileştirin
- Oyun esnasında deneme yanılma ile optimize edin
- Moderator kaç modun yüklü olduğuna bağlı olarak değişir
