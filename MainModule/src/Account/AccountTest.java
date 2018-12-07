package Account;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void getAccountType() {
        Account a = new Account("test", "test", "name", Account.AccountType.ADMIN);
        Account.AccountType type = a.getAccountType();

        Assert.assertSame(type, Account.AccountType.ADMIN);
    }

    @Test
    public void getEmail() {
        Account a = new Account("test", "test", "name", Account.AccountType.ADMIN);
        String temp = "test";

        Assert.assertSame(temp, a.getEmail());
    }

    @Test
    public void getPassword() {
        Account a = new Account("test", "test", "name", Account.AccountType.ADMIN);
        String temp = "test";

        Assert.assertSame(temp, a.getPassword());
    }

    @Test
    public void getName() {
        Account a = new Account("test", "test", "name", Account.AccountType.ADMIN);
        String temp = "name";

        Assert.assertSame(temp, a.getName());
    }
}