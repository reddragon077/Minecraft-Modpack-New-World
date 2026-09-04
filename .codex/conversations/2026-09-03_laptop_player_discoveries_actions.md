# 2026-09-03 — Laptop Player Discoveries actions acceptance

## Accepted build

- JAR: `NewWorldCore-1.21.1-NeoForge-0.5.66.1-alpha-player-discovery-actions.jar`
- SHA-256: `889900f7e2519b8e07e604431260e28e0b6f8932d3d087bc5b17f8551fda059c`
- Repo/live single-JAR and SHA-256 equality: passed.
- Compile, config, discovery, field-survey, analysis and Player Discoveries smoke suites: passed.

## Implemented behavior

- Selected Discovery details show relative `LAST SEEN` and `EST RESERVE` for geology records.
- `FAV` toggles the existing shared Discovery favorite bit and invalidates the Navigation favorite cache.
- `TARGET` sets the selected key in the existing per-ship Discovery state.
- `ROUTE` selects that same target, invokes `Navigation0472ServerRoute`, and prepares the first hop through the existing TARDIS autopilot bridge.
- The three actions can be disabled independently in `config/newworldcore/gui.properties`.
- Snapshot indexes are mapped to cached server-side Discovery keys so category filtering and pagination cannot act on a different record.

## Defect and repair

- The first `.66.0` candidate displayed the buttons but produced no server action logs.
- The legacy survey bridge reserves positive values `>=100` for client status updates, so `10000/11000/12000` C2S modes were swallowed before dispatch.
- `.66.1` moved the three 512-entry action ranges to negative values. A regression smoke test covers each boundary and confirms positive status code `100` is not an action.

## Runtime evidence

- Shared snapshot remained healthy at `synced=128 total=464 perCategory=64`.
- Archeologist Camp `minecraft:overworld|-2450|64|187`: favorite write passed, target write passed, route ready at distance 947 with range 262144 and one hop.
- Trial Chambers `minecraft:overworld|-2434|-9|150`: target and favorite writes passed; TARDIS state accepted dimension and position through two methods and two fields; one-hop route loaded at `-2434,62,150`.
- Trial Chambers travel completed with `Route complete at hop #1 exterior=-2434,63,150`.
- No Player Discoveries action, payload, target or route exception occurred. Startup SCGuns recipe and EMI/JEI ingredient errors remain unrelated third-party compatibility noise.

## Continuation

Roadmap Stage 8 is complete. Continue with Stage 9 Player Navigation: lightweight current-target, ship-distance, route/next-hop and WE-cost presentation, then favorite selection, save-location and send-to-ship actions. Keep advanced routing controls on the physical Navigation Terminal.
