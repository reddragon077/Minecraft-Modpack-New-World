# 2026-09-02 — desktop — Random-spread coordinate repair

Status: `runtime accepted`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.10-alpha-radar-random-spread.jar`

## Runtime evidence

- `0.5.59.9` logged one `CAMPSITE` placement task at the correct displayed range of 5000 blocks, but finished with zero raw candidates.
- The same zero occurred while the player was physically beside a known Campsite. Radar origin remains the ship exterior, but the known site is also inside the configured 5000-block ship radius.
- Therefore range binding and selected-task pruning worked; random-spread candidate enumeration did not.

## Root cause and repair

- The scanner converted origin chunks to random-spread region indexes and looped those indexes.
- It passed each region index directly to Minecraft's `getPotentialStructureChunk` method.
- Minecraft's method expects chunk-space coordinates and internally divides them by `spacing`; passing an already-divided index caused a second division and inspected the wrong regions.
- `0.5.59.10` passes `regionIndex * spacing`, so Minecraft performs the region division exactly once.

## Verification and installation

- Compilation and archive patching passed.
- Coordinate smoke passed for positive, negative, and zero regions (`7×32=224`, `-3×32=-96`, `0×32=0`).
- Bytecode confirms `regionSampleChunk` calls `Math.multiplyExact` before `getPotentialStructureChunk`.
- SHA-256: `876bc247597c0186bced715b05e843e9a85b00f32d3a9c81c990d6134fd63687`; size: `3556010` bytes.
- Repository and desktop instance each contain exactly one matching NewWorldCore JAR.
- Prior `0.5.59.9` is preserved under desktop `backups/custom-mods/pre-apply-20260902-021047`.

## Runtime acceptance

- The game loaded `0.5.59.10` successfully.
- At `02:28:35`, the runtime queued exactly one placement-only task at `range 5000 blocks` with `selected=CAMPSITE`.
- It finished with `1 results (1 before active filters; selected=CAMPSITE)`.
- The user visually confirmed that Campsite was found. The displayed-range, random-spread enumeration, and exact-family positive-result chain therefore passed.

## Next test

Run `ALL` once to verify mixed results and scan timing, then complete the `ARCHEOLOGIST CAMP` locate → Field Survey → dynamic-filter chain.
