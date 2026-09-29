# Stage 11 Emergency / distress beacon

User authorized continuing in roadmap order, in coherent batches, after confirming all four .70.1 basic Mining checks.

Initial inspection: main clean at 12574ad and fast-forward synchronization current. Installed/repository .70.1 remains unchanged at this gate. Existing Doctor teleporter has a configured same-level block destination; console room has a template getter but no verified emergency-return destination/transaction. Do not turn this into an unguarded teleport or silently return to a different room.

First Stage 11 package: Emergency UI, server-authoritative current-coordinate distress waypoint/target, two-step confirmation, owner/link/interior/config/cooldown checks. Reuse Discovery persistence/target selection; no route calculation, flight, WE consumption or teleport. Actual RETURN remains explicitly unavailable and open within Stage 11. Stage 10 extended buffer/link/config/multiplayer tests and Stage 14 reserve dependency remain open.

Gate commit: `342cab9`, before implementation. Runtime acceptance pending.

## Implemented / verification

Installed `.71.0-alpha-emergency-beacon`, SHA-256 `17ddc1b310b9926ee72ff65cc0d7000d1a5687747307d03b3fc24170881c7ea3`, 3675910 bytes. Recompiled helper + dispatch/render/reset hooks; existing screen header enables sixth tab. Separate bounded frame namespace. First click issues a single-use challenge, second checks the same player/ship/world/dimension/block plus current owner/link/alive/outside-interior/live policy. Confirmation 100 ticks (client cap 2 seconds), cooldown 200 ticks, refresh20/stale120. Shared target permission and waypoint quota apply. Beacon independent of normal SAVE enabled switch. Coordinate collision retains existing record/evidence/name; no replacement of discovery history. Successful save selects via the existing selectedKey writer and invalidates favorite cache. No route/flight/teleport/WE mutation. Errors fail closed; failed persistence attempt also consumes confirmation/cooldown.

All 14 suites and actual DoctorWhoMod engine-estimate fixture passed via tools/build-newworldcore-geology-patch.ps1. New fixtures cover config bounds/live permissions, first-click no write, single-use/replay/cooldown, timeout/too-fast/moved/dimension/ship/world/player/denied/stale checks, dedup/evidence/quota/route preservation, bounded malformed wire, lifecycle, layout and production dispatch/render hook audit. Production context reads existing owner resolver and rejects any TARDIS interior; real Minecraft runtime/multiplayer not claimed. Verified actual PlayerShipScreen survey wrapper and enabled sixth-tab bytecode using javap.

## Deployment / next

No Java/game process during install. Both old .70.1 originals copied and hash-verified, then retired without deletion under `backups/custom-mods/pre-emergency-beacon-20260929-01/{repository,instance}`. Navigation save from New World (3) copied and verified unchanged, SHA `c16c1a3efbc4ea2aff18dc8d70bdba3d21abce1c8f861ecc11c52e1e9b275fcd`. Replaced runtime records backed up. One matching core each endpoint. No world edits, save schema migration or Doctor change. New config copied to runtime. Next grouped test in docs/17: outside stationary beacon -> selected position and unchanged route -> cooldown/movement denial -> reopen/world persistence and on-board unavailable. Actual Return/teleporter/protection/WE transaction remains open, not a Stage 12 handoff.
