// ============================================================
// NEW WORLD - REPLICATION HAM MADDE VE DOĞAL KAYNAK SİSTEMİ
// Minecraft 1.21.1 / NeoForge / KubeJS 2101.x
// ============================================================
//
// SİSTEM MANTIĞI:
//
// - Normal itemler geçerli Matter değeri varsa parçalanabilir.
// - Normal itemler Identification Chamber'da taranamaz.
// - Yalnızca belirlenen doğal kaynaklar taranabilir.
// - Replication Terminal'de yalnızca taranan kaynaklar üretilir.
// - Replication'ın varsayılan Matter tarifleri korunur.
// - Ham madenlere New World'e özel Matter değerleri verilir.
// - Temel jeolojik / boyutsal doğal kaynaklar da üretilebilir.
// - Ortak raw taglerinde bulunan tanımsız madenler 4 Metallic alır.
// - Normal Certus Quartz üretilebilir.
// - Charged Certus Quartz taranamaz ve üretilemez.
// - Flint taranamaz, üretilemez ve Matter'a parçalanamaz.
//
// ============================================================


const BuiltInRegistries = Java.loadClass(
    'net.minecraft.core.registries.BuiltInRegistries'
)


// ============================================================
// JAVA KOLEKSİYONLARI İÇİN GÜVENLİ ITERATOR
// ============================================================

function forEachJavaIterable(iterable, callback) {

    if (iterable == null) {
        return
    }

    const iterator = iterable.iterator()

    while (iterator.hasNext()) {
        callback(iterator.next())
    }
}


/**
 * Oyunda kayıtlı bütün item ID'lerini
 * JavaScript String olarak döndürür.
 */
function getRegisteredItemIds() {

    const registeredItems = new Set()

    forEachJavaIterable(
        BuiltInRegistries.ITEM.keySet(),
        resourceLocation => {

            registeredItems.add(
                String(resourceLocation)
            )
        }
    )

    return registeredItems
}


/**
 * Dinamik kategori tag'i.
 *
 * Açıkça tanımlanmayan fakat ortak taglerden bulunan
 * alternatif itemleri tutar.
 */
function getDynamicCategoryTag(categoryKey) {

    return 'newworld:replication_dynamic/' +
        categoryKey
}


// ============================================================
// MADEN KATEGORİLERİ VE MATTER DEĞERLERİ
// ============================================================

const NW_REPLICATION_CATEGORIES = {

    // --------------------------------------------------------
    // YAYGIN METALLER
    // --------------------------------------------------------

    copper: {
        tag: 'newworld:replication/copper',
        sourceTags: [
            'c:raw_materials/copper',
            'c:raw_ores/copper'
        ],
        items: [
            'minecraft:raw_copper'
        ],
        matter: {
            metallic: 3
        }
    },

    aluminum: {
        tag: 'newworld:replication/aluminum',
        sourceTags: [
            'c:raw_materials/aluminum',
            'c:raw_materials/aluminium',
            'c:raw_ores/aluminum',
            'c:raw_ores/aluminium'
        ],
        items: [
            'immersiveengineering:raw_aluminum'
        ],
        matter: {
            metallic: 3
        }
    },

    zinc: {
        tag: 'newworld:replication/zinc',
        sourceTags: [
            'c:raw_materials/zinc',
            'c:raw_ores/zinc'
        ],
        items: [
            'create:raw_zinc'
        ],
        matter: {
            metallic: 3
        }
    },

    tin: {
        tag: 'newworld:replication/tin',
        sourceTags: [
            'c:raw_materials/tin',
            'c:raw_ores/tin'
        ],
        items: [
            'mekanism:raw_tin'
        ],
        matter: {
            metallic: 3
        }
    },

    iron: {
        tag: 'newworld:replication/iron',
        sourceTags: [
            'c:raw_materials/iron',
            'c:raw_ores/iron'
        ],
        items: [
            'minecraft:raw_iron'
        ],
        matter: {
            metallic: 4
        }
    },

    lead: {
        tag: 'newworld:replication/lead',
        sourceTags: [
            'c:raw_materials/lead',
            'c:raw_ores/lead'
        ],
        items: [
            'mekanism:raw_lead',
            'immersiveengineering:raw_lead'
        ],
        matter: {
            metallic: 4
        }
    },

    nickel: {
        tag: 'newworld:replication/nickel',
        sourceTags: [
            'c:raw_materials/nickel',
            'c:raw_ores/nickel'
        ],
        items: [
            'immersiveengineering:raw_nickel',
            'oritech:raw_nickel'
        ],
        matter: {
            metallic: 5
        }
    },

    osmium: {
        tag: 'newworld:replication/osmium',
        sourceTags: [
            'c:raw_materials/osmium',
            'c:raw_ores/osmium'
        ],
        items: [
            'mekanism:raw_osmium'
        ],
        matter: {
            metallic: 6
        }
    },


    // --------------------------------------------------------
    // DEĞERLİ METALLER
    // --------------------------------------------------------

    silver: {
        tag: 'newworld:replication/silver',
        sourceTags: [
            'c:raw_materials/silver',
            'c:raw_ores/silver'
        ],
        items: [
            'immersiveengineering:raw_silver'
        ],
        matter: {
            metallic: 4,
            precious: 3
        }
    },

    gold: {
        tag: 'newworld:replication/gold',
        sourceTags: [
            'c:raw_materials/gold',
            'c:raw_ores/gold'
        ],
        items: [
            'minecraft:raw_gold'
        ],
        matter: {
            metallic: 4,
            precious: 5
        }
    },

    platinum: {
        tag: 'newworld:replication/platinum',
        sourceTags: [
            'c:raw_materials/platinum',
            'c:raw_ores/platinum'
        ],
        items: [
            'oritech:raw_platinum'
        ],
        matter: {
            metallic: 6,
            precious: 8
        }
    },


    // --------------------------------------------------------
    // RADYOAKTİF MADENLER
    // --------------------------------------------------------

    uranium: {
        tag: 'newworld:replication/uranium',
        sourceTags: [
            'c:raw_materials/uranium',
            'c:raw_ores/uranium'
        ],
        items: [
            'mekanism:raw_uranium',
            'immersiveengineering:raw_uranium'
        ],
        matter: {
            metallic: 4,
            quantum: 6
        }
    },

    uraninite: {
        tag: 'newworld:replication/uraninite',
        sourceTags: [
            'c:raw_materials/uraninite',
            'c:raw_ores/uraninite'
        ],
        items: [
            'powah:uraninite_raw'
        ],
        matter: {
            metallic: 3,
            quantum: 8
        }
    },


    // --------------------------------------------------------
    // VANILLA DOĞAL KAYNAKLAR
    // --------------------------------------------------------

    coal: {
        tag: 'newworld:replication/coal',
        sourceTags: [
            'c:gems/coal'
        ],
        items: [
            'minecraft:coal'
        ],
        matter: {
            organic: 1
        }
    },

    diamond: {
        tag: 'newworld:replication/diamond',
        sourceTags: [
            'c:gems/diamond'
        ],
        items: [
            'minecraft:diamond'
        ],
        matter: {
            organic: 8
        }
    },

    emerald: {
        tag: 'newworld:replication/emerald',
        sourceTags: [
            'c:gems/emerald'
        ],
        items: [
            'minecraft:emerald'
        ],
        matter: {
            precious: 10
        }
    },

    redstone: {
        tag: 'newworld:replication/redstone',
        sourceTags: [
            'c:dusts/redstone'
        ],
        items: [
            'minecraft:redstone'
        ],
        matter: {
            quantum: 3
        }
    },

    lapis: {
        tag: 'newworld:replication/lapis',
        sourceTags: [
            'c:gems/lapis'
        ],
        items: [
            'minecraft:lapis_lazuli'
        ],
        matter: {
            earth: 2,
            quantum: 2
        }
    },

    quartz: {
        tag: 'newworld:replication/quartz',
        sourceTags: [
            'c:gems/quartz'
        ],
        items: [
            'minecraft:quartz'
        ],
        matter: {
            nether: 4
        }
    },

    amethyst: {
        tag: 'newworld:replication/amethyst',
        sourceTags: [
            'c:gems/amethyst'
        ],
        items: [
            'minecraft:amethyst_shard'
        ],
        matter: {
            earth: 3,
            precious: 2
        }
    },

    ancient_debris: {
        tag: 'newworld:replication/ancient_debris',
        sourceTags: [],
        items: [
            'minecraft:ancient_debris'
        ],
        matter: {
            metallic: 8,
            nether: 8
        }
    },


    // --------------------------------------------------------
    // TEMEL JEOLOJİK / BOYUTSAL KAYNAKLAR
    // --------------------------------------------------------
    //
    // New World kuralı:
    // Ham veya doğal kaynak -> Replicator ✅
    // İşlenmiş / üretilmiş ürün -> Replicator ❌
    //
    // Flint özellikle burada YOKTUR.
    // Gravel işlenerek elde edilir.
    // --------------------------------------------------------

    sand: {
        tag: 'newworld:replication/sand',
        sourceTags: [],
        items: [
            'minecraft:sand'
        ],
        matter: {
            earth: 1
        }
    },

    red_sand: {
        tag: 'newworld:replication/red_sand',
        sourceTags: [],
        items: [
            'minecraft:red_sand'
        ],
        matter: {
            earth: 1
        }
    },

    gravel: {
        tag: 'newworld:replication/gravel',
        sourceTags: [],
        items: [
            'minecraft:gravel'
        ],
        matter: {
            earth: 1
        }
    },

    clay_ball: {
        tag: 'newworld:replication/clay_ball',
        sourceTags: [],
        items: [
            'minecraft:clay_ball'
        ],
        matter: {
            earth: 2
        }
    },

    dirt: {
        tag: 'newworld:replication/dirt',
        sourceTags: [],
        items: [
            'minecraft:dirt'
        ],
        matter: {
            earth: 1
        }
    },

    cobblestone: {
        tag: 'newworld:replication/cobblestone',
        sourceTags: [],
        items: [
            'minecraft:cobblestone'
        ],
        matter: {
            earth: 1
        }
    },

    cobbled_deepslate: {
        tag: 'newworld:replication/cobbled_deepslate',
        sourceTags: [],
        items: [
            'minecraft:cobbled_deepslate'
        ],
        matter: {
            earth: 2
        }
    },

    tuff: {
        tag: 'newworld:replication/tuff',
        sourceTags: [],
        items: [
            'minecraft:tuff'
        ],
        matter: {
            earth: 2
        }
    },

    calcite: {
        tag: 'newworld:replication/calcite',
        sourceTags: [],
        items: [
            'minecraft:calcite'
        ],
        matter: {
            earth: 2
        }
    },

    netherrack: {
        tag: 'newworld:replication/netherrack',
        sourceTags: [],
        items: [
            'minecraft:netherrack'
        ],
        matter: {
            nether: 2
        }
    },

    basalt: {
        tag: 'newworld:replication/basalt',
        sourceTags: [],
        items: [
            'minecraft:basalt'
        ],
        matter: {
            earth: 2,
            nether: 1
        }
    },

    blackstone: {
        tag: 'newworld:replication/blackstone',
        sourceTags: [],
        items: [
            'minecraft:blackstone'
        ],
        matter: {
            earth: 2,
            nether: 2
        }
    },

    soul_sand: {
        tag: 'newworld:replication/soul_sand',
        sourceTags: [],
        items: [
            'minecraft:soul_sand'
        ],
        matter: {
            earth: 1,
            nether: 3
        }
    },

    end_stone: {
        tag: 'newworld:replication/end_stone',
        sourceTags: [],
        items: [
            'minecraft:end_stone'
        ],
        matter: {
            earth: 2,
            ender: 3
        }
    },


    // --------------------------------------------------------
    // MODLU DOĞAL KAYNAKLAR
    // --------------------------------------------------------

    fluorite: {
        tag: 'newworld:replication/fluorite',
        sourceTags: [
            'c:gems/fluorite'
        ],
        items: [
            'mekanism:fluorite_gem'
        ],
        matter: {
            earth: 3,
            quantum: 2
        }
    },

    certus_quartz: {
        tag: 'newworld:replication/certus_quartz',
        sourceTags: [
            'c:gems/certus_quartz'
        ],
        items: [
            'ae2:certus_quartz_crystal'
        ],
        matter: {
            earth: 2,
            quantum: 4
        }
    },

    titanium: {
        tag: 'newworld:replication/titanium',
        sourceTags: [
            'c:raw_materials/titanium',
            'c:raw_ores/titanium'
        ],
        items: [
            'dwm:titanium_raw_item'
        ],
        matter: {
            metallic: 6
        }
    },

    sulfur: {
        tag: 'newworld:replication/sulfur',
        sourceTags: [
            'c:dusts/sulfur',
            'c:gems/sulfur',
            'c:raw_materials/sulfur',
            'c:raw_ores/sulfur'
        ],
        items: [],
        matter: {
            earth: 3
        }
    },

    niter: {
        tag: 'newworld:replication/niter',
        sourceTags: [
            'c:dusts/niter',
            'c:dusts/saltpeter',
            'c:gems/niter',
            'c:gems/saltpeter'
        ],
        items: [],
        matter: {
            earth: 3
        }
    }
}


// ============================================================
// ITEM TAGLERİ VE TARAMA WHITELIST
// ============================================================

ServerEvents.tags('item', event => {

    const registeredItems =
        getRegisteredItemIds()

    // Item ID -> kategori eşleşmesi.
    const assignments = {}

    // c:raw_materials / c:raw_ores içindeki bütün itemler.
    const genericRawItems = new Set()

    // Identification Chamber tarafından taranabilecek itemler.
    const allowedScanItems = new Set()


    function itemExists(itemId) {

        return registeredItems.has(
            String(itemId)
        )
    }


    /**
     * Verilen item tagindeki kayıtlı itemleri
     * targetSet içine aktarır.
     */
    function collectTag(tagId, targetSet) {

        try {

            forEachJavaIterable(
                event.get(tagId).getObjectIds(),
                resourceLocation => {

                    const itemId =
                        String(resourceLocation)

                    if (registeredItems.has(itemId)) {

                        targetSet.add(itemId)
                    }
                }
            )

        } catch (error) {

            console.warn(
                '[New World] Tag okunamadı: ' +
                tagId +
                ' | ' +
                error
            )
        }
    }


    // ========================================================
    // ESKİ NEW WORLD TAGLERİNİ TEMİZLE
    // ========================================================

    event.removeAll(
        'newworld:replicable_raw_resources'
    )

    event.removeAll(
        'newworld:replication/default_raw_metals'
    )

    Object.keys(NW_REPLICATION_CATEGORIES)
        .forEach(categoryKey => {

            const definition =
                NW_REPLICATION_CATEGORIES[categoryKey]

            event.removeAll(
                definition.tag
            )

            event.removeAll(
                getDynamicCategoryTag(categoryKey)
            )
        })


    // ========================================================
    // BÜTÜN ORTAK HAM MADENLERİ BUL
    // ========================================================

    collectTag(
        'c:raw_materials',
        genericRawItems
    )

    collectTag(
        'c:raw_ores',
        genericRawItems
    )


    // ========================================================
    // KATEGORİ EŞLEŞMELERİNİ OLUŞTUR
    // ========================================================

    Object.keys(NW_REPLICATION_CATEGORIES)
        .forEach(categoryKey => {

            const definition =
                NW_REPLICATION_CATEGORIES[categoryKey]


            // İlgili ortak taglerdeki itemleri bul.
            definition.sourceTags.forEach(sourceTag => {

                const discoveredItems = new Set()

                collectTag(
                    sourceTag,
                    discoveredItems
                )

                discoveredItems.forEach(itemId => {

                    assignments[itemId] =
                        categoryKey
                })
            })


            // Açıkça tanımlanan itemleri ekle.
            definition.items.forEach(rawItemId => {

                const itemId =
                    String(rawItemId)

                if (!itemExists(itemId)) {
                    return
                }

                assignments[itemId] =
                    categoryKey
            })
        })


    // ========================================================
    // KATEGORİ TAGLERİNİ VE TARAMA LİSTESİNİ OLUŞTUR
    // ========================================================

    Object.keys(assignments)
        .forEach(itemId => {

            const categoryKey =
                assignments[itemId]

            const definition =
                NW_REPLICATION_CATEGORIES[categoryKey]

            const explicitItems =
                new Set(
                    definition.items.map(
                        value => String(value)
                    )
                )


            // Bütün kategori itemleri.
            event.add(
                definition.tag,
                itemId
            )


            // Açıkça tanımlanmayan alternatif itemler.
            if (!explicitItems.has(itemId)) {

                event.add(
                    getDynamicCategoryTag(categoryKey),
                    itemId
                )
            }


            event.add(
                'newworld:replicable_raw_resources',
                itemId
            )

            allowedScanItems.add(itemId)
        })


    // ========================================================
    // TANIMLANMAMIŞ MODLU HAM MADENLER
    // ========================================================
    //
    // Ortak raw taglerinde bulunan fakat özel kategoriye
    // atanamayan ham madenler varsayılan olarak 4 Metallic alır.
    // ========================================================

    genericRawItems.forEach(itemId => {

        if (assignments[itemId] !== undefined) {
            return
        }

        event.add(
            'newworld:replication/default_raw_metals',
            itemId
        )

        event.add(
            'newworld:replicable_raw_resources',
            itemId
        )

        allowedScanItems.add(itemId)
    })


    // ========================================================
    // REPLICATION TARAMA SINIRLAMASI
    // ========================================================

    event.removeAll(
        'replication:cant_be_scanned'
    )


    // Yalnızca whitelist içindeki kaynaklar taranabilir.
    registeredItems.forEach(itemId => {

        if (itemId === 'minecraft:air') {
            return
        }

        if (allowedScanItems.has(itemId)) {
            return
        }

        event.add(
            'replication:cant_be_scanned',
            itemId
        )
    })


    // Whitelist kaynaklarını diğer yasak taglerinden çıkar.
    allowedScanItems.forEach(itemId => {

        event.remove(
            'replication:cant_be_scanned',
            itemId
        )

        event.remove(
            'replication:cant_be_disintegrated',
            itemId
        )

        event.remove(
            'replication:skip_calculation',
            itemId
        )
    })


    // Charged Certus Quartz taranamaz.
    if (itemExists('ae2:charged_certus_quartz_crystal')) {

        event.add(
            'replication:cant_be_scanned',
            'ae2:charged_certus_quartz_crystal'
        )
    }


    // Flint Replicator tarafında bir kaynak değildir.
    // Gravel -> Crusher / Crushing Wheels / Fortune gibi
    // işleme yolları korunur. Matter döngüsü oluşmaması için
    // Flint ayrıca disintegrate edilemez.
    if (itemExists('minecraft:flint')) {

        event.add(
            'replication:cant_be_scanned',
            'minecraft:flint'
        )

        event.add(
            'replication:cant_be_disintegrated',
            'minecraft:flint'
        )
    }


    // ========================================================
    // DEBUG LOG
    // ========================================================

    console.info(
        '[New World] Replication maden tarama whitelist yüklendi.'
    )

    console.info(
        '[New World] Kayıtlı toplam item: ' +
        registeredItems.size
    )

    console.info(
        '[New World] Ortak raw taglerinden bulunan item: ' +
        genericRawItems.size
    )

    console.info(
        '[New World] Özel kategoriye eşleşen item: ' +
        Object.keys(assignments).length
    )

    console.info(
        '[New World] Taranabilir toplam kaynak: ' +
        allowedScanItems.size
    )


    Object.keys(NW_REPLICATION_CATEGORIES)
        .forEach(categoryKey => {

            let count = 0

            Object.keys(assignments)
                .forEach(itemId => {

                    if (
                        assignments[itemId] === categoryKey
                    ) {

                        count++
                    }
                })

            console.info(
                '[New World] ' +
                categoryKey +
                ': ' +
                count
            )
        })


    console.info(
        '[New World] Normal itemler parçalanabilir, fakat taranamaz.'
    )

    console.info(
        '[New World] Charged Certus Quartz tarama dışında bırakıldı.'
    )

    console.info(
        '[New World] Flint tarama / üretim / disintegration dışında bırakıldı.'
    )
})


// ============================================================
// MADEN MATTER VALUE TARİFLERİ
// ============================================================

ServerEvents.recipes(event => {

    const registeredItems =
        getRegisteredItemIds()

    let directRecipeCount = 0
    let dynamicTagRecipeCount = 0


    // Replication'ın varsayılan Matter tarifleri silinmez.
    // Yalnızca eski New World tarifleri temizlenir.
    Object.keys(NW_REPLICATION_CATEGORIES)
        .forEach(categoryKey => {

            event.remove({
                id: 'newworld:replication/' +
                    categoryKey
            })

            event.remove({
                id: 'newworld:replication/extra/' +
                    categoryKey
            })
        })


    event.remove({
        id: 'newworld:replication/default_raw_metals'
    })


    // ========================================================
    // AÇIKÇA TANIMLANAN ITEMLER İÇİN DOĞRUDAN TARİFLER
    // ========================================================

    Object.keys(NW_REPLICATION_CATEGORIES)
        .forEach(categoryKey => {

            const definition =
                NW_REPLICATION_CATEGORIES[categoryKey]


            definition.items.forEach(rawItemId => {

                const itemId =
                    String(rawItemId)

                if (!registeredItems.has(itemId)) {
                    return
                }

                const recipePath =
                    itemId.replace(':', '/')

                event.remove({
                    id: 'newworld:replication/item/' +
                        recipePath
                })

                event.custom(
                    Replication.matterValueForItem(
                        itemId,
                        definition.matter
                    )
                ).id(
                    'newworld:replication/item/' +
                    recipePath
                )

                directRecipeCount++
            })
        })


    // ========================================================
    // ORTAK TAGLERDEN BULUNAN ALTERNATİF ITEMLER
    // ========================================================
    //
    // Örnek:
    //
    // - Farklı modların Raw Lead / Raw Nickel çeşitleri
    // - Aynı metalin alternatif ham cevher itemleri
    // - Sulfur / Niter gibi yalnız tag üzerinden bulunanlar
    //
    // Açıkça tanımlanan itemler bu taglere eklenmediği için
    // iki kez Matter değeri uygulanmaz.
    // ========================================================

    Object.keys(NW_REPLICATION_CATEGORIES)
        .forEach(categoryKey => {

            const definition =
                NW_REPLICATION_CATEGORIES[categoryKey]

            event.custom(
                Replication.matterValueForTag(
                    '#' +
                    getDynamicCategoryTag(categoryKey),
                    definition.matter
                )
            ).id(
                'newworld:replication/extra/' +
                categoryKey
            )

            dynamicTagRecipeCount++
        })


    // ========================================================
    // TANIMLANMAMIŞ MODLU HAM MADENLER
    // ========================================================
    //
    // Raw Eclipse Alloy
    // Raw Mithril
    // Raw Blazegold
    // Raw Replica
    // Raw Ferricore
    // Raw Anthralite
    // Raw Phosphor
    //
    // gibi özel kategoriye atanamayan raw itemler
    // varsayılan olarak 4 Metallic alır.
    // ========================================================

    event.custom(
        Replication.matterValueForTag(
            '#newworld:replication/default_raw_metals',
            {
                metallic: 4
            }
        )
    ).id(
        'newworld:replication/default_raw_metals'
    )


    // ========================================================
    // LOG
    // ========================================================

    console.info(
        '[New World] Doğrudan item Matter tarifleri: ' +
        directRecipeCount
    )

    console.info(
        '[New World] Dinamik kategori Matter tarifleri: ' +
        dynamicTagRecipeCount
    )

    console.info(
        '[New World] Tanımsız ham madenler: 4 Metallic.'
    )

    console.info(
        '[New World] Raw Iron: 4 Metallic.'
    )

    console.info(
        '[New World] Raw Copper: 3 Metallic.'
    )

    console.info(
        '[New World] Raw Gold: 4 Metallic + 5 Precious.'
    )

    console.info(
        '[New World] Certus Quartz: 2 Earth + 4 Quantum.'
    )

    console.info(
        '[New World] Temel doğal kaynaklar: Sand, Red Sand, Gravel, Clay Ball, Dirt, Cobblestone, Cobbled Deepslate, Tuff, Calcite, Netherrack, Basalt, Blackstone, Soul Sand, End Stone.'
    )
})