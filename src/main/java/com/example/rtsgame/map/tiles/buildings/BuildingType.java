package com.example.rtsgame.map.tiles.buildings;

public enum BuildingType {
    CASTLE(2,2),
    BARRACKS(1,1);

    private final int width;
    private final int height;

    BuildingType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
