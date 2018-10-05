package Counselor;

public class ParentStudentPair {
    private String parentName;
    private String studentName;

    ParentStudentPair(String parent, String student) {
        this.parentName = parent;
        this.studentName = student;
    }

    public String getParentName() {
        return this.parentName;
    }
    public String getStudentName() {
        return this.studentName;
    }
}
