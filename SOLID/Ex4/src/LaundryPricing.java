public class LaundryPricing implements AddOnPricing {
    @Override
    public Money getPrice() {
        return new Money(500.0);
    }
}
