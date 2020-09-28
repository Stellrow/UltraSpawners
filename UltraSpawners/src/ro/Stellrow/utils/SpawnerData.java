package ro.Stellrow.utils;

import org.bukkit.entity.EntityType;

import java.io.Serializable;

public class SpawnerData implements Serializable {
    private int tier;
    private int stack;
    private EntityType type;

    public SpawnerData(EntityType type,int tier,int stack){
        this.tier=tier;
        this.stack=stack;
        this.type=type;
    }
    public SpawnerData(String type,int tier,int stack){
        this.tier=tier;
        this.stack=stack;
        try{
            this.type=EntityType.valueOf(type);
        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Wrong EntityType specified at SpawnerData resuming to default");
        }
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
}
