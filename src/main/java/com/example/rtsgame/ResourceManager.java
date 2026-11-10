package com.example.rtsgame;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.EnumMap;
import java.util.Map;

public class ResourceManager {
    private final Map<ResourceType, IntegerProperty> resources = new EnumMap<>(ResourceType.class);

    public ResourceManager() {
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, new SimpleIntegerProperty(0));
        }
    }

    public int getAmount(ResourceType type) {
        return resources.get(type).get();
    }

    public ReadOnlyIntegerProperty amountProperty(ResourceType type) {
        return resources.get(type);
    }

    public void addResource(ResourceType type, int amount) {
        if (amount <= 0) return;
        IntegerProperty prop = resources.get(type);
        prop.set(prop.get() + amount);
    }
    public void addResource(ResourceAmount amount){
        ResourceType type = amount.resourceType();
        int resourceAmount = amount.amount();
        addResource(type, resourceAmount);
    }

    public boolean hasEnough(ResourceType type, int amount) {
        return getAmount(type) >= amount;
    }
    public boolean hasEnough(ResourceAmount amount){
        ResourceType type = amount.resourceType();
        int resourceAmount = amount.amount();
        return hasEnough(type, resourceAmount);
    }

    public boolean trySpend(ResourceType type, int amount) {
        if (!hasEnough(type, amount)) {
            return false;
        }
        IntegerProperty prop = resources.get(type);
        prop.set(prop.get() - amount);
        return true;
    }
    public boolean trySpend(ResourceAmount amount){
        ResourceType type = amount.resourceType();
        int resourceAmount = amount.amount();
        return trySpend(type, resourceAmount);
    }

}