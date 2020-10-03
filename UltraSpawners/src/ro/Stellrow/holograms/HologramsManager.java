package ro.Stellrow.holograms;

import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.utils.SpawnerData;

import java.util.HashMap;

public class HologramsManager {
    private final UltraSpawners pl;
    private HashMap<CreatureSpawner,HologramHolder> activeHolograms = new HashMap<>();

    public HologramsManager(UltraSpawners pl) {
        this.pl = pl;
    }


    public void addHologram(CreatureSpawner spawner, SpawnerData spawnerData){
        if(activeHolograms.containsKey(spawner)){
            return;
        }
        activeHolograms.put(spawner,new HologramHolder(spawner,spawnerData,pl));
    }
    public void removeHologram(CreatureSpawner spawner){
        if(activeHolograms.containsKey(spawner)){
            activeHolograms.get(spawner).delete();
            activeHolograms.remove(spawner);
        }
    }

    public void updateHologram(CreatureSpawner spawner){
        if(activeHolograms.containsKey(spawner)){
            SpawnerData spawnerData = spawner.getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
            activeHolograms.get(spawner).getHologram().clearLines();
            activeHolograms.get(spawner).getHologram().appendTextLine(ChatColor.GREEN+""+spawnerData.getStack()+"X "+
                    ChatColor.GRAY+spawnerData.getType().toString() + ChatColor.GOLD+" Tier "+spawnerData.getTier());

        }
    }
    public void removeAll(){
        for(CreatureSpawner spawner : activeHolograms.keySet()){
            activeHolograms.get(spawner).delete();
            SpawnerData spawnerData = spawner.getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
            spawnerData.setHasHologram(false);
            changeHologramStatus(spawner,spawnerData);
            activeHolograms.remove(spawner);
        }
    }

    //Spawner Utility
    private void changeHologramStatus(CreatureSpawner spawner,SpawnerData spawnerData){
        spawner.getPersistentDataContainer().set(pl.ultraSpawnerKey,pl.persistentSpawnerData,spawnerData);
        spawner.update();
    }


}
