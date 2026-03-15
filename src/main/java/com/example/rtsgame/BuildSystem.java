package com.example.rtsgame;

import com.example.rtsgame.map.tiles.buildings.BuildingType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BuildSystem {

    private BuildingType selectedBuilding;
    private GameWorld world;
    ImageView preview;

    public BuildSystem(GameWorld world){
        this.world = world;
        preview = new ImageView();
        preview.setOpacity(0.5);
    }

    public void startBuilding(BuildingType type){
        selectedBuilding = type;
        System.out.println(selectedBuilding);
        preview.setImage(world.getMapManager().getBuildingPrefab(type).getImage());
        world.getMap().getChildren().add(preview);
    }
    public void stopBuilding(){
        selectedBuilding = null;
        world.getMap().getChildren().remove(preview);
    }
    public boolean isBuilding(){
        return selectedBuilding != null;
    }

    public void placeBuilding(int tileX, int tileY){
        if(selectedBuilding == null) return;

        if(canPlaceBuilding(tileX,tileY,selectedBuilding.getWidth(), selectedBuilding.getHeight())){
            world.placeBuilding(selectedBuilding, tileX, tileY);
        }
        stopBuilding();
    }
    public boolean canPlaceBuilding(int x, int y, int width, int height){

        for(int i=x;i<x+width;i++){
            for(int j=y;j<y+height;j++){

                if(!world.getMapManager().getTile(i,j).isTraversable()){
                    return false;
                }

            }
        }

        return true;
    }
    public ImageView getPreview(){
        return preview;
    }
}
