package controller;

import dao.CraftedItemDAO;
import dao.CraftingRecipeDAO;
import dao.RawMaterialDAO;
import model.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CraftingController {
    private static final RawMaterialDAO rawMaterialDAO = new RawMaterialDAO();
    private static final CraftedItemDAO craftedItemDAO = new CraftedItemDAO();
    private static final CraftingRecipeDAO craftingRecipeDAO = new CraftingRecipeDAO();

    public static void viewInventory(int userId) throws SQLException {
        List<CraftedItem> craftedItems = craftedItemDAO.getAllCraftedItems(userId);
        System.out.println("\n--- Your Inventory ---");

        if (craftedItems.isEmpty()) {
            System.out.println("No crafted items found.");
        } else {
            for (CraftedItem item : craftedItems) {
                System.out.println("ID: " + item.getId() +
                        ", Name: " + item.getCraftedItemName());
            }
        }
    }

    public static void viewCraftingRecipes() throws SQLException {
        List<CraftingRecipe> craftingRecipes = craftingRecipeDAO.getAllCraftingRecipes();
        System.out.println("\n--- Crafting Recipes ---");

        if (craftingRecipes.isEmpty()) {
            System.out.println("No crafting recipes found.");
        } else {
            for (CraftingRecipe recipe : craftingRecipes) {
                System.out.print("ID: " + recipe.getId() +
                        ", Name: " + recipe.getRecipeName() + " Reward: " + recipe.getXpReward() + " Required Materials: ");

                for (Map.Entry<MaterialType, Integer> material: recipe.getRequiredRawMaterials().entrySet()) {
                    System.out.print(material.getKey().name() + " x" + material.getValue() + ", ");
                }
                System.out.println();
            }
        }
    }

    public static void addCraftedItem(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // Ask for recipe to select
        CraftingRecipe selectedRecipe = null;
        while (true) {
            System.out.print("Enter the ID of the crafted item: ");
            String input = scanner.nextLine().trim();

            // Validate that the input is a number
            int selectedRecipeId;
            try {
                selectedRecipeId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid numeric ID.");
                continue;
            }

            // Check if the recipe exists
            selectedRecipe = craftingRecipeDAO.getCraftingRecipe(selectedRecipeId);
            if (selectedRecipe != null) {
                break;
            } else {
                System.out.println("❌ No recipe found with that ID. Please try again.");
            }
        }

        Map<MaterialType, Integer> requiredMaterials = new HashMap<>(selectedRecipe.getRequiredRawMaterials());
        List<RawMaterial> inventory = rawMaterialDAO.getAllRawMaterials(user.id());

        Map<MaterialType, Integer> available = new HashMap<>();

        for (RawMaterial material: inventory) {
            available.put(material.getMaterialType(), material.getQuantity());
        }

        // Check if user has enough materials
        for (Map.Entry<MaterialType, Integer> entry : requiredMaterials.entrySet()) {
            MaterialType type = entry.getKey();
            int needed = entry.getValue();
            int owned = available.getOrDefault(type, 0);

            if (owned < needed) {
                System.out.println("❌ Not enough " + type + " to craft " + selectedRecipe.getRecipeName());
                return;
            }
        }

        // Deduct materials
        for (Map.Entry<MaterialType, Integer> entry : requiredMaterials.entrySet()) {
            MaterialType type = entry.getKey();
            int needed = entry.getValue();
            int owned = available.getOrDefault(type, 0);

            int updatedQuantity = owned - needed;
            rawMaterialDAO.updateRawMaterialQuantity(user.id(), type.name(), updatedQuantity);
        }

        System.out.println("Enter the name of crafted item: ");
        String craftedItemName = scanner.nextLine();

        boolean success = craftedItemDAO.addCraftedItem(user.id(), craftedItemName);

        // Grant XP
        // userDAO.addXP(user.id(), recipe.getXpReward());

        if (success) {
            System.out.println("✅ Crafted: " + selectedRecipe.getRecipeName());
            System.out.println("🎁 XP Awarded: " + selectedRecipe.getXpReward());
        } else {
            System.out.println("Item could not be crafted.");
        }
    }

    public static void removeItem() throws SQLException{
        System.out.print("Enter the ID of the item to remove: ");
        Scanner scanner = new Scanner(System.in);

        int itemId = Integer.parseInt(scanner.nextLine());

        boolean success = craftedItemDAO.removeCraftedItem(itemId);

        if (success) {
            System.out.println("Crafted Item removed.");
        } else {
            System.out.println("Failed to remove crafted item.");
        }
    }
}
