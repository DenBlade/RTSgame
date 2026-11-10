package com.example.rtsgame.actions;

import java.util.List;

public interface ActionProvider {
    List<Action> getActions();
    boolean isSelected();
    void setSelected(boolean selected);
}
