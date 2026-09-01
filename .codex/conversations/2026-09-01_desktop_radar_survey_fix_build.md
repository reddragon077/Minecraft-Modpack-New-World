# 2026-09-01 — desktop — radar and Field Survey isolation fix build

Status: `in progress`
Branch/starting commit: `main` / `c4a4b28`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.5-alpha-radar-survey-isolation.jar`
SHA-256: `8c85c3569f9926addd68978c2bca617c57c2a699bec1deac9dcf8a9e4d4b652b`

## User goal

Fix the non-existent `MODDED STRUCTURE` destination and prevent Field Survey from reporting geology deposits as structures after the desktop acceptance test exposed both defects.

## Root cause

- Multiple registry structures can share one placement object. `PlacementTask.family()` returned the literal `MODDED STRUCTURE` whenever that placement contained more than one family.
- Four physical geology deposits are implemented as NewWorldCore jigsaw structures with `*_deposit` registry paths. Field Survey scanned all valid structure starts, so `copper_sulfide_deposit` was treated as a building.
- The earlier bad survey created a persisted `STRUCTURE` copy of the geology label, so prevention alone was insufficient; a narrow migration was required.

## Changes

- Shared multi-family placements now remain `UNKNOWN STRUCTURE` until an actual structure is confirmed.
- NewWorldCore `*_deposit` and `geology/` structure registry paths are excluded from radar placement tasks and Field Survey.
- Invalid persisted discoveries are removed only when `kind` is `STRUCTURE` and either the label is `MODDED STRUCTURE`, or both the class is `NEWWORLDCORE` and the label ends with ` DEPOSIT`; real `GEOLOGY` records and other mods' deposit-named structures are not removed.
- The cleanup runs before radar results are persisted and before a Field Survey records nearby structures.
- Patch version advanced to `0.5.59.5-alpha-radar-survey-isolation`.

## Verification

### Passed

- Rebuilt reproducibly from Git commit `6253b28`'s verified `0.5.57.0` baseline (SHA-256 `09a140f93824ca37abe85b31a384b9398f4b064c3f7c37534569bfca956351fc`).
- Java compilation, JAR validation, mod version metadata, patch manifest, ASM entry-point replacement, and helper-method presence.
- Smoke test: geology registry filtering, geology-label filtering, family normalization, real modded-family retention, and two-family fallback to `UNKNOWN STRUCTURE`.
- The four duplicate legacy class entries are unchanged from both the `0.5.57.0` baseline and `0.5.59.4`; the new patch added no duplicate entry.
- Repository and desktop CurseForge instance each contain exactly one matching `0.5.59.5` JAR.
- Previous desktop `0.5.59.4` was backed up under `backups/custom-mods/pre-apply-20260901-211819`.

### Failed

- No build or static test failed after the sandbox-only JDK file-access issue was rerun with appropriate filesystem access.

### Not tested

- Game startup and existing-world load with `0.5.59.5`.
- Runtime cleanup of old false discoveries.
- Absence of `MODDED STRUCTURE` and geology deposits from the Structure list.
- Field Survey at Abandoned Camp returning the real structure without Copper Sulfide Deposit.
- Preservation of the genuine Copper Sulfide entry in the Geology/Deposits view.

## Next executable step

Launch the desktop instance, run one radar scan, then perform Field Survey at Abandoned Camp. Inspect both Structure and Deposits lists plus `latest.log` to confirm cleanup, isolation, real geology preservation, task count, result count, and duration.

## References

- `../HANDOFF.md`
- `../project-memory.md`
- `../../docs/Known Issues.md`
- `2026-09-01_desktop_structure_radar_acceptance.md`
