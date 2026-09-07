# 2026-09-05 — Laptop roadmap reconciliation

## Scope

- Reconciled the 14-stage roadmap and cross-computer continuation records against the accepted `0.5.66.1` JAR, source-patch evidence and laptop runtime acceptance.
- No gameplay code, Java source, config, `pack-lock.json`, embedded manifest or JAR was changed or rebuilt in this documentation-only pass.

## Corrected roadmap state

- Stage 7 remains partial: the complete geology analysis chain is accepted, while advanced Structure analysis levels still await Research progression.
- Stage 9 now marks `Discovery’den hedef oluşturma` complete because `0.5.66.1` TARGET/ROUTE actions already connect shared Discovery records to the existing Navigation route/hop engine.
- Stage 14 now marks `Field Survey → Geological Scan` complete because `0.5.62.0` physically verified `TIN-RICH DEPOSIT` at `[-2696, 32, -728]` and persisted `GEOLOGY/FIELD` evidence.
- Removed the obsolete work gate that would restart Geological Field Survey, geology analysis progression and Discoveries work.
- Canonical next development order is Stage 4 Overview / Ship Status, then Stage 5 Ship Link, then the remaining Stage 9 Player Navigation panel items.

## Accepted build retained

- JAR: `NewWorldCore-1.21.1-NeoForge-0.5.66.1-alpha-player-discovery-actions.jar`
- SHA-256: `889900f7e2519b8e07e604431260e28e0b6f8932d3d087bc5b17f8551fda059c`
- Stage 8 remains complete: Discoveries details, favorite, target and real route/hop actions passed runtime acceptance.

## Metadata debt

The `0.5.66.1` JAR's embedded patch manifest does not fully describe the Player Discoveries action additions. The repository `src-patches/newworldcore/patch-manifest.json` was intentionally left unchanged so it stays aligned with the accepted JAR. Add the missing action descriptions to both source and embedded manifest metadata during the next genuine JAR build; do not create a documentation-only `0.5.66.2`.

## Continuation

Begin Stage 4 only after normal clean-branch and hash checks. Do not restart completed Field Survey, geology analysis or Stage 8 work, and do not jump to the remaining Stage 9 work before Stages 4 and 5.
