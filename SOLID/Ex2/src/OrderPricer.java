import java.util.List;
import java.util.Map;

/**
 * Computes subtotal, tax, discount, and total for an order.
 * Single responsibility: pricing calculations only.
 */
public class OrderPricer {
    private final Map<String, MenuItem> menu;

    public OrderPricer(Map<String, MenuItem> menu) {
        this.menu = menu;
    }

    public double subtotal(List<OrderLine> lines) {
        double sum = 0.0;
        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            sum += item.price * l.qty;
        }
        return sum;
    }

    public double tax(String customerType, double subtotal) {
        double taxPct = TaxRules.taxPercent(customerType);
        return subtotal * (taxPct / 100.0);
    }

    public double taxPercent(String customerType) {
        return TaxRules.taxPercent(customerType);
    }

    public double discount(String customerType, double subtotal, int distinctLines) {
        return DiscountRules.discountAmount(customerType, subtotal, distinctLines);
    }
}
