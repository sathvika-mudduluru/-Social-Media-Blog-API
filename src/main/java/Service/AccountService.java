package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public boolean usernameExists(String username) {
        return accountDAO.existsByUsername(username);
    }

    public boolean accountExists(int id) {
        return accountDAO.existsById(id);
    }

    public Account createAccount(Account a) {
        return accountDAO.create(a);
    }

    public Account validateLogin(Account login) {
        if (!usernameExists(login.getUsername())) return null;
        Account fromDB = accountDAO.findByUsername(login.getUsername());
        if (fromDB.getPassword().equals(login.getPassword())) {
            return fromDB;
        }
        return null;
    }
}