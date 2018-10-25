package Counselor;

public class StudentScore {
    private String parentName;
    private String studentName;
    private double studentScore;

    StudentScore(String parent, String studentName, double studentScore) {
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
    public double getStudentScore() { return this.studentScore; }
}
