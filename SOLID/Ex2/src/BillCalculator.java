import java.util.*;

public class BillCalculator {
    private final Map<String, MenuItem> menu;

    public BillCalculator(Map<String, MenuItem> menu) {
        this.menu = menu;
    }

    public BillDetails calculate(String customerType, List<OrderLine> lines) {
        double subtotal = computeSubtotal(lines);
        double taxPct = TaxRules.taxPercent(customerType);
        double tax = subtotal * (taxPct / 100.0);
        double discount = DiscountRules.discountAmount(customerType, subtotal, lines.size());
        double total = subtotal + tax - discount;

        return new BillDetails(lines, subtotal, taxPct, tax, discount, total);
    }

    private double computeSubtotal(List<OrderLine> lines) {
        double subtotal = 0.0;
        for (OrderLine line : lines) {
            MenuItem item = menu.get(line.itemId);
            subtotal += item.price * line.qty;
        }
        return subtotal;
    }

    public static class BillDetails {
        public final List<OrderLine> lines;
        public final double subtotal;
        public final double taxPercent;
        public final double tax;
        public final double discount;
        public final double total;

        public BillDetails(List<OrderLine> lines, double subtotal, double taxPercent, 
                           double tax, double discount, double total) {
            this.lines = lines;
            this.subtotal = subtotal;
            this.taxPercent = taxPercent;
            this.tax = tax;
            this.discount = discount;
            this.total = total;
        }
    }
}
