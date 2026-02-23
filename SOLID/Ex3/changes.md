# Ex3 Refactoring — What I Did

## The Problem

`EligibilityEngine.evaluate` had a long if/else chain. Each rule (disciplinary flag, CGR, attendance, credits) was hard-coded. Adding a new rule meant editing that method and risking regressions. Rules, evaluation logic, and configuration were all tangled together.

## What I Changed

### 1. Introduced a rule abstraction

Created `EligibilityRule` interface with a single method: `check(StudentProfile)` returns a failure reason string if the rule fails, or null if it passes. Each rule is now a small, single-purpose class.

### 2. Extracted each rule into its own class

- `DisciplinaryFlagRule` — fails if disciplinary flag is not NONE
- `CgrRule` — fails if CGR below 8.0
- `AttendanceRule` — fails if attendance below 75%
- `CreditsRule` — fails if earned credits below 20

To add a new rule, just create a new class implementing `EligibilityRule` and wire it into the list. No editing of the main evaluation logic.

### 3. Replaced the if/else chain with iteration

`EligibilityEngine` now holds a list of rules and iterates. First rule that fails wins; we return immediately with NOT_ELIGIBLE and that reason. Order is preserved: disciplinary → CGR → attendance → credits.

### 4. Decoupled persistence

Introduced `EligibilityStore` interface. `FakeEligibilityStore` implements it. Engine depends on the interface, not the concrete store.

### 5. Moved result into its own class

`EligibilityEngineResult` is now a separate file so `ReportPrinter` and others can use it cleanly.

## Files I Added

- `EligibilityRule.java` — interface for rules
- `DisciplinaryFlagRule.java`, `CgrRule.java`, `AttendanceRule.java`, `CreditsRule.java` — one rule per class
- `EligibilityEngineResult.java` — result DTO (moved out of engine)
- `EligibilityStore.java` — interface for saving evaluations

## Files I Modified

- `EligibilityEngine.java` — uses rule list, no giant conditional
- `FakeEligibilityStore.java` — implements `EligibilityStore`

## What Stayed the Same

- `Main.java`, `StudentProfile`, `ReportPrinter`, `LegacyFlags` — unchanged
- Output is identical

## Output (running `java Main`)

```
=== Placement Eligibility ===
Student: Ayaan (CGR=8.10, attendance=72, credits=18, flag=NONE)
RESULT: NOT_ELIGIBLE
- attendance below 75
Saved evaluation for roll=23BCS1001
```
