package com.example.rtsgame;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

public class InputManager {

    private final Set<KeyCode> currentKeys = new HashSet<>();
    private final Set<KeyCode> previousKeys = new HashSet<>();

    public InputManager(Scene scene) {

        scene.addEventHandler(KeyEvent.KEY_PRESSED,
                e -> currentKeys.add(e.getCode()));

        scene.addEventHandler(KeyEvent.KEY_RELEASED,
                e -> currentKeys.remove(e.getCode()));
    }

    public void update() {
        previousKeys.clear();
        previousKeys.addAll(currentKeys);
    }

    public boolean isPressed(KeyCode key) {
        return currentKeys.contains(key);
    }

    public boolean wasPressedThisFrame(KeyCode key) {
        return currentKeys.contains(key) && !previousKeys.contains(key);
    }

    public boolean wasReleasedThisFrame(KeyCode key) {
        return !currentKeys.contains(key) && previousKeys.contains(key);
    }
}