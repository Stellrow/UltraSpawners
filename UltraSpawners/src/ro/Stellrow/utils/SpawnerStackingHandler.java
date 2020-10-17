package ro.Stellrow.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ro.Stellrow.UltraSpawners;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class SpawnerStackingHandler {
    private final UltraSpawners pl;
    private final boolean chunk_stacking;

    public SpawnerStackingHandler(UltraSpawners pl, boolean chunk_stacking) {
        this.pl = pl;
        this.chunk_stacking = chunk_stacking;
    }


    public void handleSpawnerStacking(@Nullable ItemStack spawnerInvolved,@Nullable Block spawnerBeingPlaced){
        if(chunk_stacking){
            checkSameSpawnerInChunk(spawnerBeingPlaced);
            return;
        }
        if(hasSameSpawnerNearby(spawnerInvolved,spawnerBeingPlaced.getLocation())){
            setToAir(spawnerBeingPlaced);
        }
    }

    
    private void checkSameSpawnerInChunk(Block spawnerBeingPlaced){
        List<BlockState> tiles = Arrays.asList(spawnerBeingPlaced.getChunk().getTileEntities());
        for(BlockState state : tiles){
            if(state.getType()==Material.SPAWNER){
                CreatureSpawner spawner = (CreatureSpawner) state;
                if(spawner.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)){
                    if(checkMerge(spawner.getBlock(),spawnerBeingPlaced)){
                        break;
                    }
                }
            }
        }
            }
    private boolean checkMerge(Block placedSpawner, Block beingPlaced){
                CreatureSpawner cs = (CreatureSpawner) placedSpawner.getState();
                if (cs.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)) {
                    SpawnerData placedSpawnerData = cs.getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                    SpawnerData beingPlacedData = ((CreatureSpawner)beingPlaced.getState()).getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                    if(placedSpawnerData.getType()!=beingPlacedData.getType()){
                        return false;
                    }
                    if(placedSpawnerData.getTier()==beingPlacedData.getTier()){
                        if(placedSpawnerData.getStack()<pl.getConfig().getInt("GeneralConfig.max-spawner-stack")){
                        placedSpawnerData.setStack(placedSpawnerData.getStack()+1);
                        cs.getPersistentDataContainer().set(pl.ultraSpawnerKey,pl.persistentSpawnerData,placedSpawnerData);
                        cs.update();
                        pl.getHologramsManager().updateHologram(cs);
                        setToAir(beingPlaced);
                        return true;
                        }
                    }
                }
                return false;
    }
    private void setToAir(Block b){
        Bukkit.getScheduler().runTaskLater(pl,()->{
            b.setType(Material.AIR);
        },1);
    }


    //Proximity stacking
    private boolean hasSameSpawnerNearby(ItemStack spawner, Location blockLocation){
        Location min = new Location(blockLocation.getWorld(),blockLocation.getX()-2,blockLocation.getY()-2,blockLocation.getZ()-2);
        Location max = new Location(blockLocation.getWorld(),blockLocation.getX()+2,blockLocation.getY()+2,blockLocation.getZ()+2);
        //loop in a radius of 2 around the center
        for (int x = (int) min.getX(); x <= (int) max.getX(); x++) {
            for (int z = (int) min.getZ(); z <= (int) max.getZ(); z++) {
                for(int y = (int)min.getY();y<=(int)max.getY();y++) {
                    Location toCheck = new Location(blockLocation.getWorld(), x, y, z);
                    if (!toCheck.equals(blockLocation)) {
                        if (toCheck.getBlock().getType() == Material.SPAWNER) {
                            CreatureSpawner cs = (CreatureSpawner) toCheck.getBlock().getState();
                            if (cs.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)) {
                                SpawnerData toCheckData = cs.getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                                SpawnerData itemData = spawner.getItemMeta().getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                                if (toCheckData.getType()==itemData.getType()) {
                                    if(toCheckData.getTier()== itemData.getTier()) {
                                        if(toCheckData.getStack()<pl.getConfig().getInt("GeneralConfig.max-spawner-stack")) {
                                            toCheckData.setStack(toCheckData.getStack() + 1);
                                            cs.getPersistentDataContainer().set(pl.ultraSpawnerKey, pl.persistentSpawnerData, toCheckData);
                                            cs.update();
                                            pl.getHologramsManager().updateHologram(cs);
                                            return true;
                                        }
                                    }
                                    //if(shouldItSpawnHologram(newSet[4])) {
                                    //    pl.hm.tryUpdateHologram(toCheck.getBlock(), newSet[1], Integer.parseInt(ChatColor.stripColor(newSet[2])), Integer.parseInt(ChatColor.stripColor(newSet[3])));
                                    //}
                                }
                            }
                        }

                    }
                }
            }
        }
        return false;
    }

}
