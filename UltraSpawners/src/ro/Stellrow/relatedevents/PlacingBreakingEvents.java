package ro.Stellrow.relatedevents;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.utils.SpawnerData;

public class PlacingBreakingEvents implements Listener {
    private UltraSpawners pl;
    public PlacingBreakingEvents(UltraSpawners pl) {
        this.pl=pl;
    }


    //Placing a spawner
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(event.getItemInHand().getType()!= Material.SPAWNER){
            return;
        }
        if(event.getItemInHand().hasItemMeta()&&event.getItemInHand().getItemMeta().getPersistentDataContainer().has(pl.ultraSpawnerKey, pl.persistentSpawnerData)){
            PersistentDataContainer itemPdc = event.getItemInHand().getItemMeta().getPersistentDataContainer();
            CreatureSpawner spawner = (CreatureSpawner) event.getBlockPlaced().getState();
            PersistentDataContainer pdc = spawner.getPersistentDataContainer();
            SpawnerData spawnerData = itemPdc.get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
            spawnerData.setHasHologram(false);
            spawner.setSpawnedType(spawnerData.getType());
            pdc.set(pl.ultraSpawnerKey,pl.persistentSpawnerData,spawnerData);
            spawner.update();
            pl.getSpawnerStackingHandler().handleSpawnerStacking(event.getItemInHand(),event.getBlockPlaced());
        }
    }

    //Breaking a spawner
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(event.getBlock().getType()!=Material.SPAWNER){
            return;
        }
        CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
        if(creatureSpawner.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)){
            pl.getHologramsManager().removeHologram(creatureSpawner);
            event.setDropItems(false);
            for(ItemStack spawner : pl.getItemHandler().getSpawner(creatureSpawner)){
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),spawner);
            }
        }

    }


}
