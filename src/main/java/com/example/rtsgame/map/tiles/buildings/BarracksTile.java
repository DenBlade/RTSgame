package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.*;
import com.example.rtsgame.actions.Action;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.units.AttackUnit;
import com.example.rtsgame.units.UnitType;

public class BarracksTile extends BuildingTile{
    public BarracksTile(int x, int y, boolean ownedByAI) {
        super(x, y, ownedByAI, BuildingType.BARRACKS);
    }

    @Override
    protected void setupActions() {
        Action a1 = new Action(new ResourceAmount(ResourceType.GOLD, Config.SWORDSMAN_GOLD_PRICE), "Train swordsman", gameWorld -> spawnAttackUnit(gameWorld, UnitType.SWORDSMAN));
        actions.add(a1);
    }

    public void spawnAttackUnit(GameWorld gameWorld, UnitType unitType) {
        ResourceManager resources = gameWorld.getResourceManager(this.ownedByAI);
        if (!resources.trySpend(unitType.getResourceAmount())) {
            System.out.println("Not enough to train unit!");
            return;
        }

        int[] spawnTile = gameWorld.getMapManager().findNearestTraversableTile(x, y);

        if (spawnTile != null) {
            double[] coords = MapManager.convertToWorldCoordinates(spawnTile);
            AttackUnit unit = null;
            switch (unitType) {
                case SWORDSMAN ->
                        unit = AttackUnit.createSwordsman(gameWorld.getMapManager(), this.ownedByAI, coords[0] + Config.TILE_WIDTH/2.0, coords[1] + Config.TILE_HEIGHT / 2.0);
            }
            if (unit != null) {
                gameWorld.spawnUnit(unit);
            }
        }
        else{
                resources.addResource(unitType.getResourceAmount());
                System.out.println("There is no space available");
            }
        }

}
