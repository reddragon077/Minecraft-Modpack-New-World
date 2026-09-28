# 2026-09-28 — laptop — Save current location

Status: implementation/install verified; new runtime acceptance pending
Branch/start: main / ed39081c93aa63f999b83aac521d5e0b0ae7f5f9
Build: `NewWorldCore-1.21.1-NeoForge-0.5.69.2-alpha-save-current-location.jar`
SHA-256: `3253d965abe06cf134de86759c94bade33f53038a60a7a9990ef28a35db358c9`; 3641310 bytes

## User goal and prior acceptance

User confirmed the favorite selector worked and sent two screenshots, then closed the game to add SAVE CURRENT LOCATION. Favorite list shows Trial Chambers/Archeologist Camp, SYNC 2/2. Navigation then shows Trial Chambers [-2434,-9,150], 80 blocks, with the unchanged Carbon route [-2376,68,376], loaded 1/1, 219 blocks, 52 WE, available 1000 WE. latest.log corroborates favorite snapshot at 15:36:28.600, TARGET at 15:36:31.779 and updated Navigation at 15:36:32.999. This closes the basic favorite item, not its untested empty/removal/config/multiplayer/link edge cases. Server stopped at 15:37:06.685.

## Starting state

Clean main at ed39081; fetch/fast-forward found already up to date. Sandbox Git ownership check initially required the real-user elevated context; no global safe.directory change was made. Runtime `.69.1` hash matched the recorded build. Required continuity/rules were read. No Java process during installation.

## Implementation

- New PlayerLocation0692 handler, request mode 8, reads server player position/dimension only after owner/link checks. TARDIS interiors (including another interior) are rejected; player must save an exterior-world position. No arbitrary client coordinates or ship IDs accepted.
- SAVE CURRENT LOCATION button on Navigation; bounded feedback messages beneath buttons. Enabled/limit/cooldown settings are live and have Turkish comments: true / 128 (1–1024) / 40 ticks (20–1200).
- Existing shared Discovery persistence stores a favorite/visited WAYPOINT with MANUAL source and coordinate label. No active-target, route, engine, WE, chunk or world-block writes. Exact-key repeats do not duplicate; collision with an existing structure/deposit changes only favorite=true, preserving its evidence. Cap rejects new manual records instead of deleting old ones.
- Actual metadata normalizers are patched to preserve WAYPOINT/MANUAL on save/load while retaining existing GEOLOGY/STRUCTURE and FIELD/RADAR behavior. No schema version bump or migration. Waypoints have no research analysis; GUI shows SAVED COORDINATES and LOC, not fabricated structure/geology findings. Visible in ALL and FAVORITES, excluded from STRUCTURES/GEOLOGY.
- Shared favorite cache invalidated after saving. Link/session reset clears save feedback. Existing TARGET/ROUTE code reused and unchanged by the save action. SEND TO SHIP is not implemented in this slice.

## Verification

- Two full builds passed with JDK21, ASM9.8 and hash-verified `.57.0` baseline. Nine smoke suites, plus Navigation against real DoctorWhoMod engine formula, passed. Intentional bad-config/malformed-wire messages are test fixtures, not game errors.
- New suite verifies config defaults/live bounds/fallbacks, disabled/link/interior/cooldown policy and exact boundary, dedup, dimension-separated keys, cap refusal, actual metadata save/load normalization, existing ore evidence preservation, untouched selectedKey/route fixture and status reset. Navigation suite verifies button/status text within GUI bounds.
- New .69.2 game session, world-reload persistence, visible button, actual write and multiplayer/reconnect behavior have NOT been tested. Full Stage 9 remains partial.
- Accepted `.69.1` repository and instance copies were hash-verified and backed up under `backups/custom-mods/pre-save-location-20260928-01/`, with retired originals and previous config/README. Earlier known-good backups retained. Only core JAR and Navigation config/README changed at runtime; DoctorWhoMod/worlds/unrelated configs untouched.
- Repo/runtime single custom JARs, lock sizes/hashes, source/embedded manifest and version checked; generated mod list and continuity documents updated. Only project-owned JARs and scoped source/docs/config are committed; no worlds/logs/caches/backups.

## Next executable step

Outside TARDIS, near it with CONNECTED: NAVIGATION → SAVE CURRENT LOCATION, then FAVORITES. Verify LOCATION coordinates match the player's block, existing target/route stay unchanged, repeat after 2 seconds creates no duplicate, and record survives world exit/re-entry as WAYPOINT/MANUAL. See `docs/15_Player_Navigation_Runtime_Kabul.md`. After acceptance continue SEND TO SHIP.
