package controller;

import dao.LeaderboardDAO;
import model.LeaderboardEntry;

import java.sql.SQLException;
import java.util.List;

public class LeaderboardController {
    private static final LeaderboardDAO leaderboardDAO = new LeaderboardDAO();

    public static void showTopUsers() {
        try {
            List<LeaderboardEntry> leaderboard = leaderboardDAO.getTopUsersByXP(10); // Top 10 users

            if (leaderboard.isEmpty()) {
                System.out.println("\nNo users found on leaderboard.");
            } else {
                System.out.println("\n=== Leaderboard: Top Crafters ===");
                int rank = 1;
                for (LeaderboardEntry entry : leaderboard) {
                    System.out.printf("%d. %s - %d XP\n", rank++, entry.getUsername(), entry.getXp());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard: " + e.getMessage());
        }
    }
}
