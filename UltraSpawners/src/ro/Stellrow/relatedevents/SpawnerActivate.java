package ro.Stellrow.relatedevents;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.Utils;
import ro.Stellrow.utils.SpawnerData;

public class SpawnerActivate implements Listener {
    private final UltraSpawners pl;
    private final boolean one_hit;
    public SpawnerActivate(UltraSpawners pl,boolean one_hit) {
        this.pl = pl;
        this.one_hit=one_hit;
    }


    @EventHandler
    public void onSpawn(SpawnerSpawnEvent event){
        CreatureSpawner spawner = event.getSpawner();
        if(spawner.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)) {
            SpawnerData spawnerData = spawner.getPersistentDataContainer().get(pl.ultraSpawnerKey, pl.persistentSpawnerData);
            spawner.setSpawnCount(1);
            spawner.update();
            Entity spawned = event.getEntity();
            spawned.setCustomNameVisible(true);
            spawned.setCustomName(Utils.asColor("&7"+spawnerData.getType().toString()+" &8| &7Tier &a"+spawnerData.getTier()+" &8| &7x"+spawnerData.getStack()));
            if(one_hit){
                ((LivingEntity)spawned).setHealth(1.0);
            }
        }
    }
}
