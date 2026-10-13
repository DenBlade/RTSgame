package com.example.rtsgame.statemachine;

public class StateMachine<T> {

    private final T owner;
    private State<T> current;

    public StateMachine(T owner, State<T> initialState) {
        this.owner = owner;
        this.current = initialState;

        current.enter(owner);
    }
    public void update(double delta) {

        State<T> next = current.update(owner, delta);

        if (next != current) {
            transitionTo(next);
        }
    }

    private void transitionTo(State<T> next) {

        current.exit(owner);
        current = next;
        current.enter(owner);
    }
}