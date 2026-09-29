# 2026-09-29 — laptop — Public documentation closeout

Branch: main
Scope: user requested pausing development and updating GitHub information for readers.
Build unchanged: NewWorldCore 0.5.72.1-alpha-unified-ship-alerts, SHA-256 `2cba0f04b92420d34cc1e66837c7a5256efd9fb96ebba5e897d05729439bf6c2` (3701750 bytes).

## Acceptance evidence

The latest screenshot shows FE LEVEL LOW and telemetry/matrix warnings as RESOLVED, 0 ACTIVE / 0 UNKNOWN and history pagination. The subsequent user message confirms the additional HUD notification and GUI close/reopen checks were fixed. Basic .72.1 FE/history/recovery/HUD/reopen gate is accepted; do not request it again.

This does not prove all warning kinds, buffer/drive, alerts link/logout, multiplayer/protection/load cases. Stage 12 remains partial; new discovery notifications are deferred. Previous Navigation/Mining/Emergency basic acceptance stays accepted; extended gates, including Emergency target/route/WE in-game comparison, remain open.

## Documentation changes

- Added public player guide and human-readable changelog; linked them from the main README and document map.
- Reconciled mechanics/game loop/Warp, current build and alpha status, setup/sync guidance, config explanations and regression references.
- Replaced obsolete Navigation-next and Emergency Teleporter Room/beacon instructions with current accepted behavior. Structure Survey shared default remains 20 tick; Geological remains 80 tick.
- Reconciled roadmap, Known Issues and acceptance guides. Preserved historical implementation evidence without treating old pending notes as current tasks.
- Replaced accumulated HANDOFF with a concise current baton; updated memory, continuation prompt, index and workflow gate guidance. Old handoff text remains recoverable in Git history and dated records.

## Preservation and verification scope

Started from clean main at `a7f1302190ef38cc602d5ec26795f5e1c5e38de8`; fast-forward pull already up to date. This is documentation only: no new build, source implementation, config values, lock/manifest, JAR, world/save or cooldown edits. Last actual .72.1 build passed 16 suites plus Doctor entrance portal/WE fixtures; these were not rerun for prose changes.

Existing runtime copies of changed documentation are preserved under `backups/documentation/pre-public-docs-20260929-01` before scoped copying. No broad instance refresh/apply is used. Desktop is registered but was not revalidated in this closeout.

Read-only closeout checks passed: 23 changed/new documentation files and 113 local Markdown links; normal git diff whitespace validation; one current NewWorldCore and Doctor JAR per endpoint with matching expected hashes. Existing runtime documentation matched canonical HEAD (or the intended current text), with no unrelated local documentation conflict found.

## Resume only when requested

Verify clean fast-forward main and current installed hashes; read memory/HANDOFF/index first. Resume Stage 12 remaining scope in coherent 3–4-item batches. Do not repeat accepted tests, reinstall automatically or reset cooldown. Stage 13/14 remain later; scan progress is not deposit remaining reserve.

References: [player guide](../../docs/19_Oyuncu_Rehberi.md), [alerts acceptance](../../docs/18_Ship_Alerts_Runtime_Kabul.md), [roadmap](../../docs/12_Gelistirme_Yol_Haritasi.md).
