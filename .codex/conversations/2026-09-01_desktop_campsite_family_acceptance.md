# 2026-09-01 — desktop — Campsite family acceptance

Status: `passed`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.6-alpha-radar-route-cleanup.jar`

## Goal

Verify the Radar v2 `UNKNOWN` → on-site Field Survey → named modded family path for Explorify's campsite.

## Runtime evidence

- `/locate structure explorify:campsite` completed in 1098 ms and found the nearest real structure at `[-2512, ~, -992]`, 3037 blocks away.
- After teleporting to the site, Structure Field Survey identified five structure starts whose unique reported families were `ANCIENT CITY`, `SMALL DUNGEON`, `CAMPSITE`, and `FIREWELL`.
- `CAMPSITE` recognition therefore passed. The result contained no geology label.
- The Structure Radar filter popup visibly listed `CAMPSITE` among 13 discovered types, closing the dynamic-family acceptance path.
- The current log contained zero `0471g arrival check` and zero `InvocationTargetException` entries.
- Teleport/chunk loading caused one 13.278-second `Can't keep up` warning; this is separate from the placement-only Radar scan and is not counted as a radar regression.

## Follow-up defect

- The filter screenshot also showed result-list and telemetry text drawn above the popup. That presentation defect is handled separately by the `0.5.59.7` overlay-layer repair.
- Repeat the family chain for `betterarcheology:archeologist_camp_grassy` and confirm `ARCHEOLOGIST CAMP` plus its dynamic filter.

## Next step

Accept the `0.5.59.7` filter-layer repair in game. Then locate and survey the Better Archeology camp.
