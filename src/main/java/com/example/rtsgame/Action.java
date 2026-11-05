package com.example.rtsgame;

import com.example.rtsgame.GameWorld;
import java.util.function.Consumer;

public class Action {
    private final String name;
    private final Consumer<GameWorld> action;

    public Action(String name, Consumer<GameWorld> action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void execute(GameWorld world) {
        if (action != null) {
            action.accept(world);
        }
    }
}