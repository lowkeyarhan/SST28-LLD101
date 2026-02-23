import java.util.*;

public class InvoicePrinter {
    private final Map<String, MenuItem> menu;

    public InvoicePrinter(Map<String, MenuItem> menu) {
        this.menu = menu;
    }

    public String format(String invoiceId, BillCalculator.BillDetails bill) {
        StringBuilder out = new StringBuilder();
        out.append("Invoice# ").append(invoiceId).append("\n");

        for (OrderLine line : bill.lines) {
            MenuItem item = menu.get(line.itemId);
            double lineTotal = item.price * line.qty;
            out.append(String.format("- %s x%d = %.2f\n", item.name, line.qty, lineTotal));
        }

        out.append(String.format("Subtotal: %.2f\n", bill.subtotal));
        out.append(String.format("Tax(%.0f%%): %.2f\n", bill.taxPercent, bill.tax));
        out.append(String.format("Discount: -%.2f\n", bill.discount));
        out.append(String.format("TOTAL: %.2f\n", bill.total));

        return out.toString();
    }

    public void print(String invoiceContent) {
        System.out.print(invoiceContent);
    }
}
