# 2026-09-29 — laptop — waypoint terminal/route repair

Starting state: clean main 1579be9; .69.3 hash matches runtime and repository. User closed Minecraft, authorized the combined coordinate/distance/route repair and requested larger coherent work batches with fewer handoffs.

## Evidence before changes

- SEND TO SHIP succeeded at 09:55:02.951: selected minecraft:overworld|-2454|63|181. Player Navigation and physical terminal show the new name; the previous Carbon route remains unchanged.
- Read-only saved-data audit after shutdown: 473 discoveries, two WAYPOINT/MANUAL favorites at [-2454,63,181] and [-2454,63,189], selected key intact. Both saved Y values are 63; legacy Distance fields are 0. No save mutation was performed.
- Physical terminal screenshot incorrectly shows Y=-64 and 0 BLOCKS. Installed bytecode confirms preciseTargetItem replaces saved Y with resolveLandingY and transmits the stored Distance. The height resolver accepts/caches -64. This is not evidence of corrupted coordinates.
- Existing same-dimension route calculation independently uses surfaceY for all targets, including manual waypoints. New target selection itself does not recalculate or overwrite the old route.
- Scope: preserve manual waypoint coordinates, compute live ship-relative terminal distance, preserve manual final-hop Y while leaving structure/geology surface landing and intermediate hops unchanged. Keep flight/energy/ownership gates and save schema unchanged. Verify as one regression batch, then request one combined in-game check. Full Stage 9 remains partial.

## Implementation and verification

- Added Navigation0694WaypointFix adapters and fail-fast ASM hooks in preciseTargetItem, actual calculate/loadFirstHop and terminal text. Manual Y bypasses heightmap projection only for WAYPOINT/MANUAL. Live distance uses current exterior position/dimension; -1/-2 presentation sentinels are never written to saved records.
- Calculator gives even same-XZ/vertical-only manual targets a final hop. Existing RangeGuard remains active; original altitude normalizer retains intermediate cruise altitude. Inspection found its final-Y <= -60 fallback would otherwise overwrite legitimate manual coordinates: fresh manual load scopes an exact-plan ThreadLocal, restores only that last hop after original normalization, and clears context in finally. Stored plans contain the corrected coordinates without schema changes. No new flight or energy rules.
- Build command: `tools/build-newworldcore-geology-patch.ps1` with verified .57.0 baseline, local JDK 21.0.12.1+1 and ASM/ASM-tree 9.8. Eleven suites passed plus actual DoctorWhoMod formula fixture. Intentional malformed configuration/frame warnings belong to negative tests, not runtime failures.
- New smoke test executes the shipped calculator, real RangeGuard and real private altitude normalizer with external manager/world-height/write boundaries stubbed. Covers distance 11/8/0, dimension/unavailable, readonly records, manual Y=63/-30/-64, single/multiple/vertical-only/cross-dimension targets, original structure/geology/nonmanual surface policy, scoped cleanup, plan encoding and exact hook counts. Does not emulate a complete Minecraft session or actual flight.
- First loader-hook build failed fast on expected hook count (3 vs 4): actual owner is HopAutopilot, not ServerRoute. Corrected owner, rebuilt successfully. No failed candidate was installed.
- Candidate: `NewWorldCore-1.21.1-NeoForge-0.5.69.4-alpha-waypoint-terminal-route.jar`, SHA-256 `4a68d8e660cd7e85a40109a16c5e1ec1068a92aefe45efbfccd19127339fc4e8`, 3635047 bytes.
- Jar-entry comparison: only the new helper entry added, no previous entries missing. Source/embedded manifest byte-for-text match verified. Repository/laptop each have exactly one matching new core. DoctorWhoMod hash remains `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- Both original .69.3 jars copied and hash-verified before removing their active copies: laptop `backups/custom-mods/pre-waypoint-terminal-20260929-01/repository/` and `instance/`. Java absent before and immediately before installation. Discovery save hash unchanged across install; no world/config writes.

## Next grouped runtime check

SEND TO SHIP saved [-2454,63,181]; verify physical selected target Y=63 and current ship distance (~11 if exterior still [-2464,61,176]). Old Carbon route stays until CALCULATE ROUTE; then final coordinates must become [-2454,63,181]. No flight needed. Report actual runtime outcome before accepting .69.4; extended Stage 9 remains partial. Continue related work in coherent batches as requested.
