import controller.CraftingController;
import controller.LeaderboardController;
import controller.MarketplaceController;
import controller.RawMaterialController;
import dao.UserDAO;
import model.Input;
import model.User;

import java.sql.SQLException;
import java.util.Scanner;

/*
  * I bet this is going to be the best project you have seen. We did self learning and used a lot of things which are not even taught in course yet,
  like using jdbc, postgresql database, JARs, interfaces, generics, hashmaps, lambda functions, and exceptional handling. We have used the best practices out there
  to write this code like dividing the code into controllers, models, and DAOs (database access objects), and used the oop concepts
  very efficiently for creating reusable components like the dbutils. Everything is realtime and is stored in database
*/

/*
Usage of Models:
 * Contains all data model classes representing business entities.
 * Each class mirrors database tables with fields, constructors, and getters/setters.

 * Example:
 * - User.java (id, username, passwordHash, role)
 * - Material.java (id, type, quantity)
*/

/*
Usage of DAOs:
 * Handles all database operations using JDBC.
 * Each DAO corresponds to a model class and performs CRUD operations.
 *
 * Example:
 * - UserDAO.java
 * - MaterialDAO.java (addStock, checkInventory)
*/

/*
 * Contains application logic that bridges models and views.
 * Handles user input, processes data, and manages transactions.
 *
 * Currently CLI-based, but controllers are intentionally view-agnostic
 * to simplify JavaFX migration (controllers will integrate with FXML later).
 *
 * Example:
 * - MarketplaceController.java (additem)
 * - InventoryController.java (addMaterial, checkStock)
 */

/*
    Note: To Test the application use the following credentials:
    * username: testuser
    * password: testpass
*/

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("/*==== StockCraftz - A Stock Management System ====*\\");

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit\n");

            System.out.print("Enter your choice: ");
            int choice = Input.integerInput();

            if (choice == 0) {
                System.out.println("Exiting...");
                break;
            }

            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            UserDAO userDAO = new UserDAO();

            try {
                User user = null;

                if (choice == 1) {
                    user = userDAO.registerUser(username, password);
                    System.out.println(user != null ? "\nRegistered!\n" : "\nRegistration failed.\n");
                }  else if (choice == 2) {
                    user = userDAO.loginUser(username, password);
                    System.out.println(user != null ? "\nLogin success!\n" : "\nInvalid credentials.\n");
                }

                if (user != null) {
                    System.out.printf("Your balance is: %s\n", user.balance());
                    showMainMenu(scanner, user);
                    user = null; // Reset after leaving inventory
                }
            } catch (SQLException e) {
                System.out.println("Something went wrong. Please try again!");
            }
        }
    }

    private static void showMainMenu(Scanner scanner, User currentUser) {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. View Raw Materials Menu");
            System.out.println("2. View Crafted Items Menu");
            System.out.println("3. Market Place");
            System.out.println("4. Leaderboard");
            System.out.println("5. Exit");

            System.out.print("Choice: ");

            int choice = Input.integerInput();

            switch (choice) {
                case 1:
                    showRawMaterialInventoryMenu(scanner, currentUser);
                    break;
                case 2:
                    showCraftedItemsMenu(scanner, currentUser);
                    break;
                case 3:
                    showMarketplaceMenu(scanner, currentUser);
                    break;
                case 4:
                    LeaderboardController.showTopUsers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showRawMaterialInventoryMenu(Scanner scanner, User currentUser) {
        while (true) {
            System.out.println("\n=== Raw Material Inventory Menu ===");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Material");
            System.out.println("3. Remove Material");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = Input.integerInput();

            try {
                switch (choice) {
                    case 1:
                        RawMaterialController.viewInventory(currentUser);
                        break;
                    case 2:
                        RawMaterialController.addMaterial(currentUser);
                        break;
                    case 3:
                        RawMaterialController.removeMaterial(currentUser);
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

    private static void showCraftedItemsMenu(Scanner scanner, User currentUser) {
        while (true) {
            System.out.println("\n=== Crafted Items Inventory Menu ===");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Item");
            System.out.println("3. Remove Item");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = Input.integerInput();

            try {
                switch (choice) {
                    case 1:
                        CraftingController.viewInventory(currentUser.id());
                        break;
                    case 2:
                        CraftingController.viewCraftingRecipes();
                        CraftingController.addCraftedItem(currentUser);
                        break;
                    case 3:
                        CraftingController.viewInventory(currentUser.id());
                        CraftingController.removeItem();
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

    private static void showMarketplaceMenu(Scanner scanner, User currentUser) {
        while (true) {
            System.out.println("\n=== Marketplace Menu ===");
            System.out.println("1. View all available items");
            System.out.println("2. Buy items");
            System.out.println("3. sell items");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = Input.integerInput();

            try {
                switch (choice) {
                    case 1:
                        MarketplaceController.viewInventory();
                        break;
                    case 2:
                        MarketplaceController.viewInventory();
                        MarketplaceController.buyItem(currentUser);
                        break;
                    case 3:
                        CraftingController.viewInventory(currentUser.id());
                        MarketplaceController.sellItem(currentUser);
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