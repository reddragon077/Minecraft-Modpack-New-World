# 2026-09-29 — laptop — Mining yield and stop

Status: implementation, backed-up installation and four-item basic runtime acceptance complete. Branch: main; starting commit 6216a54. Acceptance reconciliation committed first as 4bda89e; implementation a8840b0.

## User goal
Continue Stage 10 after closing the game; retain coherent batches.

## Starting state / acceptance
.70.0 screenshot parity with physical terminal: 977 mined, same old scan area, 100% scan and empty Collection. Then SCANNING at [-154,11], 7.8%; then MINING/EXTRACTION, scan 100%, 347 mined (1%). Replication counts 1380→1178→1667; routed REP 4071→4835. Log statuses at 11:29:51, 11:44:30, 11:47:01 agree; no Player Mining failure found. Server stopped 11:48:34. Reopen/link recovery/full buffer parity not claimed passed.

## Decisions
Implement per-resource actual successful mining counts and persistence/ranking, plus confirmed owner/link/ship-scoped emergency stop. Stop uses existing setShieldsMiningState(false), never toggles on, changes handbrake or starts flight. Old saves cannot provide historical per-resource counts: report tracking scope honestly. Deposit remaining remains open pending real reserve integration; scan leftovers are not deposit reserves.

## Implementation

- `.70.1-alpha-mining-yield-stop`, SHA-256 `01bba56cc52ba9e7864ef2e1814ba38fa0d00cf966718d6fde1dc1f78653032d`, 3660734 bytes.
- Exact successful `MiningPhaseRuntime.extract` minedTargets increment records the primary resource ID once per mined resource block. Hazard, skipped targets and unsuccessful extraction do not count. Counts are not drop-stack quantities.
- Namespaced optional compound `NewWorldMiningYieldV1` in existing phase save; bounded valid IDs, saturating longs, deterministic top ranking, partial legacy-history label. Original fields retained, no manual world edits. Production resetArea clears ledger. Old builds ignore the namespace and can lose only the new breakdown on subsequent save; preserve backups before any downgrade.
- Player Mining schema2 includes bounded resource rows and server stop-enabled flag; main/details page and existing link/session/freshness reset. MODE10 requests a short-lived server challenge. Single-use negative token confirms same player/current owned ship with allowed link, loaded interior and recent successful telemetry; recheck live config/cooldown/expiry. Only `setShieldsMiningState(false)` can mutate game state. No arbitrary client-supplied target IDs or restart path. Main shields, handbrake, route, flight and buffer routing unchanged.
- Config-first: top_resources.rows=5 (1-5), stop.enabled=true, stop.confirm_ticks=100 (40-200), stop.cooldown_ticks=40 (20-1200), all live. Client confirm window capped at two seconds.

## Verification

Thirteen smoke suites and real DoctorWhoMod WE fixture passed. New tests cover actual patched phase State reset, production save/load bodies with substituted NBT/storage boundaries, legacy missing namespace, invalid IDs/negative values/overflow/tie ordering, unrelated-field preservation, counter injection after successful increment, live defaults/clamps, bounded protocol and detail layout. STOP tests cover first-click no write, OFF-only, unrelated-state preservation, duplicate/replay, cooldown, expiration/too-fast confirmation, wrong player/ship/token, link loss, stale view, disabled config, changed ship and failing setter. These are automated fixtures, not a Minecraft multiplayer/runtime acceptance claim.

## Deployment

No java/javaw process before or during install. Verified .70.0 repo/runtime SHA `826b667f...`; originals copied and retired under `backups/custom-mods/pre-mining-yield-stop-20260929-01/{repository,instance}`. Sixteen Mining saved-data files across four worlds and replaced runtime records/config were copied with SHA verification. Save hashes remained unchanged. Single identical .70.1 installed each endpoint; DoctorWhoMod unchanged. Build output/source/lock version and embedded manifest verified at handoff. No saves/logs/backups entered Git.

## Next

The four-item basic runtime session is accepted; do not request it again. Full buffer parity, Mining link recovery and extended multiplayer/config cases remain open. Stage 10 remains partial because true reserve percentage is a separate Stage 14 dependency. Next implementation batch requires roadmap review; Stage 11 has not been started in this acceptance-only turn.

## Runtime acceptance follow-up

User supplied screenshot `codex-clipboard-505d8b01-4e60-4559-a49a-4f9edb96cbc2.png`: MINED RESOURCES, ON BOARD, 120 BLOCKS / minecraft:raw_iron, SINCE UPDATE / 761 EARLIER UNTRACKED, MOVED AE934/REP6011. Then, explicitly asked whether confirmed STOP, world exit/re-entry persistence and physical restart also passed, answered "hepsi tamam kanka". Record all four basic checks passed by user report, with the resource view additionally screenshot-backed. No new log or saved-NBT audit performed; do not infer multiplayer, timeout, full buffers or reserve acceptance. Documentation only: no build, JAR/config/world mutation or test rerun needed.
