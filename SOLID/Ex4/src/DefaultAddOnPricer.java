import java.util.List;

public class DefaultAddOnPricer implements AddOnPricer {
    @Override
    public Money totalFor(List<AddOn> addOns) {
        double sum = 0.0;
        for (AddOn a : addOns) {
            if (a == AddOn.MESS) sum += 1000.0;
            else if (a == AddOn.LAUNDRY) sum += 500.0;
            else if (a == AddOn.GYM) sum += 300.0;
        }
        return new Money(sum);
    }
}
