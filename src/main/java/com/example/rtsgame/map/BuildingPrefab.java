package com.example.rtsgame.map;

import javafx.scene.image.Image;

public class BuildingPrefab {
    private Image image;
    private int width;
    private int height;
    public BuildingPrefab(Image image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
