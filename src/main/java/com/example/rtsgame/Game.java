package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
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

    InputManager inputManager;
    GameUpdateTimer updateTimer;

    double mapPixelWidth, mapPixelHeight;
    public Game(Group root, Scene scene) throws ParserConfigurationException, IOException, SAXException {
        this.root = root;
        this.scene = scene;

        MapManager mapManager = new MapManager("RTSmap.tmx");
        inputManager = new InputManager(scene);

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

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateMapScale(scene, mapScaleTransform));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateMapScale(scene, mapScaleTransform));

        updateTimer = new GameUpdateTimer(this);
        updateTimer.start();
    }
    public void update(long deltaTime){
        inputManager.update();
        double moveX = 0;
        double moveY = 0;
        int step = 20;
        if(inputManager.isPressed(KeyCode.A)) moveX += 1;
        if(inputManager.isPressed(KeyCode.D)) moveX -= 1;
        if(inputManager.isPressed(KeyCode.W)) moveY += 1;
        if(inputManager.isPressed(KeyCode.S)) moveY -= 1;

        //normalize input
        double length = Math.sqrt(moveX * moveX + moveY * moveY);
        if(length > 1){
            moveX /= length;
            moveY /= length;
        }

        camera.moveCamera(
                moveX * Config.CAMERA_MOVEMENT_STEP,
                moveY * Config.CAMERA_MOVEMENT_STEP
        );
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

}
