# New World current handoff

Updated: 2026-09-29
Machine: laptop
Branch: main
State: **Paused at user's request; documentation closeout, no new gameplay work.**

## Current build

- NewWorldCore: `NewWorldCore-1.21.1-NeoForge-0.5.72.1-alpha-unified-ship-alerts.jar`
- SHA-256: `2cba0f04b92420d34cc1e66837c7a5256efd9fb96ebba5e897d05729439bf6c2`; 3701750 bytes.
- Doctor: `DoctorWhoMod-1.21.1-NeoForge-1.0.16-NewWorld-EngineTravel-v5.8.19-Tall-Large-XLarge-Swap.jar`
- SHA-256: `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- Minecraft 1.21.1 / NeoForge 21.1.235. Canonical clone: D:\Projects\Minecraft-Modpack-New-World. Runtime: C:\Users\suley\curseforge\minecraft\Instances\New World.
- GitHub main is canonical; desktop is registered, not revalidated during this closeout.

## Accepted versus open

- Overview/Ship Link basic single-player acceptance retained, including loss/recovery.
- Discoveries actions accepted. Navigation view/reopen, favorite selection, current-location dedup/persistence, SEND TO SHIP preserving old route, physical terminal/manual final Y and distance accepted.
- Mining basic status/scan/extraction, resource counts, confirmed STOP, same-area persistence and physical restart accepted. Counts are mined blocks, not drops or deposit remaining.
- Emergency .71.4 normal portal arrival **inside main entrance**, successful 30-real-minute cooldown, countdown and world re-entry persistence accepted by user/screenshots/log. Beacon is canceled; separate Teleporter Room destination superseded. No automatic cooldown reset.
- Ship Alerts .72.1 **basic FE/history/recovery accepted** by screenshot: FE LOW and telemetry/matrix warnings appear RESOLVED, 0 ACTIVE / 0 UNKNOWN, history pagination. User subsequently confirmed HUD notification and GUI close/reopen were fixed. Do not request these basic tests again.
- Last build passed 16 suites plus actual Doctor entrance portal/WE fixture. This documentation turn did not rebuild.
- Stage 12 remains partial: new Structure/Geological discovery notices not implemented; all warning kinds, full-buffer/drive, alerts link/logout, multiplayer/protection/load tests are not newly accepted. Earlier Navigation/Mining/Emergency extended tests and Emergency target/route/WE in-game comparison remain open. Automated/source proof is not runtime acceptance.
- Stage 13 advanced deposit variations and Stage 14 actual reserve/depletion ledger remain later work; no Remaining % inferred from scan leftovers.

## Behavior to preserve

All raw Overview warnings feed bounded session history/HUD; FE/WE thresholds only in overview.properties (20%/5%). Unknown data does not resolve old faults. Collection type slots and current flight destination drive checks remain. History defaults to 16 transitions / 5 rows, survives GUI reopen, clears on logout; HUD is vanilla action-bar with dedup.

SAVE CURRENT LOCATION only saves a favorite; SEND TO SHIP only selects shared target; neither changes route/flight/WE. Emergency also leaves target/route/flight/WE untouched; confirmed STOP only turns Mining Shield OFF. Physical terminals retain detailed controls.

Structure Field Survey shared default is **20 tick / 1 second**, not historical 80; Geological remains 80 tick / 4 seconds.

## Preservation / handoff

Existing JAR backups remain under runtime backups/custom-mods, including pre-unified-alerts-20260929-01. Prior deployment verified 31 protected save files unchanged. This closeout changes documentation only; no JAR/config value/world/save/cooldown mutation. Existing runtime documentation is backed up before its scoped refresh.

Current human documentation: README, CHANGELOG, docs/19_Oyuncu_Rehberi.md; acceptance scope docs/15–18 and canonical roadmap docs/12.

## Next executable step — only after user resumes

Check clean branch/main and fast-forward synchronization, read project-memory, this handoff and newest conversation index record. Verify installed hashes before proposing runtime changes. Do not automatically install, reset cooldowns or repeat passed tests. Resume from **Stage 12 open scope**, keeping earlier extended gates explicit and grouping related work into 3–4 coherent items.

Historical implementation evidence remains in project-memory, dated conversation records and Git history. Newest closeout: conversations/2026-09-29_laptop_documentation_closeout.md.
