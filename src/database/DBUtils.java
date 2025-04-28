package database;

import java.sql.*;

// DBUtils for consistency and reducing boilerplate code required for every query
public class DBUtils {
    // Database Credentials (postgresdb created on neon)
    private static final String url = "jdbc:postgresql://ep-withered-voice-a5udta9w-pooler.us-east-2.aws.neon.tech/stockcraftzdb?user=stockcraftzdb_owner&password=npg_lau6ZiYgo7Gh&sslmode=require";
    private static final String user = "stockcraftzdb";
    private static final String password = "npg_lau6ZiYgo7Gh";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Load driver at class initialization
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found", e);
        }
    }

    // Creating Database connection
    public static Connection connectDB() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Executes any SQL query or update
    public static <T> T executeQuery(String sql, SQLConsumer<PreparedStatement> preparer, ResultSetMapper<T> mapper) throws SQLException {
        try (Connection conn = connectDB()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            // Set parameters using the preparer
            if (preparer != null) {
                preparer.accept(stmt);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                return mapper.map(rs);
            }
        }
    }

    // Executes an update (e.g., INSERT, UPDATE, DELETE)
    public static boolean executeUpdate(String sql, SQLConsumer<PreparedStatement> preparer) throws SQLException {
        try (Connection conn = connectDB();
        PreparedStatement stmt = conn.prepareStatement(sql);) {
            preparer.accept(stmt);

            // returns the result of query (rows affected)
            return stmt.executeUpdate() > 0;
        }
    }

    // Helper functional interface for lambda with SQLException
    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    // Functional interface to define an action to execute on a PreparedStatement
    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
