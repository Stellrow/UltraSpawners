package ro.Stellrow.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.utils.SpawnerData;

public class HologramHolder {
    private final CreatureSpawner spawner;
    private Hologram hologram;


    public HologramHolder(CreatureSpawner spawner, SpawnerData spawnerData, UltraSpawners pl) {
        this.spawner = spawner;
        hologram = HologramsAPI.createHologram(pl,spawner.getLocation().add(0.5,1.5,0.5));
        hologram.appendTextLine(ChatColor.GREEN+""+spawnerData.getStack()+"X "+ ChatColor.GRAY+spawnerData.getType().toString() + ChatColor.GOLD+" Tier "+spawnerData.getTier());
    }

    public Hologram getHologram() {
        return hologram;
    }
    public void delete(){
        hologram.delete();
    }
}
