/**
 * New World - Araştırma Sistemi Başlangıç
 * 
 * Bu dosya temel araştırma sistemi mekanizmalarını tanımlar
 * Teknoloji ağacı, öğrenme kaynakları ve ödüller
 */

// Teknoloji ağacı veri tabanı
const ResearchTree = new Map()

// Tier 1: Keşif ve Temel Analiz
ResearchTree.set('basic_discovery', {
  name: 'Temel Keşif',
  tier: 1,
  description: 'Analiz yapabilmenin temeli',
  requires: [],
  rewards: {
    recipes: ['basic_analyzer', 'data_disk'],
    unlocks: ['element_analysis']
  }
})

ResearchTree.set('element_analysis', {
  name: 'Element Analizi',
  tier: 1,
  requires: ['basic_discovery'],
  rewards: {
    recipes: ['element_database']
  }
})

// Tier 2: Veri Depolaması
ResearchTree.set('data_storage', {
  name: 'Veri Depolaması',
  tier: 2,
  requires: ['element_analysis'],
  rewards: {
    recipes: ['data_storage_block', 'wireless_receiver']
  }
})

// Oyuncu verisi - Hangi teknolojileri öğrendiği
global.playerResearch = new Map()

/**
 * Oyuncu araştırması ekle
 * @param {string} playerUUID - Oyuncu UUID
 * @param {string} techId - Teknoloji ID
 */
function unlockResearch(playerUUID, techId) {
  if (!global.playerResearch.has(playerUUID)) {
    global.playerResearch.set(playerUUID, new Set())
  }
  global.playerResearch.get(playerUUID).add(techId)
  console.log(`[Research] ${playerUUID} unlocked ${techId}`)
}

/**
 * Oyuncunun teknolojiyi öğrenip öğrenmediğini kontrol et
 * @param {string} playerUUID - Oyuncu UUID
 * @param {string} techId - Teknoloji ID
 * @returns {boolean}
 */
function hasResearch(playerUUID, techId) {
  return global.playerResearch.has(playerUUID) && 
         global.playerResearch.get(playerUUID).has(techId)
}

// Event: Oyuncu belirli bir bloka tıkladığında araştırma açılabilir
ServerEvents.entityEvent(event => {
  if (event.type === 'player.tick') {
    // Oyuncu özel alanla etkileşim kontrol
  }
})

// Craft kuralları - Sadece öğrenilmiş teknolojiler için
ServerEvents.recipes(event => {
  // Örnekler eklenecek
  console.log('[Research] Recipe system initialized')
})

console.log('[New World] Research system loaded - ' + ResearchTree.size + ' techs available')

global.Research = {
  tree: ResearchTree,
  unlock: unlockResearch,
  has: hasResearch
}
