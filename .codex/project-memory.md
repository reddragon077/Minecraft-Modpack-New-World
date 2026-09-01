# New World project memory

Last synchronized: 2026-09-01

## Canonical workflow

- GitHub repository: `https://github.com/reddragon077/Minecraft-Modpack-New-World`
- GitHub `main` is the canonical shared source for development across computers.
- CurseForge instance directories are local runtime and test endpoints.
- Registered machines: `laptop` and `desktop`; paths are stored under `machines/`.
- Desktop repository root: `E:\projects\Minecraft-Modpack-New-World`.
- Desktop CurseForge instance: `C:\Users\suley\curseforge\minecraft\Instances\New World`.

## Platform

- Minecraft 1.21.1
- NeoForge 21.1.235
- Third-party addons are pinned in `manifest.json` and audited in `pack-lock.json`.
- Only the two project-owned fork JARs are stored directly in Git: NewWorldCore and DoctorWhoMod.

## NewWorldCore continuation point

- Active synchronized test build: `NewWorldCore-1.21.1-NeoForge-0.5.59.6-alpha-radar-route-cleanup.jar` (SHA-256 `4b97a91af5d7c45aabb19c0f1fe4223141a4ffa0800878e21bca24ce2aefa8d0`). It is installed and accepted on the desktop instance; the laptop installation state remains to be verified.
- Desktop acceptance evidence shows more than four reachable results and modded classes including Explorify, Better Dungeons, and Structory. The old four-record limit is fixed, shared placement sets now remain `UNKNOWN STRUCTURE`, and the false `MODDED STRUCTURE` selection was removed safely.
- The verified `0.5.57.0` baseline is backed up under the laptop instance's `backups/custom-mods/known-good-0.5.57.0/` directory.
- The complete historical source tree is still missing. The reproducible compatibility delta is stored under `src-patches/newworldcore/` and built with `tools/build-newworldcore-geology-patch.ps1`.
- `0.5.56.0-alpha-radar-navigation-mining-recovery` was previously described as a known-good recovery baseline.
- Verify binaries and local source before selecting a development baseline; old chat labels are context, not proof.
- Preserve physical geology world generation, radar coordinates, deposit persistence, and navigation `DEPOSITS` integration.

## Geology resource rules

- One deposit family per real resource, not one duplicate per mod.
- Current `0.5.59.6` definitions: vanilla families plus Osmium, Tin, Lead, Uranium, Fluorite, Aluminum, Nickel, Silver, Zinc, Platinum, Uraninite, and Certus Quartz.
- Immersive Engineering supplies Aluminum and Silver; Nickel is shared with Oritech, while Lead and Uranium reuse their existing families.
- Create supplies Zinc; Oritech supplies Platinum; Powah Uraninite remains separate from Uranium.
- Applied Energistics 2 uses normal Certus Quartz only; Charged Certus is explicitly excluded.
- Lead is shared by Mekanism and Immersive Engineering.
- Nickel is shared by Immersive Engineering and Oritech.
- Uranium is shared where mods represent the same resource.
- The 0.5.59.4 JAR passed compile, archive, binary entry-point, definition/template, SHA-256, desktop launch/world-load, result-list, modded-candidate, and scan-completion acceptance. The desktop log shows 106 placement tasks completed with 101 results in about 5.2 seconds.
- Structure Radar evolution: `0.5.59.0` namespace batches caused 40+ second ticks; `0.5.59.1` exact locate calls still caused 2–8 second spikes; `0.5.59.2` stalled at 128 results; `0.5.59.3` completed but its legacy fixed-label finish filter removed modded families and its vanilla locate tiles caused 8–13 second spikes. `0.5.59.4` uses placement math for both vanilla and modded structures, preserves dynamic family labels, completes every task, and retains the nearest 128 results. Placement candidates may be false positives until Field Survey confirms a generated structure.
- Confirmed `0.5.59.4` label defect: a placement shared by multiple structure families falls back to `MODDED STRUCTURE` in `Navigation0581DynamicStructureScanner.PlacementTask.family()`. That is not a registry structure or a discoverable place. Multi-family candidates must remain `UNKNOWN STRUCTURE` (or otherwise explicitly possible/unconfirmed) until Field Survey identifies an actual generated structure.
- Player Structure Field Survey reads actual nearby structure starts. Desktop `0.5.59.5` acceptance identified exactly five real structures (`VILLAGE`, `ABANDONED CAMP`, `SMALL DUNGEON`, `METEORITE`, `SPIDER DUNGEON`) and no geology deposit; structure/geology isolation passed.
- `0.5.59.5` removed 17 invalid persisted structure records and eliminated `MODDED STRUCTURE` from the discovery list. Its cleanup wrote a null selected-key sentinel, causing `Navigation0471gFix.checkArrivals` to emit `InvocationTargetException` every tick when the removed record had been selected. `0.5.59.6` resets it to the required empty-string sentinel; the world remained active for over 20 minutes with zero arrival-check/InvocationTargetException entries, the UI showed `NO TARGET SELECTED`, and 28 genuine geology records including `COPPER-RICH DEPOSIT` remained visible.
- `explorify:campsite` family recognition passed on desktop: locate found `[-2512, ~, -992]`, and Field Survey reported `CAMPSITE` after travel. The dynamic `CAMPSITE` filter still needs visual confirmation; `ARCHEOLOGIST CAMP` remains the last untested family in the Radar v2 closure set.
- FE Matrix registration now deterministically selects Architectury's `register(ResourceLocation, Supplier)` overload, removing the nondeterministic startup `argument type mismatch` path.
- The Uraninite path passed an initial in-game acceptance test: Geological Radar -> Discovery -> Navigation target -> TARDIS route -> matching physical deposit. Full family, Mining and balance regression remains open.
- `docs/12_Gelistirme_Yol_Haritasi.md` is the active implementation order. Finish its Radar/Discovery/Player Interface/Deposit stages before Research and Production Chamber progression.

## Ship and progression decisions

- The ship is a permanent mobile research base and central progression space.
- The three-level main hull and room placement were considered complete.
- Standard modular concepts: five-block corridors, 3x4 bulkhead openings, and typical seven-block floor modules.
- Building Gadgets 2 and WorldEdit are development tools.
- Retained roadmap themes: research, production chambers, Android companion/UI, warp core, teleport network, nano suit, story quests, and first playable alpha.
- Radar/navigation, replication, geology, and TARDIS systems should remain one coherent progression loop.

## Safety and maintenance

- Inspect first, then change.
- Preserve known-good custom builds before replacement.
- Never load multiple NewWorldCore or DoctorWhoMod versions in one instance.
- Do not commit worlds, saves, backups, logs, crash reports, user caches, accounts, launcher metadata, or personal options.
- After substantial verified work, update this memory and `pack-lock.json`, then commit and push.
- Cross-computer conversation continuity lives in `.codex/HANDOFF.md` and `.codex/conversations/`. Read them after every pull and update them before switching computers.
