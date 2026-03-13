package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.GameWorld;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.units.WorkerUnit;

import java.util.List;

public class CastleTile extends BuildingTile {

    public CastleTile(int id, int x, int y, boolean ownedByAI) {
        super(id, x, y, ownedByAI);
    }

    @Override
    public void buildingFunction(GameWorld gameWorld) {
        List<Tile> adjacent = gameWorld.getMapManager().getAdjacentTiles(x, y);
        for(Tile tile : adjacent) {
            if(tile.isTraversable()){
                double[] coords = tile.getWorldCoordinates();
                WorkerUnit unit = WorkerUnit.createWorker(gameWorld.getMapManager(), false, coords[0], coords[1]);
                gameWorld.spawnUnit(unit);
                break;
            }
        }

    }
}
