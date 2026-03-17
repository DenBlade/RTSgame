package com.example.rtsgame.units.pathfinding;

public class PathNode {

    public int x, y;
    public double gCost; // distance from start
    public double hCost; // heuristic to end
    public double fCost;

    public PathNode parent;

    public PathNode(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void calculateFCost(){
        fCost = gCost + hCost;
    }
}
