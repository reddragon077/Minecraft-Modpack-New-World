# 2026-09-10 — laptop — sync recovery and Stage 4 Overview

Status: `in progress` — installed candidate, runtime pending
Branch/base commit: `main` / `2caedef1b588aa032be3c4b0e4e79f3b333db95c`
Build: `NewWorldCore-1.21.1-NeoForge-0.5.67.0-alpha-player-overview.jar`
SHA-256: `4273134984af8eeb8eece991c70b6a139b230c94cdd8519dd2badb1ae58f5d9e` (3611837 bytes)

## User goal and starting state

Continue the canonical next stage, Overview, respecting shared GitHub-first rules. The laptop/repo held accepted `.66.1`; unexpected `(1)` copies polluted the worktree and refs. User authorized preserving them externally and continuing.

## Recovery

- Moved 27 untracked sync-copy/old-JAR/backup files, preserving relative paths and checking SHA-256, to `D:\Projects\NewWorld-recovery-20260910-01/`. No tracked source was overwritten or file deleted.
- Fetch initially failed with `bad object refs/heads/main (1)`. `ls-remote` confirmed real main was `2caedef`.
- Archived nine duplicate Git metadata files under that recovery directory's `git-metadata/`: COMMIT_EDITMSG, FETCH_HEAD, index, ORIG_HEAD, logs/HEAD, logs/refs/heads/main, logs/refs/remotes/origin/main, refs/heads/main and refs/remotes/origin/main, each with ` (1)` suffix. Exact targets, containment and hashes were checked.
- Fetch and `pull --ff-only origin main` succeeded, already current, clean tree before implementation. Background sync is a suspected origin, not proven; no Drive settings changed.

## Implementation and decisions

- New `PlayerOverview0670` samples the existing owner-resolved manager on the server thread. FE uses LongFEEnergySystem; Warp reads the ship interior; engine/brake/shield/exterior come from TARDIS state; Mining and Navigation read shared records, and Matrix state reads ShipRoomRegistry.
- Positive reserved mode 4 requests bounded read-only snapshots. S2C data uses an isolated three-byte-per-int range, bounded framing and clientbound gating, without changing the registered packet schema. Long FE values survive transfer. No chunk generation or control writes.
- `OverviewEnergyMeter0670` observes the unchanged LongFESavedData withdrawal result. Simulations and zero withdrawals do not increment; counters are transient, pool/ship isolated, and wrap-safe for a valid long-sized sampling interval. OUT includes external transfers. NET is separately sampled storage delta; legacy administrative assignments are not OUT.
- Missing data is UNKNOWN/UNAVAILABLE; failed refresh hides old data as STALE. Screen reopen waits for a fresh snapshot; connection change resets the cache.
- `overview.properties` has Turkish documented, live-reloaded safe bounds: refresh 40 ticks, stale 120 (at least twice refresh), warning 20%, critical 5% (never above warning), warning rows 2 (1–3). Last warnings are per player/ship observation session, not persistent world events.
- No Stage 5 permanent Ship Link or Stage 9 dedicated Navigation UI was implemented. Stage 4 remains partial until gameplay acceptance.
- Updated the build manifest with real Overview work and the previously missing `.66.1` action/last-seen/reserve descriptions; source and embedded manifests now match.

## Verification

### Passed

- Build script against verified `.57.0` baseline with Temurin JDK 21.0.12.1+1 and ASM 9.8.
- Existing five smoke suites: config, Discovery schema/events, Geological Field Survey, geology analysis progression, Player Discoveries.
- New Overview smoke suite: config fallback/bounds/reload, thresholds and unknown capacity, Long.MAX_VALUE/UTF snapshot round-trip, unrelated protocol values, malformed/oversized frames preserving the previous snapshot, no-ship/reset, consumption simulation exclusion/pool isolation/wraparound, and headless text/panel bounds.
- `javap` verified Overview render hook and preserved original withdrawal method followed by the meter hook. Embedded manifest is byte-for-byte equal to source.
- Game was closed (no java/javaw). Repo and instance each have one NewWorldCore matching the candidate hash and one unchanged DoctorWhoMod matching `66c1c5e272ccb8e9c54fd879d16da75045a4c9ea07cebbf65fab455a99e38356`.
- Known-good `.66.1` copied and hash-verified before removal from mods; retained under laptop `backups/custom-mods/pre-overview-20260910-01/` and external recovery `known-good-0.5.66.1/`. Original files were archived too, not deleted.
- Scoped install copied only the new Overview config and config README. Broad apply/refresh was deliberately not used because it would overwrite unrelated user runtime config changes. Lock custom-JAR entry updated directly; third-party metadata unchanged.

### Failed during development

- Initial fetch hit the duplicate ref described above; resolved by verified archival, no reset.
- First geometry test mistakenly applied two-column bounds to the full-width STALE explanation; corrected the test mode and reran all suites successfully.
- Invalid config and EOF messages in smoke output are intentional negative fixtures, not observed game errors.

### Not tested

Game launch, real GUI layout, live FE/WE/meter correlation, engine/brake/shield updates, Navigation readout, reconnection and remote multiplayer behavior. No gameplay/log acceptance claim is made.

## Next executable step

Open Player Ship Interface → OVERVIEW and follow `docs/13_Overview_Runtime_Kabul.md`; inspect `[NewWorld Overview]` log messages afterward. Keep Stage 4 `[~]` until proven. Then proceed Stage 5, then remaining Stage 9.
