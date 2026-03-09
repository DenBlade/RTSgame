package com.example.rtsgame;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.transform.Translate;

public class Camera extends Translate {
    double cameraTargetPosX;
    double cameraTargetPosY;
    Group map;
    Scene scene;
    private double maxOffsetX, maxOffsetY;
    public Camera(Group map, Scene scene) {
        super();
        this.map = map;
        this.scene = scene;
        cameraTargetPosX = 0;
        cameraTargetPosY = 0;
        maxOffsetX = Math.max(map.getBoundsInParent().getWidth() - scene.getWidth(), 0);
        maxOffsetY = Math.max(map.getBoundsInParent().getHeight() - scene.getHeight(), 0);
    }
    public void smoothCameraHandling(double deltaTime){
        setX(getX() + (cameraTargetPosX - getX()) * deltaTime * Config.CAMERA_MOVEMENT_SPEED);
        setY(getY() + (cameraTargetPosY - getY()) * deltaTime * Config.CAMERA_MOVEMENT_SPEED);
    }
    public void moveCamera(double offsetX, double offsetY){
        updateCameraTargetPoint(offsetX, offsetY);
    }
    public void updateCameraTargetPoint(double offsetX, double offsetY){
        updateOffsets();

        cameraTargetPosX = Utils.clamp(cameraTargetPosX + offsetX, -maxOffsetX, 0);
        cameraTargetPosY = Utils.clamp(cameraTargetPosY + offsetY, -maxOffsetY, 0);
    }

    private void updateOffsets(){
        maxOffsetX = Math.max(map.getBoundsInParent().getWidth() - scene.getWidth(), 0);
        maxOffsetY = Math.max(map.getBoundsInParent().getHeight() - scene.getHeight(), 0);
    }

}
