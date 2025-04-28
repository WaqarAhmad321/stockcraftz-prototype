package controller;

import dao.CraftedItemDAO;
import dao.MarketplaceDAO;
import dao.UserDAO;
import model.Input;
import model.MarketplaceItem;
import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MarketplaceController {
    private static final MarketplaceDAO marketplaceDAO = new MarketplaceDAO();
    private static final CraftedItemDAO craftedItemDAO = new CraftedItemDAO();
    private static final UserDAO userDAO = new UserDAO();

    public static void viewInventory() throws SQLException {
        List<MarketplaceItem> marketplaceItems = marketplaceDAO.getMarketplaceInventory();
        System.out.println("\n--- Marketplace Inventory ---");

        if (marketplaceItems.isEmpty()) {
            System.out.println("No crafted items found.");
        } else {
            for (MarketplaceItem item : marketplaceItems) {
                System.out.println("ID: " + item.getId() +
                        ", Name: " + item.getCraftedItemName() + " Price: " + item.getPrice());
            }
        }
    }

    public static void buyItem(User buyer) throws SQLException {
        System.out.println("Enter the ID of the item you want to buy: ");
        int selectedItemId = Input.integerInput();

        System.out.println("Enter quantity to buy: ");
        int buyQuantity = Input.integerInput();

        List<MarketplaceItem> listings = marketplaceDAO.getMarketplaceInventory();
        MarketplaceItem selectedItem = listings.stream().filter(item -> item.getId() == selectedItemId).findFirst().orElse(null);

        if (selectedItem == null) {
            System.out.println("Listing not found.");
            return;
        }

        if (buyQuantity > selectedItem.getQuantity()) {
            System.out.println("❌ Not enough quantity available.");
            return;
        }

        double totalPrice = buyQuantity * selectedItem.getPrice();

        if (buyer.balance() < totalPrice) {
            System.out.println("❌ Not enough balance.");
            return;
        }

        craftedItemDAO.addCraftedItem(buyer.id(), selectedItem.getCraftedItemName());
        userDAO.deductBalance(buyer.id(), totalPrice);

        marketplaceDAO.removeListing(selectedItem.getId());

        System.out.println("Purchase successful. Check your crafting inventory hehe.");
    }

    public static void sellItem(User seller) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter item name to sell: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int qty = Input.integerInput();

        System.out.print("Enter price per unit: ");
        double price = Input.integerInput();

        boolean success = marketplaceDAO.addListing(seller.id(), itemName, qty, price);

        if (success) {
            System.out.println("✅ Item listed in marketplace.");
        } else {
            System.out.println("❌ Failed to list item.");
        }
    }
}
