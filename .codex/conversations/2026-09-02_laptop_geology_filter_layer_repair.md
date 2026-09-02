# 2026-09-02 — laptop Geology filter layer repair

Status: complete — `0.5.60.0` scan/runtime behavior accepted, `0.5.60.1` visual acceptance failed, and `0.5.60.2` passed static/install/runtime visual acceptance

Candidate build: `NewWorldCore-1.21.1-NeoForge-0.5.60.2-alpha-config-geology-flush.jar`

SHA-256: `423a25739e14c7db644c3389e041ad23508c06af6f2d920b33c7effb5e77c158`

## Runtime evidence from `0.5.60.0`

- Structure Radar queued 102 placement-only tasks at 5000 blocks with `selected=ALL`.
- With shipped `scan.batch_interval_ticks=8`, it returned 101 mixed-family results in about 9.98 seconds.
- Geology started a 5000-block, Speed III/III, Accuracy III/III, Efficiency III/III scan and returned 48 deposits in about 9.00 seconds.
- No NewWorldCore scan exception was found in the inspected log. One later 3069 ms / 61 tick behind warning remains a performance observation, not a crash attribution.

## Reported defect

- The Geology dynamic-filter popup opened, but deposit result rows, coordinates, the result scrollbar, and the yellow accent layer rendered above it.
- This is the Geology equivalent of the Structure filter layering defect fixed in `0.5.59.7`.

## Implemented repair

- `0.5.60.1` added `Navigation0601GeologyFilterOverlayFix`, which rendered the existing Geology popup in a dedicated `Z=1000` pose layer.
- The patcher preserves the legacy `drawExistingPopup` implementation as `drawExistingPopup0601Base` and routes the original entry point through the new foreground wrapper.
- In-game screenshot acceptance failed: deposit rows, coordinates, scrollbar, and the yellow accent still appeared above the popup. Minecraft deferred those GUI buffers and flushed them after the nominally higher-Z popup, so pose depth alone was insufficient on this render path.
- `0.5.60.2` explicitly calls `GuiGraphics.flush()` before the popup, draws it at `Z=1000`, and flushes it immediately as the final logical GUI layer.
- `0.5.60.2` build, patch replacement, bytecode wiring with both flush calls, helper-class presence, config smoke, repository/live single-JAR and hash, pack-lock, and generated mod-list checks passed.
- After Java stopped, `tools/apply-to-instance.ps1` preserved `0.5.60.0` under `backups/custom-mods/pre-apply-20260902-140313` and installed `0.5.60.1`.
- Repository/live each contain exactly one NewWorldCore JAR with the candidate hash above. DoctorWhoMod remains single and hash-matched, all eight live config files are present, and `scan.batch_interval_ticks=8` is preserved.
- After the failed screenshot, `0.5.60.1` was preserved under `backups/custom-mods/pre-apply-20260902-144912` and `0.5.60.2` was installed with matching repo/live hashes.
- All eight config files now contain Turkish inline guidance describing units, formulas, upgrade levels, direction of change, and performance risks; their numeric settings remained unchanged and repo/live file hashes match.

## Runtime visual acceptance

- Debug log confirms the loaded transition `0.5.60.1-alpha-config-geology-layer -> 0.5.60.2-alpha-config-geology-flush`.
- User screenshot acceptance passed: the complete Geology filter panel, header, two-column family list, controls, and paging render above deposit rows, coordinates, scrollbar, and yellow accent layers.
- The inspected runtime log contains no `Navigation0601`, config-wrapper, or NewWorldCore GUI exception.

## Next executable step

1. Commit and push the accepted config-suite/Geology repair to `main`.
2. Complete the remaining `ARCHEOLOGIST CAMP` Field Survey/dynamic-filter chain.
