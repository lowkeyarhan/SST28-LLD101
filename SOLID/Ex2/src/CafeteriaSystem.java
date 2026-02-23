import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final BillCalculator calculator;
    private final InvoicePrinter printer;
    private final FileStore store = new FileStore();
    private int invoiceSeq = 1000;

    public CafeteriaSystem() {
        this.calculator = new BillCalculator(menu);
        this.printer = new InvoicePrinter(menu);
    }

    public void addToMenu(MenuItem i) {
        menu.put(i.id, i);
    }

    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);

        BillCalculator.BillDetails bill = calculator.calculate(customerType, lines);
        String invoiceContent = printer.format(invId, bill);
        printer.print(invoiceContent);

        store.save(invId, invoiceContent);
        System.out.println("Saved invoice: " + invId + " (lines=" + store.countLines(invId) + ")");
    }
}
