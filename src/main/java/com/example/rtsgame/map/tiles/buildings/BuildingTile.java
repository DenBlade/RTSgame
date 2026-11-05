package com.example.rtsgame.map.tiles.buildings;

import com.example.rtsgame.*;
import com.example.rtsgame.map.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public abstract class BuildingTile extends Tile implements Damageable, ActionProvider {
    protected boolean ownedByAI;
    public int health;
    protected int maxHealth;
    private boolean isSelected;
    private BuildingType buildingType;

    private Consumer<Damageable> onDestroyedCallback;

    protected List<Action> actions;

    public BuildingTile(int x, int y, boolean ownedByAI, BuildingType buildingType) {
        super(x, y, false);
        this.ownedByAI = ownedByAI;
        this.buildingType = buildingType;
        this.maxHealth = buildingType.getMaxHealth();
        this.health = maxHealth;
        actions = new ArrayList<>();
        setupActions();
    }
    public void setOnDestroyedCallback(Consumer<Damageable> callback) {
        this.onDestroyedCallback = callback;
    }
    protected abstract void setupActions();

    @Override
    public List<Action> getActions() {
        return actions;
    }

    @Override
    public void takeDamage(int damage) {
        int newHealth = health - damage;
        if(newHealth <= 0){
            health = 0;
            if (onDestroyedCallback != null) {
                onDestroyedCallback.accept(this);
            }
        }
        else{
            health = newHealth;
        }
    }

    @Override
    public void cleanup() {
        setSelected(false);
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
        if (ownedByAI) return;
        isSelected = selected;
    }
    public boolean isSelected(){
        return isSelected;
    }
    public BuildingType getBuildingType() {
        return buildingType;
    }

}
