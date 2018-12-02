package com.psycheval.testapp;

public class Account {
    public static Account curruser;
    public enum AccountType {
        ADMIN, COUNSELOR, PARENT;
    }

    private String email;
    private String password;
    private String name;
    private AccountType accountType;
    private String userID;
    private String counselorEmail;

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
    public String getPassword(){return this.password;}
    public String getName() { return this.name; }
    public String getCounselorEmail() {return this.counselorEmail;}
    public void setCounselorEmail(String email) {this.counselorEmail = email;}
}
