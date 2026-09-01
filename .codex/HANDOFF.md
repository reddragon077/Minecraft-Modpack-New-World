# New World current handoff

Updated: 2026-09-02
Machine: desktop
Branch: `main`

## Current objective

Visually accept the `0.5.59.7` filter-overlay layer repair, then complete the remaining `ARCHEOLOGIST CAMP` family test.

## Exact state

- The GitHub repository was cloned to `E:\projects\Minecraft-Modpack-New-World`.
- The desktop CurseForge instance was registered at `C:\Users\suley\curseforge\minecraft\Instances\New World`.
- Repository config, defaultconfigs, KubeJS files, and the two current project-owned mod JARs were applied to that instance.
- The desktop NewWorldCore JAR is `NewWorldCore-1.21.1-NeoForge-0.5.59.7-alpha-radar-filter-layer.jar` and matches the repository SHA-256: `6f1fcec6139c55c2bf92145f31d3d5e5913fd0cc0333bc27d6f3a313a67ae9f6`.
- The desktop DoctorWhoMod JAR matches the repository SHA-256: `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- The older desktop custom build and previous applied directories were preserved under `backups\desktop-sync-pre-20260901-201500`.
- `tools/apply-to-instance.ps1` now moves stale NewWorldCore/DoctorWhoMod versions into a timestamped backup before installing the current pair.
- The desktop game and existing world load successfully. Structure Radar is actively finding vanilla and modded candidates.
- User-provided screenshot evidence shows `Discoveries 101`, `Favorites 0`, `Visited 6`, and many result rows. Visible modded classes include Explorify, Better Dungeons, and Structory.
- `0.5.59.5` runtime acceptance removed 17 invalid records and Field Survey identified five real structures without any geology deposit. The visible discovery list no longer contained `MODDED STRUCTURE`.
- The removed discovery was still selected, and cleanup set `selectedKey` to null; `NavigationDiscoverySavedData.selected()` calls `isBlank()` directly, producing repeated arrival-check exceptions.
- `0.5.59.6` uses the required empty-string sentinel when clearing that selection. The prior `0.5.59.5` desktop JAR is preserved under `backups\custom-mods\pre-apply-20260901-221120`.
- `0.5.59.6` runtime acceptance passed: the UI showed `NO TARGET SELECTED`; over 20 minutes of `latest.log` contained zero `0471g arrival check` and zero `InvocationTargetException` entries.
- The Deposits view still showed 28 genuine geology records, including two visible `COPPER-RICH DEPOSIT` rows. False-structure cleanup did not remove the real `GEOLOGY` data.
- `/locate structure explorify:campsite` resolved a real campsite at `[-2512, ~, -992]`; after travel, Structure Field Survey identified `CAMPSITE` alongside nearby families. Family recognition passed with zero arrival-check exceptions.
- User screenshot evidence confirmed `CAMPSITE` appears in the dynamic filter list (`FILTER: ALL // TYPES 13`, page `1/2`). This closes the Campsite family chain.
- The same screenshot exposed result-list and telemetry text drawing over the filter panel. `0.5.59.7` moves that panel to a dedicated foreground pose layer. Static pose-stack and bytecode tests passed, and the build is installed; runtime screenshot acceptance remains open.
- The prior `0.5.59.6` desktop JAR is preserved under `backups\custom-mods\pre-apply-20260902-002429`.

## Test status

- `0.5.59.4` desktop launch, >4 result list, modded-class retention, 101-result scan completion in about 5.2 seconds, and real `ABANDONED CAMP` recognition: **passed previously**.
- `0.5.59.4` shared-placement label and Field Survey isolation: **failed previously** (`MODDED STRUCTURE` and `COPPER SULFIDE DEPOSIT` appeared as structures).
- `0.5.59.5` game launch, invalid-record cleanup, absence of `MODDED STRUCTURE` from the list, and Field Survey structure/geology isolation: **passed**.
- `0.5.59.5` stale selected-target cleanup: **failed** (`InvocationTargetException` repeated every tick after cleanup).
- `0.5.59.6` compilation, JAR validation, metadata/version, non-null empty sentinel bytecode, SHA-256, install, and single-JAR/hash checks: **passed**.
- `0.5.59.6` game startup, stale-route cleanup, arrival-check stability, and genuine geology preservation: **passed**.
- `CAMPSITE` Field Survey recognition and dynamic-filter visibility: **passed**.
- `0.5.59.7` compile, archive, overlay wrapper bytecode, foreground pose-stack smoke, install, and single-JAR/hash checks: **passed**.
- `0.5.59.7` in-game filter-overlay visual acceptance: **pending**.

## Next executable test

1. Launch `0.5.59.7`, open the Structure Radar filter list over populated results, and confirm no result/telemetry text overlaps the panel.
2. Locate `betterarcheology:archeologist_camp_grassy`, travel there, and run Structure Field Survey in range.
3. Confirm `ARCHEOLOGIST CAMP` is recorded and its dynamic filter appears.
4. Then begin Discovery Database analysis-level/last-seen/event work.

## Do not assume

- Placement candidates are possible coordinates, not proof that a structure generated.
- Do not overwrite the known-good custom JAR backup or load two versions of either project-owned mod.

## Resume reading order

1. `.codex/project-memory.md`
2. This file
3. `.codex/conversations/INDEX.md`
4. `.codex/conversations/2026-09-02_desktop_radar_filter_overlay_repair.md`
5. `docs/Known Issues.md`
