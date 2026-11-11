package com.example.rtsgame.map.fogofwar;

import com.example.rtsgame.Config;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.buildings.BuildingTile;
import com.example.rtsgame.units.Unit;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class FogOfWar {

    private final int mapWidth;
    private final int mapHeight;
    private final FogState[][] fogState;
    private final Canvas canvas;
    private final GraphicsContext gc;

    public FogOfWar(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.fogState = new FogState[mapWidth][mapHeight];
        this.canvas = new Canvas(mapWidth * Config.TILE_WIDTH, mapHeight * Config.TILE_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.canvas.setMouseTransparent(true);

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                this.fogState[x][y] = FogState.UNEXPLORED;
            }
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void update(List<Unit> units, List<BuildingTile> buildings) {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (fogState[x][y] == FogState.VISIBLE) {
                    fogState[x][y] = FogState.EXPLORED;
                }
            }
        }

        for (Unit unit : units) {
            if (!unit.ownByAi()) {
                int[] tile = MapManager.convertToTileCoordinates(new double[]{unit.getCenterX(), unit.getCenterY()});
                markVisibleArea(tile[0], tile[1], Config.UNIT_VISION_RADIUS);
            }
        }

        for (BuildingTile building : buildings) {
            if (!building.ownByAi()) {
                int originX = building.getX();
                int originY = building.getY();
                int width = building.getBuildingType().getWidth();
                int height = building.getBuildingType().getHeight();

                for (int bx = originX; bx < originX + width; bx++) {
                    for (int by = originY; by < originY + height; by++) {
                        markVisibleArea(bx, by, Config.BUILDING_VISION_RADIUS);
                    }
                }
            }
        }

        for (Unit unit : units) {
            if (unit.ownByAi()) {
                int[] tile = MapManager.convertToTileCoordinates(new double[]{unit.getCenterX(), unit.getBottomY()});
                boolean isVisible = isTileVisible(tile[0], tile[1]);
                unit.setVisible(isVisible);
            }
        }

        redraw();
    }

    private void markVisibleArea(int centerX, int centerY, int radius) {
        int r2 = radius * radius;
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) {
                    int dx = x - centerX;
                    int dy = y - centerY;
                    if (dx * dx + dy * dy <= r2) {
                        fogState[x][y] = FogState.VISIBLE;
                    }
                }
            }
        }
    }

    public boolean isTileVisible(int x, int y) {
        if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) {
            return fogState[x][y] == FogState.VISIBLE;
        }
        return false;
    }

    private void redraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                FogState state = fogState[x][y];
                if (state == FogState.UNEXPLORED) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * Config.TILE_WIDTH, y * Config.TILE_HEIGHT, Config.TILE_WIDTH, Config.TILE_HEIGHT);
                } else if (state == FogState.EXPLORED) {
                    gc.setFill(Color.rgb(0, 0, 0, 0.4));
                    gc.fillRect(x * Config.TILE_WIDTH, y * Config.TILE_HEIGHT, Config.TILE_WIDTH, Config.TILE_HEIGHT);
                }
            }
        }
    }
}