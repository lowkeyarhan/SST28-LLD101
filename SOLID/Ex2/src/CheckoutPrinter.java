/**
 * Handles console output for checkout/invoice flow.
 */
public class CheckoutPrinter {
    public void printInvoice(String invoiceText) {
        System.out.print(invoiceText);
    }

    public void printSaved(String invId, int lineCount) {
        System.out.println("Saved invoice: " + invId + " (lines=" + lineCount + ")");
    }
}
