package ro.Stellrow.utils;

import org.bukkit.entity.EntityType;

import java.io.Serializable;

public class SpawnerData implements Serializable {
    private int tier;
    private int stack;
    private EntityType type;
    private boolean hasHologram = false;

    public SpawnerData(EntityType type,int tier,int stack){
        this.tier=tier;
        this.stack=stack;
        this.type=type;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public EntityType getType() {
        return type;
    }

    public boolean hasHologram() {
        return hasHologram;
    }

    public void setHasHologram(boolean hasHologram) {
        this.hasHologram = hasHologram;
    }
}
