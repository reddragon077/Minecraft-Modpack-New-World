# New World project memory

Last synchronized: 2026-09-10

## Canonical workflow

- GitHub repository: `https://github.com/reddragon077/Minecraft-Modpack-New-World`
- GitHub `main` is the canonical shared source for development across computers.
- `.cursor/rules/canonical-roadmap-workflow.mdc` is an always-applied cross-computer project rule. It requires clean fast-forward synchronization, evidence-backed roadmap state, numeric stage order, record reconciliation before new work when conflicts exist, and clean `main == origin/main` handoff.
- CurseForge instance directories are local runtime and test endpoints.
- Registered machines: `laptop` and `desktop`; paths are stored under `machines/`.
- Desktop repository root: `E:\projects\Minecraft-Modpack-New-World`.
- Desktop CurseForge instance: `C:\Users\suley\curseforge\minecraft\Instances\New World`.

## Platform

- Minecraft 1.21.1
- NeoForge 21.1.235
- Third-party addons are pinned in `manifest.json` and audited in `pack-lock.json`.
- Only the two project-owned fork JARs are stored directly in Git: NewWorldCore and DoctorWhoMod.

## NewWorldCore continuation point

- User chose shared Structure Survey `field_survey.delay_ticks=20` (1 second), superseding older 80-tick records. Repository now matches the existing laptop value; Geological Survey delay unchanged. Ship Link and Overview config values also match. This live-config update requires no additional JAR.

- Current gate (supersedes the historical continuation below): `.68.1` failed fresh-session runtime testing, permanently SYNCING even beside the TARDIS and after reopening the GUI. Stage 5 acceptance is reopened; Stage 9 is deferred.
- Installed repo/laptop candidate `.68.2`: `NewWorldCore-1.21.1-NeoForge-0.5.68.2-alpha-typed-client-link.jar`, SHA-256 `2ffb61b41cc4b3f99f820c103006d9015da7695184ac62e49f62b4224cbd88c8`, 3625901 bytes. Seven suites passed including generated JVM getter collisions in both orders; source/embedded manifest and installed hashes match. Runtime acceptance is pending.
- Cause proven by live Render-thread diagnosis: mixed environment has both `Minecraft.getConnection():Connection` (null) and `getConnection():ClientPacketListener` (live). Name-only reflection chose the wrong descriptor, so Overview/Ship Link sent no request. Both now select the exact listener return type, retaining null/missing fail-closed behavior and `.68.1` reason messages. No config or world changes.
- `.68.1` and original repo/live copies backed up before replacement at `backups/custom-mods/pre-typed-client-link-20260910-01/`; `.68.0` and older known-good backups preserved. Game was closed for installation. Next: fresh-session CONNECTED/Overview/Discoveries, GUI reopen, OUT OF RANGE/recovery; do not mark runtime success from smoke tests.

### Earlier continuation records (superseded where they name the installed build or next gate)

- Earlier 2026-09-10 acceptance: user verified `.67.1` warning states and all presented remaining Overview checks (terminal FE/WE, travel/route/cooldown, exterior coordinates, world re-entry). Stage 4 is accepted for laptop single-player. This is user-reported runtime evidence, not an independently exercised multiplayer/disconnection test. Accepted `.67.1` SHA-256: `0d08367a747efc6fa94d41270793b96de897ebb0059a2de5599c9c322badbac4`; it is now backed up, not the installed build.

- Current continuation: core Stage 5 single-player runtime acceptance passed on `.68.0` (ON BOARD, far LOST, nearby recovery, dimensional policy toggle, Discovery recovery and same-ship remote FAV/TARGET/ROUTE). Delayed Structure Survey denial was checked by log timeline + source early return, not NBT comparison. Multiplayer, artificial timeout and separate Geological delayed cancellation remain unverified.

- Installed repo/laptop candidate: `NewWorldCore-1.21.1-NeoForge-0.5.68.1-alpha-ship-link-reasons.jar`, SHA-256 `f4626881b44447184bf6003f81fb8586624c129929ecec0058cde9ae38a2db8c`, 3625579 bytes. Seven smoke suites passed; embedded/source manifest and single-JAR/hash checks passed. Overview preserves the real server link-loss reason without exposing stale telemetry. Its visual runtime check is pending, then proceed to remaining Stage 9; no new Navigation feature was implemented in this repair.

- `.68.0` SHA-256 `5637cae64a53e92140338b31d761f673f0a84c82f0fa2e8d37ceb457480c58b3` is preserved with original repo/live copies under `backups/custom-mods/pre-ship-link-reasons-20260910-01/`. Game was closed during replacement. Configs unchanged: range=5000, dimensional=true, refresh=20, stale=120, Structure Survey delay=80. Test-config backup: `backups/config-tests/ship-link-restore-20260910-144210/`.

- Ship Link uses the existing loaded-owner resolver with own-interior preference and an explicit owner recheck. On board CONNECTED; same exterior dimension uses 3D range; permitted cross-dimension is DIMENSIONAL; unavailable/out-of-range/disabled cross-dimension is LOST. Config `ship-link.properties`: range 5000, dimensional true, refresh 20, stale 120. Server-side gates and delayed-survey completion checks prevent stale client actions; no chunk loads or ongoing Mining/route cancellation. Old selection keys are ship-bound, and exterior ROUTE uses the linked interior context.
- Accepted `.67.1` is preserved under `backups/custom-mods/pre-ship-link-20260910-01/` (hash above). Older `.67.0`/`.66.1` backups remain untouched. Overview ACTIVE/CRITICAL/RESOLVED behavior and refresh=20, warning=20, critical=5 are preserved. Next: the short `.68.1` reason/recovery visual check in `docs/14_Ship_Link_Runtime_Kabul.md`.
- Overview reuses owner resolution and shared telemetry; `overview.properties` configures refresh/stale times, health thresholds and warning rows. OUT observes actual non-simulated FE-pool withdrawals; NET measures stored-energy change. No control actions or persistent warning schema were added.
- Earlier accepted laptop build: `NewWorldCore-1.21.1-NeoForge-0.5.66.1-alpha-player-discovery-actions.jar` (SHA-256 `889900f7e2519b8e07e604431260e28e0b6f8932d3d087bc5b17f8551fda059c`). Preserved under `backups/custom-mods/pre-overview-20260910-01/` and external repo recovery. Player Discoveries filters/list/details/pagination, category-balanced sync, live player distance, last-seen/reserve, favorite, Navigation target and real route/hop integration passed.
- Desktop acceptance evidence shows more than four reachable results and modded classes including Explorify, Better Dungeons, and Structory. The old four-record limit is fixed, shared placement sets now remain `UNKNOWN STRUCTURE`, and the false `MODDED STRUCTURE` selection was removed safely.
- The verified `0.5.57.0` baseline is backed up under the laptop instance's `backups/custom-mods/known-good-0.5.57.0/` directory.
- The complete historical source tree is still missing. The reproducible compatibility delta is stored under `src-patches/newworldcore/` and built with `tools/build-newworldcore-geology-patch.ps1`.
- `0.5.56.0-alpha-radar-navigation-mining-recovery` was previously described as a known-good recovery baseline.
- Verify binaries and local source before selecting a development baseline; old chat labels are context, not proof.
- Preserve physical geology world generation, radar coordinates, deposit persistence, and navigation `DEPOSITS` integration.

## Geology resource rules

- One deposit family per real resource, not one duplicate per mod.
- Current `0.5.60.2` definitions: vanilla families plus Osmium, Tin, Lead, Uranium, Fluorite, Aluminum, Nickel, Silver, Zinc, Platinum, Uraninite, and Certus Quartz.
- Immersive Engineering supplies Aluminum and Silver; Nickel is shared with Oritech, while Lead and Uranium reuse their existing families.
- Create supplies Zinc; Oritech supplies Platinum; Powah Uraninite remains separate from Uranium.
- Applied Energistics 2 uses normal Certus Quartz only; Charged Certus is explicitly excluded.
- Lead is shared by Mekanism and Immersive Engineering.
- Nickel is shared by Immersive Engineering and Oritech.
- Uranium is shared where mods represent the same resource.
- The 0.5.59.4 JAR passed compile, archive, binary entry-point, definition/template, SHA-256, desktop launch/world-load, result-list, modded-candidate, and scan-completion acceptance. The desktop log shows 106 placement tasks completed with 101 results in about 5.2 seconds.
- Structure Radar evolution: `0.5.59.0` namespace batches caused 40+ second ticks; `0.5.59.1` exact locate calls still caused 2–8 second spikes; `0.5.59.2` stalled at 128 results; `0.5.59.3` completed but its legacy fixed-label finish filter removed modded families and its vanilla locate tiles caused 8–13 second spikes. `0.5.59.4` uses placement math for both vanilla and modded structures, preserves dynamic family labels, completes every task, and retains the nearest 128 results. Placement candidates may be false positives until Field Survey confirms a generated structure.
- Confirmed `0.5.59.4` label defect: a placement shared by multiple structure families falls back to `MODDED STRUCTURE` in `Navigation0581DynamicStructureScanner.PlacementTask.family()`. That is not a registry structure or a discoverable place. Multi-family candidates must remain `UNKNOWN STRUCTURE` (or otherwise explicitly possible/unconfirmed) until Field Survey identifies an actual generated structure.
- Player Structure Field Survey reads actual nearby structure starts. Desktop `0.5.59.5` acceptance identified exactly five real structures (`VILLAGE`, `ABANDONED CAMP`, `SMALL DUNGEON`, `METEORITE`, `SPIDER DUNGEON`) and no geology deposit; structure/geology isolation passed.
- `0.5.59.5` removed 17 invalid persisted structure records and eliminated `MODDED STRUCTURE` from the discovery list. Its cleanup wrote a null selected-key sentinel, causing `Navigation0471gFix.checkArrivals` to emit `InvocationTargetException` every tick when the removed record had been selected. `0.5.59.6` resets it to the required empty-string sentinel; the world remained active for over 20 minutes with zero arrival-check/InvocationTargetException entries, the UI showed `NO TARGET SELECTED`, and 28 genuine geology records including `COPPER-RICH DEPOSIT` remained visible.
- `explorify:campsite` family recognition passed on desktop: locate found `[-2512, ~, -992]`, Field Survey reported `CAMPSITE`, and the dynamic filter list visibly exposed `CAMPSITE` among 13 discovered types.
- Opening the two-page dynamic filter over live results exposed a GUI layering defect: result and telemetry text rendered above the otherwise opaque filter panel. `0.5.59.7` wrapped the legacy overlay in a dedicated `Z=1000` pose layer; follow-up screenshot acceptance showed the popup contents unobstructed, so the layer repair passed.
- A scan with only `CAMPSITE` selected initially returned 96 mixed candidates (`UNKNOWN STRUCTURE`, `BURIED TREASURE`, `SMALL DUNGEON`, etc.). The placement-only finish path applied only the legacy vanilla bitmask and skipped `Navigation0475RadarFilter`'s dynamic selected-family set. `0.5.59.8` applied an exact normalized label filter and runtime log then showed `0 results (96 before active filters; selected=CAMPSITE)`, proving exclusion worked.
- That zero exposed a second defect: the GUI advertised 5000 blocks while placement math used a hidden 100-chunk (~1600-block) cap. `0.5.59.9` correctly bound the live 5000 range and pruned to one Campsite task, but runtime still produced zero raw candidates because random-spread region indexes were passed into Minecraft's chunk-space method and divided by spacing twice. `0.5.59.10` passes `regionIndex * spacing`; compile, coordinate smoke, bytecode, install, and hash checks passed. Runtime then queued one Campsite task at 5000 blocks and returned exactly one selected Campsite, closing the positive range/filter chain.
- Laptop synchronization passed from clean `main` at commit `3a3f35e`: `tools/apply-to-instance.ps1` backed up `0.5.59.4` under `backups/custom-mods/pre-apply-20260902-110953`, installed exactly one `0.5.59.10`, and matched the repository SHA-256. The DoctorWhoMod fork also remained single and hash-matched.
- Laptop `0.5.59.10` runtime acceptance passed: an `ALL` scan queued 102 placement-only tasks at 5000 blocks and returned 101 mixed-family results in about 5.6 seconds. The remaining Radar v2 family test is `ARCHEOLOGIST CAMP`.
- `0.5.60.0` adds live `config/newworldcore/*.properties` controls for Radar/navigation, Mining, FE/Warp Matrix, engine travel, geology scan energy, replication, room protection, and emergency network reserve. Radar pacing wraps the existing four-tick batch scheduler so FE is charged only for executed batches: shipped `scan.batch_interval_ticks=8` is approximately half rate, `4` restores the previous rate, and `16` is approximately quarter-rate.
- Laptop apply passed after game shutdown: `0.5.59.10` was preserved under `backups/custom-mods/pre-apply-20260902-133637`; live/repository `0.5.60.0` hashes match, DoctorWhoMod remains hash-matched, all eight NewWorldCore config files are present, and live Radar batch interval is `8`.
- Laptop `0.5.60.0` runtime scan acceptance passed: Structure Radar completed 102 `ALL` tasks with 101 mixed results in about 9.98 seconds, while Geology returned 48 deposits in about 9.00 seconds. The screenshot then exposed the Geology filter panel below deposit rows/coordinates/scrollbar/accent rendering.
- Laptop `0.5.60.1` apply passed after Java stopped: `0.5.60.0` was preserved under `backups/custom-mods/pre-apply-20260902-140313`; however, its `Z=1000`-only Geology fix failed screenshot acceptance because deferred result buffers rendered later.
- Laptop `0.5.60.2` apply passed after Java stopped: `0.5.60.1` was preserved under `backups/custom-mods/pre-apply-20260902-144912`; repository/live NewWorldCore hashes match, DoctorWhoMod remains single/hash-matched, all eight config files match, and Radar batch interval remains `8`.
- Every `config/newworldcore/*.properties` setting now has Turkish inline guidance covering units, formulas, upgrade levels, change direction, and performance/safety effects. Numeric defaults did not change; config smoke passed.
- Laptop `0.5.60.2` Geology filter visual acceptance passed: the complete popup renders cleanly above deposit rows, coordinates, scrollbar, and accent layers. Debug log confirms `.1 -> .2` loaded and no relevant NewWorldCore GUI/config exception was found.
- The runtime-accepted config-suite, Turkish config guidance, and Geology buffer-flush repair were committed as `344efc4` and pushed to GitHub `main`.
- `betterarcheology:archeologist_camp_grassy` resolved at `[-2448, ~, 192]`. `.4` Field Survey queued at 48 blocks/80 ticks, completed once in 4074 ms, identified `TRIAL CHAMBERS` and `ARCHEOLOGIST CAMP`, and the screenshot visibly confirmed `ARCHEOLOGIST CAMP` in Structure Filters. Radar v2 is closed.
- The accepted player survey used a fixed 96-block radius, synchronously visited 13x13/169 chunk positions, and returned the eight structures in about 38 ms. `0.5.60.4` includes the `.3` survey tuning and ships documented `player.properties` controls with `48` blocks and `80` ticks: 7x7/49 chunk positions, roughly four-second visible delay, server-thread world access, and one pending scan per player.
- Config-first is now a persistent project convention in `.cursor/rules/config-first-development.mdc`. `gui.properties` controls Player GUI backdrop, live Survey detail visibility and filter overlay depth. `network.properties` also controls FE/item/fluid/gas node transfer and capacity multipliers. Save schemas, registry IDs, protocol/slot codes, and data-integrity constants remain intentionally non-configurable.
- Discovery Database Stage 2 is complete. `0.5.61.0` adds persistent `analysisLevel` and `lastSeenAt`, schema-v2 migration, non-downgrading FIELD/visited/analysis merge rules, and common `DISCOVERED`/`SEEN`/`ANALYSIS_UPGRADED` events. Laptop NBT acceptance found both fields in all 449 records (342 level 0, 107 level 1); Archeologist Camp preserved first `307066` and advanced last-seen to `397848`.
- `0.5.62.0` completes Geological Field Survey through reserved payload mode 1. It verifies actual loaded template blocks rather than accepting Radar coordinates as physical proof. Defaults are 48 horizontal/128 vertical blocks, 80 ticks, 8 results, 4096 checks per candidate, and 3 matches, all documented in `player.properties`.
- Laptop acceptance first returned a clean zero away from evidence, then verified `TIN-RICH DEPOSIT` at `[-2696, 32, -728]` with 3/4 matching blocks in 4016 ms. Persisted NBT upgraded that exact record to `GEOLOGY/FIELD`, visited 1 and analysis level 2, advanced last-seen, and preserved first discovery. No relevant error occurred.
- `0.5.64.0` completes anomaly → metallic → resource-rich → exact deposit-family progression. Every one of the 21 families has a configurable `reveal.required_accuracy.*` threshold; defaults expose 3/10/18/21 cumulative families at Accuracy 0/I/II/III, and unknown future families safely default to III.
- Laptop runtime acceptance ran Accuracy 0/1/2/3 at 24/32/40/48 result caps in about nine seconds each. The user visually accepted staged labels and the repaired TrueSingle foreground filter; the saved world contains 47 Radar L3 records plus one preserved Field L3 TIN and 66 untouched Radar L0 records, with no relevant NewWorldCore error.
- Discovery Analysis Stage 7 is partial: the geology analysis chain is complete, while advanced Structure analysis levels still await Research progression. Player Discoveries Stage 8 is complete. The latest 64 Structure and 64 Geology records are synchronized independently from 464 total records. The GUI exposes ALL/STRUCTURES/GEOLOGY, pagination, source/analysis/resource/coordinates, last seen, estimated reserve, and live player proximity or `DIFFERENT DIMENSION`.
- Stage 8 runtime acceptance used Archeologist Camp and Trial Chambers: both target and favorite writes reached the shared database; the existing Navigation engine applied dimension/position through four TARDIS state writes, produced one-hop plans, and completed the Trial Chambers route. `.66.0` was superseded because its positive C2S action codes were swallowed by the legacy `>=100` client-status path; `.66.1` uses tested negative action ranges.
- Stage 9 is partial because Discovery `TARGET` and `ROUTE` creation was completed early during Stage 8 in `0.5.66.1`; the dedicated Player Navigation panel, telemetry and remaining actions are still pending and must not duplicate that accepted path.
- FE Matrix registration now deterministically selects Architectury's `register(ResourceLocation, Supplier)` overload, removing the nondeterministic startup `argument type mismatch` path.
- The Uraninite path passed an initial in-game acceptance test: Geological Radar -> Discovery -> Navigation target -> TARDIS route -> matching physical deposit. Full family, Mining and balance regression remains open.
- The 14-stage `docs/12_Gelistirme_Yol_Haritasi.md` is the canonical numeric development order. Stage 4 and core Stage 5 single-player acceptance are recorded; next gate is the `.68.1` Overview link-reason visual check, then remaining Stage 9 Player Navigation. Broader untested cases remain explicitly open in the acceptance document.

## Ship and progression decisions

- The ship is a permanent mobile research base and central progression space.
- The three-level main hull and room placement were considered complete.
- Standard modular concepts: five-block corridors, 3x4 bulkhead openings, and typical seven-block floor modules.
- Building Gadgets 2 and WorldEdit are development tools.
- Retained roadmap themes: research, production chambers, Android companion/UI, warp core, teleport network, nano suit, story quests, and first playable alpha.
- Radar/navigation, replication, geology, and TARDIS systems should remain one coherent progression loop.

## Safety and maintenance

- Inspect first, then change.
- Preserve known-good custom builds before replacement.
- Never load multiple NewWorldCore or DoctorWhoMod versions in one instance.
- Do not commit worlds, saves, backups, logs, crash reports, user caches, accounts, launcher metadata, or personal options.
- After substantial verified work, update this memory and `pack-lock.json`, then commit and push.
- Cross-computer conversation continuity lives in `.codex/HANDOFF.md` and `.codex/conversations/`. Read them after every pull and update them before switching computers.
