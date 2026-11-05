package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.Action;
import com.example.rtsgame.Config;
import com.example.rtsgame.GameWorld;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.units.workerunit.WorkerUnit;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class CastleTile extends BuildingTile{

    public CastleTile(int x, int y, boolean ownedByAI) {
        super(x, y, ownedByAI, BuildingType.CASTLE);
    }

    @Override
    protected void setupActions() {
        Action a1 = new Action("Train worker", this::spawnWorker);
        actions.add(a1);
    }

    private void spawnWorker(GameWorld gameWorld) {
        int[] spawnTile = gameWorld.getMapManager().findNearestTraversableTile(x, y);

        if (spawnTile != null) {
            double[] coords = MapManager.convertToWorldCoordinates(spawnTile);
            WorkerUnit unit = WorkerUnit.createWorker(gameWorld.getMapManager(), false, coords[0] + Config.TILE_WIDTH/2.0, coords[1] + Config.TILE_HEIGHT/2.0);
            gameWorld.spawnUnit(unit);
        } else {
            System.out.println("There is no space available");
        }

    }

}
