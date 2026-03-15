package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.GameWorld;
import com.example.rtsgame.UIOptionBarHolder;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.units.WorkerUnit;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class CastleTile extends BuildingTile implements UIOptionBarHolder {

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

    @Override
    public List<Node> getUI(GameWorld gameWorld) {
        Button workerButton = new Button("Train Worker");
        workerButton.setOnAction(e -> {buildingFunction(gameWorld); });
        List<Node> ui = new ArrayList<>();
        ui.add(workerButton);
        return ui;
    }
}
