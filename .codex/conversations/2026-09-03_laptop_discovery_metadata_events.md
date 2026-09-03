# 2026-09-03 — Laptop Discovery Database metadata and events

## Outcome

NewWorldCore `0.5.61.0-alpha-discovery-metadata-events` passed static, install, launch, scan, migration, persistence, and clean-log acceptance on the laptop. Roadmap Stage 2 is complete.

## Build and install

- JAR: `NewWorldCore-1.21.1-NeoForge-0.5.61.0-alpha-discovery-metadata-events.jar`
- SHA-256: `d8da6f2ae981183b5bcd2cdfcb5759dcb3eda5489c7ab2d08f117fbe3737e695`
- Size: `3570859` bytes
- The prior `.60.4` laptop JAR was preserved under `backups/custom-mods/pre-apply-20260903-101641/`.
- Repository and laptop each contain exactly one matching NewWorldCore JAR and eleven matching property files.

## Implemented model

- `NavigationDiscoverySavedData.Discovery` now contains persistent `analysisLevel` and `lastSeenAt` fields.
- Schema v2 records migrate to `NWDiscoverySchema=3`; first discovery time remains in `discoveredAt`.
- Repeat observations update `lastSeenAt` without replacing the first timestamp.
- Analysis never downgrades; FIELD evidence and visited/favorite state survive later RADAR observations.
- `NavigationDiscoveryEventBus` exposes synchronous `DISCOVERED`, `SEEN`, and `ANALYSIS_UPGRADED` events for future Research/Exploration XP listeners.
- `config/newworldcore/discovery.properties` controls Structure/Geology RADAR/FIELD starting analysis levels with Turkish guidance.

## Acceptance evidence

- Both automated config and schema-v3 merge/migration/event smoke tests passed during the build.
- The game loaded `.61.0` successfully.
- Structure Radar completed 102 placement tasks with 102 results.
- Geological Radar completed with 48 results.
- Field Survey queued at 48 blocks/80 ticks and completed once in 4052 ms, identifying `TRIAL CHAMBERS` and `ARCHEOLOGIST CAMP`.
- Saved NBT reported schema 3 and 449 discoveries; all 449 had both new fields. Distribution: 342 at level 0, 107 at level 1.
- `ARCHEOLOGIST CAMP`: first `307066`, last seen `397848`, `FIELD`, analysis 1, visited 1.
- No relevant NewWorldCore, discovery, Field Survey, reflection, verification, or invocation exception was present.

## Next work

Implement the Geological Field Survey path, then extend the analysis model into the anomaly → metallic → resource-rich → exact deposit-family progression.
