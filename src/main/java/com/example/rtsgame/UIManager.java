package com.example.rtsgame;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class UIManager extends Group {
    GameWorld gameWorld;
    VBox optionsBar;
    Scene scene;
    public UIManager(GameWorld gameWorld, Scene scene) {
        this.gameWorld = gameWorld;
        this.scene = scene;
        optionsBar = new VBox();
        getChildren().add(optionsBar);
        setLayout();
    }
    public void setLayout(){
        optionsBar.setLayoutX(scene.getWidth()-100);
        optionsBar.setLayoutY(scene.getHeight()-100);
    }
    public void enableOptionsBar(UIOptionBarHolder uiElement){
        optionsBar.getChildren().clear();
        optionsBar.getChildren().addAll(uiElement.getUI(gameWorld));
    }
    public void disableOptionsBar(){
        optionsBar.getChildren().clear();
    }
}
