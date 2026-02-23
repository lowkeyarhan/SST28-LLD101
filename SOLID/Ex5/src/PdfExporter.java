import java.nio.charset.StandardCharsets;

public class PdfExporter extends Exporter {
    private static final int MAX_BODY_LENGTH = 20;

    @Override
    public ExportResult export(ExportRequest req) {
        if (req.body != null && req.body.length() > MAX_BODY_LENGTH) {
            throw new IllegalArgumentException("PDF cannot handle content > " + MAX_BODY_LENGTH + " chars");
        }
        String content = "PDF(" + req.title + "):" + req.body;
        return new ExportResult("application/pdf", content.getBytes(StandardCharsets.UTF_8));
    }
}
