# 2026-09-10 — laptop — typed link runtime acceptance

Build unchanged: `NewWorldCore-1.21.1-NeoForge-0.5.68.2-alpha-typed-client-link.jar`, SHA-256 `2ffb61b41cc4b3f99f820c103006d9015da7695184ac62e49f62b4224cbd88c8`.

Fresh session screenshots and latest.log prove CONNECTED/NOMINAL at 16:19:17 and Discoveries 128/471 at 16:19:36. At 16:31:47 link was OUT OF RANGE (5998-block screenshot); at 16:36:53 server recovered CONNECTED/NOMINAL. The later 12-block screenshot confirms client recovery. User explicitly clarified return was already working and only screenshot delivery was late; there is no demonstrated recovery defect to fix.

Stage 5 regression gate accepted for this single-player scope. Separate forced reopen/lifecycle, multiplayer, timeout and Geological delayed cancellation remain untested. Prior dimensional/actions acceptance remains attributed to `.68.0`.

User closed the game (log stop 16:48:08, Java absent) and requested the next stage. Clean main fetched/fast-forward checked, already current. No JAR/config/world change for acceptance; pack-lock remains correct. Next is remaining Stage 9 Player Navigation; reuse existing selected target and route/hop engine rather than duplicating Discovery actions.
