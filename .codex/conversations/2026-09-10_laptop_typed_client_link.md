# 2026-09-10 — laptop — typed client link repair

Status: `in progress` — build/install verified; runtime acceptance pending.
Branch/start commit: `main` / `25aed8b24e44ff626a606c288885747b67cf117e`.
Build: `NewWorldCore-1.21.1-NeoForge-0.5.68.2-alpha-typed-client-link.jar`
SHA-256: `2ffb61b41cc4b3f99f820c103006d9015da7695184ac62e49f62b4224cbd88c8` (3625901 bytes).

## User goal and starting state

User reported permanent SYNCING on `.68.1`, even beside the TARDIS and after reopening Player GUI. They closed the game and authorized repair. Clean main fetched/fast-forward-pulled; already current. Stage 5 acceptance gate reopened; no Stage 9 work in this turn.

## Diagnosis

- Installed hash matched; config remained range=5000, dimensional=true, refresh=20, stale=120. Server performed world work; live thread dump showed normal tick waiting, not a deadlock.
- At 15:23:36, read-only diagnostic inspection showed both GUI caches had null connection, receivedAt=0 and empty server request maps, while player/world existed.
- At 15:25:01, a read-only Render-thread probe enumerated two runtime methods with identical names and arguments: `Minecraft.getConnection():net.minecraft.network.Connection` returned null; `Minecraft.getConnection():net.minecraft.client.multiplayer.ClientPacketListener` returned the live listener, identical to player/level connection fields.
- Existing reflective lookup ignored return type. It selected the null transport getter, so neither Overview nor Ship Link sent its periodic request. This is an actual runtime descriptor collision; no specific mod is blamed without attribution evidence. Earlier tests lacked this mixed-class fixture.
- Temporary diagnostic helpers and the hash-verified official Adoptium JDK archive are under ignored laptop `backups/diagnostics/ship-link-20260910/`; only missing diagnostic executables/DLLs were added to the temporary JDK. Read-only agents were not packaged into the mod, and ended with the closed JVM. No world/config/JAR mutation during live diagnosis.

## Changes and decisions

- Shared typed getter in `PlayerOverview0670`: exact name, zero arguments, nonstatic and exact `ClientPacketListener` return descriptor. Both Overview and Ship Link use it. Null remains null and a missing descriptor throws; no unrelated-getter fallback or access bypass.
- Retained `.68.1` authoritative denial reasons. No configuration, packet or save schema change; this API identity is not a configurable gameplay value.
- Ship Link smoke test uses ASM to generate real same-name/no-arg/different-return JVM methods, tests both declaration orders, wrong-null/right-live and wrong-live/right-null cases, plus missing-type rejection. Build smoke classpath reuses the already-required ASM dependency; no new runtime dependency.
- Updated lock, generated mod list, README/test guide, roadmap and continuity records. Historical `.68.0` evidence stays valid for its observed session but does not close the fresh-session regression.

## Verification

Passed: `tools/build-newworldcore-geology-patch.ps1` using known-good `.57.0` baseline, Temurin JDK 21.0.12.1+1, ASM/ASM-tree 9.8; exit 0 and all seven suites passed. Negative config/EOF fixture messages are intentional. Embedded/source manifest matched. Java absent at installation; repo/live hashes matched.

Backup: laptop `backups/custom-mods/pre-typed-client-link-20260910-01/` contains `.68.1`, hash-verified before replacement, repository-original.jar, instance-original.jar and prior local continuity records. `.68.0` and older known-good backups remain untouched. Scoped core JAR replacement only; configs/DoctorWhoMod/world data preserved.

Failed: `.68.1` in-game fresh-session SYNCING acceptance; GUI reopen did not recover.

Not tested: `.68.2` actual game startup/telemetry/reconnect; broader Geological delayed cancellation, multiplayer and forced timeout remain open.

Final config audit found laptop `field_survey.delay_ticks=20`, repo=80. User explicitly chose **20 tick / 1 second** as the shared value. Updated only the repo property to match the existing laptop setting; Geological Survey delay unchanged. Ship Link and Overview configs matched. Core/DoctorWhoMod lock hashes and sizes matched both endpoints; third-party addon lock remained unchanged. No additional JAR required for this live property.

Config follow-up test initially retained the old 4-second GUI text expectation and failed with the correct new `S:~1.0s G:~4.0s` output. Updated the shipped-profile test expectations, recompiled only the smoke test and reran it against the installed `.68.2`: passed. Inline comments now describe the shared 20-tick value; runtime comment edit was backed up first. No mod JAR rebuild for this preference.

## Next executable step

Launch a new game session, open Player GUI near TARDIS, verify CONNECTED and Overview telemetry, then Discoveries and GUI reopen. After this succeeds, verify OUT OF RANGE/recovery and the retained denial reason. Do not proceed to remaining Stage 9 until this gate passes.

## References

`docs/14_Ship_Link_Runtime_Kabul.md`, `docs/12_Gelistirme_Yol_Haritasi.md`, `.codex/HANDOFF.md`, prior `2026-09-10_laptop_ship_link_reasons.md`.
