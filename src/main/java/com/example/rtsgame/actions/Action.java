package com.example.rtsgame.actions;

import com.example.rtsgame.GameWorld;
import com.example.rtsgame.ResourceAmount;

import java.util.function.Consumer;

public class Action {
    private final String name;
    private final ResourceAmount resourceAmount;
    private final Consumer<GameWorld> action;

    public Action(ResourceAmount resourceAmount, String name, Consumer<GameWorld> action) {
        this.name = name;
        this.resourceAmount = resourceAmount;
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
    public ResourceAmount getResourceAmount(){
        return resourceAmount;
    }
}