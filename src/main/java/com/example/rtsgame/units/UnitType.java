package com.example.rtsgame.units;

import com.example.rtsgame.Config;
import com.example.rtsgame.ResourceAmount;
import com.example.rtsgame.ResourceType;

public enum UnitType {
    SWORDSMAN(new ResourceAmount(ResourceType.GOLD, Config.SWORDSMAN_GOLD_PRICE));

    private final ResourceAmount resourceAmount;
    UnitType(ResourceAmount resourceAmount){
        this.resourceAmount = resourceAmount;
    }

    public ResourceAmount getResourceAmount() {
        return resourceAmount;
    }
}
