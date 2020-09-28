package ro.Stellrow.upgradeshandling.upgrades;

import java.util.HashMap;

public class EntityTierUpgrade {

    private HashMap<Integer,Integer> upgradeCost = new HashMap<>();

    public void addUpgradeCost(int tier,int price){
        upgradeCost.put(tier,price);
    }

    public int getUpgradeCost(int tier){
        if(upgradeCost.containsKey(tier)){
            return upgradeCost.get(tier);
        }
        return -1;
    }

}
