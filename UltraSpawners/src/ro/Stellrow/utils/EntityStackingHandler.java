package ro.Stellrow.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.Utils;

public class EntityStackingHandler {
    private final UltraSpawners pl;
    private int maxStackSize;
    private boolean keepStacking;

    public EntityStackingHandler(UltraSpawners pl,int maxStackSize,boolean keepStacking) {
        this.pl = pl;
        this.maxStackSize=maxStackSize;
        this.keepStacking=keepStacking;
    }


    public void startRunnable(){
        //Check nearby same entities
        new BukkitRunnable(){

            @Override
            public void run() {
                if(!keepStacking){
                    return;
                }
                for(World wr : Bukkit.getWorlds()){
                    for(Entity e : wr.getEntities()){
                        if(e.getCustomName()!=null){
                            String name = e.getCustomName();
                            String[] rawName = name.split(" ");
                            if(rawName[2].equalsIgnoreCase(Utils.asColor("&7Tier"))){
                                try {
                                    int nr = Integer.parseInt(ChatColor.stripColor(rawName[5].split("x")[1]));
                                    if (nr < maxStackSize) {
                                        EntityType type = EntityType.valueOf(ChatColor.stripColor(rawName[0]));
                                        int tier = Integer.parseInt(ChatColor.stripColor(rawName[3]));
                                        for (Entity nearby : e.getNearbyEntities(5, 5, 5)) {
                                            if (nearby.getCustomName() != null) {

                                                String nearbyEntityName = nearby.getName();
                                                String[] rawNearbyEntityName = nearbyEntityName.split(" ");

                                                int nearbyEntityNumber = Integer.parseInt(ChatColor.stripColor(rawNearbyEntityName[5].split("x")[1]));
                                                EntityType nearbyEntityType = EntityType.valueOf(ChatColor.stripColor(rawNearbyEntityName[0]));
                                                int nearbyEntityTier = Integer.parseInt(ChatColor.stripColor(rawNearbyEntityName[3]));
                                                if (nearbyEntityNumber < maxStackSize) {
                                                    if (type==nearbyEntityType) {
                                                        if (tier == nearbyEntityTier) {
                                                            morphEntity(e, nearby, type, nr, nearbyEntityNumber, tier);
                                                            return;
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }

                                }catch (IllegalArgumentException ex){}

                            }




                        }
                    }
                }

            }
        }.runTaskTimer(pl,0,5*20);
    }
    //spawned.setCustomName(Utils.asColor("&7"+spawnerData.getType().toString()+" &8| &7Tier &a"+spawnerData.getTier()+" &8| &7x"+spawnerData.getStack()));
    private void morphEntity(Entity toMorph,Entity result,EntityType type,int toMorphNumber,int secondMorphNumber,int tier){
        if(toMorphNumber+secondMorphNumber>maxStackSize){
            if(toMorph.isDead()||result.isDead()){
                return;
            }

            Integer remaining = toMorphNumber+secondMorphNumber-maxStackSize;
            toMorph.setCustomName(Utils.asColor("&7"+type.toString()+" &8| &7Tier &a"+tier+" &8| &7x"+maxStackSize));
            result.setCustomName(Utils.asColor("&7"+type.toString()+" &8| &7Tier &a"+tier+" &8| &7x"+remaining));
            return;
        }
        if(toMorph.isDead()||result.isDead()){
            return;
        }
        toMorph.remove();
        Integer remaining = toMorphNumber+secondMorphNumber;
        result.setCustomName(Utils.asColor("&7"+type.toString()+" &8| &7Tier &a"+tier+" &8| &7x"+remaining));
        return;
    }
}
