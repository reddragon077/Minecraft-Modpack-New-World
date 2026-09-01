# 2026-09-02 — desktop — Structure Radar displayed-range repair

Status: `installed; runtime acceptance pending`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.9-alpha-radar-range-filter.jar`

## Runtime evidence

- With only `CAMPSITE` selected, `0.5.59.8` logged `0 results (96 before active filters; selected=CAMPSITE)`.
- This proved exact exclusion worked, but the known campsite was about 3037 blocks from the ship while the UI displayed a 5000-block range.
- The placement scanner still used a fixed 100-chunk (~1600-block) cap inherited from the early performance-recovery implementation.
- Explorify's `data/explorify/worldgen/structure_set/campsites.json` contains a dedicated single-family random-spread placement for `explorify:campsite`, so family ambiguity was not the reason for zero.

## Repair

- `0.5.59.9` reads the server container's `NavigationUpgradeRuntime.scanRange` value instead of using a fixed constant.
- Random-spread and concentric-ring candidates both use the derived chunk radius and an exact circular block-distance bound.
- When dynamic families are selected, unrelated placement tasks are removed before the scan begins. A Campsite-only scan therefore runs one relevant placement task instead of all 102.

## Verification and installation

- Compilation and archive validation passed.
- Dynamic selection recovery and exact include/exclude smoke passed again.
- Bytecode contains `NavigationUpgradeRuntime.scanRange`, selected-task `removeIf`, `ScanContext.rangeChunks`, and `ScanContext.rangeSq` checks.
- SHA-256: `2e571f7c66c9d54ca89f030080480f1bd4c9e6d89546a4712e2a371a731695cd`; size: `3555811` bytes.
- Repository and desktop instance each contain exactly one matching NewWorldCore JAR.
- Prior `0.5.59.8` is preserved under desktop `backups/custom-mods/pre-apply-20260902-014959`.

## Next test

Select only `CAMPSITE` and scan from the current ship position. Confirm one placement task is queued at 5000 blocks and that the only result family is `CAMPSITE`. Then run `ALL` once to check mixed results and tick stability.
