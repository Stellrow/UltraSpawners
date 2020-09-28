package ro.Stellrow.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.scheduler.BukkitRunnable;
import ro.Stellrow.UltraSpawners;

public class SpawnerStackingHandler {
    private final UltraSpawners pl;
    private final boolean chunk_stacking;

    public SpawnerStackingHandler(UltraSpawners pl, boolean chunk_stacking) {
        this.pl = pl;
        this.chunk_stacking = chunk_stacking;
    }

    public void checkSameSpawnerInChunk(Block spawnerBeingPlaced){
        new BukkitRunnable(){
            ChunkSnapshot targetChunk = spawnerBeingPlaced.getChunk().getChunkSnapshot();
            @Override
            public void run() {
                for(int x = 0;x<16;x++){
                    for(int z = 0;z<16;z++){
                        for(int y = 0;y<256;y++){
                            if(targetChunk.getBlockType(x,y,z)== Material.SPAWNER){
                                if(spawnerBeingPlaced.getChunk().getBlock(x,y,z).equals(spawnerBeingPlaced)){
                                    return;
                                }
                                    checkMerge(spawnerBeingPlaced.getChunk().getBlock(x, y, z), spawnerBeingPlaced);

                            }
                        }
                    }

                }
                this.cancel();

            }
        }.runTaskAsynchronously(pl);
    }
    private void checkMerge(Block placedSpawner, Block beingPlaced){
        new BukkitRunnable(){
            @Override
            public void run() {
                CreatureSpawner cs = (CreatureSpawner) placedSpawner.getState();
                if (cs.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)) {
                    SpawnerData placedSpawnerData = cs.getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                    SpawnerData beingPlacedData = ((CreatureSpawner)beingPlaced.getState()).getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                    if(placedSpawnerData.getType()!=beingPlacedData.getType()){
                        return;
                    }
                    if(placedSpawnerData.getTier()==beingPlacedData.getTier()){
                        placedSpawnerData.setStack(placedSpawnerData.getStack()+1);
                        cs.getPersistentDataContainer().set(pl.ultraSpawnerKey,pl.persistentSpawnerData,placedSpawnerData);
                        cs.update();
                        setToAir(beingPlaced);
                    }

                }
            }
        }.runTask(pl);
    }
    private void setToAir(Block b){
        Bukkit.getScheduler().runTaskLater(pl,()->{
            b.setType(Material.AIR);
        },1);
    }

}
