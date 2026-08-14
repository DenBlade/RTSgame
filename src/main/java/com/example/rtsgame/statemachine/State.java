package com.example.rtsgame.statemachine;

import com.example.rtsgame.units.Unit;

public interface State<T> {
    void enter(T owner);
    State<T> update(T owner, double deltaTime);
    void exit(T owner);
}