# 2026-09-01 — desktop — Campsite family acceptance

Status: `in progress`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.6-alpha-radar-route-cleanup.jar`

## Goal

Verify the Radar v2 `UNKNOWN` → on-site Field Survey → named modded family path for Explorify's campsite.

## Runtime evidence

- `/locate structure explorify:campsite` completed in 1098 ms and found the nearest real structure at `[-2512, ~, -992]`, 3037 blocks away.
- After teleporting to the site, Structure Field Survey identified five structure starts whose unique reported families were `ANCIENT CITY`, `SMALL DUNGEON`, `CAMPSITE`, and `FIREWELL`.
- `CAMPSITE` recognition therefore passed. The result contained no geology label.
- The current log contained zero `0471g arrival check` and zero `InvocationTargetException` entries.
- Teleport/chunk loading caused one 13.278-second `Can't keep up` warning; this is separate from the placement-only Radar scan and is not counted as a radar regression.

## Open acceptance

- Visually confirm that the `CAMPSITE` family is available in the Structure Radar filter list.
- Repeat the chain for `betterarcheology:archeologist_camp_grassy` and confirm `ARCHEOLOGIST CAMP` plus its dynamic filter.

## Next step

Open the Radar filter list and capture the `CAMPSITE` option. Then locate and survey the Better Archeology camp.
