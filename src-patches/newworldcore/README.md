# NewWorldCore geology patch source

The complete historical NewWorldCore source tree is not currently available in the workspace. This directory keeps the expanded-geology delta reproducible without pretending that a decompiled binary is the original project source.

The current patch:

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

Build with `tools/build-newworldcore-geology-patch.ps1`. The script verifies the baseline SHA-256 before producing a new JAR. It also needs matching ASM core and ASM tree JAR paths from the local NeoForge runtime.

This patch must eventually be folded back into the full NewWorldCore source project when that source is recovered.
