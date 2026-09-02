# 2026-09-02 — laptop config suite build

Status: static acceptance, laptop installation, and scan runtime acceptance passed; superseded by the `0.5.60.2` Geology buffer-flush candidate

Build: `NewWorldCore-1.21.1-NeoForge-0.5.60.0-alpha-config-suite.jar`

SHA-256: `8886c622e2ae962e3b7980283b9b5bc2dd796e65394c9d8a30b362b97955fb3b`

## Runtime evidence before the config build

- Laptop `0.5.59.10` queued 102 placement-only tasks at 5000 blocks with `selected=ALL`.
- The scan completed with 101 mixed-family results in about 5.6 seconds.
- This closes the pending laptop launch and `ALL` mixed-family Radar regression for `0.5.59.10`.

## Implemented configuration system

- Added the central `config/newworldcore/` directory with separate Radar, Mining, Matrix/Warp, travel, geology, replication, room, and network files.
- Radar placement pacing wraps the existing four-tick batch scheduler through `scan.batch_interval_ticks`; the shipped profile uses `8`, approximately half the prior rate. `4` restores the old rate and `16` gives an approximately quarter-rate scan. This preserves Speed-upgrade batching and charges FE only for batches that actually run.
- Radar result limit, placement-task limit, duplicate radius, navigation range/workload, CPU multipliers, and per-task FE cost are connected to live formulas.
- Mining probes, mining interval, mining FE cost, and efficiency are connected to live formulas.
- FE/Warp Matrix capacity, transfer, provider bonus, tier weight, Warp conversion cost, production, and engine travel range are connected to live formulas.
- Geology scan energy, replication interval/batch, room protection, and emergency FE reserve policy are connected to live code paths.
- Config files are rechecked at most once per second; a new Radar scan explicitly clears the config cache.

## Static acceptance

- JAR compilation and patch replacement counts passed.
- Default configuration smoke test passed.
- Bytecode inspection confirmed navigation, replication, room-protection, and emergency-network hooks.
- Repository `mods/` contains exactly one NewWorldCore JAR with the hash above; `pack-lock.json` and the generated mod list match it.
- After game shutdown, `tools/apply-to-instance.ps1` preserved `0.5.59.10` under `backups/custom-mods/pre-apply-20260902-133637` and installed `0.5.60.0`.
- Repository/instance NewWorldCore hashes match; DoctorWhoMod remained single/hash-matched; eight live config files and `scan.batch_interval_ticks=8` were verified.

## Runtime acceptance

- Structure Radar queued 102 placement-only tasks at 5000 blocks with `selected=ALL` and returned 101 mixed results in about 9.98 seconds.
- Geology returned 48 deposits from its 5000-block scan in about 9.00 seconds.
- The pacing and scan result acceptance passed. Opening the populated Geology filter exposed a separate popup layering defect; that follow-up is recorded in `2026-09-02_laptop_geology_filter_layer_repair.md`.

## Next executable step

1. Visually accept the installed `0.5.60.2` Geology buffer-flush repair.
2. Complete the remaining `ARCHEOLOGIST CAMP` Field Survey and dynamic-filter chain.
