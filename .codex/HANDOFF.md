# New World current handoff

Updated: 2026-09-02
Machine: desktop
Branch: `main`

## Current objective

Verify one `ALL` mixed-family scan, then complete the remaining `ARCHEOLOGIST CAMP` family test.

## Exact state

- The GitHub repository was cloned to `E:\projects\Minecraft-Modpack-New-World`.
- The desktop CurseForge instance was registered at `C:\Users\suley\curseforge\minecraft\Instances\New World`.
- Repository config, defaultconfigs, KubeJS files, and the two current project-owned mod JARs were applied to that instance.
- The desktop NewWorldCore JAR is `NewWorldCore-1.21.1-NeoForge-0.5.59.10-alpha-radar-random-spread.jar` and matches the repository SHA-256: `876bc247597c0186bced715b05e843e9a85b00f32d3a9c81c990d6134fd63687`.
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
- The same screenshot exposed result-list and telemetry text drawing over the filter panel. `0.5.59.7` moved that panel to a dedicated foreground pose layer; follow-up screenshots showed the popup contents unobstructed, so visual acceptance passed.
- With only `CAMPSITE` selected, a new scan still displayed 96 mixed-family results. `latest.log` confirmed `96 results (96 before active filters)`. The placement-only finish path bypassed the dynamic selected-family set and treated non-vanilla labels as always allowed.
- `0.5.59.8` reads the server-side dynamic selection and requires exact normalized family-label membership after the legacy mask. A smoke test proved `CAMPSITE` passes, `UNKNOWN STRUCTURE` / `BURIED TREASURE` / `SMALL DUNGEON` fail, and empty selection (`ALL`) preserves every label.
- `0.5.59.8` runtime log showed `0 results (96 before active filters; selected=CAMPSITE)`, proving non-selected rows were removed. However, the GUI displayed 5000 blocks while the placement scanner searched only 100 chunks (~1600 blocks), excluding the known campsite about 3037 blocks away.
- Explorify's `campsites.json` confirms the family has a dedicated single-entry random-spread placement, so a matching candidate should be calculable inside the displayed range.
- `0.5.59.9` obtains the actual range from `NavigationUpgradeRuntime.scanRange`, enforces its circular bound, and reduces selected `CAMPSITE` scans from all 102 placement tasks to the single matching task.
- Runtime logged one Campsite task at 5000 blocks but zero raw candidates. The scanner passed already-divided region indexes into Minecraft's chunk-space random-spread method, causing a second spacing division.
- `0.5.59.10` passes `regionIndex * spacing`; coordinate smoke and bytecode verification passed. Prior `0.5.59.9` is preserved under `backups\custom-mods\pre-apply-20260902-021047`.
- `0.5.59.10` runtime acceptance passed: at `02:28:35`, the log queued one placement-only task at 5000 blocks with `selected=CAMPSITE` and finished with `1 results (1 before active filters)`. The user visually confirmed Campsite was found.
- The prior `0.5.59.8` desktop JAR is preserved under `backups\custom-mods\pre-apply-20260902-014959`.
- The prior `0.5.59.7` desktop JAR is preserved under `backups\custom-mods\pre-apply-20260902-011708`.
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
- `0.5.59.7` in-game filter-overlay visual acceptance: **passed**.
- `0.5.59.7` exact dynamic-family scan filtering: **failed** (selected `CAMPSITE`, received 96 mixed results).
- `0.5.59.8` compile, archive, selected-set recovery, exact include/exclude smoke, install, and single-JAR/hash checks: **passed**.
- `0.5.59.8` in-game non-selected-family exclusion: **passed** (96 raw candidates reduced to zero with `selected=CAMPSITE`).
- `0.5.59.8` displayed 5000-block range: **failed** (hidden 100-chunk/~1600-block calculation cap).
- `0.5.59.9` compile, archive, dynamic-filter smoke, actual-range bytecode, selected-task pruning bytecode, install, and single-JAR/hash checks: **passed**.
- `0.5.59.9` in-game positive `CAMPSITE` range/filter result: **failed** (5000 bound correctly, but random-spread region coordinates were divided twice).
- `0.5.59.10` compile, coordinate smoke, bytecode, install, and single-JAR/hash checks: **passed**.
- `0.5.59.10` in-game positive `CAMPSITE` result and real 5000-block range: **passed**.

## Next executable test

1. Select `ALL`, scan again, and confirm mixed-family results return without unacceptable tick spikes.
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
4. `.codex/conversations/2026-09-02_desktop_random_spread_coordinate_repair.md`
5. `.codex/LAPTOP_RESUME_PROMPT.md` when moving to the laptop
5. `docs/Known Issues.md`
