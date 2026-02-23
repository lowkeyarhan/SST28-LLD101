public class AttendanceRule implements EligibilityRule {
    private static final int MIN_ATTENDANCE = 75;

    @Override
    public String check(StudentProfile s) {
        if (s.attendancePct < MIN_ATTENDANCE) return "attendance below 75";
        return null;
    }
}
