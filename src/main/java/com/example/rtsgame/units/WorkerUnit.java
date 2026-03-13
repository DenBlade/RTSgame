package com.example.rtsgame.units;

import com.example.rtsgame.Config;
import com.example.rtsgame.map.MapManager;

public class WorkerUnit extends Unit{


    public WorkerUnit(String fileName, AnimationData[] animationData, MapManager mapManager, double x, double y, boolean ownByAI, int spriteWidth, int spriteHeight, double scale) {
        super(fileName, animationData, mapManager, x, y, ownByAI, spriteWidth, spriteHeight, scale);
    }

    public static WorkerUnit createWorker(MapManager mapManager, boolean ownByAI, double x, double y) {
        return new WorkerUnit("/units/worker/MiniMiner.png", new AnimationData[] {Config.WORKER_IDLE_ANIM, Config.WORKER_WALK_ANIM}, mapManager, x, y, ownByAI, 32, 32, 1.5);
    }
}
