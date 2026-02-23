# Ex1 Refactoring — What I Did

## The Problem

The original `OnboardingService` was doing way too much in one place. It was parsing input, validating it, generating IDs, saving to the database, and printing stuff — all inside a single method. That made it hard to change anything without breaking something else, and nearly impossible to test properly.

## What I Changed

### 1. Pulled out the parsing logic

Created a new class `InputParser` that only knows how to turn a raw string like `name=Riya;email=riya@sst.edu` into a map of key-value pairs. Now if the input format changes, I only touch this one file.

### 2. Pulled out the validation logic

Created `StudentValidator` that takes the parsed data and returns a list of errors (or an empty list if everything's fine). All the rules (name required, email must have @, phone digits only, program must be CSE/AI/SWE) live here. The best part: I can unit test validation without needing a real database or console.

### 3. Decoupled the database

Instead of `OnboardingService` directly using `FakeDb`, I introduced a `StudentRepository` interface. `FakeDb` now implements that interface. So the service depends on "something that can save and count students" — not a specific implementation. Later I could swap in a real DB without touching the onboarding code.

### 4. Pulled out all the printing

Created `OnboardingPrinter` that handles every `System.out.println` related to onboarding — input echo, error messages, success confirmation. If we ever need to change the output format or add logging, we only change this class.

### 5. Slimmed down OnboardingService

Now `OnboardingService` is just the orchestrator. It coordinates the flow: parse → validate → if errors, print them and stop → otherwise generate ID, create record, save, print success. It doesn't do any of the heavy lifting itself; it delegates to the right components.

## Files I Added

- `InputParser.java` — parses raw input
- `StudentValidator.java` — validation rules
- `StudentRepository.java` — interface for saving/reading students
- `OnboardingPrinter.java` — all console output

## Files I Modified

- `OnboardingService.java` — refactored to use the new components
- `FakeDb.java` — now implements `StudentRepository`
- `TextTable.java` — now takes `StudentRepository` instead of `FakeDb` directly

## What Stayed the Same

- `Main.java` — no changes, entry point is the same
- `StudentRecord.java` — unchanged
- `IdUtil.java` — unchanged
- The program output is identical to before

## Output (running `java Main`)

```
=== Student Onboarding ===
INPUT: name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE
OK: created student SST-2026-0001
Saved. Total students: 1
CONFIRMATION:
StudentRecord{id='SST-2026-0001', name='Riya', email='riya@sst.edu', phone='9876543210', program='CSE'}

-- DB DUMP --
| ID             | NAME | PROGRAM |
| SST-2026-0001   | Riya | CSE     |
```

## Takeaways

Each class has one clear job now. Adding a new field (like `city`) means updating the parser and validator — not a giant method with everything mixed together. And I can test each piece in isolation, which is a big win.
