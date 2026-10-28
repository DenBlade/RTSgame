package com.example.rtsgame;

public interface Damageable {
    void takeDamage(int damage);
    boolean ownByAi();
    boolean isDestroyed();
    double getCenterX();
    double getCenterY();

    default double getDistanceTo(double fromX, double fromY) {
        double dx = getCenterX() - fromX;
        double dy = getCenterY() - fromY;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
