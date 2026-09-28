# 2026-09-28 — laptop — Navigation favorite selection

Status: implementation/installation verified; runtime acceptance pending
Branch: `main`; starting commit `c35cea7be552346209970914e21c3cf692e0add5`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.69.1-alpha-navigation-favorites.jar`
SHA-256: `d6ff78e9fce2e9d5e3b6480711dfb656c8a64f4dc1e096799b73c1d31229a91d` (3636825 bytes)

## User goal and acceptance reconciliation

User confirmed the requested Navigation GUI close/reopen check works, then asked to continue the next step. Record that specific `.69.0` acceptance by user report; do not ask for the old gate again. Earlier distance/dimension evidence tested Ship Link/Discoveries, not all later Navigation edge cases. Stage 9 remains partial.

## Starting state

Clean main matched origin/GitHub at c35cea7; fetch and fast-forward synchronization returned already up to date. Repository and laptop had one `.69.0` core each, hash `af77b62a691a18bf6340c7371647b0c90e07b7643773cf8576958aae29c8427f`. Canonical rules, memory, handoff and newest session record were read. DoctorWhoMod remains v5.8.19, hash `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.

## Decisions and changes

- Continue the existing Stage 9 favorite item, not a new navigation engine. Navigation → FAVORITES lists shared favorites; TARGET reuses the existing selected-key writer. Existing route/hop is deliberately unchanged. No flight, WE use, chunk loading, save migration or DoctorWhoMod patch.
- Separate favorite snapshot mode 7 uses the existing bounded Discovery codec and ship-bound key mapping. Filter all shared records before applying the favorite cap so older favorites outside the recent category slice remain eligible. Newest seen first; SYNC shows transferred/total favorites.
- Picker offers TARGET, REFRESH and back-to-Navigation. No ROUTE/FAV action in this view. Server rechecks link/owner, enabled config and still-favorite state; non-target actions in favorite mode are rejected. Reentering Discoveries restores its normal snapshot. Link reset clears picker/client records.
- `player-navigation.properties` adds live `favorites.enabled=true` and `favorites.sync_limit=128` (16–512), Turkish descriptions; existing refresh=20 and all other user gameplay configs preserved. Existing Discoveries target permission still applies.
- Source/test/patch manifest, lock, generated mod list, README, runtime guide, roadmap, handoff, memory, index and resume prompt updated. Stage 9 favorite item stays `[~]` pending game acceptance.

## Verification

- Build script ran with verified `.57.0` baseline and ASM 9.8. All eight smoke suites passed, plus Navigation rerun against the real DoctorWhoMod engine formula. New checks: older favorites despite recent nonfavorites, newest order, caps/empty lists, live config bounds/invalid fallbacks, target-only and removed-favorite guard, picker/empty text layout, reset cleanup. Existing protocol/link/overview/geology regressions remained green.
- Initial build attempt failed because the previous temporary JDK lost compiler tools. Recovered official Adoptium Java 21 archive through its API, verified SHA-256 before extraction to `D:\Projects\NewWorld-Toolchains\jdk-21.0.12.1+1`; no machine-wide Java setting changed and no toolchain committed. Subsequent build succeeded.
- No java/javaw process at installation. Verified original hashes, copied/verified backups, then retired the exact old files and installed the candidate. Backup: laptop `backups/custom-mods/pre-navigation-favorites-20260928-01/` with repository/instance originals and prior Navigation config/README. Known-good older backups retained; worlds/logs/caches untouched.
- Repo/runtime each have one lock-matched NewWorldCore and unchanged DoctorWhoMod. Source and embedded patch manifests match. Only Navigation config/README were deployed; broad apply/refresh scripts were avoided to preserve unrelated runtime settings.
- No new game session was launched; no claim of `.69.1` visual, live TARGET, route-preservation, reconnect or multiplayer acceptance. Intentional malformed-input smoke-test diagnostics are not runtime failures.

## Next executable step

Open Navigation → FAVORITES (create favorites through existing Discoveries FAV if empty), choose a different favorite → TARGET → < NAVIGATION. Confirm new left target and unchanged right route, then REFRESH/removal, reopen and link-loss behavior per `docs/15_Player_Navigation_Runtime_Kabul.md`. After acceptance continue SAVE CURRENT LOCATION, then SEND TO SHIP; do not duplicate accepted TARGET/ROUTE work.
