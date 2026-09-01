# New World current handoff

Updated: 2026-09-01
Machine: desktop
Branch: `main`

## Current objective

Finish the Structure Field Survey acceptance test for `NewWorldCore-1.21.1-NeoForge-0.5.59.4-alpha-full-placement-radar.jar`.

## Exact state

- The GitHub repository was cloned to `E:\projects\Minecraft-Modpack-New-World`.
- The desktop CurseForge instance was registered at `C:\Users\suley\curseforge\minecraft\Instances\New World`.
- Repository config, defaultconfigs, KubeJS files, and the two current project-owned mod JARs were applied to that instance.
- The desktop NewWorldCore JAR matches the repository SHA-256: `2530037eb38b25670a5d3d8019a04d7fc87e6ac106dc31b1a0ce90cecccee6de`.
- The desktop DoctorWhoMod JAR matches the repository SHA-256: `66c1c5e98804fa9826450b5364798e5797563248982893817ef710b6fb001f19`.
- The older desktop custom build and previous applied directories were preserved under `backups\desktop-sync-pre-20260901-201500`.
- `tools/apply-to-instance.ps1` now moves stale NewWorldCore/DoctorWhoMod versions into a timestamped backup before installing the current pair.
- The desktop game and existing world load successfully. Structure Radar is actively finding vanilla and modded candidates.
- User-provided screenshot evidence shows `Discoveries 101`, `Favorites 0`, `Visited 6`, and many result rows. Visible modded classes include Explorify, Better Dungeons, and Structory.

## Test status

- Build/archive/static validations for NewWorldCore `0.5.59.4`: **passed previously**.
- Installation in the desktop CurseForge instance: **passed**.
- Desktop game launch and existing-world load: **passed**.
- More than four Structure Radar results being visible/reachable: **passed** (`101` discoveries observed).
- Dynamic modded candidate classes being retained in the list: **passed** (Explorify, Better Dungeons, and Structory observed).
- Accurate labels for shared modded placement sets: **failed**; the UI exposes the non-existent placeholder `MODDED STRUCTURE` as a selectable target.
- Full scan completion and long-run performance: **in progress**; no completed-scan timing was reported yet.
- `UNKNOWN` candidate to confirmed family conversion through Field Survey: **not tested conclusively**.
- Field Survey isolation from `GEOLOGY`: **not tested**.

## Next executable test

1. Do not treat `MODDED STRUCTURE` as a real place; it is a confirmed shared-placement fallback defect.
2. For the current acceptance test, use `/locate structure explorify:campsite`, `/locate structure structory:abandoned_camp`, or `/locate structure betterarcheology:archeologist_camp_grassy` to reach an actual generated modded structure.
3. At the real structure, use Structure Field Survey and verify that an entry becomes the actual structure/family name and visited.
4. Confirm that the survey changes only `STRUCTURE` discovery data and does not mutate any `GEOLOGY` entry.
5. Development fix: change multi-family placement fallback from `MODDED STRUCTURE` to an explicitly unconfirmed label, then build/test the next NewWorldCore version after Minecraft is closed.
6. Let the radar scan finish and record completion time plus any freezes or tick spikes.

## Do not assume

- Placement candidates are possible coordinates, not proof that a structure generated.
- Do not overwrite the known-good custom JAR backup or load two versions of either project-owned mod.

## Resume reading order

1. `.codex/project-memory.md`
2. This file
3. `.codex/conversations/INDEX.md`
4. `.codex/conversations/2026-09-01_desktop_structure_radar_acceptance.md`
5. `docs/Known Issues.md`
