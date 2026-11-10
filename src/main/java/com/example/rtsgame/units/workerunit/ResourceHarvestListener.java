package com.example.rtsgame.units.workerunit;

import com.example.rtsgame.ResourceType;

public interface ResourceHarvestListener {
    void onResourceHarvested(WorkerUnit worker, ResourceType type, int amount);
}
