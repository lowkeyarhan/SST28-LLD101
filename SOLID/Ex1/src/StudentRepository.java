public interface StudentRepository {
    void save(StudentRecord record);

    int count();

    java.util.List<StudentRecord> all();
}
