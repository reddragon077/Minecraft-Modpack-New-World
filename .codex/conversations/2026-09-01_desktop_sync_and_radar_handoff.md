# 2026-09-01 — desktop — sync and Structure Radar handoff

Status: `in progress`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.4-alpha-full-placement-radar.jar`

## User goal

Continue the same New World project from desktop and laptop through GitHub, including enough conversation/decision context for a new Codex session to resume without guessing. The immediate gameplay goal is to test modded structure discovery after the old four-record display limitation was changed.

## Starting state

- Git had just been installed on the desktop.
- The repository was not yet present under `E:\projects`.
- The desktop CurseForge instance still contained an older NewWorldCore build.
- The user clarified that the four-record problem had been changed on the laptop but was not tested before moving to the desktop.

## Decisions

- GitHub `main` is the shared baton between computers; local CurseForge instances are test endpoints.
- Project conversations are stored as compact, dated decision/test summaries plus one current `HANDOFF.md`, not as raw transcripts.
- The four-record item remains **not tested** until the fifth and later radar results are visibly reachable and selectable.
- Dynamic placement-based candidates remain `UNKNOWN` until Structure Field Survey confirms an actual nearby structure.
- Structure survey must update `STRUCTURE` only and never alter `GEOLOGY` discovery state.

## Changes

- Cloned the canonical GitHub repository to `E:\projects\Minecraft-Modpack-New-World`.
- Registered the desktop paths in `machines/desktop.json`.
- Applied the repository state to the desktop CurseForge instance and preserved the prior instance state under `backups\desktop-sync-pre-20260901-201500`.
- Verified that the installed NewWorldCore and DoctorWhoMod JAR hashes match the repository.
- Updated `tools/apply-to-instance.ps1` to back up stale project-owned JARs before copying current versions.
- Added this Git-tracked conversation/handoff protocol and imported a compact index of earlier project conversations.

## Verification

### Passed

- Repository clone and desktop machine registration.
- Repository-to-instance file application.
- Exactly one current NewWorldCore and one current DoctorWhoMod JAR are installed; both hashes match the repository.
- The previous desktop custom build/config state was preserved in a timestamped backup.

### Failed

- No gameplay failure was observed in this desktop session because the game has not been launched yet.

### Not tested

- Desktop game launch and existing-world compatibility.
- More than four Structure Radar results being reachable through scrolling.
- Scan completion/performance and dynamic modded structure labels in `0.5.59.4`.
- Field Survey confirmation and isolation from geology discoveries.
- Laptop's currently installed custom JAR version after this Git handoff.

## Next executable step

Launch the desktop CurseForge instance, run one Structure Radar scan, and record whether it completes, how many results appear, whether row five and later are selectable, which modded families appear, and whether Field Survey changes only structure discovery data.

## References

- `../HANDOFF.md`
- `../project-memory.md`
- `../../docs/Known Issues.md`
- `../../docs/12_Gelistirme_Yol_Haritasi.md`
- Current Codex task: `Codex işlemlerini bul`
