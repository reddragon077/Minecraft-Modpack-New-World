# New World current handoff

Updated: 2026-09-10
Machine: laptop
Branch: `main`

## Current objective

Stage 4 Overview `.67.1` is accepted for laptop single-player: user confirmed warning styling and all presented remaining checks. Proceed to Stage 5 Ship Link, then remaining Stage 9. Earlier pending statements below are superseded; no multiplayer or forced-disconnection runtime test is claimed.

## Installed .68.0 — Ship Link runtime acceptance pending

- Repo/laptop: `NewWorldCore-1.21.1-NeoForge-0.5.68.0-alpha-player-ship-link.jar`, SHA-256 `5637cae64a53e92140338b31d761f673f0a84c82f0fa2e8d37ceb457480c58b3`, 3625294 bytes. Seven smoke suites passed, embedded/source manifest matched, single-JAR/hash verified. No Java process was running during installation.
- GUI header on every tab shows CONNECTED / DIMENSIONAL / LOST, player-to-exterior 3D distance and ship dimension. Own interior is ON BOARD. Live `ship-link.properties` defaults: 5000 blocks, dimensional true, refresh 20 ticks, stale 120 ticks; Turkish guidance included. Server timing is communicated to the client.
- Server owner/range checks gate remote Survey/Discoveries reads and writes; delayed Survey rechecks at execution. Stale/reopened/changed-world client state is invalidated. Discovery selection keys are tied to the resolved ship; route engine receives that ship's interior context. No world schema change, chunk load, or cancellation of ongoing Mining/route work.
- Accepted `.67.1`, original repo/live JARs and prior config README are preserved at laptop `backups/custom-mods/pre-ship-link-20260910-01/`. Old backups remain. Scoped deployment changed only custom core JAR, new ship-link config and config README; Overview user values and other configs were preserved.
- Next: `docs/14_Ship_Link_Runtime_Kabul.md`. Start with own ship ON BOARD, walk outside for distance, then use temporary range=32 to test LOST/recovery. Restore 5000 afterward. Test dimensional toggle, delayed Survey and exterior Discovery FAV/TARGET/ROUTE. Do not mark Stage 5 accepted until runtime checks pass.

## Previous .67.1 — now runtime accepted and backed up

- Repo/laptop: `NewWorldCore-1.21.1-NeoForge-0.5.67.1-alpha-overview-warning-states.jar`, SHA-256 `0d08367a747efc6fa94d41270793b96de897ebb0059a2de5599c9c322badbac4`, 3613963 bytes. Six smoke suites passed; source/embedded manifest match; single-JAR/hash checks passed. Game was closed for install.
- Warnings are server-classified per entry: yellow `[ACTIVE]`, red `[CRITICAL]`, grey `[RESOLVED]`. Active entries precede resolved history, critical entries first. `show_resolved_warnings=true` is a live client display option; false hides resolved entries only. Existing three-entry bounded wire format remains unchanged.
- User-selected `refresh_ticks=20` is now canonical and installed. Temporary warning_percent=99 was restored to 20; critical_percent remains 5. No test-only energy setting remains.
- `.67.0` runtime evidence: screenshots/logs verified engine cooldown→READY, brake/shield changes, SCANNING/MINING/COMPLETED/NO_ENERGY, FE OUT/NET load changes and NOMINAL→CRITICAL→WARNING→NOMINAL. User verified GUI reopen and live config timing. No relevant Overview failure was found. These are partial Stage 4 checks, not full multiplayer/unknown/stale-data acceptance.
- At 10:50:58 FE=8315/2250000 with CRITICAL and NO_ENERGY; at 10:51:27 FE=348910 with WARNING; at 10:51:52 FE=1864528 with NOMINAL; by 10:52:21 FE was full and Mining waited for the disabled shield. Existing warnings staying yellow after recovery motivated this repair.
- `.67.0` and prior config backed up under laptop `backups/custom-mods/pre-overview-warning-states-20260910-01/`; older `.66.1` backups untouched. Only the custom JAR and Overview config/README were deployed.
- Acceptance update: user confirmed the warning fix and all presented remaining Overview checks; Stage 4 single-player acceptance recorded before Ship Link implementation. No need to repeat energy-drain tests.

## Previous .67.0 installation record (superseded; runtime follow-up above)

- Repo/laptop: `NewWorldCore-1.21.1-NeoForge-0.5.67.0-alpha-player-overview.jar`, SHA-256 `4273134984af8eeb8eece991c70b6a139b230c94cdd8519dd2badb1ae58f5d9e`, 3611837 bytes.
- All six smoke suites passed, including Overview long/UTF frames, malformed payloads, config limits/reload, withdrawal simulation exclusion, counter isolation/wraparound, and headless layout bounds. Embedded/source patch manifests match; bytecode hooks verified. No game launch or runtime acceptance was performed.
- Repo/laptop each contain exactly one matching NewWorldCore and unchanged DoctorWhoMod; no Java process was running at deployment.
- Accepted `.66.1` is preserved in laptop `backups/custom-mods/pre-overview-20260910-01/` and `D:\Projects\NewWorld-recovery-20260910-01\known-good-0.5.66.1/`.
- Added `overview.properties` (refresh 40 ticks, warning 20%, critical 5%, stale 120 ticks, two warning rows). Only that config and its README were copied; unrelated runtime configs were not overwritten by broad apply/refresh scripts.
- Overview is read-only: FE/WE, actual FE pool OUT and sampled NET, engine/brake, Mining/shield, Navigation, Matrix states, exterior position, health and session warning history. Unknown/stale/no-ship data is not displayed as healthy telemetry. Existing owner resolver is reused; continuous Ship Link is still Stage 5.
- Next: open Player GUI → OVERVIEW, compare physical terminal values, toggle existing brake/shield controls safely and inspect logs. Detailed acceptance: `docs/13_Overview_Runtime_Kabul.md`.
- Before development, 27 unexpected sync-copy/backup files and nine `(1)` Git metadata copies were moved with hash verification outside the repo to `D:\Projects\NewWorld-recovery-20260910-01/`. Nothing was deleted. Fetch and fast-forward pull then succeeded at `2caedef`; full details are in today's conversation record.

## Earlier accepted .66.1 continuation (historical)

- `.cursor/rules/canonical-roadmap-workflow.mdc` is the mandatory always-applied rule on both computers. It locks the current order to Stage 4, then Stage 5, then remaining Stage 9 work until the roadmap is changed by a verified commit.
- Last runtime-accepted build, now backed up: `NewWorldCore-1.21.1-NeoForge-0.5.66.1-alpha-player-discovery-actions.jar`.
- SHA-256: `889900f7e2519b8e07e604431260e28e0b6f8932d3d087bc5b17f8551fda059c`.
- Player `DISCOVERIES` now reads the shared database through reserved survey mode 3 and shows ALL/STRUCTURES/GEOLOGY filters, six-row pagination and selected-record source/analysis/resource/coordinates.
- Snapshot selection keeps the newest 64 Structure and 64 Geology records independently. Laptop runtime repeatedly logged `synced=128 total=464 perCategory=64`; no Discoveries render, snapshot or packet-routing error occurred.
- The detail panel calculates live 3D distance from the current player instead of displaying the stale ship-relative scan distance; another dimension shows `DIFFERENT DIMENSION`. User visual/functional acceptance passed.
- Detail also shows relative `LAST SEEN` and geology `EST RESERVE`. `FAV` toggles the shared favorite bit, `TARGET` selects the shared Navigation target, and `ROUTE` invokes the existing Navigation route/hop engine.
- Runtime acceptance passed twice: Archeologist Camp and Trial Chambers produced target/favorite writes and ready one-hop routes. Four TARDIS destination writes were applied and Trial Chambers logged `Route complete at hop #1`. No Player Discoveries action or route error occurred.
- `.66.0` is superseded: positive action modes were swallowed by the legacy `>=100` client-status branch. `.66.1` uses negative C2S ranges guarded by smoke tests.
- Accuracy 0/I/II/III now reveals deposit families cumulatively by config-defined thresholds. All 21 current families have individual `reveal.required_accuracy.*` entries; unknown future families default safely to III.
- Laptop acceptance completed all four Accuracy scans at 24/32/40/48 result caps in about nine seconds each. The user visually confirmed staged text and a clean foreground filter popup. Save audit found 47 Radar L3 records, one preserved Field L3 TIN, and 66 untouched Radar L0 records; no relevant NewWorldCore error occurred.
- Geological Field Survey is live through payload mode 1 and verifies actual loaded deposit-template blocks. Its range, delay, result cap, check budget, and match threshold are documented in `player.properties`.
- Laptop acceptance verified `TIN-RICH DEPOSIT` at `[-2696, 32, -728]` with 3/4 matching blocks in 4016 ms. Persisted NBT is `GEOLOGY/FIELD`, visited 1, analysis level 2, with first discovery preserved and last-seen advanced; no relevant error occurred.
- Prior `.61.0` is preserved under `backups/custom-mods/pre-apply-20260903-113644/` on the laptop.
- Schema v3 stores `analysisLevel` and `lastSeenAt`; first discovery, FIELD evidence, visited/favorite and higher analysis are preserved on repeats.
- The common event bus emits `DISCOVERED`, `SEEN`, and `ANALYSIS_UPGRADED` for future Research/Exploration XP listeners.
- Laptop runtime/NBT acceptance passed with 449/449 migrated records and no relevant error. Structure Radar returned 102, Geology 48, and Field Survey identified `TRIAL CHAMBERS` plus `ARCHEOLOGIST CAMP` in 4052 ms.
- Prior `.60.4` is preserved under `backups/custom-mods/pre-apply-20260903-101641/` on the laptop.

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
- `0.5.66.1` compile, action-range regression smoke, repository/live single-JAR and SHA-256 equality: **passed**.
- Laptop Player Discoveries last-seen/reserve presentation and FAV/TARGET/ROUTE server actions: **passed**.
- Archeologist Camp and Trial Chambers one-hop route preparation plus Trial Chambers route completion: **passed**.

## Next executable test

1. Stage 4 Overview single-player acceptance is complete.
2. Runtime-test installed Stage 5 Ship Link `.68.0`; do not rebuild before checking the current candidate.
3. Then return to Stage 9 for current target/route/hop/WE presentation, favorite selection, `SAVE CURRENT LOCATION`, and `SEND TO SHIP`. Discovery TARGET/ROUTE creation is already complete in `0.5.66.1` and must not be reimplemented.

## Do not assume

- Placement candidates are possible coordinates, not proof that a structure generated.
- Do not overwrite the known-good custom JAR backup or load two versions of either project-owned mod.

## Resume reading order

1. `.codex/project-memory.md`
2. This file
3. `.codex/conversations/INDEX.md`
4. `.codex/conversations/2026-09-07_canonical_roadmap_rule.md`
5. `.codex/conversations/2026-09-05_laptop_roadmap_reconciliation.md`
6. `.codex/conversations/2026-09-03_laptop_player_discoveries_actions.md`
7. `.codex/conversations/2026-09-03_laptop_player_discoveries.md`
8. `.codex/conversations/2026-09-02_laptop_geology_filter_layer_repair.md`
9. `.codex/conversations/2026-09-02_laptop_config_suite_build.md`
10. `.codex/LAPTOP_RESUME_PROMPT.md` when moving to the laptop
11. `docs/Known Issues.md`
