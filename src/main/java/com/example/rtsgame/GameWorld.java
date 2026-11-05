package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.buildings.BuildingTile;
import com.example.rtsgame.map.tiles.buildings.BuildingType;
import com.example.rtsgame.units.Unit;
import com.example.rtsgame.units.workerunit.ConstructionTask;
import com.example.rtsgame.units.workerunit.WorkerUnit;
import javafx.scene.Group;

import java.util.*;

public class GameWorld {

    private Group map;
    private MapManager mapManager;
    private List<Unit> playerUnits;
    private BuildSystem buildSystem;
    private List<BuildingTile> buildings;

    private final Queue<Damageable> destructionQueue = new ArrayDeque<>();

    public GameWorld(Group map, MapManager mapManager) {
        this.map = map;
        this.mapManager = mapManager;
        this.buildSystem = new BuildSystem(this);
        playerUnits = new ArrayList<>();
        buildings = new LinkedList<>();
    }

    public void update(double deltaTime){
        for (Unit unit : playerUnits) {
            unit.update(deltaTime);
        }

        flushDestructionQueue();
    }

    public void spawnUnit(Unit unit){
        playerUnits.add(unit);
        map.getChildren().add(unit);
        unit.setOnDestroyedCallback(this::markForDestruction);
        if (unit instanceof WorkerUnit worker) {
            worker.setConstructionListener(this::onConstructionFinished);
        }
    }
    private boolean onConstructionFinished(WorkerUnit worker, ConstructionTask task) {
        worker.clearConstructionTask();
        return buildSystem.placeBuilding(task.getBuildingType(), task.getTileX(), task.getTileY());
    }
    private void markForDestruction(Damageable entity) {
        if (!destructionQueue.contains(entity)) {
            destructionQueue.add(entity);
        }
    }
    public void placeBuilding(BuildingType type, boolean ownByAI, int tileX, int tileY){
        BuildingTile building = mapManager.placeBuilding(type, ownByAI, tileX, tileY);
        buildings.add(building);
        building.setOnDestroyedCallback(this::markForDestruction);
    }
    public void destroyBuilding(BuildingTile tile){
        mapManager.destroyBuilding(tile);
        buildings.remove(tile);
    }
    private void flushDestructionQueue() {
        while (!destructionQueue.isEmpty()) {
            Damageable entity = destructionQueue.poll();
            entity.cleanup();

            if (entity instanceof BuildingTile building) {
                destroyBuilding(building);
            } else if (entity instanceof Unit unit) {
                removeUnit(unit);
            }
        }
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
    public void removeUnit(Unit unit){
        playerUnits.remove(unit);
        map.getChildren().remove(unit);
    }
}
