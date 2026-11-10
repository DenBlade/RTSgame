package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.ResourceAmount;
import com.example.rtsgame.ResourceType;

public enum BuildingType {
    CASTLE(2,2, 4, 50, new ResourceAmount(ResourceType.GOLD, 300)),
    BARRACKS(1,1, 2.5, 30, new ResourceAmount(ResourceType.GOLD, 225));

    private final int width;
    private final int height;
    private final double constructionTime;
    private final int maxHealth;
    private final ResourceAmount resourceAmount;

    BuildingType(int width, int height, double constructionTime, int maxHealth, ResourceAmount resourceAmount) {
        this.width = width;
        this.height = height;
        this.constructionTime = constructionTime;
        this.maxHealth = maxHealth;
        this.resourceAmount = resourceAmount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public double getConstructionTime() {
        return constructionTime;
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    public ResourceAmount getResourceAmount() {
        return resourceAmount;
    }
}
