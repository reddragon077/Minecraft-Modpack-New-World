# New World current handoff

Updated: 2026-09-29
Machine: laptop
Branch: `main`

## Current objective

Installed `.71.4-alpha-emergency-entrance`, SHA-256 `fe85ac588ab5f2f7449590eb1d939f59bdff789e4f9b6d9001728acc538690ab`, 3682811 bytes. USER DESTINATION CHANGE: normal portal arrival INSIDE the main entrance door, replacing separate Teleporter Room/no-entrance policy. .71.3 returned successfully at 14:42:25 to [-12,128,-21] with 1800000ms cooldown, but user rejected that location. Uses Doctor getEntrancePosition().relative(getEntranceFacing()), bottom-center and entrance yaw; loaded live registered door plus existing safety checks, no other-room/neighbor fallback. Beacon absent; ownership/travel veto/30-minute persistent cooldown/Nav/route/WE unchanged. Normal cooldown persistence is unchanged. User additionally authorized a one-time local test reset: exact cooldown long set to zero in matching playerdata and level.dat/Data/Player, verified all other decompressed bytes unchanged; originals preserved under backups/custom-mods/emergency-cooldown-reset-20260929-01. Fifteen suites + actual Doctor entrance portal contract + WE fixture passed. New in-game entrance/relogin acceptance PENDING. User closed game (saved 14:43:42); originals and overwritten runtime files preserved under backups/custom-mods/pre-emergency-entrance-20260929-01. Next: test immediately from outside after one-time reset; entrance-return/countdown/relogin/unchanged-route check. No Stage 12 or repeated Mining tests.

### Superseded .71.3 installation and destination

Installed `.71.3-alpha-emergency-room-check`, SHA-256 `e0a0058bf8b4552cfde56b1580237d0f92e4a74ddf315c22cc69fae8c3314dda`, 3685272 bytes. .71.2 far-range runtime attempt failed at 14:28:10 with ServerLevel.hasChunkAt NoSuchMethodException before teleport/cooldown. Reproduced against the old JAR using inherited interface-default chunk/collision methods. Both Emergency chunk checks now reuse PlayerMining0700.loaded (public inherited lookup); no safety bypass, new config, destination, cooldown, Navigation or WE changes. Fifteen suites plus actual Doctor room/WE contracts pass after repair. In-game return/relogin acceptance remains PENDING. Backups: backups/custom-mods/pre-emergency-room-check-20260929-01. Next: repeat Return/Confirm from current far position, then cooldown/relogin and unchanged target/route; no repeated Mining acceptance, no Stage 12.

### Superseded .71.2 installation

Installed `.71.2-alpha-emergency-return`, SHA-256 `ffc2a87188ef118cc01338dd6f791d0181fcb00ca9086b6f16a65853acb1bb8c`, 3685141 bytes. Beacon removed; old records preserved. Confirmed owner rescue uses actual Doctor console-side Teleporter Room placement and verifies live pad/loaded safe cells; no entrance fallback, Navigation target/route, ship flight or WE writes. Normal link range/dimension restrictions do not block rescue; ownership/freshness/outside-interior/alive/dismounted/not-flying-or-rebuilding guards remain. New request IDs/schema3 reject old Beacon requests. Successful verified arrival writes a 30-real-minute expiry into PlayerPersisted/NewWorldEmergencyReturnUntilV1; logout/death retain it, offline time counts; failed/vetoed returns do not charge. Fifteen suites + real Doctor room template/placement contract + existing WE fixture passed. Actual in-game return and cooldown persistence are PENDING, not accepted from fixture tests. Game absent at deployment; .71.1 originals, replaced runtime docs/config and Navigation data hash-backed-up at backups/custom-mods/pre-emergency-return-20260929-01; save and Doctor unchanged. Next: one grouped return/cooldown/relogin/unchanged-target-route check (docs/17), then extended far/dimensional/protection acceptance. Stage 11 only; no Stage 12.

### Historical decisions/build notes below (superseded by .71.2 above)

USER CORRECTION 2026-09-29 supersedes all beacon work below: remove beacon; implement direct owner-only safe Teleporter Room return, 30-minute persistent successful-return cooldown, no failed-return charge or Navigation/route changes. Explicit user confirmation. .71.1 feedback visually/log verified, but wrong product behavior; do not extend beacon or delete its existing saved records. Return not yet implemented/tested. See `2026-09-29_laptop_emergency_return.md`.

Current installed repair is `.71.1-alpha-emergency-feedback`, SHA-256 `3dae6836759aedc3f3d00b1985a525a70aaf3741be87b12d2bfb281a27fc0a46`, 3678605 bytes. .71.0 server logged successful beacon writes and cooldown, but user saw NO RESPONSE. The timeout's permanent late-result loss was reproduced in a failing regression; exact live client drop remains untraced. Ship/request/sequence-bound receipts now travel inside Emergency schema-v2 snapshots and are replayed by normal telemetry, without repeating writes. Delayed feedback survives transient stale data, cooldown is server-backed/counting down, and rapid retry receives WAIT. Fourteen suites + actual Doctor WE fixture passed. Game closed, .71.0 originals/current Navigation save backed up under `backups/custom-mods/pre-emergency-feedback-20260929-01/`; one identical new core per endpoint, save/config/Doctor unchanged. Runtime acceptance of this repair is PENDING: stationary outside beacon two clicks -> BEACON SENT + cooldown countdown -> selected coordinate with old route preserved. Actual RETURN remains unavailable; do not advance Stage 12 or repeat accepted Mining tests. See `2026-09-29_laptop_emergency_feedback_repair.md`. All installed .71.0 wording below is historical.

Installed candidate `.71.0-alpha-emergency-beacon`, SHA-256 `17ddc1b310b9926ee72ff65cc0d7000d1a5687747307d03b3fc24170881c7ea3`, 3675910 bytes. Emergency tab + two-click DISTRESS BEACON selects a server-observed WAYPOINT/MANUAL favorite as the shared Navigation target. Same-coordinate records retain evidence/name; quota is the existing location.max_per_ship. Single-use player/ship/world/dimension/block-bound challenge; current owner/link/alive/outside-all-TARDIS/config checks, stale-view rejection and cooldown. No route calculation, flight, teleport, WE or mining changes. RETURN explicitly unavailable, pending a separate Stage 11 integration. Fourteen suites + actual Doctor WE fixture passed; Minecraft acceptance PENDING (docs/17). Old .70.1 JARs and Navigation saved-data hash-backed-up at `backups/custom-mods/pre-emergency-beacon-20260929-01/`; game absent during install, one matching core per endpoint, saved data unchanged. Next: one grouped Emergency session, not repeated Mining/Navigation acceptance. Gate/old wording below is historical.

2026-09-29 continuation: Stage 11 first batch is now authorized and recorded in the roadmap: Emergency view + server-position DISTRESS target + two-step confirmation + owner/link/cooldown guards. Preserve existing routes/flight/WE. Actual RETURN is explicitly deferred within Stage 11 pending a verified safe Teleporter Room return destination/transaction contract; the existing Doctor teleporter block only implements local destination teleportation. Stage 10 basic acceptance below stands; extended tests/reserve dependency remain open. No Stage 12 work. Implementation/runtime acceptance not yet claimed.

Latest acceptance supersedes the pending .70.1 test below: user explicitly confirmed all four presented checks passed (resource view, confirmed stop, same-area world re-entry persistence, physical-terminal restart). Screenshot shows raw_iron 120 mined blocks / 761 earlier untracked and AE934/REP6011. The latter three checks are user report, not new independent log/NBT evidence. Do not repeat this basic acceptance. Keep full buffer parity, Mining-specific link/config/multiplayer edge tests and Stage 14-dependent deposit remaining open; no Stage 11 implementation started. This turn changes only acceptance documents, not JAR/config/worlds.

Installed `.70.1-alpha-mining-yield-stop` (SHA-256 `01bba56cc52ba9e7864ef2e1814ba38fa0d00cf966718d6fde1dc1f78653032d`, 3660734 bytes). MINED RESOURCES / FLOW: successful resource-block ranking, persisted per scan area; two-step STOP MINING only sets the existing mining shield OFF. No restart/handbrake/main-shield/routing/flight mutations. Same-area reload preserves counters; actual new-area reset clears them. Historical mined totals remain untracked by resource, explicitly labeled SINCE UPDATE. Optional `NewWorldMiningYieldV1` mining-phase NBT key; do not downgrade and save without preserving new statistics. Live config rows=5, stop enabled, confirmation100/cooldown40 ticks. All thirteen suites and actual DoctorWhoMod WE fixture passed; save/load bodies exercised with NBT/storage fixtures. In-game acceptance pending, not Stage 10 completion.

Next grouped test: Mining → MINED RESOURCES / FLOW while extracting; STOP MINING → wait for CONFIRM STOP MINING → confirm within 2 seconds; verify physical mining shield OFF and extraction paused (routing may continue). World exit/re-entry in same scan area should retain counts; restart only via physical terminal. See docs/16. Range recovery/full buffer parity and multiplayer/config edge tests still open; deposit remaining waits for Stage 14 true reserve integration. No repeated Navigation acceptance needed.

Both .70.0 originals/config/runtime records and 16 Mining save files hash-verified under laptop `backups/custom-mods/pre-mining-yield-stop-20260929-01/`; old active JARs moved to recoverable `repository/retired` and `instance/retired`. One .70.1 core per endpoint, saves unchanged, game absent during installation. Session record: `2026-09-29_laptop_mining_yield_stop.md`.

## Previous .70.0 — acceptance and superseded installation record

Acceptance update: .70.0 status/scan/extraction/live counters passed by screenshots and 11:29:51/11:44:30/11:47:01 log; user closed game, server stopped 11:48:34. New Stage 10 batch authorized: actual per-resource mining yield/ranking with persistent counters, and guarded confirmed Mining Shield OFF emergency stop. No restart/flight/handbrake/routing mutation. Remaining-reserve percentage stays blocked on Stage 14 ledger; never infer it from scan counts. Reopen/range and full buffer parity are still open, not assumed passed.

Installed candidate `NewWorldCore-1.21.1-NeoForge-0.5.70.0-alpha-player-mining-view.jar`, SHA-256 `826b667f2316e7cf47242ad9046f29097392b061a6ddfff6567ddaeecb2be2b7`, 3645288 bytes. Four read-only Mining areas implemented together: status/scan area, scan/resource extraction, three buffers, routing/SMART AUTO. Live refresh=20/stale=120, area/buffers visible. Twelve suites plus engine fixture passed; not yet tested in Minecraft. No mining controls/route writes/deposit reserve linkage.

Next: one grouped Mining session per `docs/16_Player_Mining_Runtime_Kabul.md`, compare status/progress/buffers/routing with physical terminal, then reopen/link recovery. Keep user feedback grouped; do not repeat accepted Navigation tests. Both .69.4 originals hash-backed-up at runtime `backups/custom-mods/pre-player-mining-20260929-01/{repository,instance}`; one matching new core per endpoint. DoctorWhoMod and navigation save unchanged. See `2026-09-29_laptop_mining_view_batch.md`.

## Previous .69.4 — basic acceptance passed

2026-09-29 update: .69.4 combined runtime test passed by screenshots and 10:52:13 server route log (exact [-2454,63,181], 11 blocks). Proximity completion is not a flight test. Stage 9 basic workflow accepted; extended tests remain open. User requests 3–4 related operations per batch. Proceed to Stage 10 read-only mining status/scan area, scan/extraction progress, Collection/AE/Replication buffers and routing/SMART AUTO status. The pending gate below is historical and superseded.

Installed `.69.4-alpha-waypoint-terminal-route`: `NewWorldCore-1.21.1-NeoForge-0.5.69.4-alpha-waypoint-terminal-route.jar`, SHA-256 `4a68d8e660cd7e85a40109a16c5e1ec1068a92aefe45efbfccd19127339fc4e8`, 3635047 bytes. Eleven suites + real DoctorWhoMod WE fixture passed. Physical terminal now preserves manual saved Y, samples live ship-relative distance and displays explicit dimension/unavailable states. Real calculator/range clamp/altitude-normalizer fixtures cover exact final Y including -64 and vertical-only/multi-hop targets; intermediate cruise and nonmanual surface policy remain unchanged. No flight bypass, save migration or config change.

Next: one combined game check, saved LOCATION [-2454,63,181] → SEND TO SHIP → physical terminal Y=63 and ~11 blocks if ship remains [-2464,61,176] → CALCULATE ROUTE → final [-2454,63,181]. Before calculation the old Carbon route must remain; after calculation replacement is intentional. Do not fly just for this check. New `.69.4` runtime acceptance and extended Stage 9 tests remain open; do not advance to Mining yet.

`.69.3` target write/old-route preservation passed (09:55:02.951 server log + screenshots + read-only NBT audit). Stored manual Ys are correct; terminal projection was wrong. Originals backed up/hash-verified at laptop `backups/custom-mods/pre-waypoint-terminal-20260929-01/{repository,instance}/`; single matching installed core per endpoint; embedded/source manifest matches; saved discoveries hash and DoctorWhoMod unchanged. Game closed at install. User wants related work batched with one grouped runtime handoff, not many small interruptions. Full details: `2026-09-29_laptop_waypoint_terminal_repair.md`.

## Previous .69.3 — historical installation; partial acceptance supersedes pending text

Installed `.69.3-alpha-send-to-ship`: `NewWorldCore-1.21.1-NeoForge-0.5.69.3-alpha-send-to-ship.jar`, SHA-256 `ecfcdc4f17055734c9f73e43c97f5eb1f5f9177f3e7dc5206854a3967fde36ce`, 3643035 bytes. Ten smoke suites plus actual DoctorWhoMod WE fixture passed. New runtime acceptance pending: NAVIGATION → FAVORITES → select a saved LOCATION → SEND TO SHIP; expect SENT TO SHIP, then updated left target with unchanged right loaded route. Compare physical terminal. No flight/energy consumption.

This is the existing shared TARGET writer exposed with a clear label/acknowledgement and new live favorite-send permission/cooldown, not a new flight or route implementation. `player-navigation.properties`: send_to_ship.enabled=true, send_to_ship.cooldown_ticks=40 (20–1200). Shared `gui.properties` player.discoveries.enable_target_action remains required. Same owner/link/ship/favorite safeguards; repeat sends within cooldown return SEND WAIT, REFRESH does not bypass it. Normal Discoveries TARGET/ROUTE are unchanged. Accepted `.69.2` repository/runtime originals and config/README backed up under `backups/custom-mods/pre-send-to-ship-20260928-01/`; game/Java absent at install, worlds and DoctorWhoMod untouched. See `2026-09-28_laptop_send_to_ship.md` and `docs/15_Player_Navigation_Runtime_Kabul.md`. Stage 9 remains partial; do not skip to Mining before current acceptance.

## Previous .69.2 — accepted basic save/reload; backed up

Acceptance update: `.69.2` first location, exact-coordinate dedup and second location passed by screenshots and server log (16:38:14 / 16:38:19 / 16:38:54; favorites 3/3 -> 3/3 -> 4/4). User confirmed persistence after world exit/re-entry. SEND TO SHIP is now the next implementation: explicitly send a selected favorite to the existing shared target writer, leaving the route, flight and energy unchanged. Runtime-pending statements below are installation history; extended cases remain open.

Installed Stage 9 SAVE CURRENT LOCATION candidate `.69.2`: `NewWorldCore-1.21.1-NeoForge-0.5.69.2-alpha-save-current-location.jar`, SHA-256 `3253d965abe06cf134de86759c94bade33f53038a60a7a9990ef28a35db358c9`, 3641310 bytes. Nine smoke suites plus actual engine WE fixture passed; new runtime acceptance is pending. Next: outside the TARDIS with a connected link, Navigation → SAVE CURRENT LOCATION → FAVORITES; verify player block coordinates/dimension, no duplicate on repeat and persistence after world reload. No target/route/flight/energy changes. SEND TO SHIP is still unimplemented; Stage 9 remains partial.

The new record is a shared WAYPOINT/MANUAL favorite, not structure/geology evidence. Server reads player position (not supplied coordinates), rechecks owner/link and rejects TARDIS interiors. Existing coordinate collisions only favorite the original record without replacing metadata. Live settings in `player-navigation.properties`: location.enabled=true, location.max_per_ship=128 (1–1024), location.cooldown_ticks=40 (20–1200). Limits reject new saves without deleting old data. Metadata normalizers now preserve WAYPOINT/MANUAL through existing schema-v3 persistence; no migration or new save-file format.

`.69.1` basic favorite acceptance is now passed: user screenshots show Trial Chambers/Archeologist Camp, SYNC 2/2, then Trial Chambers selected at [-2434,-9,150] with the old Carbon route unchanged (next [-2376,68,376], 1/1, 219 blocks, 52 WE). Log 15:36:28 favorite snapshot, 15:36:31 TARGET and 15:36:32 updated Navigation corroborate this. Empty/removal/config/multiplayer/link edge cases are not newly claimed. User closed the game; server stopped at 15:37:06 and no Java process was present during install.

Accepted `.69.1` originals and replaced config/README are preserved at laptop `backups/custom-mods/pre-save-location-20260928-01/`. Scoped install only; DoctorWhoMod and worlds/unrelated configs unchanged. Repo/runtime single JAR hashes/lock/embedded manifest verified. Latest details: `2026-09-28_laptop_save_current_location.md`.

## Previous .69.1 — backed up; basic favorite acceptance supersedes pending wording below

Installed Stage 9 favorite-picker candidate `.69.1`: `NewWorldCore-1.21.1-NeoForge-0.5.69.1-alpha-navigation-favorites.jar`, SHA-256 `d6ff78e9fce2e9d5e3b6480711dfb656c8a64f4dc1e096799b73c1d31229a91d`, 3636825 bytes. Navigation → FAVORITES uses shared favorite records and the existing guarded TARGET writer; selecting a target leaves the existing route unchanged. No flight, WE consumption, world migration or DoctorWhoMod change. Live `player-navigation.properties` adds `favorites.enabled=true` and `favorites.sync_limit=128` (16–512). Old favorites are queried independently of the recent Discovery quota.

Eight smoke suites and actual DoctorWhoMod WE fixture passed. Verified source/embedded manifest, lock/hash and one custom JAR per mod at repo/runtime. Game was closed for scoped deployment; `.69.0` repo/live originals and replaced configs preserved at laptop `backups/custom-mods/pre-navigation-favorites-20260928-01/`. Other configs/worlds/backups untouched. Missing temporary JDK was recovered from the official checksum-verified Adoptium package to `D:\Projects\NewWorld-Toolchains\jdk-21.0.12.1+1` (local tooling, not Git).

Acceptance reconciliation: user confirmed the requested `.69.0` GUI close/reopen check (“çalışıyor kanka baktım”). This closes that specific item by user report, not by a new log audit. Earlier range/Nether screenshots belong to Ship Link/Discoveries; they are not newly claimed Navigation tests. Stage 9 remains partial. Next executable step: test `.69.1` Navigation → FAVORITES → select → TARGET → < NAVIGATION; confirm new target and unchanged old route, then REFRESH/empty/link-loss checks in `docs/15_Player_Navigation_Runtime_Kabul.md`. Favorite runtime acceptance is pending; SAVE CURRENT LOCATION and SEND TO SHIP are still unimplemented. Do not repeat accepted TARGET/ROUTE development.

## Previous .69.0 — backed up; historical installation and partial acceptance

The following 10 September entries are historical. Their pending GUI-reopen wording is superseded by the user acceptance above, and `.69.1` is now installed.

Latest runtime update: `.69.0` Navigation initial view and Aluminum -> Carbon TARGET/ROUTE refresh passed by screenshots and server log (17:19:37 / 17:24:04). Loaded hop stays 1/1; coordinates/distances update and estimate changes 48 -> 52 WE. No Navigation runtime failure found. The candidate paragraph below describes installation-time status; it is superseded by this partial acceptance. Explicit GUI close/reopen was requested but not yet confirmed; do not count the user's GitHub-save request as test confirmation. Navigation-specific range/dimension/config and route edge-case checks also remain open. Next: resume the remaining checks in `docs/15_Player_Navigation_Runtime_Kabul.md`, then favorite/current-location/send actions. No new JAR required for this handoff.

Installed Stage 9 view candidate `.69.0`: `NewWorldCore-1.21.1-NeoForge-0.5.69.0-alpha-player-navigation-view.jar`, SHA-256 `af77b62a691a18bf6340c7371647b0c90e07b7643773cf8576958aae29c8427f`, 3635246 bytes. Eight smoke suites plus real DoctorWhoMod next-hop WE test passed; source/embedded manifest and single-JAR hashes match. No runtime acceptance yet. Next: `docs/15_Player_Navigation_Runtime_Kabul.md`; do not implement further actions before checking this view. `.68.2` and repo/live originals preserved at laptop `backups/custom-mods/pre-player-navigation-20260910-01/`. New live `player-navigation.properties` only; existing gameplay configs untouched. No changes to DoctorWhoMod, worlds or existing TARGET/ROUTE behavior.

Stage 5 fresh-session regression gate passed on `.68.2`: Overview and Discoveries loaded; OUT OF RANGE at 5998 blocks recovered automatically to CONNECTED/NOMINAL (12-block screenshot). User clarified the apparent stuck screenshot was delayed, not a recovery failure. Next: remaining Stage 9 Player Navigation; reuse accepted Discovery TARGET/ROUTE. Broader multiplayer/forced-timeout tests remain open. Historical pending wording below is superseded by this acceptance.

## Previous .68.2 — accepted, backed up; historical installation details

- User resolved the config divergence: shared Structure Survey `field_survey.delay_ticks=20` (1 second), matching the existing laptop value. Repository updated from 80 to 20; Ship Link and Overview configs match too. No JAR rebuild needed for this live property. Geological Survey delay remains unchanged.

- Repo/laptop: `NewWorldCore-1.21.1-NeoForge-0.5.68.2-alpha-typed-client-link.jar`, SHA-256 `2ffb61b41cc4b3f99f820c103006d9015da7695184ac62e49f62b4224cbd88c8`, 3625901 bytes. Seven smoke suites passed; source/embedded manifest and installed hashes matched. No Java process during replacement.
- Live read-only diagnosis at 15:25:01 on Render thread found two `Minecraft.getConnection()` methods: return `net.minecraft.network.Connection` was null; return `ClientPacketListener` was non-null and identical to player/level connection. Reflection by name/arguments selected the former, preventing mode-4/5 requests entirely. No particular mod is blamed without attribution evidence.
- Overview and Ship Link now share exact zero-argument, nonstatic return-descriptor selection. Null listener stays null; missing descriptor fails instead of selecting another getter. No config, packet, save or access policy changes. The `.68.1` reason fix is retained.
- ASM regression creates real same-name/no-arg/different-return methods, tests both declaration orders, null wrong getter/live listener, live wrong getter/null listener and missing-descriptor rejection. Existing seven suites passed; this is not runtime acceptance.
- Previous `.68.1` and original repo/live copies preserved at laptop `backups/custom-mods/pre-typed-client-link-20260910-01/`, along with prior local continuity records. Known-good `.68.0` and older backups untouched. Temporary read-only diagnostic agents/tools remain under ignored laptop backups, not in the shipped JAR or Git.
- Next: launch a fresh game session; near TARDIS, Overview must leave SYNCING and show CONNECTED with telemetry; Discoveries must load. Reopen GUI, then verify real OUT OF RANGE/recovery and retained reason text. See `docs/14_Ship_Link_Runtime_Kabul.md`.

## Previous .68.1 — runtime failed (permanent SYNCING), now backed up

- Repo/laptop: `NewWorldCore-1.21.1-NeoForge-0.5.68.1-alpha-ship-link-reasons.jar`, SHA-256 `f4626881b44447184bf6003f81fb8586624c129929ecec0058cde9ae38a2db8c`, 3625579 bytes. Seven smoke suites passed, including six link-loss causes, denied telemetry, wire round-trip and headless GUI text. Embedded/source manifest matches; each endpoint has one hash-matched core JAR. Java was stopped for installation.
- Overview now preserves the authoritative resolver reason: `SHIP LINK LOST // OUT OF RANGE` or `DIMENSION LINK DISABLED`, etc. No config values, access policy, packets or save schemas changed.
- `.68.0` core runtime evidence: ON BOARD, 11770-block loss, 9-block recovery, dimensional true/false/true live toggle, Discovery list recovery, same-ship remote FAV/TARGET/ROUTE and physical Navigation Terminal ready-hop confirmation. Delayed Structure Survey denial is supported by log timing + source early return, not an NBT before/after audit. See `docs/14_Ship_Link_Runtime_Kabul.md` and newest conversation record for exact scope.
- `.68.0` and repo/live originals preserved with hash checks at laptop `backups/custom-mods/pre-ship-link-reasons-20260910-01/`; older backups untouched. No configs or world data overwritten. Restored canonical settings remain range=5000, dimensional=true, refresh=20, stale=120, Structure Survey delay=80.
- Next: user visually checks the `.68.1` Overview loss reason and recovery. Separate Geological delayed cancellation, forced stale/network tests, multiplayer and exhaustive reopen/world lifecycle tests remain unverified; do not count automatic tests as runtime evidence.

## Previous .68.0 installation — core runtime results supersede pending wording below

- Repo/laptop: `NewWorldCore-1.21.1-NeoForge-0.5.68.0-alpha-player-ship-link.jar`, SHA-256 `5637cae64a53e92140338b31d761f673f0a84c82f0fa2e8d37ceb457480c58b3`, 3625294 bytes. Seven smoke suites passed, embedded/source manifest matched, single-JAR/hash verified. No Java process was running during installation.
- GUI header on every tab shows CONNECTED / DIMENSIONAL / LOST, player-to-exterior 3D distance and ship dimension. Own interior is ON BOARD. Live `ship-link.properties` defaults: 5000 blocks, dimensional true, refresh 20 ticks, stale 120 ticks; Turkish guidance included. Server timing is communicated to the client.
- Server owner/range checks gate remote Survey/Discoveries reads and writes; delayed Survey rechecks at execution. Stale/reopened/changed-world client state is invalidated. Discovery selection keys are tied to the resolved ship; route engine receives that ship's interior context. No world schema change, chunk load, or cancellation of ongoing Mining/route work.
- Accepted `.67.1`, original repo/live JARs and prior config README are preserved at laptop `backups/custom-mods/pre-ship-link-20260910-01/`. Old backups remain. Scoped deployment changed only custom core JAR, new ship-link config and config README; Overview user values and other configs were preserved.
- Its core runtime checks subsequently passed within the scope above. Historical installation details are retained; use the current `.68.1` acceptance gate, not the original pending plan.

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
2. Stage 5 `.68.2` fresh-session and automatic recovery, `.69.0` Navigation view/GUI reopen, `.69.1` basic favorites and `.69.2` save/dedup/reload acceptance are recorded; do not repeat these old gates.
3. Test installed `.69.3` FAVORITES → saved LOCATION → SEND TO SHIP acknowledgement, shared target and preserved route using `docs/15_Player_Navigation_Runtime_Kabul.md`. Extended Navigation checks remain open. Discovery TARGET/ROUTE already works and must not be reimplemented.

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
