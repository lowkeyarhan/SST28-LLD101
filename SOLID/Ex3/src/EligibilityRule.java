public interface EligibilityRule {
    /**
     * Check if this rule fails for the given profile.
     * @return failure reason if rule fails, null if rule passes
     */
    String check(StudentProfile profile);
}
