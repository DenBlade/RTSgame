package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;



public class HelloApplication extends Application {
    double mapPixelWidth, mapPixelHeight;
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 1000, 600);

        MapManager mapManager = new MapManager("RTSmap.tmx");

        Canvas mapCanvas = mapManager.getCanvas();
        Group map = new Group(mapCanvas);
        mapPixelHeight = mapCanvas.getHeight();
        mapPixelWidth = mapCanvas.getWidth();

        root.getChildren().add(map);

        Scale mapScaleTransform = new Scale();
        mapScaleTransform.setPivotX(0);
        mapScaleTransform.setPivotY(0);

        map.getTransforms().add(mapScaleTransform);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateMapScale(scene, mapScaleTransform));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateMapScale(scene, mapScaleTransform));


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    private void updateMapScale(Scene scene, Scale scaleTransform) {
        double scaleX = scene.getWidth() / mapPixelWidth;
        double scaleY = scene.getHeight() / mapPixelHeight;

        double scale = Math.max(scaleX, scaleY);

        scaleTransform.setX(scale);
        scaleTransform.setY(scale);
    }


}