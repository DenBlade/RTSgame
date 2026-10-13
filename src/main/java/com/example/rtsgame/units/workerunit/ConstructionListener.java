package com.example.rtsgame.units.workerunit;

@FunctionalInterface
public interface ConstructionListener {

    boolean onConstructionFinished(
            WorkerUnit worker,
            ConstructionTask task
    );
}