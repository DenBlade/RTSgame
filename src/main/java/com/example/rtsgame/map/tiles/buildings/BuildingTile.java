package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.Config;
import com.example.rtsgame.Damageable;
import com.example.rtsgame.GameWorld;
import com.example.rtsgame.Utils;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.map.tiles.Tile;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;


public abstract class BuildingTile extends Tile implements Damageable {
    protected boolean ownedByAI;
    public int health;
    protected int maxHealth;
    private boolean isSelected;
    MapManager mapManager;
    private BuildingType buildingType;

    public BuildingTile(int x, int y, boolean ownedByAI, BuildingType buildingType, MapManager mapManager) {
        super(x, y, false);
        this.ownedByAI = ownedByAI;
        this.buildingType = buildingType;
        this.mapManager = mapManager;
        this.maxHealth = buildingType.getMaxHealth();
        this.health = maxHealth;
    }

    @Override
    public void takeDamage(int damage) {
        int newHealth = health - damage;
        if(newHealth <= 0){
            health = 0;
            mapManager.destroyBuilding(this);
        }
        else{
            health = newHealth;
        }
    }

    @Override
    public boolean ownByAi() {
        return ownedByAI;
    }
    @Override
    public boolean isDestroyed() {
        return health <= 0;
    }

    @Override
    public double getCenterX() {
        return (x + buildingType.getWidth()/2.0) * Config.TILE_WIDTH;
    }
    @Override
    public double getCenterY() {
        return (y + buildingType.getHeight()/2.0) * Config.TILE_HEIGHT;
    }

    @Override
    public double getDistanceTo(double fromX, double fromY) {
        int width = buildingType.getWidth();
        int height = buildingType.getHeight();

        double minX = Config.TILE_WIDTH;
        double maxX = (x + width) * Config.TILE_WIDTH;
        double minY = y * Config.TILE_HEIGHT;
        double maxY = (y + height) * Config.TILE_HEIGHT;

        double closestX = Utils.clamp(fromX, minX, maxX);
        double closestY = Utils.clamp(fromY, minY, maxY);

        double dx = fromX - closestX;
        double dy = fromY - closestY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean isSelected(){
        return isSelected;
    }
    public BuildingType getBuildingType() {
        return buildingType;
    }

}
