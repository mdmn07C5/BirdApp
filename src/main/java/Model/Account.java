package Model;


public class Account {

    private int account_id;
    private String username;
    private String password;

    public Account(){

    }
    /**
     * Two arg constructor for when the DB will be generating the id
     * @param username
     * @param password
     */
    public Account(String username, String password){
        this.username = username;
        this.password = password;
    }
    /**
     * All field constructor for accounts returned from the DB
     * @param account_id
     * @param username
     * @param password
     */
    public Account(int account_id, String username, String password) {
        this.account_id = account_id;
        this.username = username;
        this.password = password;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @param o the other object.
     * @return true if o is equal to this object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account a = (Account) o;
        return account_id == a.account_id && username.equals(a.username) && password.equals(a.password);
    }
    /**
     * For easy debugging.
     * @return a String representation of this class.
     */
    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + account_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
