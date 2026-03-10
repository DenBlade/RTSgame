package com.example.rtsgame.units;

import javafx.animation.Timeline;

public class AnimationTimeline {
    private Timeline timeline;
    private AnimationData animationData;
    public AnimationTimeline(AnimationData animationType, Timeline timeline) {
        this.animationData = animationType;
        this.timeline = timeline;
    }

    public AnimationData getAnimationData() {
        return animationData;
    }
    public Timeline getTimeline() {
        return timeline;
    }

    public void setAnimationData(AnimationData animationData) {
        this.animationData = animationData;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }
}
