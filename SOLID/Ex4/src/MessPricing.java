public class MessPricing implements AddOnPricing {
    @Override
    public Money getPrice() {
        return new Money(1000.0);
    }
}
