package com.example.rtsgame.units.workerunit;

import com.example.rtsgame.*;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.buildings.BuildingType;
import com.example.rtsgame.statemachine.*;
import com.example.rtsgame.units.AnimationData;
import com.example.rtsgame.units.Unit;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class WorkerUnit extends Unit implements ActionProvider {

    private ConstructionTask constructionTask;
    private ConstructionListener constructionListener;

    private List<Action> actions;

    public WorkerUnit(String fileName, AnimationData[] animationData, MapManager mapManager, int health, double speed, double x, double y, boolean ownByAI, int spriteWidth, int spriteHeight, double scale) {
        super(fileName, animationData, mapManager, health, speed, x, y, ownByAI, spriteWidth, spriteHeight, scale);
        actions = new ArrayList<>();
        setupActions();
    }
    private void setupActions(){
        Action a1 = new Action("Build Castle", gameWorld -> {
            BuildSystem buildSystem = gameWorld.getBuildSystem();
            if(buildSystem.isBuilding()){
                buildSystem.stopPreviewing();
            }
            else{
                buildSystem.startBuilding(BuildingType.CASTLE);
            }
        });
        actions.add(a1);

        Action a2 = new Action("Build barracks", gameWorld -> {
            BuildSystem buildSystem = gameWorld.getBuildSystem();
            if(buildSystem.isBuilding()){
                buildSystem.stopPreviewing();
            }
            else{
                buildSystem.startBuilding(BuildingType.BARRACKS);
            }
        });
        actions.add(a2);
    }

    @Override
    public List<Action> getActions() {
        return actions;
    }

    @Override
    public boolean setupPath(double targetX, double targetY) {
        clearConstructionTask();
        return super.setupPath(targetX, targetY);
    }

    @Override
    protected void setupStateMachine(){

        System.out.println("override state machine");
        State<WorkerUnit> idleState = new IdleState<>();
        State<WorkerUnit> movingState = new MovingState<>();
        State<WorkerUnit> buildingState = new BuildingState();
        State<WorkerUnit> deadState = new DeadState<>();

        stateMachine = new StateMachine<>(this, idleState);

        Transition<WorkerUnit> dead = new Transition<>((unit) -> unit.health <= 0, deadState);

        Transition<WorkerUnit> movingFromIdle = new Transition<>((unit) -> unit.hasPath(), movingState);
        idleState.addTransition(movingFromIdle);
        idleState.addTransition(dead);
//        Transition<WorkerUnit> buildingFromIdle = new Transition<>((unit) -> unit.assignedBuilding, buildingState);
//        idleState.addTransition(buildingFromIdle);

        Transition<WorkerUnit> buildingFromMoving = new Transition<>((unit) -> unit.isFinishedMoving() && unit.hasConstructionTask(), buildingState);
        Transition<WorkerUnit> idleFromMoving = new Transition<>(Unit::isFinishedMoving, idleState);

        movingState.addTransition(buildingFromMoving);
        movingState.addTransition(idleFromMoving);
        movingState.addTransition(dead);

        Transition<WorkerUnit> idleFromBuilding = new Transition<>((unit) -> !unit.hasConstructionTask(), idleState);
        Transition<WorkerUnit> movingFromBuilding = new Transition<>((unit) -> unit.hasPath(), movingState);

        buildingState.addTransition(idleFromBuilding);
        buildingState.addTransition(movingFromBuilding);
        buildingState.addTransition(dead);


    }

    public static WorkerUnit createWorker(MapManager mapManager, boolean ownByAI, double x, double y) {
        return new WorkerUnit("/units/worker/MiniMiner.png", new AnimationData[] {Config.WORKER_IDLE_ANIM, Config.WORKER_WALK_ANIM, Config.WORKER_WORK_ANIM, Config.WORKER_DEATH_ANIM}, mapManager, Config.WORKER_MAX_HEALTH, Config.WORKER_MOVEMENT_SPEED, x, y, ownByAI, 32, 32, 1.5);
    }
    public void setConstructionListener(ConstructionListener listener) {
        this.constructionListener = listener;
    }

    public void buildAt(BuildingType type, double x, double y) {
        int[] tileCoords = MapManager.convertToTileCoordinates(new double[]{x, y});
        double[] layoutCoords = MapManager.convertToWorldCoordinates(tileCoords);
        setupPath(layoutCoords[0], layoutCoords[1]);
        constructionTask = new ConstructionTask(type, tileCoords[0], tileCoords[1]);
    }
    public boolean finishConstruction() {
        if (constructionListener != null) {
            return constructionListener.onConstructionFinished(this, constructionTask);
        }
        return false;
    }

    public void clearConstructionTask(){
        constructionTask = null;
    }
    public boolean hasConstructionTask() {
        return constructionTask != null;
    }

    public ConstructionTask getConstructionTask() {
        return constructionTask;
    }

}
