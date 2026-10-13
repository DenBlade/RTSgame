package com.example.rtsgame.statemachine;

import java.util.function.Predicate;

public class Transition<T> {
    public Predicate<T> condition;
    public State<T> state;

    public Transition(Predicate<T> predicate, State<T> state) {
        this.condition = predicate;
        this.state = state;
    }
    public State<T> evaluate(T owner){
        if(condition.test(owner)){
            return state;
        }
        return null;
    }
}
