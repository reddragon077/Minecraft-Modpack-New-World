# 2026-09-10 — laptop — Navigation runtime evidence and GitHub handoff

Status: verified synchronization evidence; partial runtime acceptance
Starting branch/commit: `main` / `8906c6d`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.69.0-alpha-player-navigation-view.jar`
SHA-256: `af77b62a691a18bf6340c7371647b0c90e07b7643773cf8576958aae29c8427f`, 3635246 bytes.

## User goal

Save all current project work and test progress to canonical GitHub main without omissions. No new feature/build requested.

## Starting state

Clean main; fetch and fast-forward synchronization reported already up to date. `.69.0` code, tests, JAR, config and lock were already committed/pushed in `8906c6d`. Two new runtime screenshots needed recording.

## Decisions and changes

- Record partial acceptance, not full Stage 9 completion. Update memory, handoff, resume prompt, index, roadmap, README and runtime test guide. No JAR/source/config changes or rebuild.
- Preserve existing worlds and known-good JAR backups. Do not run broad refresh over runtime-generated client data.
- Runtime differences excluded intentionally: Indigo and WorldEdit generated timestamp comments; FTB Chunks client minimap entity-icon entries (local client state/preferences). No NewWorldCore property differences. Runtime-only cache/fingerprint/backup/web-server files are ignored and not project changes.

## Verification

### Passed

- Screenshot `codex-clipboard-d7ac5d44-ed22-4beb-bc7a-0df9e919cf8e.png`: LIVE / READ ONLY, Aluminum `[-2456,40,328]`, ship distance154; loaded hop `[-2456,61,328]`, distance152, 1/1, estimate48 WE, available1000 WE.
- Screenshot `codex-clipboard-67eacf73-4b70-4fe2-85e5-83171563b447.png`: after TARGET/ROUTE, Carbon `[-2376,32,376]`, distance220; loaded hop `[-2376,68,376]`, distance219, 1/1, estimate52 WE. Header player-to-ship distance12 is distinct. UI is readable.
- Local latest.log: Navigation Aluminum at17:19:37.088; Carbon TARGET at17:24:03.242; ROUTE ready=true at17:24:04.011; Navigation Carbon at17:24:04.968. No Navigation sampling/render/snapshot/decode failure found. Log itself stays local.
- Both endpoints have one NewWorldCore and one DoctorWhoMod; filenames, SHA-256 and sizes equal pack-lock. DoctorWhoMod unchanged: `DoctorWhoMod-1.21.1-NeoForge-1.0.16-NewWorld-EngineTravel-v5.8.19-Tall-Large-XLarge-Swap.jar`, SHA-256 `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`, 4782531 bytes.
- Compared 607 tracked config/defaultconfigs/kubejs paths; none missing (Unicode filename checked unquoted). Only the three non-gameplay/client-state differences above. All 275 installed-addon IDs/file IDs/enabled flags match lock. No unignored runtime-only files in these directories. Lock/mod list therefore need no binary/version change.
- Earlier eight smoke suites and actual-engine fixture passed at build time; not rerun for documentation-only changes.

### Issues observed outside this feature

- Third-party EMI/JEI ChemicalStack ingredient and smithingtemplateviewer startup errors remain in the log. No attribution to Navigation and no fix attempted in this save-only turn; do not describe the entire game log as error-free.

### Not tested

- Explicit GUI close/reopen: suggested but no user confirmation before save request.
- Navigation-specific range loss/recovery, cross-dimension, empty/completed/multi-hop routes, live presentation config, actual travel cost debit, multiplayer/stale network scenarios.

## Next executable step

Confirm GUI close/reopen returns current target/route; continue remaining checks in `docs/15_Player_Navigation_Runtime_Kabul.md`. Then remaining Stage 9 favorite selection, SAVE CURRENT LOCATION and SEND TO SHIP. Do not duplicate accepted Discovery TARGET/ROUTE or skip to Stage 10.

## References

- `2026-09-10_laptop_navigation_view.md`: implementation/build/install evidence.
- `2026-09-10_laptop_link_acceptance.md`: preceding accepted Ship Link regression.
- `docs/12_Gelistirme_Yol_Haritasi.md`: canonical stage order.
