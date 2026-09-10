# 2026-09-10 — laptop — Ship Link candidate

Status: in progress — installed; runtime acceptance pending
Branch/base: main / b16fc64 (Overview acceptance recorded before implementation)
Build: NewWorldCore-1.21.1-NeoForge-0.5.68.0-alpha-player-ship-link.jar
SHA-256: 5637cae64a53e92140338b31d761f673f0a84c82f0fa2e8d37ceb457480c58b3
Size: 3625294 bytes

## User goal

After confirming every presented Overview check, user authorized Stage 5 Ship Link. GitHub main was clean and fast-forward synchronization was already current at 39eabbf. Acceptance reconciled in b16fc64 before changing code.

## Decisions and changes

- Existing loaded-owner resolution is retained, with preference for the player's own interior and explicit owner recheck. No new persistent ship binding or world schema, no chunk loading.
- CONNECTED on board or within 3D exterior range, DIMENSIONAL across dimensions when allowed, LOST otherwise. Header visible on all Player GUI tabs. Lost/stale content is covered and clicks blocked; tab switching remains available.
- Server-side dispatch executes on server thread; read/write gates cover Survey and Discoveries, Overview resolves only a linked ship. Delayed surveys recheck at execution. Server rejects client-sent snapshot/status codes. Existing route engine is reused with the linked interior as context so exterior requests do not resolve a different world/ship.
- Client snapshot reset on screen/connection/player/world changes and ship/loss transitions; Discovery list reloads after reconnect. Server selection indexes are scoped to ship identity.
- `ship-link.properties`: range_blocks=5000, allow_dimensional_link=true, refresh_ticks=20, stale_after_ticks=120; bounded live reload and Turkish comments. Server sends refresh/stale timing to client. Overview refresh20 and thresholds20/5 unchanged.
- Link loss does not cancel already running ship Mining/route work. It prevents new Player GUI remote operations. No Stage 9 redesign.

## Verification

Passed: build via existing tools/build-newworldcore-geology-patch.ps1, verified .57.0 baseline, JDK21/ASM9.8. All seven smoke suites passed. New tests cover inclusive range boundary, vertical distance through real reflection adapter, own/foreign interior ownership, unloaded/unknown ship, dimensional toggle, numeric bounds/invalid fallback/live reload, wire roundtrip/malformed/oversized frames, stale/reopen invalidation, content click gate and header geometry/buffer flushes. Negative-fixture warning/EOF output is intentional.

Bytecode shows PlayerShipScreen beforeRender/afterRender hooks. Installed DoctorWhoMod API signatures verified for owner/id/world/exterior resolution. Source/embedded manifest exact match, repo/live single-JAR and SHA-256 checks passed. Automated tests do not replace real multiplayer or GUI runtime acceptance.

## Installation and backups

No Java process found at installation. Accepted .67.1 SHA-256 0d08367a747efc6fa94d41270793b96de897ebb0059a2de5599c9c322badbac4 was copied and hash checked before moving originals to laptop backups/custom-mods/pre-ship-link-20260910-01/. Original repository/live JARs and config README are preserved there. Only new core JAR, ship-link config and config README deployed; other configs/worlds/logs/caches untouched. Older known-good JARs remain.

Lock, generated mod list, README, test guide, roadmap, memory, handoff and this record accompany the candidate. Runtime acceptance is NOT yet claimed.

## Next executable step

Open Player GUI on board, then outside and verify distance. Follow docs/14_Ship_Link_Runtime_Kabul.md for temporary range32 LOST/recovery, dimensional policy, delayed Survey and exterior FAV/TARGET/ROUTE regression. Restore range5000. Advance to remaining Stage 9 only after Stage 5 acceptance.
