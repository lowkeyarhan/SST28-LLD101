import java.util.Random;

public class HostelFeeCalculator {
    private static final Money DEPOSIT = new Money(5000.00);

    private final BookingRepository repo;
    private final RoomPricer roomPricer;
    private final AddOnPricer addOnPricer;

    public HostelFeeCalculator(BookingRepository repo) {
        this.repo = repo;
        this.roomPricer = new DefaultRoomPricer();
        this.addOnPricer = new DefaultAddOnPricer();
    }

    public HostelFeeCalculator(BookingRepository repo, RoomPricer roomPricer, AddOnPricer addOnPricer) {
        this.repo = repo;
        this.roomPricer = roomPricer;
        this.addOnPricer = addOnPricer;
    }

    public void process(BookingRequest req) {
        Money monthly = calculateMonthly(req);

        ReceiptPrinter.print(req, monthly, DEPOSIT);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000));
        repo.save(bookingId, req, monthly, DEPOSIT);
    }

    private Money calculateMonthly(BookingRequest req) {
        Money base = roomPricer.baseRate(req.roomType);
        Money addOnTotal = addOnPricer.totalFor(req.addOns);
        return base.plus(addOnTotal);
    }
}
