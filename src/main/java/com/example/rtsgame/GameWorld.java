package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.fogofwar.FogOfWar;
import com.example.rtsgame.map.tiles.buildings.BuildingTile;
import com.example.rtsgame.map.tiles.buildings.BuildingType;
import com.example.rtsgame.units.Unit;
import com.example.rtsgame.units.workerunit.ConstructionListener;
import com.example.rtsgame.units.workerunit.ConstructionTask;
import com.example.rtsgame.units.workerunit.WorkerUnit;
import javafx.scene.Group;

import java.util.*;

public class GameWorld {

    private Group map;
    private MapManager mapManager;
    private FogOfWar fogOfWar;
    private List<Unit> playerUnits;
    private BuildPreviewSystem buildPreviewSystem;
    private List<BuildingTile> buildings;

    private final ResourceManager playerResourceManager;
    private final ResourceManager aiResourceManager;

    private final Queue<Damageable> destructionQueue = new ArrayDeque<>();

    public GameWorld(Group map, MapManager mapManager) {
        this.map = map;
        this.mapManager = mapManager;
        this.buildPreviewSystem = new BuildPreviewSystem(this);
        playerUnits = new ArrayList<>();
        buildings = new LinkedList<>();
        this.playerResourceManager = new ResourceManager();
        this.aiResourceManager = new ResourceManager();
        playerResourceManager.addResource(ResourceType.GOLD, 1000);
        this.fogOfWar = new FogOfWar(mapManager.mapWidth, mapManager.mapHeight);
        map.getChildren().add(fogOfWar.getCanvas());
    }

    public void update(double deltaTime){
        for (Unit unit : playerUnits) {
            unit.update(deltaTime);
        }

        flushDestructionQueue();

        if (Config.ENABLE_FOW && fogOfWar != null) {
            fogOfWar.update(playerUnits, buildings);
        }
    }

    public void spawnUnit(Unit unit){
        playerUnits.add(unit);
        map.getChildren().add(unit);
        unit.setOnDestroyedCallback(this::markForDestruction);

        if (unit instanceof WorkerUnit worker) {
            worker.setConstructionListener(new ConstructionListener() {
                @Override
                public boolean onConstructionFinished(WorkerUnit worker, ConstructionTask task) {
                    if (mapManager.canPlaceBuilding(task.getTileX(), task.getTileY(), task.getBuildingType())) {
                        worker.clearConstructionTask();
                        placeBuilding(task.getBuildingType(), worker.ownByAi(), task.getTileX(), task.getTileY());
                        return true;
                    }
                    return false;
                }
                @Override
                public void onConstructionCancelled(WorkerUnit worker, ConstructionTask task) {
                    ResourceManager targetResources = getResourceManager(worker.ownByAi());
                    targetResources.addResource(task.getBuildingType().getResourceAmount());
                }
            });

            worker.setResourceHarvestListener((workerUnit, resourceType, amount) -> {
                ResourceManager targetResources = getResourceManager(workerUnit.ownByAi());
                targetResources.addResource(resourceType, amount);
            });
        }
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

    public BuildPreviewSystem getBuildPreviewSystem() {
        return buildPreviewSystem;
    }
    public void removeUnit(Unit unit){
        playerUnits.remove(unit);
        map.getChildren().remove(unit);
    }
    public ResourceManager getPlayerResourceManager() {
        return playerResourceManager;
    }

    public ResourceManager getAiResourceManager() {
        return aiResourceManager;
    }
    public ResourceManager getResourceManager(boolean ownByAI) {
        return ownByAI ? aiResourceManager : playerResourceManager;
    }
}
