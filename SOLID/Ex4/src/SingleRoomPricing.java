public class SingleRoomPricing implements RoomPricing {
    @Override
    public Money getBasePrice() {
        return new Money(14000.0);
    }
}
