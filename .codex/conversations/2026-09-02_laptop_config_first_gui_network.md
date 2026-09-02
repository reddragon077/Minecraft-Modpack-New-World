# 2026-09-02 — laptop config-first GUI and network

Status: `0.5.60.4` build/static/install/runtime acceptance passed; Radar v2 closed

Installed build: `NewWorldCore-1.21.1-NeoForge-0.5.60.4-alpha-config-first-gui-network.jar`

SHA-256: `171b1cb6a0ca78c243ad81584f820d89fc32829e0269459048d549b462f390e9`

## Request and decision

- User requested that simple future adjustments should not require rebuilding the JAR.
- Config-first is now an always-applied project convention in `.cursor/rules/config-first-development.mdc`.
- Gameplay balance, duration, range, energy, capacity, performance and safe GUI appearance belong in `config/newworldcore` with Turkish comments and smoke coverage.
- Save schemas, registry IDs, packet/slot codes and data-integrity constants remain intentionally non-configurable.

## Added controls

- `gui.properties`: filter overlay Z depth, Player GUI background dim percentage, Survey range visibility and Survey delay visibility.
- The Player SURVEY tab no longer contains `STRUCTURE RANGE: 96 blocks`; it builds `48 blocks // ~4.0s` from live config values.
- `network.properties`: FE/item/fluid/gas network-node transfer and buffer-capacity multipliers for the existing MK0-MK8 curves.
- The `.3` Field Survey tuning remains included: 48 blocks, 80 ticks, 7x7/49 chunk positions, server-thread scan and one pending request per player.

## Static/install evidence

- Config smoke passed for Survey, GUI, FE/item network values and the live detail line.
- Bytecode calls `playerSurveyDetailLine`, `guiPlayerBackdropArgb`, `guiFilterOverlayZ`, `networkNodeTransferLimit` and `networkNodeCapacityLimit`; the old 96-block label is absent.
- Repository and laptop contain one matching `.4` JAR with the SHA-256 above.
- Ten `.properties` files exist in both locations.
- `.3` was preserved under `backups/custom-mods/pre-apply-20260902-164534`.

## Next test

Runtime evidence: `.4` loaded, queued `range=48 blocks delay=80t`, completed once in `4074ms`, and identified `TRIAL CHAMBERS, ARCHEOLOGIST CAMP`. No relevant NewWorldCore error occurred. The user screenshot visibly confirms `ARCHEOLOGIST CAMP` in the clean Structure Filters popup.

Next work: commit/push `.4`, then begin Discovery Database analysis-level/last-seen/event work.
