# 2026-09-01 — desktop — Field Survey acceptance and route cleanup

Status: `in progress`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.6-alpha-radar-route-cleanup.jar`
SHA-256: `4b97a91af5d7c45aabb19c0f1fe4223141a4ffa0800878e21bca24ce2aefa8d0`

## User goal

Verify the post-fix structure scan and continue development after false `MODDED STRUCTURE` and geology-as-structure records were addressed.

## Runtime evidence from 0.5.59.5

- Existing world loaded and the cleanup removed 17 invalid structure discovery records.
- The discovery list dropped from the earlier 101 records to 98 and no longer displayed `MODDED STRUCTURE`; shared candidates display as `UNKNOWN STRUCTURE`.
- Field Survey identified exactly five real structures: `VILLAGE`, `ABANDONED CAMP`, `SMALL DUNGEON`, `METEORITE`, and `SPIDER DUNGEON`.
- No `COPPER SULFIDE DEPOSIT` or other geology family appeared in the Field Survey result. Radar/Field Survey isolation therefore passed.
- The monitor still showed the removed legacy target in the route header. Starting immediately after cleanup, `Navigation0471gFix.checkArrivals` logged `InvocationTargetException` repeatedly.

## Root cause and 0.5.59.6 change

The cleanup cleared `ShipState.selectedKey` with `null`, while `NavigationDiscoverySavedData.selected()` directly invokes `selectedKey.isBlank()`. The patch now clears the removed selection with the model's existing empty-string sentinel.

## Verification

- Rebuilt reproducibly from commit `6253b28`'s verified `0.5.57.0` baseline.
- Java compilation, JAR entries, metadata version, patch manifest, and bytecode use of the empty-string sentinel passed.
- Repository and desktop instance each contain exactly one matching `0.5.59.6` JAR.
- Prior `0.5.59.5` is preserved at `backups/custom-mods/pre-apply-20260901-221120` in the desktop instance.
- Game startup, stale target disappearance, and absence of new arrival-check errors on `0.5.59.6` remain to be tested.

## Next executable step

Launch the desktop instance, open the Navigation display, and leave the world running for at least 20 seconds. Verify the stale `MODDED STRUCTURE` target is gone and `latest.log` has no new `0471g arrival check` exception. Then verify the genuine geology record still exists under Deposits.
