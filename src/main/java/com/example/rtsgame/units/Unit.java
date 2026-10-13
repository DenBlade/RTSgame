package com.example.rtsgame.units;

import com.example.rtsgame.Config;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.statemachine.*;
import com.example.rtsgame.units.pathfinding.Pathfinding;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Unit extends Group {

    private Image spriteSheet;
    private ImageView unitSprite;
    private double spriteWidth, spriteHeight;
    protected HashMap<AnimationType, AnimationData> animations;
    protected AnimationTimeline currentAnimation;
    protected int currentFrame;

    private boolean isSelected = false;

    protected MapManager mapManager;

    protected StateMachine<? extends Unit> stateMachine;
    private boolean ownByAI;

    private Pathfinding pathfinding;
    public List<double[]> path = new ArrayList<>();
    public int pathIndex;

    protected int health;

    public Unit(String fileName, AnimationData[] animationData, MapManager mapManager, int health, double x, double y, boolean ownByAI, int spriteWidth, int spriteHeight, double scale) {
        this.spriteWidth = spriteWidth * scale;
        this.spriteHeight = spriteHeight * scale;
        this.ownByAI = ownByAI;
        this.mapManager = mapManager;
        this.health = health;
        setCenterX(x);
        setBottomY(y);


        pathfinding = new Pathfinding(mapManager);

        spriteSheet = new Image(getClass().getResourceAsStream(fileName));
        spriteSheet = resample(spriteSheet, scale);

        unitSprite = new ImageView(spriteSheet);
        unitSprite.setViewport(new Rectangle2D(0, 0, this.spriteWidth, this.spriteHeight));
        getChildren().add(unitSprite);
        setupAnimations(animationData);

        setCurrentAnimation(AnimationType.IDLE);

        setPickOnBounds(false); //click is detected only if clicked on opaque pixels

        setupStateMachine();
    }
    protected void setupStateMachine(){
        State<Unit> idleState = new IdleState<>();
        State<Unit> movingState = new MovingState<>();

        stateMachine = new StateMachine<>(this, idleState);

        Transition<Unit> t = new Transition<>((unit) -> !unit.hasNoPath(), movingState);
        idleState.addTransition(t);

        Transition<Unit> t2 = new Transition<>(Unit::isFinishedMoving, idleState);
        movingState.addTransition(t2);
    }
    private void setupAnimations(AnimationData[] animationData) {
        animations = new HashMap<>();
        for (int i = 0; i < animationData.length; i++) {
            animations.put(animationData[i].getType(), animationData[i]);
        }
    }
    public void setCurrentAnimation(AnimationType type) {
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
    public void moveTo(double targetX, double targetY) {
        int[] start = mapManager.convertToTileCoordinates(
                new double[]{getCenterX(), getBottomY()}
        );

        int[] end = mapManager.convertToTileCoordinates(
                new double[]{targetX, targetY}
        );
        List<int[]> tilePath = pathfinding.findPath(
                start[0], start[1],
                end[0], end[1]
        );
        path.clear();

        Iterator<int[]> iterator = tilePath.iterator();
        while (iterator.hasNext()) {
            int[] tile = iterator.next();
            double offsetX = Config.TILE_WIDTH / 2.0;
            double offsetY = Config.TILE_HEIGHT / 2.0;

            path.add(new double[]{
                    tile[0] * Config.TILE_WIDTH + offsetX,
                    tile[1] * Config.TILE_HEIGHT + offsetY
            });
        }

        if(!path.isEmpty()){
            path.add(new double[]{targetX, targetY});
        }
        path = smoothPath(path);
        if(!path.isEmpty()){
            pathIndex = 1; //if we start from 0 then unit centers himself in his starting tile
        }
        else{
            System.out.println("No path found");
        }
    }
    public boolean hasLineOfSight(double x1, double y1, double x2, double y2){

        double dx = x2 - x1;
        double dy = y2 - y1;

        int steps = (int)(Math.max(Math.abs(dx), Math.abs(dy)) / 2);

        if(steps == 0){
            return true;
        }

        for(int i = 0; i <= steps; i++){

            double t = i / (double) steps;

            double x = x1 + dx * t;
            double y = y1 + dy * t;

            if(!mapManager.isTraversableAt(x, y)){
                return false;
            }
        }

        return true;
    }
    public List<double[]> smoothPath(List<double[]> path){

        List<double[]> result = new ArrayList<>();

        int i = 0;

        while(i < path.size()-1){

            result.add(path.get(i));

            int j = path.size() - 1;

            for(; j > i; --j){
                if(hasLineOfSight(
                        path.get(i)[0], path.get(i)[1],
                        path.get(j)[0], path.get(j)[1]
                )){
                    break;
                }
            }

            i = j;
        }
        if(!result.isEmpty()){
            result.add(path.getLast());
        }

        return result;
    }

    public void update(double delta) {
        if(isInsideCollidableTile()){
            resolveIfInsideCollidableTile(delta);
        }

        stateMachine.update(delta);
    }
    public boolean isCloseTo(double targetX, double targetY) {

        double dx = targetX - getCenterX();
        double dy = targetY - getBottomY();

        double distanceSquared = dx * dx + dy * dy;

        double threshold = 1;

        return distanceSquared < threshold * threshold;
    }
    public void moveSmooth(double targetX, double targetY, double deltaTime) {
        double dx = targetX - getCenterX();
        double dy = targetY - getBottomY();

        double distance = Math.sqrt(dx * dx + dy * dy);

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
    public boolean isInsideCollidableTile() {
        int[] tile = mapManager.convertToTileCoordinates(
                new double[]{getCenterX(), getBottomY()}
        );
        return !mapManager.isTileTraversable(tile[0], tile[1]);
    }
    public void resolveIfInsideCollidableTile(double deltaTime) {
        int[] tile = mapManager.convertToTileCoordinates(
                new double[]{getCenterX(), getBottomY()}
        );
        int[] newPos = pathfinding.findNearestTraversableTile(tile[0], tile[1]);

        double dx = newPos[0] - tile[0];
        double dy = newPos[1] - tile[1];
        double newX = dx*deltaTime*Config.UNIT_COLLISION_PUSH_SPEED + getCenterX();
        double newY = dy*deltaTime*Config.UNIT_COLLISION_PUSH_SPEED + getBottomY();

        setCenterX(newX);
        setBottomY(newY);
    }
    public void achievedTarget(){
        setCurrentAnimation(AnimationType.IDLE);
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
    public boolean isFinishedMoving(){
        return pathIndex >= path.size();
    }
    public boolean hasNoPath(){
        return path.isEmpty();
    }
}


