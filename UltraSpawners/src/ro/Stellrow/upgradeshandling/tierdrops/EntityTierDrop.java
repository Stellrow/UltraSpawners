package ro.Stellrow.upgradeshandling.tierdrops;


import java.util.HashMap;

public class EntityTierDrop {
    private HashMap<Integer,TierDrop> entityDropList = new HashMap<>();

    public HashMap<Integer, TierDrop> getEntityDropList() {
        return entityDropList;
    }

    public void setEntityDropList(HashMap<Integer, TierDrop> entityDropList) {
        this.entityDropList = entityDropList;
    }
}
