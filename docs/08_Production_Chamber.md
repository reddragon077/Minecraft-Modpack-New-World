# Production Chamber

Durum: oda ve entegrasyon prototipi; nihai üretim kataloğu tamamlanmadı

## Amaç

Production Chamber, tek blokluk sihirli makine yerine gemiye fiziksel olarak yerleştirilen uzmanlaşmış üretim odasıdır. Oda kontrolcüsü, yapı bileşenleri, enerji/ağ bağlantıları ve araştırma aşaması birlikte geçerlilik oluşturur.

## Mevcut altyapı

- NewWorldCore oda kontrolcüleri ve kapalı oda/matrix modeli
- Gemi FE/eşya/sıvı/gaz ağı
- Custom Machinery ve Ars/Create/EMI/Mekanism/PneumaticCraft entegrasyonları
- KubeJS ile tarif ve bilgi kapısı düzenleme imkânı
- Building Gadgets/WorldEdit şemalarıyla modüler oda üretimi

## Hedef oda modeli

1. Oyuncu ilgili teknolojiyi araştırır.
2. Standart bağlantı ölçülerine uygun oda kurar.
3. Controller gerekli modülleri ve sınırları tarar.
4. Oda gemi ağına bağlanır.
5. Üretim yalnız geçerli yapı, yeterli enerji ve doğru girdilerle çalışır.

## Mk gelişimi

- **Mk-I:** tek işlem ailesi, sınırlı hız ve basit I/O
- **Mk-II:** daha iyi enerji verimi, paralel işlem veya ek sıvı/gaz kanalları
- **Mk-III:** ileri malzemeler, akıllı yönlendirme ve gemi telemetrisi

## Tasarım sınırları

- Oda dekoratif kabuktan ibaret olmamalıdır.
- Her mod için ayrı oda açmak yerine üretim işlevi esas alınmalıdır.
- Oda söküldüğünde kalıcı verinin ve ağ bağlantılarının güvenli kapanması gerekir.
- Nihai tarifler araştırma sistemi tamamlanmadan “bitmiş” kabul edilmez.

## Sıradaki iş

Jeoloji/replikasyon zinciri doğrulandıktan sonra ilk tamamlanacak oda için girişler, çıktılar, araştırma koşulu ve test şeması sabitlenecektir.
