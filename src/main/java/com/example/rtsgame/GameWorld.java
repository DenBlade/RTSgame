package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.units.Unit;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {

    private Group map;
    private MapManager mapManager;
    private List<Unit> playerUnits;

    public GameWorld(Group map, MapManager mapManager) {
        this.map = map;
        this.mapManager = mapManager;
        playerUnits = new ArrayList<>();
    }

    public void spawnUnit(Unit unit){
        playerUnits.add(unit);
        map.getChildren().add(unit);
    }

    public List<Unit> getPlayerUnits(){
        return playerUnits;
    }

    public MapManager getMapManager(){
        return mapManager;
    }

    public Group getMap(){
        return map;
    }
}
