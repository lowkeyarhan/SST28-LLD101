/**
 * Abstracts room-type base pricing. New room types can be added by implementing this.
 */
public interface RoomPricer {
    Money baseRate(int roomType);
}
