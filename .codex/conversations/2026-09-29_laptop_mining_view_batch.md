# 2026-09-29 — laptop — Mining view batch

Status: implementation/tests/install verified; Minecraft runtime acceptance pending.
Branch: main. Gate decision commit: `5f734d3`.
Build: `NewWorldCore-1.21.1-NeoForge-0.5.70.0-alpha-player-mining-view.jar`
SHA-256: `826b667f2316e7cf47242ad9046f29097392b061a6ddfff6567ddaeecb2be2b7`; 3645288 bytes.

## User goal

Proceed with the next roadmap work, combining 3–4 related operations into one batch and one grouped game test.

## Starting state

Clean synchronized main/origin at a8fefb7; installed .69.4 matched lock and runtime. Screenshots show manual target [-2454,63,181], Y=63, distance=11 and matching calculated route. Server 10:52:13.787 confirms exact final coordinates; 10:52:13.980 proximity completion at unchanged exterior is not a flight test.

## Decisions

- Basic Stage 9 workflow accepted; extended config/permissions/Navigation-specific link/multiplayer cases remain open. Verified roadmap gate committed before new implementation.
- Stage 10 batch is read-only mining status/scan area, scan/extraction progress, Collection/AE/Replication buffers and routing/SMART AUTO telemetry. No controls, false deposit identity, reserve ledger, flight or saved-schema change.
- Progress is scoped to recorded scan-center chunk. LAST SCAN explicitly marks another chunk/dimension. Extraction uses mined resource targets / resource found counts, excluding hazard counts; scanning/zero denominator is N/A. It is not physical deposit remaining percentage. Buffer capacity is 256 resource types, not a fictitious shared item-volume percent.

## Changes

- PlayerMining0700 server adapter uses existing state maps, never creating ship state via state(id); no bind/ensure/tick/start/stop calls. Buffer validation requires loaded chunk first and reads existing IDs/counts. Public inherited interface chunk checks are supported.
- Dispatcher mode 9, independent bounded wire range 1540000000..1556777217, owner/link checks, server throttling, ship/session reset and stale-client hiding. No remote controls.
- Mining tab activated and rendered through existing GUI wrapper. Content cannot fall into Survey actions; Overview/Survey headers and Navigation transition verified.
- Live config refresh_ticks=20, stale_after_ticks=120, show_scan_area/show_buffers=true; bounded/fallback tests and Turkish documentation. Refresh/stale server timing transmitted to client.
- Exact .69.4 originals copied and hash-verified before active copies removed: runtime `backups/custom-mods/pre-player-mining-20260929-01/{repository,instance}/`. Old build recoverable. New candidate copied to both endpoints; exactly one core each. New config added without overwriting existing runtime config.

## Verification

### Passed

- Build: `tools/build-newworldcore-geology-patch.ps1` using hash-verified .57.0 baseline, JDK 21.0.12.1+1, ASM/ASM-tree 9.8.
- Twelve smoke suites plus actual DoctorWhoMod WE formula fixture. New test uses shipped Mining State classes and checks absent maps/no mutation, mode/area/progress/hazard semantics, long bounds, zero-denominator, config reload/default/clamp, protocol collision/malformed/overflow, stale/ship/link/reset, read-only reflection targets, interface default lookup, GUI geometry/display flags and header transitions.
- Final patched PlayerShipScreen bytecode inspected: enabled colors cover Mining, content dispatch reaches wrapper.
- Both installed core hashes match candidate. DoctorWhoMod SHA remains `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`. Navigation saved-data hash unchanged across install. No world edits. Java absent before and immediately after backup.

### Failed

- No build/smoke failure. Code review caught potential swallowed Overview/Survey headers and inherited LevelReader method lookup before final deployment; both corrected and regression tested. Negative malformed/config test warnings are intentional.

### Not tested

- Actual Minecraft Mining render/live updates/buffer parity/range recovery; multi-player and broader runtime config cases. No claim that Stage 10 is accepted or complete.

## Next executable step

Open game and run the four grouped checks in `docs/16_Player_Mining_Runtime_Kabul.md`. Report evidence once; retain unobserved cases as open. Do not request already accepted Navigation steps again.

## References

`docs/12_Gelistirme_Yol_Haritasi.md`, `.codex/HANDOFF.md`, `2026-09-29_laptop_waypoint_terminal_repair.md`, `pack-lock.json`.
