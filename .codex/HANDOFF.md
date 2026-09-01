# New World current handoff

Updated: 2026-09-01
Machine: desktop
Branch: `main`

## Current objective

Run the first desktop in-game acceptance test for `NewWorldCore-1.21.1-NeoForge-0.5.59.4-alpha-full-placement-radar.jar`.

## Exact state

- The GitHub repository was cloned to `E:\projects\Minecraft-Modpack-New-World`.
- The desktop CurseForge instance was registered at `C:\Users\suley\curseforge\minecraft\Instances\New World`.
- Repository config, defaultconfigs, KubeJS files, and the two current project-owned mod JARs were applied to that instance.
- The desktop NewWorldCore JAR matches the repository SHA-256: `2530037eb38b25670a5d3d8019a04d7fc87e6ac106dc31b1a0ce90cecccee6de`.
- The desktop DoctorWhoMod JAR matches the repository SHA-256: `66c1c5e98804fa9826450b5364798e5797563248982893817ef710b6fb001f19`.
- The older desktop custom build and previous applied directories were preserved under `backups\desktop-sync-pre-20260901-201500`.
- `tools/apply-to-instance.ps1` now moves stale NewWorldCore/DoctorWhoMod versions into a timestamped backup before installing the current pair.

## Test status

- Build/archive/static validations for NewWorldCore `0.5.59.4`: **passed previously**.
- Installation in the desktop CurseForge instance: **passed**.
- Desktop game launch and existing-world load: **not tested yet**.
- Structure Radar result-list/scroll fix for the old four-record limit: **changed but not tested**.
- Dynamic modded structure family discovery and Field Survey isolation: **not tested on desktop**.

## Next executable test

1. Launch the `New World` instance and load the existing test world.
2. Run Structure Radar and wait for the scan to complete without long freezes.
3. Confirm the result count can exceed four and the fifth and later rows can be reached and selected by scrolling.
4. Look for `Campsite`, `Abandoned Camp`, and `Archeologist Camp` families.
5. Visit one candidate and verify `UNKNOWN` becomes a real family only after Structure Field Survey.
6. Confirm that the survey changes only `STRUCTURE` discovery data and does not mutate any `GEOLOGY` entry.
7. Record observed counts, labels, timings, failures, and screenshots/log excerpts in a new dated conversation record.

## Do not assume

- Do not call the four-record issue fixed until step 3 passes in-game.
- Placement candidates are possible coordinates, not proof that a structure generated.
- Do not overwrite the known-good custom JAR backup or load two versions of either project-owned mod.

## Resume reading order

1. `.codex/project-memory.md`
2. This file
3. `.codex/conversations/INDEX.md`
4. `.codex/conversations/2026-09-01_desktop_sync_and_radar_handoff.md`
5. `docs/Known Issues.md`
