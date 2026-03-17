package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.units.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Scale;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Game extends Group{
    Group root;
    Scene scene;
    Group map;
    Tile[][] tilesData;
    Camera camera;
    double[] sceneSize;

    InputManager inputManager;
    GameUpdateTimer updateTimer;
    GameWorld gameWorld;
    BuildSystem buildSystem;
    ImageView buildingPreview;
    UIManager uiManager;
    SelectionManager selectionManager;
    double mapPixelWidth, mapPixelHeight;

    List<Unit> playerUnits;
    List<Unit> selectedUnits;
    public Game(Group root, Scene scene) throws ParserConfigurationException, IOException, SAXException {
        this.root = root;
        this.scene = scene;

        MapManager mapManager = new MapManager("RTSmap.tmx");
        tilesData = mapManager.getTilesData();
        inputManager = new InputManager(scene);

        Canvas mapCanvas = mapManager.getCanvas();
        map = new Group(mapCanvas);
        mapPixelHeight = mapCanvas.getHeight();
        mapPixelWidth = mapCanvas.getWidth();

        root.getChildren().add(map);

        gameWorld = new GameWorld(map, mapManager);
        buildSystem = gameWorld.getBuildSystem();
        playerUnits = gameWorld.getPlayerUnits();
        selectionManager = new SelectionManager(playerUnits);

        uiManager = new UIManager(gameWorld, scene);
        root.getChildren().add(uiManager);

        Scale mapScaleTransform = new Scale();
        mapScaleTransform.setPivotX(0);
        mapScaleTransform.setPivotY(0);

        sceneSize = new double[]{scene.getWidth(), scene.getHeight()};

        camera = new Camera(map, scene);
        map.getTransforms().add(camera);
        camera.updateCameraTargetPoint(0, (int) -scene.getHeight());
        map.getTransforms().add(mapScaleTransform);


        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateMapScale(scene, mapScaleTransform);
            uiManager.setLayout();
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            updateMapScale(scene, mapScaleTransform);
            uiManager.setLayout();
        });

        AttackUnit swordsman = AttackUnit.createSwordsman(mapManager, false, 200, 200);
        playerUnits.add(swordsman);
        map.getChildren().add(swordsman);

        WorkerUnit worker = WorkerUnit.createWorker(mapManager, false, 300, 200);
        playerUnits.add(worker);
        map.getChildren().add(worker);

        selectedUnits = selectionManager.getSelectedPlayerUnits();

        updateTimer = new GameUpdateTimer(this);
        updateTimer.start();


    }
    public void update(long deltaTime){
        if(buildSystem.isBuilding()){
            double[] mousePos = inputManager.getMousePosition();
            Point2D mapPoint = map.sceneToLocal(mousePos[0], mousePos[1]);
            int[] temp = MapManager.convertToTileCoordinates(new double[]{mapPoint.getX(), mapPoint.getY()});
            double[] previewPos = MapManager.convertToWorldCoordinates(temp);
            ImageView view = buildSystem.getPreview();
            view.setLayoutX(previewPos[0]);
            view.setLayoutY(previewPos[1]);
        }

        if(selectedUnits.size() == 1){
            if(selectedUnits.get(0) instanceof WorkerUnit unit){
                System.out.println("");
                if(unit.isAssignedBuilding() && unit.hasReachedTarget()){
                    unit.setAssignedBuilding(false);
                    gameWorld.getBuildSystem().placeBuilding(unit.getBuildingPositionX(), unit.getBuildingPositionY());
                }
            }
        }


        if(inputManager.wasMousePressed(MouseButton.PRIMARY)){
            double[] mousePos = inputManager.getMouseClickPosition();
            Point2D mapPoint = map.sceneToLocal(mousePos[0], mousePos[1]);

            Unit clickedUnit = inputManager.getClickedUnit();

            if(clickedUnit != null){
                clickedUnit.toogleUnitSelection();
            } else {
                if(!buildSystem.isBuilding()){
                    uiManager.disableOptionsBar();
                    // clicked ground → deselect all
                    for(Unit unit : playerUnits){
                        unit.setSelected(false);
                    }
                }
            }

            selectedUnits = selectionManager.getSelectedPlayerUnits();
            if(selectedUnits.size() == 1){
                if(selectedUnits.get(0) instanceof UIOptionBarHolder unitWithUI){
                    System.out.println("One selected");
                    uiManager.enableOptionsBar(unitWithUI);
                }
            }
            else{
                uiManager.disableOptionsBar();
            }

            if(gameWorld.getBuildSystem().isBuilding()){
                WorkerUnit unit = (WorkerUnit) selectedUnits.getFirst();
                unit.buildAt(mapPoint.getX(), mapPoint.getY());
                return;
            }

            Tile clickedTile = gameWorld.getMapManager().getTileAt(mapPoint.getX(), mapPoint.getY());
            if (clickedTile instanceof UIOptionBarHolder castleTile) {
                uiManager.enableOptionsBar(castleTile);
            }
        }
        if(inputManager.wasMousePressed(MouseButton.SECONDARY)){
            double[] mousePos = inputManager.getMouseClickPosition();
            Point2D mapPoint = map.sceneToLocal(mousePos[0], mousePos[1]);
            System.out.println("MouseX: " + mousePos[0] + " " + mousePos[1]);
            for(Unit unit : selectedUnits){
                unit.moveTo(mapPoint.getX(), mapPoint.getY());
            }
        }
        for(Unit unit : playerUnits){
            unit.update(deltaTime/1e9);
        }

        double moveX = 0;
        double moveY = 0;
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

        inputManager.update();
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
