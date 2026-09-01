# Multi-computer workflow

GitHub `main` is the canonical source for New World. Each computer keeps two local endpoints:

1. A Git clone used for versioning and collaboration.
2. A CurseForge instance used to launch and test the pack.

## Repository contents

- `manifest.json`: CurseForge-compatible, pinned project and file IDs.
- `pack-lock.json`: exact addon list, filenames, enabled state, and available hashes.
- `config/`, `defaultconfigs/`, `kubejs/`: shared pack behavior.
- `mods/NewWorldCore-*.jar` and `mods/DoctorWhoMod-*.jar`: the two project-owned fork builds.
- `machines/`: non-secret connection records for each development computer.

Third-party mod, resource-pack, and shader-pack binaries are not committed. CurseForge resolves their pinned versions from `manifest.json`.

## Normal workflow

Before work:

1. Pull `main` in the repository.
2. Read `.codex/HANDOFF.md`, `.codex/project-memory.md`, and the newest record listed in `.codex/conversations/INDEX.md`.
3. Apply repository files to the local CurseForge instance with `tools/apply-to-instance.ps1`.
4. Launch and test through CurseForge.

After a successful change:

1. Refresh the repository from the tested instance with `tools/refresh-from-instance.ps1`, passing the registered machine name (`laptop` or `desktop`).
2. Regenerate the readable inventory with `tools/update-mod-list.ps1` so it stays aligned with `pack-lock.json`.
3. Review `git status` and the diff.
4. Commit and push to `main`.

Before pausing or changing computers, even when a fix is not yet tested:

1. Replace `.codex/HANDOFF.md` with the exact current state and next executable step.
2. Create a dated summary from `.codex/conversations/TEMPLATE.md` and add it to `.codex/conversations/INDEX.md`.
3. Clearly separate `verified`, `failed`, and `not tested` statements.
4. Commit and push the code and context together. On the other computer, pull before opening or testing the pack.

The Git record stores compact project decisions and test evidence, not raw private transcripts. GitHub `main` is the handoff point; a Codex chat being visible in the same account is not required for continuity.

Do not sync worlds, saves, JourneyMap data, options, accounts, logs, crash reports, caches, or backups through GitHub.

## First setup on another computer

Build a CurseForge import archive with `tools/build-curseforge-package.ps1`, import the generated ZIP in CurseForge, clone this repository, and add a new machine record under `machines/`. Record each computer's actual paths rather than copying paths between machines.

Open the exact clone directory as the Codex/VS Code project (for example `E:\projects\Minecraft-Modpack-New-World`), not only its parent directory. This makes the repository guidance and handoff files discoverable immediately.
