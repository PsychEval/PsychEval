package Counselor;

public class ParentStudentPair {
    private String parentEmail;
    private String studentName;
    private Boolean approved;

    ParentStudentPair(String parentEmail, Boolean approved, String student) {
        this.parentEmail = parentEmail;
        this.studentName = student;
        this.approved = approved;
    }

    public String getParentEmail() {
        return this.parentEmail;
    }
    public String getStudentName() {
        return this.studentName;
    }
    public Boolean getApproved() { return this.approved; }
}
