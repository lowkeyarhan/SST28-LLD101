public class DeluxeRoomPricing implements RoomPricing {
    @Override
    public Money getBasePrice() {
        return new Money(16000.0);
    }
}
