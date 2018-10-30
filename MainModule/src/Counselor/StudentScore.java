package Counselor;

public class StudentScore {
    private String parentName;
    private String studentName;
    private int studentScore;

    StudentScore(String parent, String studentName, int studentScore) {
        this.parentName = parent;
        this.studentName = studentName;
        this.studentScore = studentScore;
    }

    public String getParentName() {
        return this.parentName;
    }
    public String getStudentName() {
        return this.studentName;
    }
    public int getStudentScore() { return this.studentScore; }
}
