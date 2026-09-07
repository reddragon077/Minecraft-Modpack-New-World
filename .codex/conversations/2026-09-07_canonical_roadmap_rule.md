# 2026-09-07 — canonical roadmap and cross-computer rule

Status: `verified`
Branch/commit: `main` / rule-introduction commit (see file history)
Build: documentation/rule-only change; accepted `0.5.66.1` JAR unchanged

## User goal

Make the reconciled roadmap order and safe two-computer workflow a permanent project-wide rule.

## Decisions and changes

- Added `.cursor/rules/canonical-roadmap-workflow.mdc` with `alwaysApply: true`.
- The rule makes GitHub `main` canonical, requires clean fast-forward startup/handoff checks, and makes the 14-stage roadmap the numeric implementation order.
- Until a verified roadmap commit changes the gate, development proceeds through Stage 4, Stage 5, then the remaining Stage 9 work.
- Stale prompts cannot reopen completed work; conflicts between records and real JAR/runtime evidence must be reconciled before new development.
- Root `AGENTS.md`, project memory and handoff explicitly reference the mandatory rule so both computers follow it after pulling `main`.

## Verification

- Rule frontmatter uses the required `.mdc` format and `alwaysApply: true`.
- No JAR, Java source, config, manifest, lock, world, save, log, backup or cache file was changed.

## Next executable step

On the next computer, fast-forward `main`, read the normal handoff chain, and begin Stage 4 Overview / Ship Status.
