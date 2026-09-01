# New World project guidance

This repository is the canonical source for the New World Minecraft modpack.

- Read `.codex/project-memory.md` before changing or diagnosing the project.
- GitHub `main` is the shared source of truth; CurseForge instance directories are local runtime/test endpoints.
- Verify runtime files before copying changes back into this repository.
- Keep third-party mod JARs out of Git. They are pinned through `manifest.json` and `pack-lock.json`.
- Only the two project-owned fork builds may be committed under `mods/`: `NewWorldCore-*.jar` and `DoctorWhoMod-*.jar`.
- Never commit saves, backups, logs, crash reports, user caches, launcher credentials, or machine-specific Minecraft options.
- Preserve a known-good custom JAR before replacing it, and never load multiple versions of the same custom mod in one instance.

