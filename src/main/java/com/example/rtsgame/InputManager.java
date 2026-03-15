package com.example.rtsgame;

import com.example.rtsgame.map.tiles.buildings.BuildingTile;
import com.example.rtsgame.units.Unit;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

public class InputManager {

    private final Set<KeyCode> currentKeys = new HashSet<>();
    private final Set<KeyCode> previousKeys = new HashSet<>();

    private final Set<MouseButton> currentMouseButtons = new HashSet<>();
    private final Set<MouseButton> previousMouseButtons = new HashSet<>();

    private final double[] mouseClickPosition = new double[2];
    private final double[] mousePosition = new double[2];

    private Node clickedObject;

    public InputManager(Scene scene) {

        scene.addEventHandler(KeyEvent.KEY_PRESSED,
                e -> currentKeys.add(e.getCode()));

        scene.addEventHandler(KeyEvent.KEY_RELEASED,
                e -> currentKeys.remove(e.getCode()));

        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            currentMouseButtons.add(e.getButton());

            mouseClickPosition[0] = e.getX();
            mouseClickPosition[1] = e.getY();

            clickedObject = e.getPickResult().getIntersectedNode();
        });

        scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            mousePosition[0] = e.getSceneX();
            mousePosition[1] = e.getSceneY();
        });

        scene.addEventHandler(MouseEvent.MOUSE_RELEASED,
                e -> currentMouseButtons.remove(e.getButton()));
    }

    public void update() {
        previousKeys.clear();
        previousKeys.addAll(currentKeys);

        previousMouseButtons.clear();
        previousMouseButtons.addAll(currentMouseButtons);
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
    public boolean isMousePressed(MouseButton button) {
        return currentMouseButtons.contains(button);
    }

    public boolean wasMousePressed(MouseButton button) {
        return currentMouseButtons.contains(button) && !previousMouseButtons.contains(button);
    }

    public boolean wasMouseReleased(MouseButton button) {
        return !currentMouseButtons.contains(button) && previousMouseButtons.contains(button);
    }
    public double[] getMouseClickPosition() {
        return mouseClickPosition;
    }
    public double[] getMousePosition() {
        return mousePosition;
    }
    public Unit getClickedUnit() {
        Node node = clickedObject;

        while (node != null) {
            if (node instanceof Unit unit) {
                return unit;
            }

            if (node instanceof javafx.scene.Node n) {
                node = n.getParent();
            } else {
                return null;
            }
        }

        return null;
    }

}