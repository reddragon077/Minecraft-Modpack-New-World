# 2026-09-10 — laptop — Player Navigation view candidate

User accepted automatic Ship Link recovery, closed the game and requested next-stage work. Stage 5 acceptance recorded separately at `c00ac5f`; Stage 9 first read-only slice implemented without duplicating accepted Discovery TARGET/ROUTE.

Build: `NewWorldCore-1.21.1-NeoForge-0.5.69.0-alpha-player-navigation-view.jar`
SHA-256: `af77b62a691a18bf6340c7371647b0c90e07b7643773cf8576958aae29c8427f`, 3635246 bytes.

New `PlayerNavigation0690` samples selected discovery and existing PLANS on the server after Ship Link owner/range resolution. Uses map lookup, not state creation; no route/flight/world writes. Selected target distance is 3D from ship exterior; loaded route hop comes from existing MultiHop loadedHop parser. Only a destination matching the loaded hop is estimated using DoctorWhoMod EngineTravelGate distance/modules and EngineTravelBalance.warpCost. No copied formula, no total-route estimate, no invented number on missing APIs. Geological names honor current analysis masking.

Added isolated mode-6 request, bounded atomic int frames, server rate limit, client stale/ship/link reset protection, enabled Navigation tab styling and click routing. Config `player-navigation.properties`: refresh20, stale120, coordinates and WE estimate true; Turkish guidance. Existing configs, DoctorWhoMod, Discovery actions and route engine preserved.

Verification: eight smoke suites pass plus Navigation suite repeated with actual DoctorWhoMod JAR to check 5-block zero-module estimate=101 WE. Includes config reload/bounds, masking, no target, completed/out-of-bounds/foreign route, changed destination, cross-dimension distance, wire roundtrip/oversize/truncated/invalid timing, stale/link/ship denial and column/text bounds. Initial full suite also passed before extra integration coverage. Invalid config and rejected frame messages are intentional test fixtures. Compiled JAR shows mode6, Navigation render/reset routing, enabled-tab threshold4 and retained Ship Link hooks. Source/embedded manifest matches.

Deployment: Java absent, old `.68.2` hashes checked and backed up at `backups/custom-mods/pre-player-navigation-20260910-01/` (named old JAR, instance-original.jar, repository-original.jar, repository-retired.jar). New JAR repo/live each single and matching above hash. Config README backed up; only new navigation property added. No old backups deleted, no save/log/cache added to Git. Lock core entry updated only; generated mod list refreshed.

Runtime: NOT TESTED. Next: `docs/15_Player_Navigation_Runtime_Kabul.md`, first open Navigation and compare target/route/loaded hop/WE with physical terminal. Remaining Stage 9 favorite selection, SAVE CURRENT LOCATION and SEND TO SHIP intentionally deferred until this view is accepted. Broader Ship Link multiplayer/timeout/Geological delayed cancellation remain open.
