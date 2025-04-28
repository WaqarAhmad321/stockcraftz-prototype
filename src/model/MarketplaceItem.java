package model;

public class MarketplaceItem extends CraftedItem {
    private int sellerId;
    private double price;
    private int quantity;

    public MarketplaceItem(int id, String craftedItemName, int sellerId, double price, int quantity) {
        super(id, craftedItemName);
        this.sellerId = sellerId;
        this.price = price;
        this.quantity = quantity;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
