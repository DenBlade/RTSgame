package com.example.rtsgame.map.tiles;

import com.example.rtsgame.Config;

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
    public double[] getWorldCoordinates() {
        return new double[] {this.x* Config.TILE_WIDTH, this.y*Config.TILE_HEIGHT};
    }
    public static int[] convertToTileCoordinates(double[] coords){
        return new int[]{(int)coords[0]/Config.TILE_WIDTH,(int)coords[1]/Config.TILE_HEIGHT};
    }
    public int[] getCoordinates() {
        return new int[] {this.x,this.y};
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isTraversable() {
        return isTraversable;
    }
}
