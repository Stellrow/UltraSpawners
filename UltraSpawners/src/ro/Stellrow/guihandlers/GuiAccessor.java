package ro.Stellrow.guihandlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuiAccessor {
    private Player whoAccesed;
    private Inventory createdInventory;

    public GuiAccessor(Player whoAccesed,Inventory createdInventory){
        this.whoAccesed=whoAccesed;
        this.createdInventory=createdInventory;
    }


    public Player getWhoAccesed() {
        return whoAccesed;
    }
    public Inventory getCreatedInventory() {
        return createdInventory;
    }
}
