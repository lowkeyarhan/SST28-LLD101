# Ex2 Refactoring — Student Club Management Billing

## The Problem

The `CafeteriaSystem.checkout` method was violating Single Responsibility Principle (SRP) by doing multiple things:

- Looking up menu items and calculating the subtotal
- Computing tax based on customer type
- Determining discount amounts based on rules
- Formatting the invoice as a string
- Persisting the invoice to storage
- Printing to console

This made the code hard to test and risky to modify, since changing any single responsibility could break the entire checkout flow.

## What I Changed

### 1. Extracted bill calculation logic

Created `BillCalculator.java` that encapsulates all the financial calculations:

- Takes the menu and a list of order lines
- Uses `TaxRules` and `DiscountRules` to compute amounts
- Returns a `BillDetails` object with all calculated values (subtotal, tax, discount, total)

The calculator is now testable in isolation without needing any persistence or UI code.

### 2. Separated invoice formatting

Created `InvoicePrinter.java` that handles all formatting concerns:

- Takes invoice ID and bill details
- Formats invoice as a string with proper layout and line items
- Provides a `print()` method for console output

This makes it easy to change invoice formatting without touching billing logic—for example, adding a header or footer later doesn't affect calculations.

### 3. Refactored CafeteriaSystem as an orchestrator

`CafeteriaSystem` now has a single responsibility: **orchestrating the checkout workflow**:

- Accepts the order and customer type
- Delegates calculation to `BillCalculator`
- Delegates formatting to `InvoicePrinter`
- Handles persistence to `FileStore`
- Coordinates all the pieces together

It no longer contains any business logic itself; it just coordinates.

## Files I Added

- `BillCalculator.java` — calculates subtotal, tax, discount, total
- `InvoicePrinter.java` — formats invoice content

## Files I Modified

- `CafeteriaSystem.java` — refactored to use the new components

## Files I Kept Unchanged

- `MenuItem.java` — data container unchanged
- `OrderLine.java` — data container unchanged
- `TaxRules.java` — policy class still in place (could be further refactored)
- `DiscountRules.java` — policy class still in place (could be further refactored)
- `FileStore.java` — persistence layer unchanged
- `InvoiceFormatter.java` — still there (no-op wrapper)
- `Main.java` — entry point unchanged

## Output (Verified Correct)

```text
=== Cafeteria Billing ===
Invoice# INV-1001
- Veg Thali x2 = 160.00
- Coffee x1 = 30.00
Subtotal: 190.00
Tax(5%): 9.50
Discount: -10.00
TOTAL: 189.50
Saved invoice: INV-1001 (lines=7)
```

## Benefits of This Refactoring

1. **Testability**: Can test `BillCalculator` without console or file I/O
2. **Clarity**: Each class has one clear purpose
3. **Extensibility**: Adding a new rule type doesn't affect multiple classes
4. **Maintainability**: Invoice formatting changes don't risk billing logic bugs

## Adherence to SRP

- `BillCalculator`: Responsible only for financial calculations
- `InvoicePrinter`: Responsible only for formatting and printing
- `CafeteriaSystem`: Responsible only for orchestrating the workflow
- Each class has one reason to change
