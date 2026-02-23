# Ex2 Refactoring — What I Did

## The Problem

`CafeteriaSystem.checkout` was doing everything: menu lookup, pricing, tax, discount, invoice string building, printing, and saving to a file store. One big method with too many responsibilities.

## What I Changed

### 1. Pulled out pricing logic

Created `OrderPricer` that computes subtotal, tax, and discount. It uses `TaxRules` and `DiscountRules` (which already existed) but keeps all the money math in one place. Adding a new discount type means touching `DiscountRules` or wiring a new rule — not the checkout flow.

### 2. Pulled out invoice formatting

Created `InvoiceBuilder` that takes the order lines, menu, and computed amounts, and builds the formatted invoice text. All the string formatting (line items, subtotal, tax, discount, total) lives here. If we change the invoice layout, we only edit this class.

### 3. Decoupled persistence

Introduced `InvoiceStore` interface. `FileStore` implements it. `CafeteriaSystem` now depends on "something that can save invoices" — not a concrete file store. Easier to test with a mock, or swap in a real DB later.

### 4. Pulled out printing

Created `CheckoutPrinter` for the console output: printing the invoice and the "Saved invoice" line. Keeps IO concerns separate from business logic.

### 5. Slimmed down CafeteriaSystem

Now it just orchestrates: create pricer → compute amounts → build invoice text → print → save → print saved confirmation. No formatting, no direct FileStore knowledge, no tax/discount details.

## Files I Added

- `InvoiceStore.java` — interface for saving invoices
- `OrderPricer.java` — subtotal, tax, discount calculations
- `InvoiceBuilder.java` — builds formatted invoice text
- `CheckoutPrinter.java` — console output

## Files I Modified

- `CafeteriaSystem.java` — refactored to orchestrate only
- `FileStore.java` — now implements `InvoiceStore`

## What Stayed the Same

- `Main.java`, `MenuItem`, `OrderLine`, `TaxRules`, `DiscountRules`, `InvoiceFormatter` — unchanged
- Output is identical

## Output (running `java Main`)

```
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
