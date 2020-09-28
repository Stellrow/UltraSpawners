package ro.Stellrow.relatedevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpawnerEntityKill implements Listener {
    private final UltraSpawners pl;
    public SpawnerEntityKill(UltraSpawners pl) {
        this.pl = pl;
    }


    @EventHandler
    public void onEntityKill(EntityDeathEvent event){
        if(event.getEntityType()== EntityType.PLAYER){
            return;
        }
        Entity involved = event.getEntity();
        if(involved.getCustomName()!=null){
            String name = involved.getCustomName();
            String[] raw = name.split(" ");
            if(raw[2].equalsIgnoreCase(Utils.asColor("&7Tier"))){
                int tier = 1;
                int stack = 1;
                try{
                    tier = Integer.parseInt(ChatColor.stripColor(raw[3]));
                    stack = Integer.parseInt(ChatColor.stripColor(raw[5].split("x")[1]));
                }catch (IllegalArgumentException ex){}
                List<ItemStack> toDrop = pl.getTierDropManager().returnDropForTier(event.getEntityType(),tier);
                if(toDrop.isEmpty()){
                    List<ItemStack> currentDrops = new ArrayList<>();
                    for(ItemStack drop : event.getDrops()){
                        currentDrops.add(drop);
                    }
                    event.getDrops().clear();
                    for(int x = 0;x<stack;x++){
                        for(ItemStack drop : currentDrops){
                            dropItem(drop,involved.getLocation());
                        }
                    }
                    event.setDroppedExp(event.getDroppedExp()*stack);
                    return;
            }
                event.setDroppedExp(event.getDroppedExp()*stack);
                event.getDrops().clear();
                for(int x = 0;x<stack;x++) {
                    for (ItemStack i : toDrop) {
                        event.getDrops().add(i);
                    }
                }


            }
        }
    }


    private void dropItem(ItemStack toDrop, Location dropLocation){
        dropLocation.getWorld().dropItemNaturally(dropLocation,toDrop);
    }


}
