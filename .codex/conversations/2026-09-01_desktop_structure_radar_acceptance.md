# 2026-09-01 — desktop — Structure Radar acceptance

Status: `superseded`
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
- The log records 106 placement tasks queued at `20:28:13.869` and 101 results completed at `20:28:19.032`, about 5.2 seconds later, without a scanner exception.
- Field Survey identified a real `ABANDONED CAMP` plus Village, Small Dungeon, Meteorite, and Spider Dungeon.

### Failed

- Shared placement sets appear as the selectable target `MODDED STRUCTURE`, although no registry structure or real place has that name.
- The selected example routed to placement candidate `X 256, Y -64, Z 256`; this is not proof that a structure generated there.
- Root cause is the multi-family fallback in `Navigation0581DynamicStructureScanner.PlacementTask.family()`: when `families.size()` is not one, it returns the literal `MODDED STRUCTURE`.
- Field Survey identified `COPPER SULFIDE DEPOSIT` as one of six structures in two consecutive log entries (`21:08:14.607` and `21:08:16.885`). This proves the runtime geology/structure isolation is still broken.

### Not tested

- Whether each placement candidate corresponds to a structure that actually generated in its biome.
- Direct family checks for Campsite, Abandoned Camp, and Archeologist Camp.

## Next executable step

The two failures in this record were patched in `0.5.59.5-alpha-radar-survey-isolation`. Continue with `2026-09-01_desktop_radar_survey_fix_build.md` and run its in-game acceptance steps.

## References

- `../HANDOFF.md`
- `../project-memory.md`
- `../../docs/Known Issues.md`
- `2026-09-01_desktop_sync_and_radar_handoff.md`
