# 2026-09-10 — Laptop Ship Link evidence and Overview reason repair

Status: `in progress` — build/install verified; new message visual acceptance pending.
Branch/start commit: `main` / `23ab28f2842948ba8a656171d97a9f9d1c30ed3d`

## Scope and sync

User authorized the next scoped repair and test-record reconciliation. Started on clean `main`, fetched and fast-forward-pulled; already current at `23ab28f2842948ba8a656171d97a9f9d1c30ed3d`. Read runtime memory, canonical rules, memory/handoff/index and prior Ship Link record. No Stage 9 features were added.

## Runtime evidence from .68.0

Screenshots and laptop log on 10 September show:

- 13:37:02.677 CONNECTED ON BOARD.
- 14:24:02.349 LOST OUT OF RANGE; screenshot 11770 blocks. 14:24:40.745 CONNECTED IN RANGE; screenshot 9 blocks and restored Overview telemetry.
- 14:26:28.833 DIMENSIONAL from Nether with ship in Overworld; no fabricated cross-dimension distance. At 14:28:10.282 live dimensional=false caused LOST / DIMENSION LINK DISABLED and blocked Discoveries. At 14:28:37.396 true restored DIMENSIONAL and list snapshots (`128/471`).
- Same ship `d3ea04e9-015a-4a3a-b192-aa19f17133ee`: favorite at 14:32:14.441, target at 14:32:16.100, route ready at 14:32:17.775, all for `minecraft:overworld|-2456|40|328`. Physical Navigation Terminal screenshot subsequently showed ALUMINUM-RICH DEPOSIT, READY_HOP1_LOADED and one hop. This verifies plan transfer, not a completed flight.
- First delayed Survey: queued 14:35:13.878 for 80 ticks, finished empty at 14:35:17.887, link lost 14:35:20.373. Not valid cancellation evidence.
- Second Structure Survey: queued 14:37:34.652 for 400 ticks; dimensional permission disabled and LOST at 14:37:46.037; delayed callback at 14:37:54.660 returned through `requireLink` before searching/writing. No positive identification/new-record result was observed. Wrapper `completed` is task completion, not successful survey. Evidence is timeline plus the source gate before discovery mutation; no before/after NBT audit was performed.
- Temporary values were restored: delay=80, dimensional=true (14:42:16.673 DIMENSIONAL). Backup test configs: `backups/config-tests/ship-link-restore-20260910-144210/`. Current range=5000, refresh=20, stale=120.

Core Stage 5 single-player checklist is accepted within this scope. Separate Geological delayed cancellation, positive Field Survey regression under the new gates, forced timeout/lifecycle edge cases and multiplayer isolation are not claimed as runtime-tested.

## .68.1 repair and verification

Overview previously discarded the denial reason by calling `linkedShip` and mapped all nulls to NO OWNED SHIP / LOADED INTERIOR REQUIRED. It now uses one authoritative `resolve` result, clears the inaccessible view and retains its reason as `SHIP LINK LOST // ...`. No access policy, configuration value, packet framing or saved-data schema changed.

Build: `NewWorldCore-1.21.1-NeoForge-0.5.68.1-alpha-ship-link-reasons.jar`

- SHA-256: `f4626881b44447184bf6003f81fb8586624c129929ecec0058cde9ae38a2db8c`
- Size: 3625579 bytes.
- Seven smoke suites passed. Added six cause cases, denied telemetry assertions, wire round-trip, headless GUI text and null fallback. Invalid config/EOF log messages during negative fixtures are intentional.
- Embedded `META-INF/newworld-geology-patch.json` matches the source manifest.
- Build command: `tools/build-newworldcore-geology-patch.ps1` with the hash-verified `.57.0` baseline under laptop `backups/custom-mods/known-good-0.5.57.0/`, Temurin JDK 21.0.12.1+1 and ASM/ASM-tree 9.8. Exit 0; all seven suites passed. `tools/update-mod-list.ps1` regenerated the custom-build row from the updated lock; `git diff --check` passed.
- Java was stopped during install. Original `.68.0` hash `5637cae64a53e92140338b31d761f673f0a84c82f0fa2e8d37ceb457480c58b3` verified before/after backup at `backups/custom-mods/pre-ship-link-reasons-20260910-01/`; repository-original.jar and instance-original.jar also preserved. Older backups remain untouched.
- Scoped replacement only: core JAR in repo/live. DoctorWhoMod and configs remain unchanged. No world/save/log/cache files added to Git.

## Next gate

User must visually confirm `.68.1` Overview shows OUT OF RANGE / DIMENSION LINK DISABLED for the actual loss reason and restores telemetry after reconnect. This new visual result is pending, not inferred from headless tests. Then continue remaining Stage 9 Player Navigation; preserve accepted TARGET/ROUTE integration.

## References

- `docs/12_Gelistirme_Yol_Haritasi.md`, `docs/14_Ship_Link_Runtime_Kabul.md`, `.codex/HANDOFF.md`.
- Prior implementation record: `2026-09-10_laptop_ship_link_candidate.md`.
- Lock, generated mod list, JAR test guide, README and continuity records were reconciled. The stale laptop resume prompt was updated to this gate and instructed to defer to newer verified records.
