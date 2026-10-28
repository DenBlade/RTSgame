package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.Tile;
import com.example.rtsgame.map.tiles.buildings.BuildingTile;
import com.example.rtsgame.map.tiles.buildings.BuildingType;
import com.example.rtsgame.units.*;
import com.example.rtsgame.units.workerunit.WorkerUnit;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
    UIManager uiManager;
    SelectionManager selectionManager;
    double mapPixelWidth, mapPixelHeight;

    List<Unit> playerUnits;
    List<Unit> selectedUnits;
    List<Unit> unitsToRemove;
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
        unitsToRemove = new ArrayList<>();
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
        gameWorld.spawnUnit(swordsman);
        AttackUnit swordsman2 = AttackUnit.createSwordsman(mapManager, true, 500, 200);
        gameWorld.spawnUnit(swordsman2);

        WorkerUnit worker = WorkerUnit.createWorker(mapManager, false, 300, 200);
        gameWorld.spawnUnit(worker);

        gameWorld.placeBuilding(BuildingType.CASTLE, true, Config.ENEMY_CASTLE_POS[0], Config.ENEMY_CASTLE_POS[1]);
        gameWorld.placeBuilding(BuildingType.CASTLE, false, Config.PLAYER_CASTLE_POS[0], Config.PLAYER_CASTLE_POS[1]);

        selectedUnits = selectionManager.getSelectedPlayerUnits();

        updateTimer = new GameUpdateTimer(this);
        updateTimer.start();

    }
    public void update(long deltaTime){
        
        if(uiManager.getUiOwner() != null && !uiManager.getUiOwner().isSelected()){
            uiManager.disableOptionsBar();
        }

        if(inputManager.isPressed(KeyCode.ESCAPE)){
            uiManager.disableOptionsBar();
            buildSystem.stopPreviewing();
            for(Unit unit : selectedUnits){
                unit.setSelected(false);
            }
        }

        if(buildSystem.isBuilding()){
            double[] mousePos = inputManager.getMousePosition();
            Point2D mapPoint = map.sceneToLocal(mousePos[0], mousePos[1]);
            int[] temp = MapManager.convertToTileCoordinates(new double[]{mapPoint.getX(), mapPoint.getY()});
            double[] previewPos = MapManager.convertToWorldCoordinates(temp);
            ImageView view = buildSystem.getPreview();

            int[] tileCoords = MapManager.convertToTileCoordinates(new double[]{mapPoint.getX(), mapPoint.getY()});
            if(gameWorld.getBuildSystem().canPlaceBuilding(tileCoords[0], tileCoords[1], gameWorld.getBuildSystem().selectedBuilding)){
                gameWorld.getBuildSystem().changePreviewColorBlending(Color.GREEN);
            }
            else {
                gameWorld.getBuildSystem().changePreviewColorBlending(Color.RED);
            }

            view.setLayoutX(previewPos[0]);
            view.setLayoutY(previewPos[1]);
        }

        if(inputManager.wasMousePressed(MouseButton.PRIMARY)){
            double[] mousePos = inputManager.getMouseClickPosition();
            Point2D mapPoint = map.sceneToLocal(mousePos[0], mousePos[1]);

            Tile clickedTile = gameWorld.getMapManager().getTileAt(mapPoint.getX(), mapPoint.getY());
            if (clickedTile instanceof UIOptionBarHolder tileWithUI) {
                tileWithUI.setSelected(true);
                System.out.println(tileWithUI.isSelected());
                uiManager.enableOptionsBar(tileWithUI);
            }

            Unit clickedUnit = inputManager.getClickedUnit();
            if(clickedUnit != null){
                clickedUnit.toggleSelection();
                selectedUnits = selectionManager.getSelectedPlayerUnits();
                if(selectedUnits.size() == 1 && selectedUnits.getFirst() instanceof UIOptionBarHolder unitWithUI){
                    uiManager.enableOptionsBar(unitWithUI);
                }
                else{
                    uiManager.disableOptionsBar();
                }
            }

            if(gameWorld.getBuildSystem().isBuilding()){
                WorkerUnit unit = (WorkerUnit) selectedUnits.getFirst();
                int[] tileCoords = MapManager.convertToTileCoordinates(new double[]{mapPoint.getX(), mapPoint.getY()});
                if(gameWorld.getBuildSystem().canPlaceBuilding(tileCoords[0], tileCoords[1], gameWorld.getBuildSystem().selectedBuilding)){
                    unit.buildAt(gameWorld.getBuildSystem().selectedBuilding, mapPoint.getX(), mapPoint.getY());
                }
                else{
                    gameWorld.getBuildSystem().stopPreviewing();
                    System.out.println("Cannot build here");
                }
                return;
            }

        }
        if(inputManager.wasMousePressed(MouseButton.SECONDARY)){
            double[] mousePos = inputManager.getMouseClickPosition();
            Point2D mapPoint = map.sceneToLocal(mousePos[0], mousePos[1]);

            Unit clickedUnit = inputManager.getClickedUnit();
            Tile clickedTile = gameWorld.getMapManager().getTileAt(mapPoint.getX(), mapPoint.getY());

            Damageable targetDamageable = null;
            if (clickedUnit != null) {
                targetDamageable = clickedUnit;
            } else if (clickedTile instanceof BuildingTile buildingTile) {
                targetDamageable = buildingTile;
            }
            for(Unit unit : selectedUnits){
                if (unit instanceof AttackUnit attackUnit && (targetDamageable != null && targetDamageable.ownByAi())) {
                    attackUnit.attackTarget(targetDamageable);
                } else {
                    unit.setupPath(mapPoint.getX(), mapPoint.getY());
                }
            }
        }

        for(Unit unit : playerUnits){
            unit.update(deltaTime/1e9);
            if(unit.isRemovalRequested()){
                unitsToRemove.add(unit);
            }
        }

        ListIterator<Unit> removeUnitsIt = unitsToRemove.listIterator();
        while(removeUnitsIt.hasNext()){
            Unit unit = removeUnitsIt.next();
            gameWorld.removeUnit(unit);
            removeUnitsIt.remove();
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
