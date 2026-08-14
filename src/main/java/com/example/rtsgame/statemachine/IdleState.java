package com.example.rtsgame.statemachine;

import com.example.rtsgame.units.Unit;

public class IdleState implements State<Unit> {


    @Override
    public void enter(Unit unit) {
        System.out.println("IdleState: enter");
    }

    @Override
    public State<Unit> update(Unit unit, double delta) {

        if(!unit.path.isEmpty()){
            return new MovingState();
        }
        return this;
    }

    @Override
    public void exit(Unit unit) {
    }

}
