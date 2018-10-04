package Account;



public class Account {
    public enum AccountType {
        ADMIN, COUNSELOR, PARENT;
    }

    private String email;
    private String password;
    private String name;
    private AccountType accountType;
    private String userID;

    public Account(String email, String password, String name, AccountType a) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.accountType = a;

    }

    public AccountType getAccountType() {
        return this.accountType;
    }
    public String getEmail(){
        return this.email;
    }
    //general Account functions
    private void editPassword() { }

    //admin functions
    private void addCounselor() { }

    //counselor functions
    private void authorizeParent() { }
    private void viewAllScores() { }
    private void notifyParent() { }

    //parent functions
    private void addStudentProfile() { }
    private void submitStudentProfile() { }
    private void receive() { }


}
