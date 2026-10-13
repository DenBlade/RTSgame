package com.example.rtsgame.statemachine;

import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.Unit;

import java.util.List;

public class MovingState<T extends Unit> extends State<T> {
    @Override
    public void enter(T unit) {
        System.out.println("Moving state entered");
    }

    @Override
    public State<T> update(T unit, double delta) {
        State<T> next = transition(unit);
        if (next != null) {
            return next;
        }

        double[] target = unit.path.get(unit.pathIndex);

        unit.moveSmooth(target[0], target[1], delta);

        if(unit.isCloseTo(target[0], target[1])){
            System.out.println("x: " + target[0] + " y: " + target[1]);
            unit.pathIndex++;
        }
        return this;
    }

    @Override
    public void exit(T unit) {
        unit.setCurrentAnimation(AnimationType.IDLE);
        unit.achievedTarget();
        unit.path.clear();
    }
}
