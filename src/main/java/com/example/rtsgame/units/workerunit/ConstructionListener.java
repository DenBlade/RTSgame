package com.example.rtsgame.units.workerunit;

public interface ConstructionListener {

    boolean onConstructionFinished(WorkerUnit worker, ConstructionTask task);
    void onConstructionCancelled(WorkerUnit worker, ConstructionTask task);
}