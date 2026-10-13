package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.buildings.BuildingType;
import com.example.rtsgame.units.Unit;
import com.example.rtsgame.units.workerunit.ConstructionTask;
import com.example.rtsgame.units.workerunit.WorkerUnit;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {

    private Group map;
    private MapManager mapManager;
    private List<Unit> playerUnits;
    private BuildSystem buildSystem;

    public GameWorld(Group map, MapManager mapManager) {
        this.map = map;
        this.mapManager = mapManager;
        this.buildSystem = new BuildSystem(this);
        playerUnits = new ArrayList<>();
    }

    public void spawnUnit(Unit unit){
        playerUnits.add(unit);
        map.getChildren().add(unit);
        if (unit instanceof WorkerUnit worker) {
            worker.setConstructionListener(this::onConstructionFinished);
        }
    }
    private boolean onConstructionFinished(WorkerUnit worker, ConstructionTask task) {
        worker.clearConstructionTask();
        return buildSystem.placeBuilding(task.getBuildingType(), task.getTileX(), task.getTileY());
    }
    public void placeBuilding(BuildingType type, int tileX, int tileY){
        mapManager.placeBuilding(type, false, tileX, tileY);
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

    public BuildSystem getBuildSystem() {
        return buildSystem;
    }
}
