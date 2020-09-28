package ro.Stellrow.relatedevents;


import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import ro.Stellrow.UltraSpawners;

public class UseSpawnerEvent implements Listener {
    private final UltraSpawners pl;

    public UseSpawnerEvent(UltraSpawners pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event){
        if(event.getAction()== Action.RIGHT_CLICK_BLOCK) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (event.getClickedBlock().getType() == Material.SPAWNER) {
                    CreatureSpawner spawner = (CreatureSpawner) event.getClickedBlock().getState();
                    if (!spawner.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)) {
                        return;
                    }
                    pl.getGuiHandle().openInventory(spawner, event.getPlayer());
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
