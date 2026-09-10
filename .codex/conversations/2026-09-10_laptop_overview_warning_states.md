# 2026-09-10 — laptop — Overview warning states

Status: in progress — `.67.1` installed, new visual acceptance pending
Branch/base: main / 4ea918191a7132bb8d7e5adbef00984528a15d42
Build: NewWorldCore-1.21.1-NeoForge-0.5.67.1-alpha-overview-warning-states.jar
SHA-256: 0d08367a747efc6fa94d41270793b96de897ebb0059a2de5599c9c322badbac4 (3613963 bytes)

## User request

After Overview returned to NOMINAL, old warnings still looked active. User authorized grey RESOLVED vs active severity styling and closed the game for installation. Preserve their refresh_ticks=20 preference.

## Verified .67.0 runtime evidence

- Screenshots and log confirm clear Overview layout, FE/WE and Matrix data, cooldown→READY, brake/shield changes, Mining wait/scanning/mining/completed states and higher OUT during load. User confirmed reopening GUI and live config changes work.
- Temporary warning_percent=99 was used for a low-energy test, but energy dropped farther than planned. At 10:50:58 log had CRITICAL, FE=8315/2250000, Mining NO_ENERGY. At 10:51:27 FE=348910 and WARNING; at 10:51:52 FE=1864528 and NOMINAL. Both FE critical and Mining NO_ENERGY were present, so this is not isolated proof of either trigger alone. By 10:52:21 FE was full, shield off, Mining waiting for shield.
- Restored warning_percent=20 / critical_percent=5, retained refresh_ticks=20. Original test config is preserved at `backups/config-tests/overview-20260910-104036/overview.properties`.
- Old warnings remaining yellow after NOMINAL were real presentation ambiguity, not continuing low energy. No relevant Overview routing/render/decode error was found in inspected latest.log.

## Change

- Each snapshot carries server-authored ACTIVE/CRITICAL/RESOLVED labels in the existing bounded warning strings. Per-entry severity is not inferred from client config or the overall ship label.
- Critical and other active conditions precede resolved history; retained/wire history remains at most three entries. Reactivation and escalation/downgrade reuse the same warning identity without duplicates.
- Telemetry read failures stay active until the next successful sample, rather than being incorrectly marked resolved immediately.
- Grey resolved rows can be hidden with live `show_resolved_warnings=false`; active rows remain visible. Config comments/README updated. User refresh=20 copied into canonical profile; no temporary energy threshold committed.
- No engine, Mining, energy withdrawal or save-state behavior changed. No new network registration/schema needed.

## Verification and installation

- Clean main, fetch and ff-only pull already current before editing.
- Built against preserved `.57.0` baseline using the existing JDK21/ASM9.8 script. All six smoke suites passed, including warning activation→critical→resolved→reactivation, repeated resolution, active overflow priority, empty history, live hide/show, exact drawn colors, geometry and mixed-state packet round-trip.
- Invalid-config/EOF lines in tests are intentional negative fixtures.
- Embedded/source manifest exact match verified. No Java process during install. Repo/laptop each hold one matching `.67.1`; DoctorWhoMod unchanged at `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- Hash-verified `.67.0`, original repo/live JARs and config/README preserved at laptop `backups/custom-mods/pre-overview-warning-states-20260910-01/`. Older known-good backups untouched. Scoped deploy avoids unrelated runtime configs, worlds and caches.

## Next

New `.67.1` has not been launched or visually accepted. Use the brake-released + Shield ON warning, then Shield OFF resolution test (no mining/energy drain needed); check grey RESOLVED and the hide/show config. Full Stage 4 still has unverified unknown/stale, multiplayer and terminal-comparison cases; do not claim the entire stage complete from these screenshots alone.
