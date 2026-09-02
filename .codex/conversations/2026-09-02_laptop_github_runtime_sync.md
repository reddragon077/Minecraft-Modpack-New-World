# 2026-09-02 — laptop — GitHub/runtime synchronization

Status: `verified`
Branch/commit: `main` / `3a3f35e` at synchronization start
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.10-alpha-radar-random-spread.jar` — SHA-256 `876bc247597c0186bced715b05e843e9a85b00f32d3a9c81c990d6134fd63687`

## User goal

Continue from the desktop handoff, synchronize the laptop repository and CurseForge endpoint safely, then resume the remaining Radar v2 acceptance tests.

## Starting state

- Laptop repository `main` was clean at `63580ce` and matched its then-known `origin/main`.
- Laptop CurseForge instance still used `0.5.59.4-alpha-full-placement-radar`.
- The game/Java process was closed.

## Decisions

- GitHub `main` remains canonical; the laptop runtime is only a test endpoint.
- The desktop runtime-accepted `0.5.59.10` is adopted without rebuilding it on the laptop.
- Runtime acceptance is not inferred from installation/hash verification; it remains a separate laptop test.

## Changes

- Fetched and fast-forwarded laptop `main` to desktop handoff commit `3a3f35e`.
- Ran `tools/apply-to-instance.ps1` against the path registered in `machines/laptop.json`.
- Preserved the prior laptop `0.5.59.4` JAR under `backups/custom-mods/pre-apply-20260902-110953`.
- Applied shared config/defaultconfigs/KubeJS files and the two project-owned JARs to the laptop instance.

## Verification

### Passed

- Repository and instance each contain exactly one NewWorldCore JAR.
- Both NewWorldCore copies match SHA-256 `876bc247597c0186bced715b05e843e9a85b00f32d3a9c81c990d6134fd63687`.
- Repository and instance each contain exactly one DoctorWhoMod fork; both match SHA-256 `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- The stale laptop NewWorldCore JAR exists in the timestamped backup.
- Repository `main` matched `origin/main` at `3a3f35e` before recording this synchronization.

### Failed

- None during synchronization.

### Not tested

- Laptop game launch and world load with `0.5.59.10`.
- Laptop `ALL` mixed-family scan timing and results.
- `ARCHEOLOGIST CAMP` locate → Field Survey → dynamic-filter chain.

## Next executable step

Launch the laptop instance, run one `ALL` Structure Radar scan, confirm mixed families and reasonable completion time, then complete the `betterarcheology:archeologist_camp_grassy` Field Survey chain.

## References

- `.codex/HANDOFF.md`
- `.codex/conversations/2026-09-02_desktop_random_spread_coordinate_repair.md`
- `machines/laptop.json`
- `docs/Known Issues.md`
