import controller.CraftingController;
import controller.LeaderboardController;
import controller.MarketplaceController;
import controller.RawMaterialController;
import dao.UserDAO;
import model.Input;
import model.User;

import java.sql.SQLException;
import java.util.Scanner;

// mention about team, self learning, show confidence, tell about learning, resources

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("/*==== StockCraftz - A Stock Management System ====*\\");

        User currentUser = null;

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit\n");

            System.out.print("Enter your choice: ");
            int choice = Input.integerInput();

            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            UserDAO userDAO = new UserDAO();

            try {
                switch (choice) {
                    case 1:
                        currentUser = userDAO.registerUser(username, password);

                        if (currentUser != null) {
                            System.out.println("Registration successful");
                        }
                        break;
                    case 2:
                        currentUser = userDAO.loginUser(username, password);
                        if (currentUser != null) {

                        }
                        System.out.println("Login successful");
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        continue;
                }

                if (currentUser != null) {
                    System.out.printf("Your balance is: %s\n", currentUser.balance());
                    showMainMenu(scanner, currentUser);
                    currentUser = null; // Reset after leaving inventory
                }
            } catch (SQLException e) {
                e.printStackTrace();
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