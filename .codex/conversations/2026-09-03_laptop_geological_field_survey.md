# 2026-09-03 — laptop — Geological Field Survey acceptance

Status: implemented, installed, runtime accepted, and persisted

Build: `NewWorldCore-1.21.1-NeoForge-0.5.62.0-alpha-geological-field-survey.jar`

SHA-256: `253ba89166c0a6fc3e2cfb26d5a053e77a2075335702fafb0c714d997fe62bb5`

## Implementation

- Enabled the Player Survey geological card through the existing reserved payload mode 1 without changing the network schema.
- Added a delayed server-thread Geological Field Survey with per-player duplicate-request protection.
- Candidate coordinates use the same deterministic region/template math as physical deposit materialization.
- A Radar coordinate is not accepted as proof: the survey checks real block states in loaded chunks against the expected deposit template.
- Verified results enter the common Discovery Database/event path as `GEOLOGY/FIELD`, visited, and analysis level 2.
- Added Turkish-documented controls in `player.properties`: horizontal/vertical range, delay, result cap, block-check budget, and minimum match count.

## Static acceptance

- Config smoke, Discovery schema/event smoke, and Geological Field Survey smoke passed.
- Patched bytecode contains the mode dispatcher, client status handler, enabled GUI click wrapper, and all new survey classes.
- Repository and laptop each contain exactly one matching NewWorldCore JAR; eleven property files are installed.
- The previous `.61.0` laptop JAR is backed up under `backups/custom-mods/pre-apply-20260903-113644/`.

## Runtime acceptance

- `.62.0` loaded successfully.
- A survey away from physical evidence queued with `horizontal=48 vertical=128 delay=80t` and completed with 0 results in 4039 ms.
- A second survey verified `TIN-RICH DEPOSIT` at `[-2696, 32, -728]`, matching 3 of the first 4 checked template blocks, and completed with 1 result in 4016 ms.
- No related NewWorldCore, Geological Survey, reflection, dispatch, or crash error occurred.

## Persistence acceptance

- After clean world shutdown, the exact TIN record persisted as `newworldcore:tin_lode`, `Kind=GEOLOGY`, `Source=FIELD`, `Visited=1`, and `AnalysisLevel=2`.
- `LastSeenAt` advanced to the field observation while the original `discoveredAt` value remained unchanged.
- Resource metadata remained intact: primary Mekanism raw tin, copper/osmium secondary resources, raw iron byproduct, reserve 2350, radius 14, density 77, rarity COMMON.

## Next work

Implement the staged Geological analysis reveal: anomaly → metallic → resource-rich → exact deposit family, with Accuracy and Field Survey quality affecting the achievable level.
