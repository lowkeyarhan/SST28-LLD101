public class GymPricing implements AddOnPricing {
    @Override
    public Money getPrice() {
        return new Money(300.0);
    }
}
