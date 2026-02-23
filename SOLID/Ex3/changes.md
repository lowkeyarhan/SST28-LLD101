# Ex3 Refactoring — Placement Eligibility Rules Engine

## The Problem

The `EligibilityEngine.evaluate` method had a long if-else chain checking multiple eligibility criteria:

- Checking disciplinary flag
- Checking CGR threshold
- Checking attendance percentage
- Checking earned credits

This violates the **Open/Closed Principle** (OCP) because:

1. Adding a new rule requires editing the massive `evaluate` method
2. The rules are hard-coded in the engine
3. It's hard to test individual rules in isolation
4. The code is rigid to extension

## What I Changed

### 1. Created the EligibilityRule interface

Introduced an `EligibilityRule` interface with a single responsibility:

```java
public interface EligibilityRule {
    String check(StudentProfile profile);
}
```

Each rule implementation returns a failure reason (or null if the rule passes).

### 2. Extracted each rule into its own class

- `DisciplinaryFlagRule` — checks for disciplinary flags
- `CgrRule` — checks if CGR is at least 8.0
- `AttendanceRule` — checks if attendance is at least 75%
- `CreditsRule` — checks if earned credits are at least 20

Each rule:

- Has a single purpose (one eligibility criterion)
- Can be tested independently
- Contains its own thresholds/constants
- Returns only a failure reason (null means pass)

### 3. Refactored EligibilityEngine as a rule orchestrator

The engine now:

- Maintains a **list** of rules instead of a giant if/else chain
- Iterates through each rule and collects failure reasons
- Stops at the first failure (preserving original behavior)
- Determines the final status based on whether any failures exist
- No longer contains the hard-coded logic

## Files I Added

- `EligibilityRule.java` — the abstraction
- `DisciplinaryFlagRule.java` — checks disciplinary status
- `CgrRule.java` — checks CGR threshold
- `AttendanceRule.java` — checks attendance percentage
- `CreditsRule.java` — checks earned credits

## Files I Modified

- `EligibilityEngine.java` — refactored to use rule list

## Files I Kept Unchanged

- `StudentProfile.java` — data container
- `ReportPrinter.java` — printing logic
- `FakeEligibilityStore.java` — persistence
- `LegacyFlags.java` — flag constants
- `Numbers.java` — utility
- `Main.java` — entry point

## Output (Verified Correct)

```text
=== Placement Eligibility ===
Student: Ayaan (CGR=8.10, attendance=72, credits=18, flag=NONE)
RESULT: NOT_ELIGIBLE
- attendance below 75
Saved evaluation for roll=23BCS1001
```

## Benefits of This Refactoring

1. **Open for extension**: Add new rules by creating a new class implementing `EligibilityRule`
2. **Closed for modification**: No need to touch `EligibilityEngine` to add a new rule
3. **Testability**: Each rule can be unit tested independently
4. **Clarity**: Each rule is self-contained and easy to understand
5. **Single Responsibility**: Engine orchestrates, rules evaluate

## Adherence to OCP

- The system is **open for extension**: new rules can be added by creating new classes
- The system is **closed for modification**: the engine doesn't need to change to support new rules
- Follow the pattern: create a new rule class, implement `EligibilityRule`, add to the list in the engine constructor

## Future Enhancement

To add a new rule (e.g., "gpa must be above 7.0"):

1. Create `GpaRule implements EligibilityRule`
2. Add it to the rules list in `EligibilityEngine` constructor
3. No other files need to change
