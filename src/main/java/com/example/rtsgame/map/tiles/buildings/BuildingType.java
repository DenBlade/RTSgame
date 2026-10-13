package com.example.rtsgame.map.tiles.buildings;

public enum BuildingType {
    CASTLE(2,2, 4),
    BARRACKS(1,1, 2.5);

    private final int width;
    private final int height;
    private final double constructionTime;

    BuildingType(int width, int height, double constructionTime) {
        this.width = width;
        this.height = height;
        this.constructionTime = constructionTime;
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
}
