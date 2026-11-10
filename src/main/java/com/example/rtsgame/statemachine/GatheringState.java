package com.example.rtsgame.statemachine;

import com.example.rtsgame.Config;
import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.workerunit.WorkerUnit;

public class GatheringState extends State<WorkerUnit> {

    private double timer;

    @Override
    public void enter(WorkerUnit unit) {
        System.out.println("GatheringState: enter");
        unit.setCurrentAnimationLoop(AnimationType.WORK);
        timer = 0;
    }

    @Override
    public State<WorkerUnit> update(WorkerUnit unit, double deltaTime) {
        State<WorkerUnit> next = transition(unit);
        if (next != null) {
            return next;
        }

        timer += deltaTime;
        if (timer >= Config.WORKER_HARVEST_INTERVAL) {
            unit.harvestResource();
            timer = 0;
        }

        return this;
    }

    @Override
    public void exit(WorkerUnit unit) {
    }
}