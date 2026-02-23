import java.util.*;

public class PricingRegistry {
    private final Map<Integer, RoomPricing> roomPricings;
    private final Map<AddOn, AddOnPricing> addOnPricings;

    public PricingRegistry() {
        this.roomPricings = new LinkedHashMap<>();
        this.roomPricings.put(LegacyRoomTypes.SINGLE, new SingleRoomPricing());
        this.roomPricings.put(LegacyRoomTypes.DOUBLE, new DoubleRoomPricing());
        this.roomPricings.put(LegacyRoomTypes.TRIPLE, new TripleRoomPricing());
        this.roomPricings.put(LegacyRoomTypes.DELUXE, new DeluxeRoomPricing());

        this.addOnPricings = new LinkedHashMap<>();
        this.addOnPricings.put(AddOn.MESS, new MessPricing());
        this.addOnPricings.put(AddOn.LAUNDRY, new LaundryPricing());
        this.addOnPricings.put(AddOn.GYM, new GymPricing());
    }

    public RoomPricing getRoomPricing(int roomType) {
        return roomPricings.getOrDefault(roomType, new DeluxeRoomPricing());
    }

    public AddOnPricing getAddOnPricing(AddOn addOn) {
        return addOnPricings.get(addOn);
    }
}
