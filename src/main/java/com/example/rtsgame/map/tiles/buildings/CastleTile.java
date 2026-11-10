package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.*;
import com.example.rtsgame.actions.Action;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.units.workerunit.WorkerUnit;

public class CastleTile extends BuildingTile{

    public CastleTile(int x, int y, boolean ownedByAI) {
        super(x, y, ownedByAI, BuildingType.CASTLE);
    }

    @Override
    protected void setupActions() {
        Action a1 = new Action(new ResourceAmount(ResourceType.GOLD, Config.WORKER_GOLD_PRICE), "Train worker", this::spawnWorker);
        actions.add(a1);
    }

    public void spawnWorker(GameWorld gameWorld) {
        ResourceManager resources = gameWorld.getResourceManager(this.ownedByAI);
        if (!resources.trySpend(ResourceType.GOLD, Config.WORKER_GOLD_PRICE)) {
            System.out.println("Not enough gold to train worker!");
            return;
        }

        int[] spawnTile = gameWorld.getMapManager().findNearestTraversableTile(x, y);

        if (spawnTile != null) {
            double[] coords = MapManager.convertToWorldCoordinates(spawnTile);
            WorkerUnit unit = WorkerUnit.createWorker(gameWorld.getMapManager(), this.ownedByAI, coords[0] + Config.TILE_WIDTH/2.0, coords[1] + Config.TILE_HEIGHT/2.0);
            gameWorld.spawnUnit(unit);
        } else {
            resources.addResource(ResourceType.GOLD, Config.WORKER_GOLD_PRICE);
            System.out.println("There is no space available");
        }

    }

}
