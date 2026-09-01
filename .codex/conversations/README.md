# Project conversation records

These files carry project context between the desktop and laptop through GitHub. They are concise engineering records, not raw chat exports.

## Rules

- One file per meaningful work session, named `YYYY-MM-DD_machine_topic.md`.
- Copy `TEMPLATE.md`, fill every applicable section, and link the new file from `INDEX.md`.
- Put the latest actionable state in `../HANDOFF.md`; do not make the next computer reconstruct it from many old records.
- Separate facts into **verified**, **failed**, and **not tested**. A compiled or copied build is not an in-game pass.
- Include exact versions, hashes, paths relative to the repository, commit IDs, and test observations when relevant.
- Capture stable user decisions and rejected approaches so another chat does not reopen settled choices without evidence.
- Do not store credentials, tokens, personal data, raw launcher files, full logs, worlds, or large verbatim conversations.
- If an old chat conflicts with current files or test evidence, the current Git state and newest verified record win.

## Device-switch contract

Before leaving one computer: update `../HANDOFF.md`, add a session record, update `INDEX.md`, then commit and push. On the other computer: pull first, read the handoff, apply the repository to the registered CurseForge instance, then continue the stated next test.
