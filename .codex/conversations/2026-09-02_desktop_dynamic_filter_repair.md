# 2026-09-02 — desktop — Dynamic structure filter repair

Status: `installed; runtime acceptance pending`
Branch: `main`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.59.8-alpha-radar-dynamic-filter.jar`

## Runtime evidence

- The user selected only `CAMPSITE` and ran a complete Structure Radar scan.
- The UI still showed 96 mixed candidates, including `UNKNOWN STRUCTURE`, `BURIED TREASURE`, and `SMALL DUNGEON`.
- `latest.log` recorded `Placement radar finished with 96 results (96 before active filters)`, proving the selected family did not affect the placement result list.
- The `0.5.59.7` foreground popup itself remained unobstructed; that separate visual repair passed.

## Root cause and repair

- Dynamic checkbox clicks already reached `Navigation0475RadarFilter.SERVER_SELECTED` on the server.
- `Navigation0581DynamicStructureScanner.prepareClassification` used only the old 13-family vanilla bitmask and directly finalized discoveries, bypassing the dynamic selected-family filtering in the newer wrapper.
- Non-vanilla and unknown labels therefore fell through as always allowed.
- `0.5.59.8` recovers the server-side selected labels, normalizes them, and requires exact label membership after the legacy mask. An empty selection remains `ALL`.

## Verification and installation

- A focused smoke test recovered `CAMPSITE` from the server selection map.
- The test accepted `CAMPSITE`, rejected `UNKNOWN STRUCTURE`, `BURIED TREASURE`, and `SMALL DUNGEON`, and accepted all labels for an empty (`ALL`) selection.
- Patched bytecode contains both `selectedLabels` and `passesDynamicFilter` calls in the placement finish path.
- SHA-256: `26f1ef3c761ebc8672ea4414f1f2939a1159e2f8608088ac19248fb863068e44`; size: `3555344` bytes.
- Repository and desktop instance each contain exactly one matching NewWorldCore JAR.
- Prior `0.5.59.7` is preserved under desktop `backups/custom-mods/pre-apply-20260902-011708`.

## Next test

Select only `CAMPSITE` and scan. Every returned row must be `CAMPSITE`; zero rows is valid if no candidate is in range. Then select `ALL` and confirm mixed-family results return.
