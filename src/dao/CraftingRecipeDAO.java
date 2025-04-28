package dao;

import database.DBUtils;
import model.CraftingRecipe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.MaterialType;
import org.json.JSONObject;

public class CraftingRecipeDAO {
    public List<CraftingRecipe> getAllCraftingRecipes() throws SQLException {
        String sql = "SELECT * FROM crafting_recipes";

        return DBUtils.executeQuery(sql, null, rs -> {
            List<CraftingRecipe> craftingRecipes = new ArrayList<>();

            while (rs.next()) {
                if (rs.next()) {
                    JSONObject json = new JSONObject(rs.getString("required_materials"));
                    Map<MaterialType, Integer> materials = new HashMap<>();

                    for (String key: json.keySet()) {
                        materials.put(MaterialType.valueOf(key), json.getInt(key));
                    }

                    craftingRecipes.add(new CraftingRecipe(rs.getInt("id"), rs.getString("recipe_name"), materials ,rs.getInt("xp_reward")));
                }
            }

            return craftingRecipes;
        });
    }

    public CraftingRecipe getCraftingRecipe(int recipeId) throws SQLException {
        String sql = "SELECT * FROM crafting_recipes WHERE id = ?";

        return DBUtils.executeQuery(sql, stmt -> {
            stmt.setInt(1, recipeId);
        }, rs -> {
            if (rs.next()) {
                JSONObject json = new JSONObject(rs.getString("required_materials"));
                Map<MaterialType, Integer> materials = new HashMap<>();

                for (String key: json.keySet()) {
                    materials.put(MaterialType.valueOf(key), json.getInt(key));
                }

                return new CraftingRecipe(rs.getInt("id"), rs.getString("recipe_name"), materials, rs.getInt("xp_reward"));
            }

            return null;
        });
    }

    public boolean removeCraftingRecipe(int recipeId) throws SQLException {
        String sql = "DELETE FROM crafting_recipes WHERE id = ?";

        return DBUtils.executeUpdate(sql, stmt -> {
            stmt.setInt(1, recipeId);
        });
    }
}
