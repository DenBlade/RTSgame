package com.example.rtsgame.units;

import com.example.rtsgame.Config;
import com.example.rtsgame.map.MapManager;

public class AttackUnit extends Unit {
    public AttackUnit(String fileName, AnimationData[] animationData, MapManager mapManager, double x, double y, boolean ownByAI, int spriteWidth, int spriteHeight, double scale) {
        super(fileName, animationData, mapManager, x, y, ownByAI, spriteWidth, spriteHeight, scale);
    }
    public static AttackUnit createSwordsman(MapManager mapManager, boolean ownByAI, double x, double y) {
        return new AttackUnit("/units/swordman/MiniSwordMan.png", new AnimationData[]{Config.SWORDSMAN_IDLE_ANIM, Config.SWORDSMAN_WALK_ANIM}, mapManager, x, y, ownByAI, 32, 32, 1.5);
    }
}
