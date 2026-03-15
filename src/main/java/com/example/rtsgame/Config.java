package com.example.rtsgame;

import com.example.rtsgame.units.AnimationData;
import com.example.rtsgame.units.AnimationType;

public class Config {
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int[] PLAYER_CASTLE_POS = {2, 26};
    public static final int[] ENEMY_CASTLE_POS = {36, 2};

    public static final double CAMERA_MOVEMENT_SPEED = 8;
    public static final double CAMERA_MOVEMENT_STEP = 15;

    public static final double SWORDSMAN_MOVEMENT_SPEED = 60;
    public static final double ERROR_TOLERANCE = 0.5;

    public static final AnimationData SWORDSMAN_IDLE_ANIM = new AnimationData(AnimationType.IDLE, 0, 4);
    public static final AnimationData SWORDSMAN_WALK_ANIM = new AnimationData(AnimationType.WALK, 1, 6);

    public static final AnimationData WORKER_IDLE_ANIM = new AnimationData(AnimationType.IDLE, 0, 4);
    public static final AnimationData WORKER_WALK_ANIM = new AnimationData(AnimationType.WALK, 3, 6);
}
