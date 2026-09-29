# 2026-09-29 — laptop — Mining yield and stop

Status: in progress. Branch: main; starting commit 6216a54.

## User goal
Continue Stage 10 after closing the game; retain coherent batches.

## Starting state / acceptance
.70.0 screenshot parity with physical terminal: 977 mined, same old scan area, 100% scan and empty Collection. Then SCANNING at [-154,11], 7.8%; then MINING/EXTRACTION, scan 100%, 347 mined (1%). Replication counts 1380→1178→1667; routed REP 4071→4835. Log statuses at 11:29:51, 11:44:30, 11:47:01 agree; no Player Mining failure found. Server stopped 11:48:34. Reopen/link recovery/full buffer parity not claimed passed.

## Decisions
Implement per-resource actual successful mining counts and persistence/ranking, plus confirmed owner/link/ship-scoped emergency stop. Stop uses existing setShieldsMiningState(false), never toggles on, changes handbrake or starts flight. Old saves cannot provide historical per-resource counts: report tracking scope honestly. Deposit remaining remains open pending real reserve integration; scan leftovers are not deposit reserves.

## Verification / next
Implementation, automatic tests and new runtime acceptance pending.
