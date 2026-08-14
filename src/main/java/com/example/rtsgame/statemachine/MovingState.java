package com.example.rtsgame.statemachine;

import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.Unit;

public class MovingState implements State<Unit> {
    @Override
    public void enter(Unit unit) {
        System.out.println("Moving state entered");
    }

    @Override
    public State<Unit> update(Unit unit, double delta) {
        if(unit.pathIndex >= unit.path.size()) {
            return new IdleState();
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
    public void exit(Unit unit) {
        unit.setCurrentAnimation(AnimationType.IDLE);
        unit.achievedTarget();
        unit.path.clear();
    }
}
