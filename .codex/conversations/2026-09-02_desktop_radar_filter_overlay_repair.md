# 2026-09-02 — desktop — Radar filter overlay repair

Status: `passed`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.7-alpha-radar-filter-layer.jar`

## Evidence and diagnosis

- User screenshot confirmed the unlocked dynamic filter list contains `CAMPSITE`, with 13 discovered types over two pages. Campsite family acceptance is complete.
- Radar result rows and telemetry labels were visibly drawn over the otherwise opaque filter popup.
- The popup was rendered after the base radar content but inherited a lower pose depth than earlier text, so normal draw order alone did not place it in front.

## Repair

- The patcher preserves the legacy `Navigation0510RadarDualMode.renderFilterOverlay` body as `renderFilterOverlay0592Base` and adds a wrapper with the original signature.
- `Navigation0592RadarFilterOverlayFix` pushes the GUI pose, translates the popup to `Z=1000`, invokes the preserved renderer, and restores the pose in `finally`.
- The helper adapts to float or double pose translation signatures and unwraps reflected render failures without leaking pose state.

## Verification and installation

- Build, archive entry, version marker, helper inclusion, and patched wrapper bytecode checks passed.
- A mock-pose smoke test confirmed the preserved overlay renders at depth 1 / `Z=1000`, and that the pose stack restores on both success and failure.
- SHA-256: `6f1fcec6139c55c2bf92145f31d3d5e5913fd0cc0333bc27d6f3a313a67ae9f6`; size: `3554686` bytes.
- Repository and desktop instance each contain exactly one matching NewWorldCore JAR.
- Prior `0.5.59.6` was preserved under desktop `backups/custom-mods/pre-apply-20260902-002429`.

## Next test

Follow-up screenshots showed the popup content unobstructed, so the foreground layer repair passed. A separate exact-selection defect found during the `CAMPSITE` scan is tracked in `2026-09-02_desktop_dynamic_filter_repair.md`.
