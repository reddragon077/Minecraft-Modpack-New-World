# New World current handoff

Updated: 2026-09-01
Machine: desktop
Branch: `main`

## Current objective

Close Radar v2's modded family test with `CAMPSITE` and `ARCHEOLOGIST CAMP`; `ABANDONED CAMP` already passed.

## Exact state

- The GitHub repository was cloned to `E:\projects\Minecraft-Modpack-New-World`.
- The desktop CurseForge instance was registered at `C:\Users\suley\curseforge\minecraft\Instances\New World`.
- Repository config, defaultconfigs, KubeJS files, and the two current project-owned mod JARs were applied to that instance.
- The desktop NewWorldCore JAR matches the repository SHA-256: `4b97a91af5d7c45aabb19c0f1fe4223141a4ffa0800878e21bca24ce2aefa8d0`.
- The desktop DoctorWhoMod JAR matches the repository SHA-256: `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- The older desktop custom build and previous applied directories were preserved under `backups\desktop-sync-pre-20260901-201500`.
- `tools/apply-to-instance.ps1` now moves stale NewWorldCore/DoctorWhoMod versions into a timestamped backup before installing the current pair.
- The desktop game and existing world load successfully. Structure Radar is actively finding vanilla and modded candidates.
- User-provided screenshot evidence shows `Discoveries 101`, `Favorites 0`, `Visited 6`, and many result rows. Visible modded classes include Explorify, Better Dungeons, and Structory.
- `0.5.59.5` runtime acceptance removed 17 invalid records and Field Survey identified five real structures without any geology deposit. The visible discovery list no longer contained `MODDED STRUCTURE`.
- The removed discovery was still selected, and cleanup set `selectedKey` to null; `NavigationDiscoverySavedData.selected()` calls `isBlank()` directly, producing repeated arrival-check exceptions.
- `0.5.59.6` uses the required empty-string sentinel when clearing that selection. The prior `0.5.59.5` desktop JAR is preserved under `backups\custom-mods\pre-apply-20260901-221120`.
- `0.5.59.6` runtime acceptance passed: the UI showed `NO TARGET SELECTED`; over 20 minutes of `latest.log` contained zero `0471g arrival check` and zero `InvocationTargetException` entries.
- The Deposits view still showed 28 genuine geology records, including two visible `COPPER-RICH DEPOSIT` rows. False-structure cleanup did not remove the real `GEOLOGY` data.

## Test status

- `0.5.59.4` desktop launch, >4 result list, modded-class retention, 101-result scan completion in about 5.2 seconds, and real `ABANDONED CAMP` recognition: **passed previously**.
- `0.5.59.4` shared-placement label and Field Survey isolation: **failed previously** (`MODDED STRUCTURE` and `COPPER SULFIDE DEPOSIT` appeared as structures).
- `0.5.59.5` game launch, invalid-record cleanup, absence of `MODDED STRUCTURE` from the list, and Field Survey structure/geology isolation: **passed**.
- `0.5.59.5` stale selected-target cleanup: **failed** (`InvocationTargetException` repeated every tick after cleanup).
- `0.5.59.6` compilation, JAR validation, metadata/version, non-null empty sentinel bytecode, SHA-256, install, and single-JAR/hash checks: **passed**.
- `0.5.59.6` game startup, stale-route cleanup, arrival-check stability, and genuine geology preservation: **passed**.

## Next executable test

1. Use Radar to locate an unknown candidate for `explorify:campsite` or `betterarcheology:archeologist_camp_grassy`.
2. Travel to the candidate and run Structure Field Survey in range.
3. Confirm the record upgrades from `UNKNOWN STRUCTURE` to `CAMPSITE` or `ARCHEOLOGIST CAMP` and unlocks the dynamic family filter.
4. Repeat for the remaining family; then begin Discovery Database analysis-level/last-seen/event work.

## Do not assume

- Placement candidates are possible coordinates, not proof that a structure generated.
- Do not overwrite the known-good custom JAR backup or load two versions of either project-owned mod.

## Resume reading order

1. `.codex/project-memory.md`
2. This file
3. `.codex/conversations/INDEX.md`
4. `.codex/conversations/2026-09-01_desktop_field_survey_acceptance_route_cleanup.md`
5. `docs/Known Issues.md`
