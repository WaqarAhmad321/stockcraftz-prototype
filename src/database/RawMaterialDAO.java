package database;

import model.MaterialType;
import model.RawMaterial;

import java.sql.SQLException;
import java.util.ArrayList;

public class RawMaterialDAO {
    public ArrayList<RawMaterial> getAllRawMaterials(int userId) throws SQLException {
        String sql = "SELECT * FROM raw_material_inventory WHERE user_id = ?";

        return DBUtils.executeQuery(sql, stmt -> {
            stmt.setInt(1, userId);
        }, rs -> {
            ArrayList<RawMaterial> rawMaterials = new ArrayList<>();

            while (rs.next()) {
                rawMaterials.add(
                        new RawMaterial(
                                rs.getInt("id"),
                                rs.getInt("quantity"),
                                MaterialType.valueOf(rs.getString("material_type"))
                        )
                );
            }

            return rawMaterials;
        });
    }

    // Add raw material
    public boolean addRawMaterial(int userId, MaterialType materialType, int quantity) throws SQLException {
        String sql = "INSERT INTO raw_material_inventory (user_id, material_type, quantity) VALUES (?, ?, ?)";

        return DBUtils.executeUpdate(sql, stmt -> {
            stmt.setInt(1, userId);
            stmt.setString(2, materialType.name());
            stmt.setInt(3, quantity);
            }
        );
    }

    // Delete raw material
    public boolean removeRawMaterial(int materialId) throws SQLException {
        String sql = "DELETE FROM raw_material_inventory WHERE id = ?";

        return DBUtils.executeUpdate(sql, stmt -> {
            stmt.setInt(1, materialId);
        });
    }
}
