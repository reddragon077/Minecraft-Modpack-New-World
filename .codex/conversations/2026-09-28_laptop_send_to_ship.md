# 2026-09-28 — laptop — Send to ship

Status: implementation/tests/install verified; new runtime acceptance pending
Branch/start: main / 368a1900de28ad0938158226d451b641785db282
Build: `NewWorldCore-1.21.1-NeoForge-0.5.69.3-alpha-send-to-ship.jar`
SHA-256: `ecfcdc4f17055734c9f73e43c97f5eb1f5f9177f3e7dc5206854a3967fde36ce`; 3643035 bytes

## User goal and accepted prior work

User confirmed first location saved without duplicates, second location saved, and both remained after world exit/re-entry; then authorized proceeding to SEND TO SHIP. Screenshot shows [-2454,63,181] and [-2454,63,189], WAYPOINT/MANUAL, favorites 4/4. Read-only latest.log check found first save at 16:38:14.955, same-key repeat 16:38:19.368 retaining 3/3, second save 16:38:54.240 followed by 4/4 at 16:38:55.776. No relevant exception in this interval; unrelated third-party startup/recipe errors exist. World reload persistence is explicit user-reported acceptance, not a separate NBT audit. Extended interior/config/multiplayer tests remain open.

## Starting state

Required runtime memory, canonical rules/memory/handoff/index/latest record and roadmap read. Runtime/repo memory hashes matched. Clean main at 368a190; fetch and fast-forward pull already current. Runtime .69.2 hash matched. No java/javaw process at inspection or installation. Prior acceptance reconciled before implementation.

## Decisions and changes

- SEND TO SHIP means send the selected favorite (including saved locations) to the shared Navigation target, not summon the ship or execute flight. Existing favorite TARGET button widened/renamed; existing packet request, snapshot index/ship mapping and selectTarget writer reused. No duplicate route engine, world schema or arbitrary coordinate payload.
- New PlayerNavigationSend0693 helper exposes live enable and 40-tick per-player cooldown bounded to 20–1200. Existing shared target permission also required. Existing server link/owner/same-ship/favorite checks execute before policy and write. Refresh does not clear cooldown. Other players independent.
- Successful acknowledgement SENT TO SHIP; repeat during cooldown SEND WAIT. Existing client link reset clears shared status. Normal Discoveries TARGET/ROUTE unchanged. Loaded route, WE and flight untouched by the common target writer.
- Source/embedded manifest version and marker updated. Navigation config/README, roadmap/acceptance/test guide, lock/mod list and continuity records updated. .69.2 basic save acceptance closed; Stage 9 extended scope remains partial.

## Verification

### Passed

- Full build after test-fixture correction: ten smoke suites and Navigation against actual DoctorWhoMod engine estimate passed.
- New suite tests config defaults/live toggles/bounds/invalid fallback, actual shared gui target permission, gate booleans, exact cooldown boundary, per-player isolation and refresh persistence. Calls the actual shared selectTarget method using a fake state/data fixture; verifies only selectedKey/dirty change and route/WE/flight remain unchanged. Exercises actual favorite/detail renderer for new button and all three acknowledgements; checks layout bounds, retained Discoveries buttons and reset/status protocol separation.
- Existing nine suites passed, including actual waypoint metadata persistence normalizers and same-name getter collision regression.
- Embedded patch manifest equals source; new helper class present. Build and both installed core hashes match lock. Previous repo/runtime .69.2 hashes verified before backup/move. Only one core at each endpoint; unchanged DoctorWhoMod retained.
- Scoped deployment backs up original .69.2 and config/README under laptop `backups/custom-mods/pre-send-to-ship-20260928-01/` (repository/instance/config subfolders). No worlds/save/other config edits. Live Navigation values compared with prior HEAD before replacement; existing values preserved.

### Failed then corrected

Initial new test used discovery.properties for the shared target permission and correctly failed its assertion. Inspection showed actual setting is gui.properties `player.discoveries.enable_target_action`. Test fixture and config comment corrected; production code already used the existing correct getter. Full rerun passed. Invalid-config/decode warning lines in successful tests are deliberate fixtures, not runtime failures.

### Not tested

New .69.3 button, acknowledgement, config denial/cooldown and terminal comparison in Minecraft; multiplayer/range/dimension edge cases. Automated fixture success is not end-to-end runtime acceptance.

## Next executable step

Near ship with CONNECTED: NAVIGATION → FAVORITES → select saved LOCATION → SEND TO SHIP. Expect SENT TO SHIP; return to NAVIGATION and compare left target coordinates with saved record and physical terminal. Right route/hop remains unchanged; no movement/WE consumption. Then follow `.69.3` section of `docs/15_Player_Navigation_Runtime_Kabul.md`. Do not recreate accepted location persistence or normal TARGET/ROUTE; do not close Stage 9 prematurely.
