# 2026-09-01 — desktop — Structure Radar acceptance

Status: `in progress`
Branch/starting commit: `main` / `2631582`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.4-alpha-full-placement-radar.jar` (`2530037eb38b25670a5d3d8019a04d7fc87e6ac106dc31b1a0ce90cecccee6de`)

## User goal

Verify that the corrected Structure Radar can expose more than four results, retain modded structures, and support visiting candidates before moving on to Field Survey validation.

## Starting state

- The current build was installed on the desktop but had not been launched there.
- The old four-record limitation and modded-family retention were changed in code but still lacked desktop in-game evidence.

## Observed evidence

- The game and existing world are running on NeoForge 1.21.1.
- The navigation monitor shows `Discoveries 101`, `Favorites 0`, and `Visited 6`.
- Many rows beyond the former four-record limit are visible and the user reports travelling to random candidates.
- Visible result classes include `VANILLA`, `EXPLORIFY`, `BETTERDUNGEONS`, `STRUCTORY`, and `UNKNOWN`.
- Visible named candidates include Bastion Chambers, Buried Treasure, Jungle Pyramid, Bastion Pyramid, and Skeleton Dungeon, followed by generic modded/unknown candidates awaiting confirmation.
- Evidence source: user-provided in-game screenshot and live test report. The temporary screenshot itself is intentionally not committed.

## Verification

### Passed

- Desktop game launch and existing-world load.
- More than four Structure Radar records are reachable; 101 discoveries are reported.
- Dynamic modded candidates are retained and displayed for multiple mod namespaces/classes.
- Navigation can route the user among radar candidates; six entries are reported visited.

### Failed

- No new failure was reported in this acceptance step.

### Not tested

- Whether the current scan reaches its terminal completed state without a late stall or long tick spike.
- Whether each placement candidate corresponds to a structure that actually generated in its biome.
- `UNKNOWN STRUCTURE` to exact family-name conversion through Structure Field Survey.
- Structure Field Survey isolation from all `GEOLOGY` discovery entries.
- Direct family checks for Campsite, Abandoned Camp, and Archeologist Camp.

## Next executable step

At the next real generated structure reached from an `UNKNOWN STRUCTURE` entry, use Structure Field Survey and record the before/after label, visited state, and whether any geology entry changed. Also note when the radar scan finishes and whether it caused any freeze.

## References

- `../HANDOFF.md`
- `../project-memory.md`
- `../../docs/Known Issues.md`
- `2026-09-01_desktop_sync_and_radar_handoff.md`
