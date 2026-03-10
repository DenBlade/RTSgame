package com.example.rtsgame.units;

public class AnimationData {
    final int row;
    final int spritesCount;
    final AnimationType type;
    public AnimationData(AnimationType type, final int row, final int spritesCount) {
        this.type = type;
        this.row = row;
        this.spritesCount = spritesCount;
    }

    public int getRow() {
        return row;
    }

    public int getSpritesCount() {
        return spritesCount;
    }

    public AnimationType getType() {
        return type;
    }
}
