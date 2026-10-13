package com.example.rtsgame.statemachine;

import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.workerunit.WorkerUnit;

public class BuildingState extends State<WorkerUnit>{

    private double currentTime;
    @Override
    public void enter(WorkerUnit owner) {
        System.out.println("BuildingState: enter");
        owner.setCurrentAnimation(AnimationType.WORK);
        currentTime = 0;
    }

    @Override
    public State<WorkerUnit> update(WorkerUnit unit, double delta) {

        currentTime += delta;
        if(currentTime >= unit.getConstructionTask().getBuildingType().getConstructionTime()) {
            unit.finishConstruction();
        }

        State<WorkerUnit> next = transition(unit);
        if (next != null) {
            return next;
        }
        return this;
    }

    @Override
    public void exit(WorkerUnit owner) {
        owner.clearConstructionTask();
    }
}
