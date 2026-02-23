public class DoubleRoomPricing implements RoomPricing {
    @Override
    public Money getBasePrice() {
        return new Money(15000.0);
    }
}
