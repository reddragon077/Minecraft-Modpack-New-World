# Emergency feedback repair — 2026-09-29 laptop

User reported repeated DISTRESS clicks appeared ineffective; screenshot shows CONNECTED / 16 BLK, READY, X=-2457 Y=63 Z=190 and NO RESPONSE / CHECK CONNECTION. After diagnostic-only inspection, user explicitly authorized the fix and closed Minecraft. Canonical main was clean/current at bd54b6b before edits. No new feature/stage authorized here.

## Evidence and reproduced defect

Runtime latest.log: server SENT (1440004100) at 13:05:26.046 and 13:05:37.005; seven WAIT (1440004103) responses at 13:05:27.213/27.709/37.617/37.964/38.482/38.929/39.421. No Emergency exception or decode failure. Server-behind warnings exist, but do not prove where the actual client response was discarded. Game stopped at 13:09:13.511 and saved worlds. Do not claim disk target correctness solely from SENT; no NBT semantic audit this turn.

Added a regression to the unmodified .71.0 helper: arm pending response, age it six seconds, call the actual button timeout, then deliver SENT. It failed with `Late successful reply permanently discarded after UI timeout`. Existing code also reset pending actions on transient stale Emergency data. These are reproduced code defects; the precise live packet timing remains uninstrumented in .71.0.

## Repair

- .71.1-alpha-emergency-feedback; SHA-256 `3dae6836759aedc3f3d00b1985a525a70aaf3741be87b12d2bfb281a27fc0a46`, 3678605 bytes.
- Emergency-only bounded wire schema v2 adds a per-player weakly held receipt: sequence, request, result, remaining confirmation and cooldown. Receipt is recorded before response transmission; normal telemetry repeats it without running the action again. Transport failures cannot replace an already recorded success with FAILED.
- UI matches receipt to current ship, outstanding request and a newer sequence. Old/repeated/other-request results cannot rearm or settle a different confirmation. Reopen may show the current server cooldown but cannot replay a confirmation. Single-use server position/owner/config/expiry gates remain unchanged.
- Five-second delay now reports SERVER DELAYED / WAITING FOR RESULT without discarding pending acknowledgement. Temporary stale telemetry hides coordinates and disarms the click but preserves pending result recovery; real link/session/ship changes still invalidate through existing Ship Link hooks.
- Server-backed cooldown countdown blocks repeated clicks. A retry within the 200ms server throttle records WAIT for the next poll instead of silently abandoning the request. Client logs consumed final result codes for future diagnosis.
- No config defaults, world schema, waypoint writer, route, flight, teleport, WE or Mining changes. Existing refresh=20, stale=120, confirmation=100, cooldown=200 ticks remain. Both endpoints require this same core version. RETURN remains explicitly unavailable.

## Verification and installation

All fourteen build smoke suites plus actual DoctorWhoMod engine fixture pass. New checks cover late success after timeout; real PlayerDiscoveries payload routing of framed challenges/results; omitted immediate reply recovered from subsequent telemetry; stale-view recovery; old/duplicate/unrelated receipt rejection; expiry, cross-ship, reopen, policy denial, cooldown and malformed receipt bounds; rapid server-throttled retry. Existing gate/persistence tests still cover single-use, move/world/dimension/player/config/link changes, quota/dedup and route preservation. Synthetic tests are not new Minecraft acceptance.

Java/Minecraft absent before and during install. Hash-verified copies and retired originals of both .71.0 JARs preserved at runtime `backups/custom-mods/pre-emergency-feedback-20260929-01/{repository,instance}`. Runtime records and current Navigation save also backed up. Save: `saves/New World (3)/data/newworld_navigation_discoveries.dat`, 46410 bytes, SHA-256 `90b2551acb25b4786b9877a9b515bf5a782bdfa6dab8a314e37ac03807a0902b`; unchanged by installation. No saves/logs/backups/dist tracked. DoctorWhoMod unchanged.

Next: short grouped feedback retest in docs/17. Stage 11 basic runtime acceptance and extended cases remain open; actual Return is separate Stage 11 work, not Stage 12. Accepted Mining/Navigation checks are not repeated.
