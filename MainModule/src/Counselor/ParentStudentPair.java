package Counselor;

public class ParentStudentPair {
    private String parentName;
    private String studentName;
    private Boolean approved;

    ParentStudentPair(String parent, Boolean approved, String student) {
        this.parentName = parent;
        this.studentName = student;
        this.approved = approved;
    }

    public String getParentName() {
        return this.parentName;
    }
    public String getStudentName() {
        return this.studentName;
    }
    public Boolean getApproved() { return this.approved; }
}
