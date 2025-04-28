package model;

import java.util.Map;

public class CraftingRecipe {
    private int id;
    private String recipeName;
    private Map<MaterialType, Integer> requiredRawMaterials;
    private int xpReward;

    public CraftingRecipe(int id, String recipeName, Map<MaterialType, Integer> requiredRawMaterials, int xpReward) {
        this.id = id;
        this.recipeName = recipeName;
        this.requiredRawMaterials = requiredRawMaterials;
        this.xpReward = xpReward;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Map<MaterialType, Integer> getRequiredRawMaterials() {
        return requiredRawMaterials;
    }

    public void setRequiredRawMaterials(Map<MaterialType, Integer> requiredRawMaterials) {
        this.requiredRawMaterials = requiredRawMaterials;
    }

    public int getXpReward() {
        return xpReward;
    }

    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }
}
