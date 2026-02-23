public class TripleRoomPricing implements RoomPricing {
    @Override
    public Money getBasePrice() {
        return new Money(12000.0);
    }
}
