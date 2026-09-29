# 2026-09-29 — laptop — Emergency entrance destination

Starting commit: main / 1d77528. Status: automated checks passed; new in-game destination acceptance pending.

## User decision and evidence

User reported successful return to the wrong location and explicitly requested the inside front of the main door, the normal portal arrival location. This supersedes the earlier separate Teleporter Room and no-entrance policy. Server log 14:42:25.185 confirms .71.3 arrival at [-12,128,-21] and 1800000ms cooldown; client success receipt followed. User closed game; all dimensions saved 14:43:42. User then explicitly requested resetting their existing cooldown to allow immediate testing. The UUID-verified playerdata and singleplayer level.dat Player compound were backed up and only NewWorldEmergencyReturnUntilV1 changed from 1790683944807 to 0. Exact decompressed-byte comparison verifies all other NBT data unchanged. Navigation save hash remains 90b2551acb25b4786b9877a9b515bf5a782bdfa6dab8a314e37ac03807a0902b. No terrain, inventory, position or other player change.

## Implementation and verification

Actual shipped Doctor BaseTardisExteriorBlock.getPortalDestination uses getEntrancePosition().relative(getEntranceFacing()), Vec3.atBottomCenterOf, and facing.toYRot. .71.4 reuses those getters and exact position/yaw while keeping existing EntityHelper travel-veto-aware teleport and verified-arrival cooldown. Requires the registered interior door to exist in the loaded world; same floor/air/fluid/hazard/border/collision checks. Unsafe entrance fails without fallback or cooldown. UI/config comment updated, numeric settings unchanged.

Full build passed 15 smoke suites, actual Doctor portal bytecode-contract check and existing WE fixture. Adapter coverage includes four cardinal facings, translated door, exact vector/yaw, inherited chunk/collision methods, missing/blocked/unloaded/hazard/collision entrance, no safe-neighbor fallback, travel veto, busy ship, success and persistent cooldown. These are not in-game entrance acceptance.

Installed `.71.4-alpha-emergency-entrance`, SHA-256 `fe85ac588ab5f2f7449590eb1d939f59bdff789e4f9b6d9001728acc538690ab`, 3682811 bytes. USER DESTINATION CHANGE: normal portal arrival INSIDE the main entrance door, replacing separate Teleporter Room/no-entrance policy. .71.3 returned successfully at 14:42:25 to [-12,128,-21] with 1800000ms cooldown, but user rejected that location. Uses Doctor getEntrancePosition().relative(getEntranceFacing()), bottom-center and entrance yaw; loaded live registered door plus existing safety checks, no other-room/neighbor fallback. Beacon absent; ownership/travel veto/30-minute persistent cooldown/Nav/route/WE unchanged. Normal cooldown persistence is unchanged. User additionally authorized a one-time local test reset: exact cooldown long set to zero in matching playerdata and level.dat/Data/Player, verified all other decompressed bytes unchanged; originals preserved under backups/custom-mods/emergency-cooldown-reset-20260929-01. Fifteen suites + actual Doctor entrance portal contract + WE fixture passed. New in-game entrance/relogin acceptance PENDING. User closed game (saved 14:43:42); originals and overwritten runtime files preserved under backups/custom-mods/pre-emergency-entrance-20260929-01. Next: test immediately from outside after one-time reset; entrance-return/countdown/relogin/unchanged-route check. No Stage 12 or repeated Mining tests.

## Next grouped check

Existing cooldown was reset once at the user's request; test now from outside and compare to the normal portal's inside door arrival. Verify new 30-minute countdown, unchanged Navigation target/route, then logout/re-entry persistence. Do not repeat the one-time reset or accepted Mining tests. See docs/17.
