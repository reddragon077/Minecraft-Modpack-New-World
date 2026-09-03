# 2026-09-03 — Laptop Player Discoveries core acceptance

## Accepted build

- JAR: `NewWorldCore-1.21.1-NeoForge-0.5.65.2-alpha-player-discoveries.jar`
- SHA-256: `95c9b44d5bbf54de7e8570a1e655de28e81572d057c8b7a55f9499630bce2b76`
- Repository and laptop each contained exactly one matching NewWorldCore JAR.

## Implemented

- Enabled the previously reserved Player Ship Interface `DISCOVERIES` tab.
- Reused the bidirectional Player Survey payload with reserved mode 3 and a guarded snapshot decoder.
- Added `ALL`, `STRUCTURES` and `GEOLOGY` filters, six-row pagination and selected-record details.
- Preserved newest-first ordering while assigning independent configurable sync quotas to Structure and Geology.
- Added Turkish guidance for per-category sync limit and rows-per-page settings in `gui.properties`.
- Replaced persisted ship-relative scan distance with live three-dimensional player distance; cross-dimension records show `DIFFERENT DIMENSION`.

## Runtime acceptance

- The initial 64-total snapshot exposed an empty Structure filter because recent Geology scans occupied the entire window.
- The corrected runtime repeatedly logged `synced=128 total=464 perCategory=64`.
- User screenshots confirmed the enabled tab style, populated Structure filter, list/detail layout and live player distance.
- After world/player runtime start, the log contained no Player Discoveries render, snapshot, payload-routing exception, ERROR or FATAL entry.
- Existing SCGuns recipe-schema and EMI/JEI ingredient errors occur during startup and are unrelated third-party compatibility noise.

## Next work

1. Show last-seen and estimated reserve in the detail panel.
2. Connect navigation target selection.
3. Add route and favorite actions, then close roadmap Stage 8.
