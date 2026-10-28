package com.example.rtsgame.map.tiles.buildings;

public enum BuildingType {
    CASTLE(2,2, 4, 50),
    BARRACKS(1,1, 2.5, 30);

    private final int width;
    private final int height;
    private final double constructionTime;
    private final int maxHealth;

    BuildingType(int width, int height, double constructionTime, int maxHealth) {
        this.width = width;
        this.height = height;
        this.constructionTime = constructionTime;
        this.maxHealth = maxHealth;
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
}
