package com.example.rtsgame.statemachine;

import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.workerunit.WorkerUnit;

public class BuildingState extends State<WorkerUnit>{

    private double currentTime;
    @Override
    public void enter(WorkerUnit owner) {
        System.out.println("BuildingState: enter");
        owner.setCurrentAnimationLoop(AnimationType.WORK);
        currentTime = 0;
    }

    @Override
    public State<WorkerUnit> update(WorkerUnit unit, double deltaTime) {

        State<WorkerUnit> next = transition(unit);
        if (next != null) {
            return next;
        }

        currentTime += deltaTime;
        if(currentTime >= unit.getConstructionTask().getBuildingType().getConstructionTime()) {
            unit.finishConstruction();
        }

        return this;
    }

    @Override
    public void exit(WorkerUnit owner) {
        owner.clearConstructionTask();
    }
}
