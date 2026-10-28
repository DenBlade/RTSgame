package com.example.rtsgame;

import javafx.scene.Node;

import java.util.List;

public interface UIOptionBarHolder {
    List<Node> getUI(GameWorld gameWorld);
    boolean isSelected();
    void setSelected(boolean selected);
}
