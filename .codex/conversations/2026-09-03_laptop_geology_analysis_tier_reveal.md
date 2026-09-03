# Laptop Geological Analysis tier reveal

- Date: 2026-09-03
- Machine: laptop
- Accepted build: `NewWorldCore-1.21.1-NeoForge-0.5.64.0-alpha-geology-tier-reveal.jar`
- SHA-256: `29cff3cce98bbcf762e2aeace7d06109f94eef99c483470bc766c6e0a44bb85b`

## Implemented

- Added per-result Geological Radar analysis metadata so a mastered record stays exact while unrelated results remain masked.
- Connected Radar Accuracy to anomaly, metallic, resource-rich and exact-family progression.
- Added one configurable `reveal.required_accuracy.*` key for each of the 21 current deposit families. Defaults reveal 3/10/18/21 cumulative families at Accuracy 0/I/II/III; an unknown future family defaults to III until explicitly configured.
- Preserved non-downgrading FIELD evidence. Geological Field Survey can reveal a family early and later Radar scans cannot reduce its source or analysis level.
- Wrapped the active `Navigation0561TrueSingleRadarUi.drawFilterPopup` path with the existing pre/post buffer flush and foreground Z layer. The earlier repair only covered the legacy SingleOwner path.
- Added the safe `tools/reset-geology-radar-analysis.ps1` maintenance command. It backs up the save, resets only non-zero `GEOLOGY/RADAR` analysis values, verifies a gzip/NBT round trip, and preserves FIELD records.

## Acceptance evidence

- Build, config, discovery schema-v3, Geological Field Survey and per-result analysis smoke tests passed.
- Bytecode inspection confirmed `drawFilterPopup0632Base`, the active wrapper, timed-scan wrapper and per-result packet wrappers.
- Repository and laptop each contained one NewWorldCore JAR with matching SHA-256. DoctorWhoMod remained single and hash-matched; repository/live discovery configs matched with 21 family entries.
- Accuracy 0/1/2/3 scans completed at result caps 24/32/40/48 in approximately 8.998/8.986/9.011/8.995 seconds.
- The user visually confirmed staged text and that filter-popup text no longer intersects result rows or coordinates.
- Final save audit found 113 Radar and one Field geology record: 47 Radar L3, 66 untouched Radar L0, and one preserved TIN Field L3. The 48 records covered by the final scan were therefore exact without downgrading the Field source.
- No relevant NewWorldCore exception occurred after the tests.

## Backups

- Repository `.63.1`: `backups/custom-mods/pre-0.5.64.0-20260903-144636/`.
- Laptop `.63.1`: `backups/custom-mods/pre-apply-20260903-144645/`.
- Pre-test discovery data: `backups/discovery-data/pre-analysis-reset-20260903-144653/`.

## Continuation

Discovery Analysis Stage 7 is complete. Continue with roadmap Stage 8: populate the Player GUI Discoveries tab using the existing shared database and event model.
