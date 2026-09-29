# 2026-09-29 — laptop — Emergency room check

Status: verified automated repair; runtime acceptance pending
Branch/starting commit: main / e229ed5

## User goal

Repair the failed emergency return. User closed the game and authorized continuation.

## Starting state

.71.2 at 25962 blocks displayed READY but RETURN FAILED. Server log 14:28:10.620 reports NoSuchMethodException: ServerLevel.hasChunkAt; client received FAILED at 14:28:10.632. The legacy reflective helper walks declared superclass methods, not interface defaults. Failure occurred before teleport and cooldown. Game saved/stopped 14:29:41–46; no Java process before work.

## Decisions and changes

Reuse the existing PlayerMining0700.loaded method at both Emergency room/pad and candidate checks, preserving fail-closed unloaded behavior. No range/destination/30-minute cooldown/safety policy changes, no Beacon, no config changes, no new roadmap stage. Fixture World now inherits default hasChunkAt and noCollision, matching the previously missed game API shape.

## Verification

Failed as expected: compiled updated regression against original .71.2 JAR, reproducing World.hasChunkAt NoSuchMethodException in production destination code.

Passed after repair: all 15 build smoke suites, actual Doctor room NBT/placement contract and Doctor WE formula fixture. Tests cover loaded/unloaded/blocked/missing-pad/hazard/collision, travel veto, busy ship, success and persisted cooldown. Deployment checks passed: git diff check, one core JAR per endpoint, both custom-mod hashes/sizes, matching embedded manifest, unchanged Navigation data and identical unchanged Emergency config.

Not tested: actual in-game return/relogin/cooldown and multiplayer/protection edge cases.

Installed `.71.3-alpha-emergency-room-check`, SHA-256 `e0a0058bf8b4552cfde56b1580237d0f92e4a74ddf315c22cc69fae8c3314dda`, 3685272 bytes. .71.2 far-range runtime attempt failed at 14:28:10 with ServerLevel.hasChunkAt NoSuchMethodException before teleport/cooldown. Reproduced against the old JAR using inherited interface-default chunk/collision methods. Both Emergency chunk checks now reuse PlayerMining0700.loaded (public inherited lookup); no safety bypass, new config, destination, cooldown, Navigation or WE changes. Fifteen suites plus actual Doctor room/WE contracts pass after repair. In-game return/relogin acceptance remains PENDING. Backups: backups/custom-mods/pre-emergency-room-check-20260929-01. Next: repeat Return/Confirm from current far position, then cooldown/relogin and unchanged target/route; no repeated Mining acceptance, no Stage 12.

## Backup and next step

Preserve .71.2 repo/runtime originals, overwritten runtime records and Navigation data under the backup directory above. Navigation data remains SHA-256 90b2551acb25b4786b9877a9b515bf5a782bdfa6dab8a314e37ac03807a0902b; Doctor/config unchanged. Do not write world data during installation.

Repeat Return/Confirm from the current far position, then verify safe room arrival, roughly 30-minute countdown, persistence after logout/re-entry, and unchanged Navigation target/route. See docs/17.
