package database;

import model.User;
import model.UserRole;

import java.sql.*;

public class UserDAO {
    public User registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        int rowsAffected = DBUtils.executeUpdate(sql, stmt -> {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, "CRAFTER");
        });

        if (rowsAffected > 0) {
            return new User(1, username, 0, UserRole.CRAFTER, password);
        } else {
            return null;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        return DBUtils.execute(sql, stmt -> {
            stmt.setString(1, username);
            stmt.setString(2, password);
        }, stmt -> {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getDouble("balance"),
                        UserRole.CRAFTER,
                        rs.getString("password")

                );
            }
                return null;
            }
        );
    }
}
