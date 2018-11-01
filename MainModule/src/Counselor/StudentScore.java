package Counselor;

public class StudentScore {
    private String parentEmail;
    private String studentName;
    private int studentScore;

    StudentScore(String parent, String studentName, int studentScore) {
        this.parentEmail = parent;
        this.studentName = studentName;
        this.studentScore = studentScore;
    }

    public String getParentEmail() {
        return this.parentEmail;
    }
    public String getStudentName() {
        return this.studentName;
    }
    public int getStudentScore() { return this.studentScore; }
}
