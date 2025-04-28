package dao;

import database.DBUtils;
import model.LeaderboardEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardDAO {
    public List<LeaderboardEntry> getTopUsersByXP(int limit) throws SQLException {
        String sql = "SELECT username, xp FROM users ORDER BY xp DESC LIMIT ?";

        return DBUtils.executeQuery(sql, stmt -> {
            stmt.setInt(1, limit);
        }, rs -> {
            List<LeaderboardEntry> leaderboard = new ArrayList<>();
            while (rs.next()) {
                leaderboard.add(new LeaderboardEntry(
                        rs.getString("username"),
                        rs.getInt("xp")
                ));
            }
            return leaderboard;
        });
    }
}
