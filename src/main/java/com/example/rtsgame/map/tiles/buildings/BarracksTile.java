package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.GameWorld;
import com.example.rtsgame.UIOptionBarHolder;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.units.AttackUnit;
import com.example.rtsgame.units.UnitType;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class BarracksTile extends BuildingTile implements UIOptionBarHolder {

    public BarracksTile(int x, int y, boolean ownedByAI) {
        super(x, y, ownedByAI);
    }
    private void spawnAttackUnit(GameWorld gameWorld, UnitType unitType) {
        List<Tile> adjacent = gameWorld.getMapManager().getAdjacentTiles(x, y);
        for(Tile tile : adjacent) {
            if(tile.isTraversable()){
                double[] coords = tile.getWorldCoordinates();
                AttackUnit unit = null;
                switch (unitType) {
                    case SWORDSMAN -> {
                        unit = AttackUnit.createSwordsman(gameWorld.getMapManager(), false, coords[0], coords[1]);
                    }
                }
                gameWorld.spawnUnit(unit);
                break;
            }
        }
    }

    @Override
    public List<Node> getUI(GameWorld gameWorld) {
        Button swordsmanButton = new Button("Train swordsman");
        swordsmanButton.setOnAction(e -> {spawnAttackUnit(gameWorld, UnitType.SWORDSMAN); });
        List<Node> ui = new ArrayList<>();
        ui.add(swordsmanButton);
        return ui;
    }
}
