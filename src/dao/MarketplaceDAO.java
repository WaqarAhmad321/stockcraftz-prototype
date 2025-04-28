package dao;

import database.DBUtils;
import model.MarketplaceItem;
import model.MaterialType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceDAO {
    public List<MarketplaceItem> getMarketplaceInventory() throws SQLException {
        String sql = "SELECT * FROM marketplace_inventory";

        return DBUtils.executeQuery(sql, null, rs -> {
            List<MarketplaceItem> marketplaceItems = new ArrayList<>();

            while (rs.next()) {
                marketplaceItems.add(
                        new MarketplaceItem(rs.getInt("id"), rs.getString("item_name"), rs.getInt("seller_id"), rs.getDouble("price"), rs.getInt("quantity"))
                );
            }

            return marketplaceItems;
        });
    }

    public boolean addListing(int sellerId, String itemName, int quantity, double price) throws SQLException {
        String sql = "INSERT INTO marketplace_inventory (seller_id, item_name, quantity, price) VALUES (?, ?, ?, ?)";

        return DBUtils.executeUpdate(sql, stmt -> {
                    stmt.setInt(1, sellerId);
                    stmt.setString(2, itemName);
                    stmt.setInt(3, quantity);
                    stmt.setDouble(4, price);
                }
        );
    }

    public boolean removeListing(int materialId) throws SQLException {
        String sql = "DELETE FROM marketplace_inventory WHERE id = ?";

        return DBUtils.executeUpdate(sql, stmt -> {
            stmt.setInt(1, materialId);
        });
    }
}
