import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final InvoiceStore store;
    private final InvoiceBuilder invoiceBuilder;
    private final CheckoutPrinter printer;
    private int invoiceSeq = 1000;

    public CafeteriaSystem() {
        this.store = new FileStore();
        this.invoiceBuilder = new InvoiceBuilder();
        this.printer = new CheckoutPrinter();
    }

    public CafeteriaSystem(InvoiceStore store) {
        this.store = store;
        this.invoiceBuilder = new InvoiceBuilder();
        this.printer = new CheckoutPrinter();
    }

    public void addToMenu(MenuItem i) { menu.put(i.id, i); }

    public void checkout(String customerType, List<OrderLine> lines) {
        OrderPricer pricer = new OrderPricer(menu);
        String invId = "INV-" + (++invoiceSeq);

        double subtotal = pricer.subtotal(lines);
        double taxPct = pricer.taxPercent(customerType);
        double tax = pricer.tax(customerType, subtotal);
        double discount = pricer.discount(customerType, subtotal, lines.size());
        double total = subtotal + tax - discount;

        String printable = invoiceBuilder.build(invId, lines, menu, subtotal, taxPct, tax, discount, total);

        printer.printInvoice(printable);
        store.save(invId, printable);
        printer.printSaved(invId, store.countLines(invId));
    }
}
