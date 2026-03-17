package com.example.rtsgame.units;

import com.example.rtsgame.BuildSystem;
import com.example.rtsgame.Config;
import com.example.rtsgame.GameWorld;
import com.example.rtsgame.UIOptionBarHolder;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.buildings.BuildingType;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class WorkerUnit extends Unit implements UIOptionBarHolder {

    private boolean assignedBuilding;
    private int buildingPositionX;
    private int buildingPositionY;

    public WorkerUnit(String fileName, AnimationData[] animationData, MapManager mapManager, double x, double y, boolean ownByAI, int spriteWidth, int spriteHeight, double scale) {
        super(fileName, animationData, mapManager, x, y, ownByAI, spriteWidth, spriteHeight, scale);
        assignedBuilding = false;
    }

    public static WorkerUnit createWorker(MapManager mapManager, boolean ownByAI, double x, double y) {
        return new WorkerUnit("/units/worker/MiniMiner.png", new AnimationData[] {Config.WORKER_IDLE_ANIM, Config.WORKER_WALK_ANIM}, mapManager, x, y, ownByAI, 32, 32, 1.5);
    }

    @Override
    public void moveTo(double targetX, double targetY) {
        super.moveTo(targetX, targetY);
        setAssignedBuilding(false);
    }

    public void setAssignedBuilding(boolean assignedBuilding) {
        this.assignedBuilding = assignedBuilding;
    }
    public void buildAt(double x, double y) {
        System.out.println("Building at " + x + ", " + y);
        int[] tileCoords = MapManager.convertToTileCoordinates(new double[]{x, y});
        double[] layoutCoords = MapManager.convertToWorldCoordinates(tileCoords);

        moveTo(layoutCoords[0]-10, layoutCoords[1]-10);
        buildingPositionX = tileCoords[0];
        buildingPositionY = tileCoords[1];
        assignedBuilding = true;
    }
    public boolean isAssignedBuilding() {
        return assignedBuilding;
    }

    public int getBuildingPositionX() {
        return buildingPositionX;
    }

    public int getBuildingPositionY() {
        return buildingPositionY;
    }

    @Override
    public List<Node> getUI(GameWorld gameWorld) {
        Button buildCastle = new Button("Build castle");
        buildCastle.setOnAction(e -> {
            BuildSystem buildSystem = gameWorld.getBuildSystem();
            if(buildSystem.isBuilding()){
                buildSystem.stopBuilding();
            }
            else{
                buildSystem.startBuilding(BuildingType.CASTLE);
            }
        });
        List<Node> ui = new ArrayList<>();
        ui.add(buildCastle);
        return ui;
    }
}
