package Account;

enum AccountType {
    ADMIN, COUNSELOR, PARENT;
}

public class Account {

    private String email;
    private String password;
    private String name;
    private AccountType accountType;

    public Account(String email, String password, String name, AccountType a) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.accountType = a;
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
