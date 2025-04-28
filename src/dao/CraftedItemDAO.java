package dao;

import database.DBUtils;
import model.CraftedItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class CraftedItemDAO {
    public ArrayList<CraftedItem> getAllCraftedItems(int userId) throws SQLException {
        String sql = "SELECT * FROM crafted_item_inventory WHERE user_id = ?";

        return DBUtils.executeQuery(sql, stmt -> {
            stmt.setInt(1, userId);
        }, rs -> {
            ArrayList<CraftedItem> craftedItems = new ArrayList<>();

            while (rs.next()) {
                craftedItems.add(
                        new CraftedItem(
                                rs.getInt("id"),
                                rs.getString("crafted_item_name")
                ));
            }

            return craftedItems;
        });
    }

    // Add crafted item
    public boolean addCraftedItem(int userId, String craftedItemName) throws SQLException {
        String sql = "INSERT INTO crafted_item_inventory (user_id, crafted_item_name) VALUES (?, ?)";

        return DBUtils.executeUpdate(sql, stmt -> {
                    stmt.setInt(1, userId);
                    stmt.setString(2, craftedItemName);
                }
        );
    }

    // Delete crafted item
    public boolean removeCraftedItem(int materialId) throws SQLException {
        String sql = "DELETE FROM crafted_item_inventory WHERE id = ?";

        return DBUtils.executeUpdate(sql, stmt -> {
            stmt.setInt(1, materialId);
        });
    }
}
