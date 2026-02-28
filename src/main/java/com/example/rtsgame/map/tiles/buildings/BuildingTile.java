package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.map.tiles.Tile;

public abstract class BuildingTile extends Tile {
    private static int buildingsCount = 0;
    protected final int buildingId;
    protected boolean ownedByAI;
    public BuildingTile(int x, int y, boolean ownedByAI) {
        super(x, y, false);
        buildingsCount++;
        buildingId = buildingsCount;
        this.ownedByAI = ownedByAI;
    }
    public BuildingTile(int id, int x, int y, boolean ownedByAI) {
        super(x, y, false);
        buildingId = id;
        this.ownedByAI = ownedByAI;
    }
    public static int getBuildingsCount() {
        return buildingsCount;
    }
    public static void incrementBuildingsCount() {
        buildingsCount++;
    }
}
