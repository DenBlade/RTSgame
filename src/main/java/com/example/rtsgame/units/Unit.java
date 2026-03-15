package com.example.rtsgame.units;

import com.example.rtsgame.Config;
import com.example.rtsgame.map.MapManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.HashMap;

public class Unit extends Group {

    private Image spriteSheet;
    private ImageView unitSprite;
    private double spriteWidth, spriteHeight;
    protected HashMap<AnimationType, AnimationData> animations;
    protected AnimationTimeline currentAnimation;
    protected int currentFrame;
    private boolean ownByAI;

    private boolean isSelected = false;

    private double targetX;
    private double targetY;

    protected MapManager mapManager;

    public Unit(String fileName, AnimationData[] animationData, MapManager mapManager, double x, double y, boolean ownByAI, int spriteWidth, int spriteHeight, double scale) {
        this.spriteWidth = spriteWidth * scale;
        this.spriteHeight = spriteHeight * scale;
        this.ownByAI = ownByAI;
        this.mapManager = mapManager;
        setCenterX(x);
        setBottomY(y);
        setTarget(x,y);

        spriteSheet = new Image(getClass().getResourceAsStream(fileName));
        spriteSheet = resample(spriteSheet, scale);

        unitSprite = new ImageView(spriteSheet);
        unitSprite.setViewport(new Rectangle2D(0, 0, this.spriteWidth, this.spriteHeight));
        getChildren().add(unitSprite);
        setupAnimations(animationData);

        setCurrentAnimation(AnimationType.IDLE);

        setPickOnBounds(false); //click is detected only if clicked on opaque pixels

//        setOnMouseClicked(this::handleMouseClick);
    }
    private void setupAnimations(AnimationData[] animationData) {
        animations = new HashMap<>();
        for (int i = 0; i < animationData.length; i++) {
            animations.put(animationData[i].getType(), animationData[i]);
        }
    }
    private void setCurrentAnimation(AnimationType type) {
        AnimationData animationData = animations.get(type);
        if (animationData != null) {
            if(currentAnimation == null) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.millis(100), e -> {

                            currentFrame = (currentFrame + 1) % currentAnimation.getAnimationData().getSpritesCount();

                            double x = currentFrame * spriteWidth;
                            double y = currentAnimation.getAnimationData().getRow() * spriteHeight;
                            unitSprite.setViewport(new Rectangle2D(x, y, spriteWidth, spriteHeight));
                        })
                );
                currentAnimation = new AnimationTimeline(animationData, timeline);
                currentAnimation.getTimeline().setCycleCount(Timeline.INDEFINITE);
                currentAnimation.getTimeline().play();
            }
            else if(currentAnimation.getAnimationData().getType() != type){
                currentAnimation.setAnimationData(animationData);
            }
        }
        else {
            System.out.println("No animation for type " + type);
        }
    }

    private void handleMouseClick(MouseEvent event) {
        if(ownByAI) return;
        if(event.getButton() == MouseButton.PRIMARY)
            toogleUnitSelection();
    }
    public void toogleUnitSelection(){
        setSelected(!isSelected);
    }

    public void setSelected(boolean value) {
        if(ownByAI) return;
        isSelected = value;
        toggleSelectionShadow();
    }
    public boolean isSelected() {
        return isSelected;
    }
    private void toggleSelectionShadow(){
        DropShadow outline = new DropShadow();
        outline.setColor(Color.YELLOW);
        outline.setRadius(10);
        outline.setSpread(0.6);

        unitSprite.setEffect(isSelected ? outline : null);
    }
    private Image resample(Image input, double scaleFactor) {
        int W = (int) input.getWidth();
        int H = (int) input.getHeight();

        int newW = (int) Math.round(W * scaleFactor);
        int newH = (int) Math.round(H * scaleFactor);

        WritableImage output = new WritableImage(newW, newH);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < newH; y++) {
            for (int x = 0; x < newW; x++) {
                // Find nearest source pixel
                int srcX = (int) (x / scaleFactor);
                int srcY = (int) (y / scaleFactor);
                writer.setArgb(x, y, reader.getArgb(srcX, srcY));
            }
        }

        return output;
    }
    public void setTarget(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }
    public void moveSmooth(double deltaTime) {
        double dx = targetX - getCenterX();
        double dy = targetY - getBottomY();


        double distance = Math.sqrt(dx * dx + dy * dy);

        if (hasReachedTarget()) {
            achievedTarget();
            return;
        }
        //flip image if moving left
        if(dx<0){
            unitSprite.setScaleX(-1);
        }
        else{
            unitSprite.setScaleX(1);
        }

        setCurrentAnimation(AnimationType.WALK);
        double moveDistance = Config.SWORDSMAN_MOVEMENT_SPEED * deltaTime;

        double dirX = dx / distance;
        double dirY = dy / distance;

        double newX = getCenterX() + dirX * moveDistance;
        double newY = getBottomY() + dirY * moveDistance;

        int tileX = (int)(newX / Config.TILE_WIDTH);
        int tileY = (int)(newY / Config.TILE_HEIGHT);
        if(!mapManager.isTileTraversable(tileX, tileY)){
            resolveTileCollision(newX, newY);
        }
        else{
            setCenterX(newX);
            setBottomY(newY);
        }

    }
    protected void achievedTarget(){
        setCurrentAnimation(AnimationType.IDLE);
        setCenterX(targetX);
        setBottomY(targetY);
    }
    public boolean hasReachedTarget(){

        double dx = targetX - getCenterX();
        double dy = targetY - getBottomY();

        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < Config.ERROR_TOLERANCE;
    }
    private void resolveTileCollision(double newX, double newY) {

        double oldX = getCenterX();
        double oldY = getBottomY();

        // try moving only X
        if(mapManager.isTraversableAt(newX, oldY)){
            setCenterX(newX);
            return;
        }

        // try moving only Y
        if(mapManager.isTraversableAt(oldX, newY)){
            setBottomY(newY);
            return;
        }

        // both blocked → stay
    }
    private double getCenterX(){
        return getLayoutX() + spriteWidth/2;
    }
    private double getCenterY(){
        return getLayoutY() + spriteHeight/2;
    }
    private double getBottomY(){
        return getLayoutY() + spriteHeight;
    }
    private void setCenterX(double x) {
        setLayoutX(x-spriteWidth/2);
    }
    private void setCenterY(double y) {
        setLayoutY(y-spriteHeight/2);
    }
    private void setBottomY(double y) {
        setLayoutY(y-spriteHeight);
    }
}


