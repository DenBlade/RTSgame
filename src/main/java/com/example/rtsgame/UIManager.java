package com.example.rtsgame;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UIManager extends Group {
    private GameWorld gameWorld;
    private Scene scene;

    private VBox optionsBar;
    private ActionProvider uiOwner;

    private Label fpsLabel;
    private long frameCount = 0;
    private double elapsedTimeAccumulator = 0;

    public UIManager(GameWorld gameWorld, Scene scene) {
        this.gameWorld = gameWorld;
        this.scene = scene;

        optionsBar = new VBox();

        fpsLabel = new Label("FPS: --");
        fpsLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 16px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, black, 4, 0, 1, 1);");

        getChildren().addAll(optionsBar, fpsLabel);
        setLayout();
    }
    public void updateFPS(double deltaTime) {
        frameCount++;
        elapsedTimeAccumulator += deltaTime;

        if (elapsedTimeAccumulator >= 0.5) {
            double fps = (frameCount) / elapsedTimeAccumulator;
            fpsLabel.setText(String.format("FPS: %.0f", fps));
            frameCount = 0;
            elapsedTimeAccumulator = 0;
        }
    }
    public void setLayout(){
        optionsBar.setLayoutX(scene.getWidth()-100);
        optionsBar.setLayoutY(scene.getHeight()-100);

        fpsLabel.setLayoutX(10);
        fpsLabel.setLayoutY(10);
    }

    public void enableOptionsBar(ActionProvider provider) {
        optionsBar.getChildren().clear();

        for (Action action : provider.getActions()) {
            Button button = new Button(action.getName());
            button.setOnAction(e -> action.execute(gameWorld));
            optionsBar.getChildren().add(button);
        }

        uiOwner = provider;
    }
    public void disableOptionsBar(){
        optionsBar.getChildren().clear();
        uiOwner = null;
    }
    public ActionProvider getUiOwner(){
        return uiOwner;
    }
}
