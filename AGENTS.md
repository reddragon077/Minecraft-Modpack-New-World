# New World project guidance

This repository is the canonical source for the New World Minecraft modpack.

- Before changing or diagnosing the project, read `.codex/project-memory.md`, `.codex/HANDOFF.md`, and the newest entry in `.codex/conversations/INDEX.md`.
- GitHub `main` is the shared source of truth; CurseForge instance directories are local runtime/test endpoints.
- Verify runtime files before copying changes back into this repository.
- Keep third-party mod JARs out of Git. They are pinned through `manifest.json` and `pack-lock.json`.
- Only the two project-owned fork builds may be committed under `mods/`: `NewWorldCore-*.jar` and `DoctorWhoMod-*.jar`.
- Never commit saves, backups, logs, crash reports, user caches, launcher credentials, or machine-specific Minecraft options.
- Preserve a known-good custom JAR before replacing it, and never load multiple versions of the same custom mod in one instance.
- When work pauses, the computer changes, or a substantial decision is made, update `.codex/HANDOFF.md` and add a dated record under `.codex/conversations/` using `TEMPLATE.md`.
- Record decisions, exact build names/hashes, changed files, commands or tests actually run, their real results, and the next executable step. Never mark an untested fix as passed.
- Keep raw personal chat, credentials, launcher data, logs, worlds, and large copied transcripts out of Git; store concise project-relevant summaries instead.

