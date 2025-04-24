package database;

import model.User;
import model.UserRole;

import java.sql.*;

public class UserDAO {
        public boolean userExists(String username) throws SQLException {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

            return DBUtils.executeQuery(sql, stmt -> {
                stmt.setString(1, username);
            }, rs -> {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }

                return false;
            });
        }

        public User registerUser(String username, String password) throws SQLException {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?) RETURNING id, username, balance, role";

            if (userExists(username)) {
                throw new SQLException("User already exists. Please try again.");
            }

            return DBUtils.executeQuery(sql, stmt -> {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, UserRole.CRAFTER.toString());
            }, rs -> {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getDouble("balance"),
                            UserRole.valueOf(rs.getString("role"))
                    );
                }
                throw new SQLException("Failed to register user");
            });
        }

    public User loginUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        return DBUtils.executeQuery(sql, stmt -> {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                }, rs -> rs.next()
                        ? new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getDouble("balance"),
                        UserRole.valueOf(rs.getString("role"))
                )
                : null
        );
    }
}
