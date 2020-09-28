package ro.Stellrow.upgradeshandling;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ro.Stellrow.upgradeshandling.tierdrops.EntityTierDrop;
import ro.Stellrow.upgradeshandling.tierdrops.TierDrop;
import ro.Stellrow.utils.CustomConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TierDropManager {

    private CustomConfig dropsConfig;
    private boolean useTierDrops = true;
    public TierDropManager(CustomConfig dropsConfig){
        this.dropsConfig=dropsConfig;
    }

    private HashMap<EntityType, EntityTierDrop> entityDrops = new HashMap<>();

    public void buildDrops(){
        entityDrops.clear();
        FileConfiguration config = dropsConfig.getConfig();
        if(!config.contains("Drops")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]No drops entry could be found in the config");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Please make sure your drops.yml is not empty");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Or if you dont want to use this feature ignore this!");
            useTierDrops=false;
            return;
        }
        for(String entity : config.getConfigurationSection("Drops").getKeys(false)){
            try{
                EntityType type = EntityType.valueOf(entity);
                entityDrops.put(type,buildEntityTierDrop(entity));
            }catch (IllegalArgumentException ex){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Found wrong EntityType name in drops.yml!");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]While this will only get skipped please");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]recheck the file for possible mistakes");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]The error occured while trying to load "+ChatColor.GREEN+entity);
            }
        }

    }

    private EntityTierDrop buildEntityTierDrop(String configPath){
        EntityTierDrop entityTierDrop = new EntityTierDrop();
        HashMap<Integer, TierDrop> entityDropList = new HashMap<>();
        for(String tier : dropsConfig.getConfig().getConfigurationSection("Drops."+configPath).getKeys(false)){
            try{
                int tierNumber = Integer.parseInt(tier);
                entityDropList.put(tierNumber,buildTierDrop(dropsConfig.getConfig().getStringList("Drops."+configPath+"."+tier)));
            }catch (IllegalArgumentException ex){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Found wrong tier number in drops.yml!");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]While this will only get skipped please");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]recheck the file for possible mistakes");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]The error occured while trying to load "+ChatColor.GREEN+configPath+
                        ChatColor.RED+ " at tier number: "+ChatColor.GREEN+tier);
            }
        }
        entityTierDrop.setEntityDropList(entityDropList);
        return entityTierDrop;
    }
    private TierDrop buildTierDrop(List<String> materials){
        TierDrop tierDrop = new TierDrop();
        List<ItemStack> drops = new ArrayList<>();
        for(String mat : materials){
            try{
                drops.add(new ItemStack(Material.valueOf(mat)));
            }catch (IllegalArgumentException ex){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Found wrong material in drops.yml!"+ChatColor.GREEN+" "+mat);
            }
        }
        tierDrop.setTierDrops(drops);
        return tierDrop;
    }

    public List<ItemStack> returnDropForTier(EntityType type,int tier){
        if(entityDrops.containsKey(type)) {
            return entityDrops.get(type).getEntityDropList().get(tier).getTierDrops();
        }
        return Collections.emptyList();
    }




}
