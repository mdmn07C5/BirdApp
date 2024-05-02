package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    /**
     * Attempts to add account to the DB
     * @param account 
     * @return A newly created account, or null
     */
    public Account addAccount(Account account) {
        if (account.getUsername().equals("")) {
            return null;
        }
        if (account.getPassword().length() < 8) {
            return null;
        }
        return this.accountDAO.insertAccount(account);
    }

     /**
     * Retrieves account iff supplied password matches saved password
     * @param account 
     * @return the matching account
     */
    public Account getAccount(Account account) {
        Account accFromDB = this.accountDAO.getAccount(account);
        if (accFromDB != null && 
            !accFromDB.getPassword().equals(account.getPassword())) {
            return null;
        }
        return accFromDB;
    }
}
