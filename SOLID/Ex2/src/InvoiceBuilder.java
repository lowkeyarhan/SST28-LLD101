import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds the formatted invoice text from order and pricing data.
 * Single responsibility: invoice formatting only.
 */
public class InvoiceBuilder {
    public String build(String invId, List<OrderLine> lines, Map<String, MenuItem> menu,
                        double subtotal, double taxPct, double tax, double discount, double total) {
        List<String> lineDescriptions = new ArrayList<>();
        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            double lineTotal = item.price * l.qty;
            lineDescriptions.add(String.format("- %s x%d = %.2f", item.name, l.qty, lineTotal));
        }

        StringBuilder out = new StringBuilder();
        out.append("Invoice# ").append(invId).append("\n");
        for (String desc : lineDescriptions) {
            out.append(desc).append("\n");
        }
        out.append(String.format("Subtotal: %.2f\n", subtotal));
        out.append(String.format("Tax(%.0f%%): %.2f\n", taxPct, tax));
        out.append(String.format("Discount: -%.2f\n", discount));
        out.append(String.format("TOTAL: %.2f\n", total));
        return InvoiceFormatter.identityFormat(out.toString());
    }
}
