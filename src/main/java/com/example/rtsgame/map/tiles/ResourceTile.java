package com.example.rtsgame.map.tiles;

import com.example.rtsgame.ResourceType;
import com.example.rtsgame.units.workerunit.WorkerUnit;

public class ResourceTile extends Tile {
    private final ResourceType type;
    private final int getPerHarvest;
    private WorkerUnit assignedWorker;
    public ResourceTile(int x, int y, ResourceType type, int getPerHarvest) {
        super(x, y, false);
        this.type = type;
        this.getPerHarvest = getPerHarvest;
    }
    public ResourceType getType() {
        return type;
    }

    public int getGetPerHarvest() {
        return getPerHarvest;
    }
    public boolean isOccupied() {
        return assignedWorker != null;
    }

    public WorkerUnit getAssignedWorker() {
        return assignedWorker;
    }

    public boolean assignWorker(WorkerUnit worker) {
        if (isOccupied()) {
            return false;
        }
        this.assignedWorker = worker;
        return true;
    }

    public void releaseWorker() {
        this.assignedWorker = null;
    }
    @Override
    public Tile cloneWithPosition(int x, int y) {
        return new ResourceTile(x, y, this.type, this.getPerHarvest);
    }
}
