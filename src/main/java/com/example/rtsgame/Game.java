package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Scale;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Game extends Group{
    Group root;
    Scene scene;
    Group map;
    Camera camera;
    double[] sceneSize;


    GameUpdateTimer updateTimer;

    double mapPixelWidth, mapPixelHeight;
    public Game(Group root, Scene scene) throws ParserConfigurationException, IOException, SAXException {
        this.root = root;
        this.scene = scene;

        MapManager mapManager = new MapManager("RTSmap.tmx");

        Canvas mapCanvas = mapManager.getCanvas();
        map = new Group(mapCanvas);
        mapPixelHeight = mapCanvas.getHeight();
        mapPixelWidth = mapCanvas.getWidth();

        root.getChildren().add(map);

        Scale mapScaleTransform = new Scale();
        mapScaleTransform.setPivotX(0);
        mapScaleTransform.setPivotY(0);

        sceneSize = new double[]{scene.getWidth(), scene.getHeight()};

        camera = new Camera(map, scene);
        map.getTransforms().add(camera);
        camera.updateCameraTargetPoint(0, (int) -scene.getHeight());
        map.getTransforms().add(mapScaleTransform);


        scene.setOnKeyPressed(event -> handleInput(event));

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateMapScale(scene, mapScaleTransform));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateMapScale(scene, mapScaleTransform));

        updateTimer = new GameUpdateTimer(this);
        updateTimer.start();
    }
    public void update(long deltaTime){
        camera.smoothCameraHandling(deltaTime/ 1000000000.0);
    }

    private void updateMapScale(Scene scene, Scale scaleTransform) {
        double scaleX = scene.getWidth() / sceneSize[0];
        double scaleY = scene.getHeight() / sceneSize[1];

        double scale = Math.max(scaleX, scaleY);

        scaleTransform.setX(scale);
        scaleTransform.setY(scale);

        camera.updateCameraTargetPoint(0,0); // needed to update camera's target position because of the difference in map size
    }
    private void handleInput(KeyEvent event){
        switch (event.getCode()){
            case A -> camera.moveCamera(20, 0);
            case D -> camera.moveCamera(-20, 0);
            case W -> camera.moveCamera(0, 20);
            case S -> camera.moveCamera(0, -20);
        }
    }


}
