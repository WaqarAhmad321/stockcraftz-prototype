package database;

import java.sql.*;

// This is our database helper that reduces boilerplate code significantly
public class DBUtils {
    // Database Credentials (postgresdb created on neon)
    private static final String url = "jdbc:postgresql://ep-withered-voice-a5udta9w-pooler.us-east-2.aws.neon.tech/stockcraftzdb?user=stockcraftzdb_owner&password=npg_lau6ZiYgo7Gh&sslmode=require";
    private static final String user = "stockcraftzdb";
    private static final String password = "npg_lau6ZiYgo7Gh";

    // This runs when the class first loads - it sets up the database driver
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find the database driver!", e);
        }
    }

    // Opens a connection to the database
    public static Connection connectDB() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // For SELECT queries:
    // 1. Takes a SQL string with ? placeholders
    // 2. Lets you set parameters with a lambda
    // 3. Converts results to Java objects with another lambda
    public static <T> T executeQuery(String sql,
                                     SQLConsumer<PreparedStatement> preparer,
                                     ResultSetMapper<T> mapper) throws SQLException {
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Fill in the ? marks in the SQL
            if (preparer != null) {
                preparer.accept(stmt);
            }

            // Turn database rows into Java objects
            try (ResultSet rs = stmt.executeQuery()) {
                return mapper.map(rs);
            }
        }
    }

    // For INSERT/UPDATE/DELETE:
    // Same as above but returns true if the query changed any data
    public static boolean executeUpdate(String sql,
                                        SQLConsumer<PreparedStatement> preparer) throws SQLException {
        try (Connection conn = connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preparer.accept(stmt);
            return stmt.executeUpdate() > 0;
        }
    }

    // --- Our special lambda helpers ---

    // Like Consumer but can throw SQLExceptions.
    // Use to fill in query parameters.
    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    // convert database rows into Java objects
    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}