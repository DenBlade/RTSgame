package com.example.rtsgame;

import com.example.rtsgame.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionManager {
    private List<Unit> playerUnits;
    public SelectionManager(List<Unit> playerUnits) {
        this.playerUnits = playerUnits;
    }
    public List<Unit> getSelectedPlayerUnits() {
        return playerUnits.stream().filter(Unit -> Unit.isSelected()).collect(Collectors.toList());
    }
}
