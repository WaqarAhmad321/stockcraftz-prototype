import database.RawMaterialDAO;
import database.UserDAO;
import model.RawMaterial;
import model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("/*==== StockCraftz - A Stock Management System ====*\\");

        User currentUser = null;

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit\n");

            System.out.print("Enter your choice: ");
            int choice = input.nextInt();
            input.nextLine();

            if (choice == 0) {
                System.out.println("Exiting...");
                break;
            }

            System.out.print("Enter your username: ");
            String username = input.nextLine();

            System.out.print("Enter your password: ");
            String password = input.nextLine();

            UserDAO userDAO = new UserDAO();

            try {
                switch (choice) {
                    case 1:
                        currentUser = userDAO.registerUser(username, password);
                        System.out.println("Registration successful");
                        break;
                    case 2:
                        currentUser = userDAO.loginUser(username, password);
                        System.out.println("Login successful");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        continue;
                }

                if (currentUser != null) {
                    System.out.printf("Your balance is: %s\n", currentUser.balance());
                    showInventoryMenu(input, currentUser);
                    currentUser = null; // Reset after leaving inventory
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Something went wrong. Please try again!");
            }
        }
        input.close();
    }

    private static void showInventoryMenu(Scanner scanner, User currentUser) {
        while (true) {
            System.out.println("\n=== Raw Material Inventory Menu ===");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Material");
            System.out.println("3. Remove Material");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            try {
                switch (choice) {
                    case 1:
                        RawMaterial.viewInventory(currentUser);
                        break;
                    case 2:
                        RawMaterial.addMaterial(currentUser);
                        break;
                    case 3:
                        RawMaterial.removeMaterial(currentUser);
                        break;
                    case 4:
                        System.out.println("Exiting inventory...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }
}