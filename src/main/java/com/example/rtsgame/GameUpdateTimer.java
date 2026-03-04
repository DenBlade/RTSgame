package com.example.rtsgame;

import javafx.animation.AnimationTimer;

public class GameUpdateTimer extends AnimationTimer {
    private long lastUpdate;
    private Game game;
    public GameUpdateTimer(Game game) {
        this.game = game;
    }
    @Override
    public void handle(long l) {
        game.update(l - lastUpdate);
        lastUpdate = l;
    }
    @Override
    public void start() {
        lastUpdate = System.nanoTime();
        super.start();
    }
}