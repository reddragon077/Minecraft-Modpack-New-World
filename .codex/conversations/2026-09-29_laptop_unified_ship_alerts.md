# 2026-09-29 — laptop — Unified Ship Alerts

Status: verified automatically; runtime acceptance pending
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.72.1-alpha-unified-ship-alerts.jar`
SHA-256: `2cba0f04b92420d34cc1e66837c7a5256efd9fb96ebba5e897d05729439bf6c2`; 3701750 bytes.

## User goal

All existing Overview warnings must also appear in Alert History. Continue in coherent batches; preserve accepted gameplay.

## Starting state

Clean `main` at `0f50310be52f39bb340778c4f1352761f530ddb1`, fast-forward pull already up to date. Both endpoints had .72.0 / SHA-256 `09395920e5a52f87f106399caca248df3bb662ca92c738937c75adb671a20ec2`. Screenshots show connected/on-board Overview FE 250K/2.25M with FE LEVEL LOW but History 0 ACTIVE and no events. Source confirmed .72.0 sampled WE but omitted FE and several other Overview warnings. This was a coverage gap, not evidence of a failed payload.

## Decisions

- Use the full raw Overview warning map before its three-row display limit. Do not independently duplicate energy/engine/matrix/mining warning policy.
- Shared FE/WE thresholds come from `overview.properties` (20%/5% defaults). Remove obsolete `ship-alerts.properties` warp threshold keys; runtime config was checked against the prior shared source before replacing it.
- Severity follows Overview: Matrix offline and BUFFER_FULL WARNING, broken engine and NO_ENERGY CRITICAL. Collection type-slot pressure remains 80% WARNING/100% CRITICAL; current destination drive checks unchanged.
- Unknown low-energy/matrix/engine/mining state does not resolve an old operational fault. Missing telemetry is a separate visible warning. ENGINE UNKNOWN and engine telemetry failure share one kind; WAITING reasons share a kind with Overview retaining the precise status.
- New discovery notifications remain deferred. No Navigation, flight, mining-control, save, Doctor or emergency cooldown changes.

## Changes

- `PlayerOverview0670`: immutable Inspection with complete raw warning map and unchanged Overview snapshot. Background measurement state is isolated from visible FE/t sampling intervals.
- `PlayerAlerts0720`: FE, Mining energy/wait, meter/energy/matrix/engine/mining/navigation/exterior/partial telemetry kinds. Existing Collection/drive remain; unrecognized future Overview warnings get a fallback kind instead of being dropped. Wire schema2 widens known-mask from byte to int for 23 kinds; rejects invalid masks. This is not a saved-world schema change.
- Tests extend full warning mapping beyond three display rows, shared live thresholds/obsolete-key isolation, FE unknown/recovery, high-bit wire, FE HUD notice and production request integration.
- Manifest, lock/mod list, config inventory, roadmap, runtime acceptance guide and handoff updated. Last 16 session transitions / five rows per page remain bounded; not an unlimited world journal.
- Verified .72.0 originals and replaced runtime docs/config backed up under `backups/custom-mods/pre-unified-alerts-20260929-01/{repository,instance,instance-files}`. The intermediate .72.1 candidate was also preserved before final clock-isolated rebuild installation.

## Verification

### Passed

- Full `tools/build-newworldcore-geology-patch.ps1` build: sixteen suites, actual Doctor entrance portal bytecode contract, actual Doctor WE estimate fixture. Final build hash above.
- Production sampler/request path with external game fixtures: low FE at 11/100 appears despite other concurrent warnings, missing FE telemetry does not generate false recovery, restored energy resolves both low FE and telemetry warnings. Denied link and throttling remain enforced.
- Background poll does not touch visible Overview sampling state. Unknown-preserving History, bounded wire/malformed inputs, high-mask round trip, notifications/dedup/escalation/recovery/layout and read-only writer audit pass.
- Installation with no Java/game process; one hash-matching NewWorldCore per endpoint, embedded/source manifest match. Thirty-one protected world data/player/level files byte-identical across deployment; Doctor hash remains `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.

### Failed / repaired

- .72.0 low-FE History coverage was visibly absent. No .72.1 automatic test failed; malformed-input rejection output is intentional negative-test coverage.

### Not tested

- Actual .72.1 gameplay FE/HUD/RESOLVED/reopen acceptance, extended multiplayer/protection/load cases. Earlier healthy History rendering does not prove these. Prior accepted Navigation/Mining/Emergency tests remain accepted; no duplicate runtime test is requested.

## Next executable step

Open the game: if FE remains below 20%, compare Overview with ALERT HISTORY and watch the small HUD notice. Let energy refill normally to verify RESOLVED; close/reopen GUI to retain the same session history. No save/energy mutation needed for testing. Stage 12 stays partial pending this evidence.

## References

- `docs/18_Ship_Alerts_Runtime_Kabul.md`
- `docs/12_Gelistirme_Yol_Haritasi.md` — Stage 12
- `2026-09-29_laptop_ship_alerts.md` — historical .72.0 batch
