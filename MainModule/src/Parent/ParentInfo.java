package Parent;



public class ParentInfo {
    private String parentEmail;
    private String parentName;

    public ParentInfo(String parentName, String parentEmail) {
        this.parentName = parentName;
        this.parentEmail = parentEmail;
    }

    public String getParentEmail() {
        return this.parentEmail;
    }
    public String getParentName() {
        return this.parentName;
    }

}
