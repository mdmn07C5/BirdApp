package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    /**
     * Inserts new account into the DB account table
     * @param account the account to be inserted
     * @return account if successfully inserted, null otherwise (such as in
     *         case of it already existing)
     */
    public Account insertAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account(username, password) VALUES(?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int generated_accountID = (int) resultSet.getLong(1);
                return new Account(
                    generated_accountID,
                    account.getUsername(),
                    account.getPassword()
                );
            }
        } catch (SQLException e) {
            // do some logging
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves an account if it exists in account table
     * @param account the account to match against the DB
     * @return the account, null if not found
     */
    public Account getAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Account(
                    (int) resultSet.getInt("account_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            // TODO: some logging
            System.out.println(e.getMessage());
        }
        return null;
    }
}
