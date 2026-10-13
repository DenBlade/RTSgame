package com.example.rtsgame;

import com.example.rtsgame.map.tiles.buildings.BuildingType;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BuildSystem {

    public BuildingType selectedBuilding;
    private final GameWorld world;
    ImageView preview;
    Blend blend;

    public BuildSystem(GameWorld world){
        this.world = world;
        preview = new ImageView();
        preview.setOpacity(0.5);
        blend = new Blend();
        blend.setMode(BlendMode.SRC_ATOP);
        preview.setEffect(blend);
    }

    public void startBuilding(BuildingType type){
        selectedBuilding = type;
        System.out.println(selectedBuilding);
        preview.setImage(world.getMapManager().getBuildingPrefab(type).getImage());
        changePreviewColorBlending(Color.GREEN);

        world.getMap().getChildren().add(preview);
    }
    public void changePreviewColorBlending(Color color){
        ColorInput colorInput = new ColorInput(
                0, 0,
                world.getMapManager().getBuildingPrefab(selectedBuilding).getImage().getWidth(),
                world.getMapManager().getBuildingPrefab(selectedBuilding).getImage().getHeight(),
                color
        );
        blend.setTopInput(colorInput);
    }
    public void stopPreviewing(){
        selectedBuilding = null;
        world.getMap().getChildren().remove(preview);
    }
    public boolean isBuilding(){
        return selectedBuilding != null;
    }

    public boolean placeBuilding(BuildingType buildingType, int tileX, int tileY){
        if(canPlaceBuilding(tileX,tileY,buildingType)){
            world.placeBuilding(buildingType, tileX, tileY);
            return true;
        }
        stopPreviewing();
        return false;
    }
    public boolean canPlaceBuilding(int x, int y, BuildingType buildingType){
        int width = buildingType.getWidth();
        int height = buildingType.getHeight();
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
