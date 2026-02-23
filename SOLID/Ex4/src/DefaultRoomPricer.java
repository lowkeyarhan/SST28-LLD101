public class DefaultRoomPricer implements RoomPricer {
    @Override
    public Money baseRate(int roomType) {
        return switch (roomType) {
            case LegacyRoomTypes.SINGLE -> new Money(14000.0);
            case LegacyRoomTypes.DOUBLE -> new Money(15000.0);
            case LegacyRoomTypes.TRIPLE -> new Money(12000.0);
            default -> new Money(16000.0);
        };
    }
}
