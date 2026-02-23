import java.util.List;
import java.util.Map;

public class OnboardingService {
    private final StudentRepository repository;
    private final InputParser parser;
    private final StudentValidator validator;
    private final OnboardingPrinter printer;

    public OnboardingService(StudentRepository repository) {
        this.repository = repository;
        this.parser = new InputParser();
        this.validator = new StudentValidator();
        this.printer = new OnboardingPrinter();
    }

    public void registerFromRawInput(String raw) {
        printer.printInput(raw);

        Map<String, String> kv = parser.parse(raw);
        List<String> errors = validator.validate(kv);

        if (!errors.isEmpty()) {
            printer.printValidationErrors(errors);
            return;
        }

        String id = IdUtil.nextStudentId(repository.count());
        StudentRecord rec = new StudentRecord(
                id,
                kv.getOrDefault("name", ""),
                kv.getOrDefault("email", ""),
                kv.getOrDefault("phone", ""),
                kv.getOrDefault("program", ""));

        repository.save(rec);
        printer.printSuccess(id, repository.count(), rec);
    }
}
