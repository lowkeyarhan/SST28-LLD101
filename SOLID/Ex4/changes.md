# Ex4 Refactoring — Hostel Fee Calculator

## The Problem

The `HostelFeeCalculator.calculateMonthly` method violated the **Open/Closed Principle** (OCP) by containing hard-coded logic in two places:

1. **Room type switch-case**: To add a new room type, you had to edit the `switch` statement
2. **Add-on if-else chain**: To add a new add-on, you had to edit the `if-else` chain
3. Both pricing strategies were mixed into a single method, making the code rigid and hard to extend

## What I Changed

### 1. Created pricing abstractions

- `RoomPricing` interface — encapsulates room base pricing logic
- `AddOnPricing` interface — encapsulates add-on pricing logic

### 2. Implemented concrete pricing classes

**Room types:**

- `SingleRoomPricing` — returns 14000.0
- `DoubleRoomPricing` — returns 15000.0
- `TripleRoomPricing` — returns 12000.0
- `DeluxeRoomPricing` — returns 16000.0

**Add-ons:**

- `MessPricing` — returns 1000.0
- `LaundryPricing` — returns 500.0
- `GymPricing` — returns 300.0

Each pricing class has a single responsibility: return its price.

### 3. Created a PricingRegistry

The `PricingRegistry` class:

- Maps room types to their pricing implementations
- Maps add-ons to their pricing implementations
- Provides a unified lookup interface
- Acts as the "registry" where all pricing strategies are registered

### 4. Refactored the calculator

`HostelFeeCalculator.calculateMonthly` now:

- Looks up the room pricing from the registry (no switch statement)
- Iterates through add-ons and looks up their pricing (no if-else chain)
- Uses Money objects to handle arithmetic cleanly
- Delegates pricing decisions to strategy objects

## Files I Added

- `RoomPricing.java` — interface for room pricing
- `AddOnPricing.java` — interface for add-on pricing
- `SingleRoomPricing.java` — concrete room pricing
- `DoubleRoomPricing.java` — concrete room pricing
- `TripleRoomPricing.java` — concrete room pricing
- `DeluxeRoomPricing.java` — concrete room pricing
- `MessPricing.java` — concrete add-on pricing
- `LaundryPricing.java` — concrete add-on pricing
- `GymPricing.java` — concrete add-on pricing
- `PricingRegistry.java` — pricing lookup registry

## Files I Modified

- `HostelFeeCalculator.java` — refactored to use pricing registry

## Files I Kept Unchanged

- `BookingRequest.java` — data container
- `LegacyRoomTypes.java` — room type constants
- `AddOn.java` — add-on enum
- `Money.java` — value object
- `ReceiptPrinter.java` — printing logic
- `FakeBookingRepo.java` — persistence
- `Main.java` — entry point

## Output (Verified Correct)

```text
=== Hostel Fee Calculator ===
Room: DOUBLE | AddOns: [LAUNDRY, MESS]
Monthly: 16500.00
Deposit: 5000.00
TOTAL DUE NOW: 21500.00
Saved booking: H-7985
```

## Benefits of This Refactoring

1. **Open for extension**: Add a new room type by creating a new `RoomPricing` implementation and registering it
2. **Closed for modification**: No need to edit `HostelFeeCalculator` to add pricing
3. **Separation of concerns**: Each pricing strategy is in its own class
4. **Testability**: Each pricing class can be tested independently
5. **Clarity**: Easy to understand which prices apply to which room/add-on

## Adherence to OCP

The system is:

- **Open for extension**: New pricing strategies can be added by implementing the interfaces
- **Closed for modification**: The calculator doesn't change when new prices are added

## Future Enhancements

To add a new room type (e.g., "economy"):

1. Create `EconomyRoomPricing implements RoomPricing`
2. Add it to `PricingRegistry`
3. Done! No changes needed to `HostelFeeCalculator`

To add a new add-on (e.g., "parking"):

1. Add `PARKING` to the `AddOn` enum
2. Create `ParkingPricing implements AddOnPricing`
3. Add it to `PricingRegistry`
4. Done!
