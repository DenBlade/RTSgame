package com.example.rtsgame.statemachine;


import java.util.ArrayList;
import java.util.List;

public abstract class State<T> {
    private final List<Transition<T>> transitions = new ArrayList<>();
    abstract void enter(T owner);
    abstract State<T> update(T owner, double deltaTime);
    abstract void exit(T owner);

    public void addTransition(Transition<T> transition) {
        transitions.add(transition);
    }
    protected State<T> transition(T owner){
        for(Transition<T> t : transitions){
            State<T> a = t.evaluate(owner);
            if(a != null){
                return a;
            }
        }
        return null;
    }
}