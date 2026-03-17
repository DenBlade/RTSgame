package com.example.rtsgame.units.pathfinding;

import com.example.rtsgame.map.MapManager;

import java.util.ArrayList;
import java.util.List;

public class Pathfinding {

    private MapManager mapManager;

    public Pathfinding(MapManager mapManager){
        this.mapManager = mapManager;
    }

    public List<int[]> findPath(int startTileX, int startTileY, int endTileX, int endTileY){

        List<PathNode> openList = new ArrayList<>();
        List<PathNode> closedList = new ArrayList<>();

        PathNode startNode = new PathNode(startTileX, startTileY);
        PathNode endNode = new PathNode(endTileX, endTileY);

        openList.add(startNode);

        while(!openList.isEmpty()){

            PathNode current = getLowestFCost(openList);

            if(current.x == endNode.x && current.y == endNode.y){
                return reconstructPath(current);
            }

            openList.remove(current);
            closedList.add(current);

            for(int[] dir : getDirections()){
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                if(!mapManager.isTileTraversable(nx, ny)) continue;

                if(contains(closedList, nx, ny)) continue;

                double newG = current.gCost + 1;

                PathNode neighbor = getNode(openList, nx, ny);

                if(neighbor == null){
                    neighbor = new PathNode(nx, ny);
                    neighbor.gCost = newG;
                    neighbor.hCost = heuristic(nx, ny, endTileX, endTileY);
                    neighbor.calculateFCost();
                    neighbor.parent = current;
                    openList.add(neighbor);
                }
                else if(newG < neighbor.gCost){
                    neighbor.gCost = newG;
                    neighbor.calculateFCost();
                    neighbor.parent = current;
                }
            }
        }

        return new ArrayList<>(); // no path
    }
    private double heuristic(int x1, int y1, int x2, int y2){
        return Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
    }

    private int[][] getDirections(){
        return new int[][]{
                {1,0}, {-1,0}, {0,1}, {0,-1}
        };
    }

    private PathNode getLowestFCost(List<PathNode> list){
        PathNode best = list.get(0);
        for(PathNode n : list){
            if(n.fCost < best.fCost){
                best = n;
            }
        }
        return best;
    }

    private boolean contains(List<PathNode> list, int x, int y){
        return list.stream().anyMatch(n -> n.x == x && n.y == y);
    }

    private PathNode getNode(List<PathNode> list, int x, int y){
        for(PathNode n : list){
            if(n.x == x && n.y == y) return n;
        }
        return null;
    }

    private List<int[]> reconstructPath(PathNode endNode){
        List<int[]> path = new ArrayList<>();
        PathNode current = endNode;

        while(current != null){
            path.add(0, new int[]{current.x, current.y});
            current = current.parent;
        }

        return path;
    }
}