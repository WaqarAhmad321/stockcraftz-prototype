package model;

public class User {
    private final int id;
    private final String username;
    private final String password;

    private final double balance;
    private final UserRole role;

    public User(int id, String username, double balance, UserRole role, String password) {
        this.id = id;
        this.username = username;
        this.balance = balance;
        this.role = role;
        this.password = password;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public double getBalance() {
        return balance;
    }
    public UserRole getRole() {
        return role;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", username=" + username + ", balance=" + balance + ", role=" + role + "]";
    }
}
