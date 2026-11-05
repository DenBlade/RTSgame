package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.Action;
import com.example.rtsgame.Config;
import com.example.rtsgame.GameWorld;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.units.AttackUnit;
import com.example.rtsgame.units.UnitType;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class BarracksTile extends BuildingTile{
    public BarracksTile(int x, int y, boolean ownedByAI) {
        super(x, y, ownedByAI, BuildingType.BARRACKS);
    }

    @Override
    protected void setupActions() {
        Action a1 = new Action("Train swordsman", gameWorld -> spawnAttackUnit(gameWorld, UnitType.SWORDSMAN));
        actions.add(a1);
    }

    private void spawnAttackUnit(GameWorld gameWorld, UnitType unitType) {
        int[] spawnTile = gameWorld.getMapManager().findNearestTraversableTile(x, y);

        if (spawnTile != null) {
            double[] coords = MapManager.convertToWorldCoordinates(spawnTile);
            AttackUnit unit = null;
            switch (unitType) {
                case SWORDSMAN ->
                        unit = AttackUnit.createSwordsman(gameWorld.getMapManager(), false, coords[0] + Config.TILE_WIDTH/2.0, coords[1] + Config.TILE_HEIGHT / 2.0);
            }
            if (unit != null) {
                gameWorld.spawnUnit(unit);
            }
        }
        else{
                System.out.println("There is no space available");
            }
        }

}
