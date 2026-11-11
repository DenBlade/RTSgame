package com.example.rtsgame;

import com.example.rtsgame.actions.Action;
import com.example.rtsgame.actions.ActionProvider;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UIManager extends Group {
    private GameWorld gameWorld;
    private Scene scene;

    private VBox optionsBar;
    private ActionProvider uiOwner;

    private HBox resourceBar;
    private Label goldLabel;

    private Label fpsLabel;
    private long frameCount = 0;
    private double elapsedTimeAccumulator = 0;

    public UIManager(GameWorld gameWorld, Scene scene) {
        this.gameWorld = gameWorld;
        this.scene = scene;

        optionsBar = new VBox();
        optionsBar.layoutXProperty().bind(scene.widthProperty()
                .subtract(optionsBar.widthProperty())
                .subtract(20)
        );

        fpsLabel = new Label("FPS: --");
        fpsLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 16px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, black, 4, 0, 1, 1);");

        ResourceManager playerResources = gameWorld.getPlayerResourceManager();
        resourceBar = new HBox(15);
        resourceBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 8px 15px; -fx-background-radius: 5;");
        resourceBar.layoutXProperty().bind(scene.widthProperty()
                        .subtract(resourceBar.widthProperty())
                        .subtract(50)
        );

        goldLabel = new Label();
        goldLabel.setStyle("-fx-text-fill: #f3ebeb; -fx-font-size: 14px; -fx-font-weight: bold;");
        goldLabel.textProperty().bind(
                playerResources.amountProperty(ResourceType.GOLD).asString("Gold: %d")
        );

        resourceBar.getChildren().addAll(goldLabel);

        getChildren().addAll(optionsBar, fpsLabel, resourceBar);
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
        optionsBar.setLayoutY(scene.getHeight()-100);

        fpsLabel.setLayoutX(10);
        fpsLabel.setLayoutY(10);

        resourceBar.setLayoutY(10);
    }

    public void enableOptionsBar(ActionProvider provider) {
        optionsBar.getChildren().clear();

        for (Action action : provider.getActions()) {
            String buttonText = action.getName();
            ResourceAmount resourceAmount = action.getResourceAmount();
            if(resourceAmount != null){
                buttonText = resourceAmount.amount() + " " + resourceAmount.resourceType() + " - " + buttonText;
            }

            Button button = new Button(buttonText);
            button.setMaxWidth(Double.MAX_VALUE);
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
