package com.example.rtsgame.statemachine;

import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.Unit;

import java.util.List;

public class IdleState<T extends Unit> extends State<T> {

    @Override
    public void enter(T unit) {
        System.out.println("IdleState: enter");
        unit.setCurrentAnimation(AnimationType.IDLE);
    }

    @Override
    public State<T> update(T unit, double delta) {
        State<T> next = transition(unit);
        if (next != null) {
            return next;
        }
        return this;
    }

    @Override
    public void exit(T unit) {
    }

}
