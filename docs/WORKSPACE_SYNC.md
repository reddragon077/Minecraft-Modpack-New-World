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

1. Check the branch and working tree. Preserve local changes; reconcile them before pulling. With clean `main`, fetch and pull fast-forward only.
2. Read `.codex/HANDOFF.md`, `.codex/project-memory.md`, and the newest record listed in `.codex/conversations/INDEX.md`.
3. Compare the registered instance with the repository first. If runtime installation is needed, close the game, preserve known-good files/backups, review the scope and use `tools/apply-to-instance.ps1`. Documentation-only updates do not require a new build or broad runtime/config apply.
4. Launch and test through CurseForge.

After a successful change:

1. For tested runtime changes, inspect the scope of `tools/refresh-from-instance.ps1` and pass the registered machine name (`laptop` or `desktop`). Reconcile source/config differences; do not overwrite newer canonical work blindly. Documentation-only changes can be made directly in the repository.
2. If inventory changed, regenerate the readable inventory with `tools/update-mod-list.ps1` so it stays aligned with `pack-lock.json`. Do not churn locks/manifests or produce a new JAR for documentation-only edits.
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
