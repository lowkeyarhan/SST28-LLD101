public interface BookingRepository {
    void save(String bookingId, BookingRequest req, Money monthly, Money deposit);
}
