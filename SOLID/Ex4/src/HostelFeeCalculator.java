import java.util.*;

public class HostelFeeCalculator {
    private final FakeBookingRepo repo;
    private final PricingRegistry pricingRegistry;

    public HostelFeeCalculator(FakeBookingRepo repo) {
        this.repo = repo;
        this.pricingRegistry = new PricingRegistry();
    }

    public void process(BookingRequest req) {
        Money monthly = calculateMonthly(req);
        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000)); // deterministic-ish
        repo.save(bookingId, req, monthly, deposit);
    }

    private Money calculateMonthly(BookingRequest req) {
        RoomPricing roomPricing = pricingRegistry.getRoomPricing(req.roomType);
        Money basePrice = roomPricing.getBasePrice();

        Money addOnTotal = new Money(0.0);
        for (AddOn addOn : req.addOns) {
            AddOnPricing addOnPricing = pricingRegistry.getAddOnPricing(addOn);
            if (addOnPricing != null) {
                addOnTotal = addOnTotal.plus(addOnPricing.getPrice());
            }
        }

        return basePrice.plus(addOnTotal);
    }
}
