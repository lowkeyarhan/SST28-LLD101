import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EligibilityEngine {
    private final EligibilityStore store;
    private final ReportPrinter printer;
    private final List<EligibilityRule> rules;

    public EligibilityEngine(EligibilityStore store) {
        this.store = store;
        this.printer = new ReportPrinter();
        this.rules = defaultRules();
    }

    public EligibilityEngine(EligibilityStore store, List<EligibilityRule> rules) {
        this.store = store;
        this.printer = new ReportPrinter();
        this.rules = rules;
    }

    private static List<EligibilityRule> defaultRules() {
        List<EligibilityRule> r = new ArrayList<>();
        r.add(new DisciplinaryFlagRule());
        r.add(new CgrRule());
        r.add(new AttendanceRule());
        r.add(new CreditsRule());
        return Collections.unmodifiableList(r);
    }

    public void runAndPrint(StudentProfile s) {
        EligibilityEngineResult r = evaluate(s);
        printer.print(s, r);
        store.save(s.rollNo, r.status);
    }

    public EligibilityEngineResult evaluate(StudentProfile s) {
        List<String> reasons = new ArrayList<>();
        for (EligibilityRule rule : rules) {
            String reason = rule.check(s);
            if (reason != null) {
                reasons.add(reason);
                return new EligibilityEngineResult("NOT_ELIGIBLE", reasons);
            }
        }
        return new EligibilityEngineResult("ELIGIBLE", reasons);
    }
}
