# Ex4 Refactoring — What I Did

## The Problem

`HostelFeeCalculator.calculateMonthly` had a switch-case on room types and a loop with if/else for each add-on. Adding a new room type or add-on meant editing that method. The calculator also handled printing and persistence directly.

## What I Changed

### 1. Encapsulated room pricing behind an interface

Created `RoomPricer` with `baseRate(roomType)`. `DefaultRoomPricer` implements it with the existing switch logic. To add a new room type, implement a new pricer or extend the default — no editing of the calculator's core logic.

### 2. Encapsulated add-on pricing behind an interface

Created `AddOnPricer` with `totalFor(addOns)`. `DefaultAddOnPricer` implements it with the existing MESS/LAUNDRY/GYM logic. New add-ons = new pricer or extend the default.

### 3. Removed switch-case from the calculator

`HostelFeeCalculator.calculateMonthly` now does: `base + addOns`. It asks the room pricer for the base, the add-on pricer for the extra amount, and adds them. No branching on room type or add-on type.

### 4. Decoupled persistence

Introduced `BookingRepository` interface. `FakeBookingRepo` implements it. Calculator depends on the abstraction.

### 5. Kept printing separate

`ReceiptPrinter` already existed. The calculator still calls it but doesn't format strings — that stays in ReceiptPrinter.

## Files I Added

- `RoomPricer.java` — interface for room base pricing
- `DefaultRoomPricer.java` — current room price table
- `AddOnPricer.java` — interface for add-on pricing
- `DefaultAddOnPricer.java` — current add-on price table
- `BookingRepository.java` — interface for saving bookings

## Files I Modified

- `HostelFeeCalculator.java` — uses pricers, no switch/if chain
- `FakeBookingRepo.java` — implements `BookingRepository`

## What Stayed the Same

- `Main.java`, `BookingRequest`, `AddOn`, `LegacyRoomTypes`, `Money`, `ReceiptPrinter` — unchanged
- Output format is identical

## Output (running `java Main`)

```
=== Hostel Fee Calculator ===
Room: DOUBLE | AddOns: [LAUNDRY, MESS]
Monthly: 16500.00
Deposit: 5000.00
TOTAL DUE NOW: 21000.00
Saved booking: H-7781
```
