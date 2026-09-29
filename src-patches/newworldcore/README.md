# NewWorldCore geology patch source

The complete historical NewWorldCore source tree is not currently available in the workspace. This directory keeps the expanded-geology delta reproducible without pretending that a decompiled binary is the original project source.

The current patch:

- `.72.1-alpha-unified-ship-alerts`: all raw Overview warnings now feed the background history/HUD sampler, not its three clipped display rows. FE/WE thresholds come only from `overview.properties`; offline matrices/BUFFER_FULL follow Overview WARNING severity. Mining energy/wait and telemetry failures are covered; unknown operational faults never falsely resolve. Existing Collection/drive checks remain. Alert wire schema2 uses an int known-mask for 23 kinds; no persistent-save schema change. Runtime acceptance pending. `.72.0` screenshot proved missing FE coverage. See `docs/18_Ship_Alerts_Runtime_Kabul.md`.

- changes Emergency destination in `.71.4` at the user's explicit request: inside the main entrance door, exactly where the normal portal arrives (`getEntrancePosition().relative(getEntranceFacing())`, bottom center and entrance yaw). Requires a live loaded registered door and safe arrival cell; no Teleporter Room or neighboring-cell fallback. The `.71.3` return succeeded in-game at 14:42:25, but its separate room was not the user's intended destination. New entrance runtime acceptance is pending.

- replaces Beacon with confirmed owner-only emergency return. No Navigation target/route, ship flight, WE or existing discovery writes. Old Beacon request codes are rejected. Normal remote-link range/dimension limits do not disable rescue; ownership and fresh server telemetry still apply.
- persists a 30-real-minute successful-return cooldown in the vanilla/NeoForge PlayerPersisted player compound, including logout/death; offline time counts. Failed/vetoed returns do not charge. Existing cooldown is not reset by the destination update. Loaded chunks, air/solid floor, hazards, world border and collision are checked using the inherited-method-aware chunk helper; no terrain changes.
- passes 15 suites, the actual Doctor entrance portal bytecode contract, and the existing Doctor WE fixture. Adapter tests cover four facings, translated doors, exact bottom-center/yaw, missing/unloaded/blocked/hazard/collision, no fallback, veto and persisted cooldown. External game boundaries are fixtures; runtime acceptance remains separate (docs/17).
- includes accepted `.70.1` per-area persistent successful resource-block counts and confirmed Mining Shield OFF-only STOP; legacy counts remain explicitly untracked. Basic runtime user acceptance passed, broader edge tests remain open.

- adds the `.70.0` read-only Player Mining four-area view: runtime/scan center, resource-only progress, three item/type-slot buffers and routing mode/counters. Separate bounded frames, owner/link/stale guards and live `player-mining.properties`; no controls, buffer binding or chunk loading;
- verifies the mining adapter against actual shipped mining State classes, progress/hazard semantics, inherited chunk-check API, tab navigation, malformed protocol, config and layout fixtures. Build now runs twelve smoke suites plus the actual DoctorWhoMod WE formula fixture; full Minecraft runtime acceptance remains separate;

- recompiles the compatibility helpers under `java/`;
- patches the existing radar scan entry points so installed modded structures are read from Minecraft's live registry and searched through placement math without chunk generation;
- normalizes structure variants into shared discovery families;
- isolates Player Field Survey from geology records and scans actual nearby structure starts;
- selects the FE Matrix `ResourceLocation` registration overload deterministically;
- preserves the verified `0.5.57.0` JAR as its baseline;
- adds eight new data-driven deposit families;
- updates the existing Lead and Uranium physical palettes so those families are shared across installed mods;
- uses a new persistent chunk marker while preserving the older vanilla and Mekanism markers;
- records all template seeds and registry IDs in `patch-manifest.json`.
- upgrades the shared Discovery Database to schema v3 with persistent analysis level and last-seen fields;
- preserves first discovery time and stronger FIELD evidence on repeat observations;
- exposes one synchronous discovery event bus for later Research and Exploration XP listeners.
- enables the Player Survey geological card and routes its reserved mode through a short-range physical-evidence scan;
- verifies loaded deposit-template blocks before recording visited GEOLOGY/FIELD discoveries at the configured analysis level;
- exposes geological field-survey range, delay, result cap, verification budget, and match threshold in `player.properties`.
- adds the read-only Player Overview using owner-resolved server snapshots, shared FE/Warp/engine/Mining/Navigation sources and bounded independent network frames;
- observes actual non-simulated FE withdrawals without changing the original pool operation; counters are transient, isolated by pool/ship, and separate from sampled net FE change;
- supplies `overview.properties` for refresh, stale handling, health thresholds and session-warning rows, with protocol/config/geometry/meter regression tests.

Build with `tools/build-newworldcore-geology-patch.ps1`. The script verifies the baseline SHA-256 before producing a new JAR, then runs the config, field-survey, and Discovery schema/event smoke tests. It also needs matching ASM core and ASM tree JAR paths from the local NeoForge runtime.

This patch must eventually be folded back into the full NewWorldCore source project when that source is recovered.
