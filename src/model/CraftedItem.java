package model;

public class CraftedItem {
    private int id;
    private String craftedItemName;

    public CraftedItem(int id, String craftedItemName) {
        this.id = id;
        this.craftedItemName = craftedItemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCraftedItemName() {
        return craftedItemName;
    }

    public void setCraftedItemName(String craftedItemName) {
        this.craftedItemName = craftedItemName;
    }
}
