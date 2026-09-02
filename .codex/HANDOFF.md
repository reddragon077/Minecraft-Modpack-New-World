# New World current handoff

Updated: 2026-09-02
Machine: laptop
Branch: `main`

## Current objective

Begin Discovery Database analysis-level, last-seen, and common discovery-event work under the config-first rule. Radar v2 and the `.4` Player Field Survey closure are complete.

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
- Laptop `main` was clean and fast-forwarded from `63580ce` to desktop handoff commit `3a3f35e`.
- `tools/apply-to-instance.ps1` applied the repository to the registered laptop instance. It preserved the prior laptop `0.5.59.4` JAR under `backups\custom-mods\pre-apply-20260902-110953`.
- Laptop `0.5.59.10` runtime acceptance passed: `ALL` queued 102 placement-only tasks at 5000 blocks and returned 101 mixed-family results in about 5.6 seconds.
- Repository and laptop instance each contain exactly one `NewWorldCore-1.21.1-NeoForge-0.5.60.0-alpha-config-suite.jar`; both SHA-256 values are `8886c622e2ae962e3b7980283b9b5bc2dd796e65394c9d8a30b362b97955fb3b`.
- The prior laptop `0.5.59.10` JAR is preserved under `backups\custom-mods\pre-apply-20260902-133637`. DoctorWhoMod remained single and hash-matched; all eight NewWorldCore property files reached the live instance and Radar batch interval is `8`.
- `config/newworldcore/` now exposes live Radar/navigation, Mining, FE/Warp Matrix, engine travel, geology, replication, room-protection, and emergency-network settings. The shipped Radar profile is `scan.batch_interval_ticks=8`, approximately half the previous four-tick batch rate while preserving Speed-upgrade batching and FE accounting.
- Laptop `0.5.60.0` runtime scan acceptance passed: Radar `ALL` completed 102 tasks with 101 mixed results in about 9.98 seconds; Geology completed with 48 deposits in about 9.00 seconds.
- The populated Geology filter popup rendered below deposit rows, coordinates, the scrollbar, and yellow accent layer. `0.5.60.1` moved it to a dedicated `Z=1000` pose layer, passed static/install checks, but failed screenshot acceptance because Minecraft flushed deferred result buffers afterward.
- Installed candidate `NewWorldCore-1.21.1-NeoForge-0.5.60.2-alpha-config-geology-flush.jar` (SHA-256 `423a25739e14c7db644c3389e041ad23508c06af6f2d920b33c7effb5e77c158`) flushes earlier buffers before the popup and flushes the popup immediately as the final GUI layer. Compile, replacement, bytecode flush hooks, config smoke, pack-lock, generated mod-list, and repo/live hash checks passed.
- After Java stopped, apply-to-instance preserved `0.5.60.1` under `backups\custom-mods\pre-apply-20260902-144912` and installed `0.5.60.2`. Repo/live each contain one matching NewWorldCore JAR; DoctorWhoMod remains single/hash-matched, eight configs are present and identical, and Radar interval remains `8`.
- All config settings now include Turkish inline explanations for units, formulas, upgrade levels, change direction, and performance impact; numeric values were preserved.
- `0.5.60.2` runtime visual acceptance passed by screenshot: the full Geology filter popup stays above deposit rows, coordinates, scrollbar, and yellow accent layers. Debug log confirms `.1 -> .2` loaded; no relevant NewWorldCore GUI/config exception was found.
- The accepted config-suite, Turkish inline guidance, and Geology buffer-flush repair were committed as `344efc4` and pushed to GitHub `main`.
- `/locate structure betterarcheology:archeologist_camp_grassy` resolved `[-2448, ~, 192]`. The player Field Survey identified eight nearby structures including `ARCHEOLOGIST CAMP`, so family recognition passed.
- The old player survey checked 96 blocks by synchronously visiting 13x13/169 loaded chunk positions and returned in about 38 ms. Installed `0.5.60.4-alpha-config-first-gui-network` (SHA-256 `171b1cb6a0ca78c243ad81584f820d89fc32829e0269459048d549b462f390e9`) ships `48`-block and `80`-tick controls, checks 7x7/49 positions, dispatches the delayed scan back to the server thread, and prevents duplicate pending scans. The Player GUI now shows the live range/delay instead of the old hardcoded 96-block text.
- `gui.properties` exposes filter layer depth, Player GUI background dimming and Survey detail visibility. Network-node FE/item/fluid/gas transfer/capacity curves now have live multipliers in `network.properties`. The persistent `.cursor/rules/config-first-development.mdc` requires future adjustable features to ship config, Turkish guidance and smoke coverage.
- Apply-to-instance preserved `.3` under `backups\custom-mods\pre-apply-20260902-164534`. Repository and laptop each contain one matching `.4` JAR and ten `.properties` files.
- Laptop `.4` runtime acceptance passed: the log queued `range=48 blocks delay=80t`, completed exactly once in `4074ms`, and identified `TRIAL CHAMBERS, ARCHEOLOGIST CAMP` with no relevant NewWorldCore error. User screenshot visibly confirmed `ARCHEOLOGIST CAMP` in the clean foreground Structure Filters panel.
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
- Laptop fast-forward sync, stale-JAR backup, apply-to-instance, single-JAR counts, and repository/instance SHA-256 checks: **passed**.
- Laptop `0.5.59.10` launch and `ALL` mixed-family Radar acceptance: **passed** (102 tasks, 101 results, about 5.6 seconds).
- `0.5.60.0` compilation, config formula smoke, replacement counts, bytecode hooks, repository single-JAR/hash, pack-lock, and generated mod-list checks: **passed**.
- Laptop `0.5.60.0` installation, prior-JAR backup, single-JAR/hash equality, config count, and 8-tick live setting: **passed**.
- Laptop `0.5.60.0` launch, 8-tick Radar batch-pacing, mixed-family results, and Geology result acceptance: **passed**.
- `0.5.60.1` Geology Z-only filter visual acceptance: **failed** (deferred rows/accent still rendered above the popup).
- `0.5.60.2` Geology buffer-flush build, patch replacement, both flush hooks, config smoke, repository single-JAR/hash, pack-lock, and generated mod-list checks: **passed**.
- Laptop `0.5.60.2` installation, prior-JAR backup, repo/live single-JAR/hash, DoctorWhoMod hash, eight-config equality, and Radar pacing checks: **passed**.
- Laptop `0.5.60.2` loaded-version, clean-log, and Geology filter screenshot acceptance: **passed**.
- `ARCHEOLOGIST CAMP` in-place Field Survey family recognition: **passed** (one survey identified it among eight loaded structures).
- `0.5.60.4` build, config smoke, 48-block/3-chunk/80-tick formulas, live GUI label/dimming/layer hooks, network multiplier hooks, delayed executor, server-thread dispatch, pending lock, repository single-JAR/hash, and install checks: **passed**.
- Laptop `0.5.60.4` launch, four-second/48-block survey behavior, family recognition, clean log, and `ARCHEOLOGIST CAMP` dynamic-filter visibility: **passed**.

## Next executable test

1. Inspect the current `NavigationDiscoverySavedData.Discovery` schema and every record/update path.
2. Add config-backed analysis-level and last-seen behavior with save migration and smoke coverage.
3. Add one common discovery event emitted by both Structure and Geology record paths for later Research/Exploration XP listeners.

## Do not assume

- Placement candidates are possible coordinates, not proof that a structure generated.
- Do not overwrite the known-good custom JAR backup or load two versions of either project-owned mod.

## Resume reading order

1. `.codex/project-memory.md`
2. This file
3. `.codex/conversations/INDEX.md`
4. `.codex/conversations/2026-09-02_laptop_geology_filter_layer_repair.md`
5. `.codex/conversations/2026-09-02_laptop_config_suite_build.md`
6. `.codex/LAPTOP_RESUME_PROMPT.md` when moving to the laptop
7. `docs/Known Issues.md`
