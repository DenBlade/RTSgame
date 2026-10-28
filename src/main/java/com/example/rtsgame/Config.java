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

    public static final double UNIT_COLLISION_PUSH_SPEED = 100;
    public static final double UNIT_DEATH_ANIM_TIME = 2;

    public static final double SWORDSMAN_MOVEMENT_SPEED = 70;
    public static final int SWORDSMAN_MAX_HEALTH = 20;
    public static final int SWORDSMAN_ATTACK_DAMAGE = 5;
    public static final double SWORDSMAN_ATTACK_RANGE = 30.0;
    public static final double SWORDSMAN_ATTACK_COOLDOWN = 1.0;

    public static final AnimationData SWORDSMAN_IDLE_ANIM = new AnimationData(AnimationType.IDLE, 0, 4);
    public static final AnimationData SWORDSMAN_WALK_ANIM = new AnimationData(AnimationType.WALK, 1, 6);
    public static final AnimationData SWORDSMAN_DEATH_ANIM = new AnimationData(AnimationType.DEATH, 5, 4);
    public static final AnimationData SWORDSMAN_ATTACK_ANIM = new AnimationData(AnimationType.ATTACK, 3, 6);

    public static final int WORKER_MAX_HEALTH = 3;
    public static final int WORKER_MOVEMENT_SPEED = 60;

    public static final AnimationData WORKER_IDLE_ANIM = new AnimationData(AnimationType.IDLE, 0, 4);
    public static final AnimationData WORKER_WALK_ANIM = new AnimationData(AnimationType.WALK, 3, 6);
    public static final AnimationData WORKER_WORK_ANIM = new AnimationData(AnimationType.WORK, 2, 5);
    public static final AnimationData WORKER_DEATH_ANIM = new AnimationData(AnimationType.DEATH, 7, 4);


}
