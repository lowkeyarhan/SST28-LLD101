# Ex6 Refactoring — Notification Sender Inheritance (LSP)

## The Problem

The original code violated LSP in three distinct ways, all rooted in the same root cause:
`NotificationSender` had **no documented contract**, so subtypes were free to invent their own rules with no consistency.

### Specific violations

**EmailSender** — silent data mutation  
The body was silently truncated when it exceeded 40 characters. A caller passing a long message would receive no error and no indication that the delivered content was different from what they sent. This is a semantic change: the subtype weakened the postcondition (delivered content ≠ input content).

**WhatsAppSender** — undocumented precondition  
The class threw `IllegalArgumentException` if the phone number wasn't in E.164 format. But the base class `send(Notification n)` gave no hint that any format was required. Callers using `NotificationSender` had no way to know they needed to validate the phone number first — unless they read the source and checked for `instanceof WhatsAppSender`.

**SmsSender** — undefined contract around unused fields  
Subject was ignored without any documentation. By itself this is fine (SMS has no subject), but with no base contract stating which fields senders are allowed to omit, it looked like a bug rather than a design decision.

## What Changed

### NotificationSender (base class)

Added a clear Javadoc contract covering:

- Preconditions: `n` must not be null
- Postconditions: channel receives the notification; content is not silently modified; `audit.add()` is called once on success
- Documented that concrete classes may throw `IllegalArgumentException` for channel-specific format requirements — and that such constraints must be documented on the subclass

This is the core fix. Once the contract is explicit, callers know what they can rely on and what they need to check for.

### EmailSender

Removed the silent truncation entirely. Email has no reason to cap body length — it was an arbitrary smell left in the original. Now body is delivered exactly as provided, which is what the base contract requires.

### SmsSender

The code itself didn't change. Added Javadoc clarifying that omitting `subject` is intentional: SMS has no subject field and omitting it is not a semantic change to the content.

### WhatsAppSender

The E.164 validation stays — it's a real constraint of the WhatsApp channel. The fix is making it **explicit and documented** on the class, so callers don't need `instanceof` to discover it. This is the same approach used in Ex5 for `PdfExporter`'s size limit: the exception is allowed by the base contract, but the specific trigger must be documented on the subclass.

## Files Modified

- `NotificationSender.java` — added explicit contract
- `EmailSender.java` — removed silent truncation, added Javadoc
- `SmsSender.java` — added Javadoc clarifying subject omission
- `WhatsAppSender.java` — removed violation comment, added Javadoc for E.164 constraint

## Files Unchanged

- `Notification.java` — data container
- `AuditLog.java` — persistence/logging
- `Main.java` — entry point
- `ConsolePreview.java` — unused helper (left in place)
- `SenderConfig.java` — unused config (left in place)

## Output (Verified Correct)

```text
=== Notification Demo ===
EMAIL -> to=riya@sst.edu subject=Welcome body=Hello and welcome to SST!
SMS -> to=9876543210 body=Hello and welcome to SST!
WA ERROR: phone must start with + and country code
AUDIT entries=3
```

## Acceptance Criteria Check

| Criterion                                                                    | Met?                                                                             |
| ---------------------------------------------------------------------------- | -------------------------------------------------------------------------------- |
| Base contract is clear and upheld                                            | Yes — Javadoc on `NotificationSender` defines pre/postconditions                 |
| No subtype tightens preconditions compared to base **without documentation** | Yes — WA constraint is now documented on the class                               |
| Caller does not need `instanceof` to be safe                                 | Yes — base contract tells you exceptions are possible and to check subclass docs |
| Substitutability: code that works with the base works with any sender        | Yes — no hidden surprises remain                                                 |
| Output unchanged                                                             | Yes — exact match                                                                |
