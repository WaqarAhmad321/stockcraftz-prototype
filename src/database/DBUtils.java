package database;

import java.sql.*;

public class DBUtils {
    private static final String url = "jdbc:postgresql://ep-withered-voice-a5udta9w-pooler.us-east-2.aws.neon.tech/stockcraftzdb?user=stockcraftzdb_owner&password=npg_lau6ZiYgo7Gh&sslmode=require";
    private static final String user = "stockcraftzdb";
    private static final String password = "npg_lau6ZiYgo7Gh";

    public static Connection connectDB() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Executes any SQL query or update
    public static <T> T execute(String sql, SQLConsumer<PreparedStatement> preparer, DBAction<T> action) {
        try (Connection conn = connectDB()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            // Set parameters using the preparer
            preparer.accept(stmt);
            // Execute the action and return the result
            return action.apply(stmt);

        } catch (SQLException e) {
            e.printStackTrace();  // Log the error (could be enhanced with better logging)
            return null;
        }
    }

    // Executes an update (e.g., INSERT, UPDATE, DELETE)
    public static int executeUpdate(String sql, SQLConsumer<PreparedStatement> preparer) {
        return execute(sql, preparer, stmt -> stmt.executeUpdate());  // Returns the number of rows affected
    }

    // Helper functional interface for lambda with SQLException
    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    // Functional interface to define an action to execute on a PreparedStatement
    @FunctionalInterface
    public interface DBAction<T> {
        T apply(PreparedStatement stmt) throws SQLException;
    }
}
