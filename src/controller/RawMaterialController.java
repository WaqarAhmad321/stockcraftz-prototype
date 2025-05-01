package controller;

import dao.RawMaterialDAO;
import model.MaterialType;
import model.RawMaterial;
import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class RawMaterialController {
    // Controllers
    public static void viewInventory(User user) throws SQLException {
        RawMaterialDAO rawMaterialDAO = new RawMaterialDAO();

        List<RawMaterial> materials = rawMaterialDAO.getAllRawMaterials(user.id());
        System.out.println("\n--- Your Inventory ---");

        if (materials.isEmpty()) {
            System.out.println("No materials found.");
        } else {
            for (RawMaterial mat : materials) {
                System.out.println("ID: " + mat.getId() +
                        ", Type: " + mat.getMaterialType() +
                        ", Qty: " + mat.getQuantity());
            }
        }
    }

    public static void addMaterial(User user) throws SQLException {
        RawMaterialDAO rawMaterialDAO = new RawMaterialDAO();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available Material Types:");
        for (MaterialType type : MaterialType.values()) {
            System.out.println("- " + type.name().toLowerCase());
        }

        MaterialType materialType = null;
        while (materialType == null) {
            System.out.print("Enter material type: ");
            String input = scanner.nextLine().trim().toUpperCase();

            try {
                materialType = MaterialType.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid material type. Please choose from the list above.");
            }
        }

        int quantity = -1;
        while (quantity < 0) {
            System.out.print("Enter quantity: ");
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity < 0) {
                    System.out.println("Quantity must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter digits only.");
            }
        }

        boolean success = rawMaterialDAO.addRawMaterial(user.id(), materialType, quantity);

        if (success) {
            System.out.println("Material added successfully.");
        } else {
            System.out.println("Failed to add material.");
        }
    }

    public static void removeMaterial(User user) throws SQLException {
        viewInventory(user);
        RawMaterialDAO rawMaterialDAO = new RawMaterialDAO();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the material to remove: ");
        int materialId = Integer.parseInt(scanner.nextLine());

        boolean success = rawMaterialDAO.removeRawMaterial(materialId);

        if (success) {
            System.out.println("Material removed.");
        } else {
            System.out.println("Failed to remove material.");
        }
    }
}
