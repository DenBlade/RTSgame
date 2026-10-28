package com.example.rtsgame.statemachine;

import com.example.rtsgame.Config;
import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.Unit;

public class DeadState<T extends Unit> extends State<T>{
    private double currentTime;
    private double animationDuration;

    @Override
    void enter(T owner) {
        System.out.println("enter dead state");
        animationDuration = owner.setCurrentAnimationOnce(AnimationType.DEATH, 200);
        currentTime = 0;
    }

    @Override
    State<T> update(T owner, double deltaTime) {
        currentTime += deltaTime;
        if(currentTime >= animationDuration / 1000.0) {
            owner.stopAnimation();
            owner.requestRemoval();
        }
        return this;
    }

    @Override
    void exit(T owner) {
        owner.setSelected(false);
    }
}
