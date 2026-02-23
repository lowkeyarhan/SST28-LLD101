/**
 * Contract for a single eligibility rule. Returns failure reason if check fails, null if passed.
 */
public interface EligibilityRule {
    String check(StudentProfile profile);
}
