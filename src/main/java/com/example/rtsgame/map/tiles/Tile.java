package com.example.rtsgame.map.tiles;

public class Tile {
    protected int x;
    protected int y;
    protected boolean isTraversable;
    public Tile(int x, int y, boolean isTraversable) {
        this.x = x;
        this.y = y;
        this.isTraversable = isTraversable;
    }
    public Tile cloneWithPosition(int x, int y) {
        return new Tile(x, y, this.isTraversable);
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isTraversable() {
        return isTraversable;
    }

    public void setTraversable(boolean traversable) {
        isTraversable = traversable;
    }
}
