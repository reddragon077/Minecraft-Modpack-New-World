# New World current handoff

Updated: 2026-09-01
Machine: desktop
Branch: `main`

## Current objective

Run the first in-game acceptance test for `NewWorldCore-1.21.1-NeoForge-0.5.59.5-alpha-radar-survey-isolation.jar`.

## Exact state

- The GitHub repository was cloned to `E:\projects\Minecraft-Modpack-New-World`.
- The desktop CurseForge instance was registered at `C:\Users\suley\curseforge\minecraft\Instances\New World`.
- Repository config, defaultconfigs, KubeJS files, and the two current project-owned mod JARs were applied to that instance.
- The desktop NewWorldCore JAR matches the repository SHA-256: `8c85c3569f9926addd68978c2bca617c57c2a699bec1deac9dcf8a9e4d4b652b`.
- The desktop DoctorWhoMod JAR matches the repository SHA-256: `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- The older desktop custom build and previous applied directories were preserved under `backups\desktop-sync-pre-20260901-201500`.
- `tools/apply-to-instance.ps1` now moves stale NewWorldCore/DoctorWhoMod versions into a timestamped backup before installing the current pair.
- The desktop game and existing world load successfully. Structure Radar is actively finding vanilla and modded candidates.
- User-provided screenshot evidence shows `Discoveries 101`, `Favorites 0`, `Visited 6`, and many result rows. Visible modded classes include Explorify, Better Dungeons, and Structory.
- `0.5.59.5` excludes NewWorldCore `*_deposit` jigsaw structures from Structure Radar and Field Survey, changes shared multi-family placement labels to `UNKNOWN STRUCTURE`, and removes persisted false `MODDED STRUCTURE` or geology-as-`STRUCTURE` records without deleting real `GEOLOGY` records.
- The prior `0.5.59.4` desktop JAR is preserved under `backups\custom-mods\pre-apply-20260901-211819`.

## Test status

- `0.5.59.4` desktop launch, >4 result list, modded-class retention, 101-result scan completion in about 5.2 seconds, and real `ABANDONED CAMP` recognition: **passed previously**.
- `0.5.59.4` shared-placement label and Field Survey isolation: **failed previously** (`MODDED STRUCTURE` and `COPPER SULFIDE DEPOSIT` appeared as structures).
- `0.5.59.5` compilation, JAR validation, metadata/version, helper behavior smoke test, ASM entry-point verification, and SHA-256: **passed**.
- Exactly one `0.5.59.5` JAR in both repository and desktop instance with matching hashes: **passed**.
- `0.5.59.5` game launch, cleanup migration, scan result labels, and Field Survey isolation: **not tested yet**.

## Next executable test

1. Launch the desktop instance and confirm the existing world loads without registry or patch errors.
2. Run one Structure Radar scan. Confirm there is no selectable `MODDED STRUCTURE`, no `... DEPOSIT` in Structures, and shared placement groups remain `UNKNOWN STRUCTURE`.
3. Check the log for a successful invalid-record cleanup message and completed placement scan; record task count, result count, and duration.
4. Return to or locate `structory:abandoned_camp`, then run Field Survey.
5. Confirm the survey result contains `ABANDONED CAMP` but not `COPPER SULFIDE DEPOSIT` or any other geology family.
6. Open the Deposits/Geology view and confirm the genuine Copper Sulfide geology record still exists; cleanup must remove only the false `STRUCTURE` copy.

## Do not assume

- Placement candidates are possible coordinates, not proof that a structure generated.
- Do not overwrite the known-good custom JAR backup or load two versions of either project-owned mod.

## Resume reading order

1. `.codex/project-memory.md`
2. This file
3. `.codex/conversations/INDEX.md`
4. `.codex/conversations/2026-09-01_desktop_radar_survey_fix_build.md`
5. `docs/Known Issues.md`
