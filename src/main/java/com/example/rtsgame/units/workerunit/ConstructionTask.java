package com.example.rtsgame.units.workerunit;

import com.example.rtsgame.map.tiles.buildings.BuildingType;

public class ConstructionTask {

    private final BuildingType buildingType;
    private final int tileX;
    private final int tileY;

    public ConstructionTask(
            BuildingType buildingType,
            int tileX,
            int tileY
    ) {
        this.buildingType = buildingType;
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }
}
