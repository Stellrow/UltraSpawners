package ro.Stellrow.upgradeshandling.tierdrops;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TierDrop {
    private List<ItemStack> tierDrops = new ArrayList<>();

    public List<ItemStack> getTierDrops() {
        return tierDrops;
    }

    public void setTierDrops(List<ItemStack> tierDrops) {
        this.tierDrops = tierDrops;
    }
}
