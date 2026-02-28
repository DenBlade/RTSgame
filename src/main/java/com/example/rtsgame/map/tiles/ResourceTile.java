package com.example.rtsgame.map.tiles;

import com.example.rtsgame.ResourceType;

public class ResourceTile extends Tile {
    private ResourceType type;
    private int getPerHarvest;
    public ResourceTile(int x, int y, ResourceType type, int getPerHarvest) {
        super(x, y, false);
        this.type = type;
        this.getPerHarvest = getPerHarvest;
    }

    @Override
    public Tile cloneWithPosition(int x, int y) {
        return new ResourceTile(x, y, this.type, this.getPerHarvest);
    }
}
