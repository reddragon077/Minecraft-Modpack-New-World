# Replikasyon sistemi

Durum: KubeJS tabanlı alpha uygulama aktif

## Temel ilke

Replikasyon üç koşul ister:

1. Kaynağın tanımlanmış bilgisi
2. Uygun Matter türü ve miktarı
3. Gerekli enerji ve çalışan gemi altyapısı

## Aktif paket kuralları

- Normal eşyalar geçerli Matter değeri varsa parçalanabilir.
- Normal eşyalar Identification Chamber’da doğal kaynak gibi taranamaz.
- Yalnızca tanımlanan doğal kaynaklar taranabilir.
- Replication Terminal yalnız öğrenilmiş kaynakları üretir.
- Varsayılan Replication Matter tarifleri korunur.
- Ham madenlere New World’e özel Matter değerleri verilir.
- Ortak raw taglerinde bulunan tanımsız madenler için kontrollü fallback kullanılır.
- Normal Certus Quartz üretilebilir.
- Charged Certus Quartz taranamaz ve üretilemez.
- Flint taranamaz, üretilemez ve Matter’a parçalanamaz.

## Mineral aileleri

KubeJS betiği modlar arası ortak tagleri ve açık item kimliklerini aynı kaynak kategorisinde birleştirir. Lead, Nickel ve Uranium gibi kaynaklarda duplicate bilgi kaydı oluşmaması hedeflenir. Uraninite ayrı kategoridir.

## Mining bağlantısı

Mining Terminal, canlı Replication Chip Storage bilgisini ve daha önce öğrenilmiş kalıcı kaynak bilgisini birleştirir. Bir routing kuralı tek başına kaynağı “öğrenilmiş” yapmaz.

## Güvenlik

- Bilinmeyen kaynaklar terminalde maskelenir.
- Flint ve özel hazard hedefleri normal çıkarma/vein davranışından ayrılır.
- Kayıp yakınlık bağlantısı nedeniyle öğrenilmiş bilginin UNKNOWN’a dönmemesi için kalıcı recovery yolu bulunur.

## Sıradaki doğrulama

Jeolojik deposit etiketi, Radar/Navigation `DEPOSITS` kaydı ve Replication kategorisi aynı mineral ailesine bağlanmalıdır. Yeni modlu depositler eklenirken ilk kontrol budur.
