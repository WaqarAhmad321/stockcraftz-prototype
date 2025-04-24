package model;

import database.RawMaterialDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class RawMaterial extends InventoryItem {
    private MaterialType materialType;

    public RawMaterial(int id, int quantity, MaterialType materialType) {
        super(id, quantity);
        this.materialType = materialType;
    }

    // Getters & Setters
    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

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

        System.out.println("Enter Material Type: ");
        for (MaterialType type : MaterialType.values()) {
            System.out.println("- " + type);
        }

        MaterialType type = MaterialType.valueOf(scanner.nextLine().toUpperCase());

        //System.out.print("Enter item name (display name): ");
        //String itemName = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        boolean success = rawMaterialDAO.addRawMaterial(user.id(), type, qty);

        if (success) {
            System.out.println("Material added successfully.");
        } else {
            System.out.println("Failed to add material.");
        }
    }

    public static void removeMaterial(User user) throws SQLException {
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
