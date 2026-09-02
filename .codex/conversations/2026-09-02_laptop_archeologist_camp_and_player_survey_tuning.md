# 2026-09-02 — laptop Archeologist Camp and Player Survey tuning

Status: `ARCHEOLOGIST CAMP` Field Survey recognition passed; `.3` static acceptance passed, then `.3` was superseded by installed `.4` config-first candidate before runtime acceptance

Candidate build: `NewWorldCore-1.21.1-NeoForge-0.5.60.3-alpha-player-survey-tuning.jar`

SHA-256: `954b2075506dfd4696206c83399f2512ee8581943e245e5a04f27cfa9e41035b`

## Runtime evidence before tuning

- `/locate structure betterarcheology:archeologist_camp_grassy` resolved `[-2448, ~, 192]`, 785 blocks from the player.
- Field Survey identified eight loaded structures in one run: `TRIAL CHAMBERS`, `DARK`, `VILLAGE`, `DENSE FOREST RUIN`, `INFERNAL PUMPKIN`, `ARCHEOLOGIST CAMP`, `SMALL DUNGEON`, and `RUINED PORTAL`.
- The server-side structure enumeration itself completed in about 38 ms.
- Code inspection showed the old survey checked a 96-block radius by visiting 13x13 (169) loaded chunk positions synchronously.
- This passes `ARCHEOLOGIST CAMP` family recognition but makes the player scanner feel too broad and immediate.

## Implemented tuning

- Added `config/newworldcore/player.properties` with documented controls:
  - `field_survey.range_blocks=48`
  - `field_survey.delay_ticks=80` (about four seconds)
- The default chunk grid falls from 13x13 (169 positions) to 7x7 (49 positions), while the final structure bounding-box distance still enforces the exact 48-block range.
- The delay runs off-thread, then returns the actual world scan to the Minecraft server thread.
- Only one pending Structure Field Survey is allowed per player; repeated clicks do not queue duplicates.
- Zero delay remains available for immediate results.
- Build, config smoke, range/chunk/delay formulas, delayed executor, server-thread dispatch, pending lock, repository single-JAR/hash, pack-lock, and generated mod-list checks passed.

## Next executable test

1. Continue with `2026-09-02_laptop_config_first_gui_network.md` and runtime-test installed `.4`.
2. Run Structure Field Survey while within 48 blocks of the Archeologist Camp.
3. Confirm `queued range=48 blocks delay=80t`, a roughly four-second visible scan, one completion, and `ARCHEOLOGIST CAMP` recognition.
4. Open the dynamic Structure filters and confirm `ARCHEOLOGIST CAMP` appears; this closes Radar v2.
