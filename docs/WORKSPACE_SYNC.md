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
2. Apply repository files to the local CurseForge instance with `tools/apply-to-instance.ps1`.
3. Launch and test through CurseForge.

After a successful change:

1. Refresh the repository from the tested instance with `tools/refresh-from-instance.ps1`, passing the registered machine name (currently `laptop`).
2. Review `git status` and the diff.
3. Commit and push to `main`.

Do not sync worlds, saves, JourneyMap data, options, accounts, logs, crash reports, caches, or backups through GitHub.

## First setup on another computer

Build a CurseForge import archive with `tools/build-curseforge-package.ps1`, import the generated ZIP in CurseForge, clone this repository, and add a new machine record under `machines/`. Once the desktop computer is available, record its actual paths rather than copying the laptop paths.
