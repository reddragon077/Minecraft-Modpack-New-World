# 2026-09-29 — laptop — Ship Alerts batch

Status: automatic verification passed; runtime acceptance pending
Branch: main
Build: `NewWorldCore-1.21.1-NeoForge-0.5.72.0-alpha-ship-alerts.jar`, SHA-256 `09395920e5a52f87f106399caca248df3bb662ca92c738937c75adb671a20ec2`, 3698089 bytes.

## User goal and decision

Proceed in coherent batches. Stage 12 authorized: low WE, Collection pressure, Engine/Matrix/required drive, GUI history and small HUD notifications. Preserve accepted Emergency behavior. Discovery alerts deferred.

## Starting evidence

Installed .71.4 entrance build unchanged. Fifteen baseline suites, real Doctor portal contract and WE formula fixture rerun passed. Source review finds no Emergency Navigation/route/WE writer. Production adapter fixtures verify failed/vetoed returns charge no cooldown. Actual-game invariance and multiplayer/death/protection cases remain open; fixture checks are not runtime acceptance.

## Next executable step

Run one grouped new-alert acceptance session from docs/18; do not repeat already accepted door/relogin/Mining/Navigation checks. Do not claim unobserved low-WE/full-buffer/drive cases as passed. Discovery alerts deferred; Stage 13 not started.

## Implementation and verification

- Existing client post-tick subscriber polls the server even before GUI opens. New read-only poll is independent of client GUI link state; server re-resolves ownership/range/dimension each request. UI/history separate from screen-bound Ship Link resets.
- Low WE 20%/5%, Collection virtual type slots 80%/100%, true BUFFER_FULL, three offline matrices, broken engine, required flight-destination drive. Slot3/4 travel policy verified against actual Doctor gate bytecode. Unknown modules/room/buffer data remain unknown, not fake missing or recovery.
- Last 16 transition events, 5 rows/page under Overview; no persistent history schema. GUI reopen preserves history, actual logout clears it. Vanilla action-bar notices deduplicate, throttle same-kind repeats, allow critical escalation, cancel resolved/unknown queued notices and preserve watermark across temporary same-ship link loss. Dimension transition invalidates displayed telemetry without replaying old notices.
- Sixteen smoke suites plus actual Doctor portal contract and WE formula fixture passed. New suite covers production sampling/request adapter with external game boundaries as fixtures, denied link, request throttle, pure rules, live config bounds/defaults, malformed/oversized wire, real payload codec dispatch, layout/paging and post-tick injection. Fixture coverage is not real game acceptance.
- Prior Emergency source reviewed: no Navigation/route/WE writer. Failed/veto adapter paths do not charge cooldown. Actual-game invariance/extended cases remain open.
- README had stale .69.3 installed-build text, conflicting with actual .71.4 starting files; reconciled to .72.0 candidate while retaining historical evidence.
- Backup location: runtime `backups/custom-mods/pre-ship-alerts-20260929-01/` with repository/instance originals. No save edits, cooldown reset or Doctor JAR change. Navigation data hash retained `90b2551acb25b4786b9877a9b515bf5a782bdfa6dab8a314e37ac03807a0902b`.
- Deployment verified with no Java/game process: one matching NewWorldCore at each endpoint, source/embedded manifest identical, Doctor hash unchanged and all 31 protected data/player/level save files byte-identical before/after. Original .71.4 JARs moved out of active mods only after verified backup copies; they remain recoverable in the backup directory.
