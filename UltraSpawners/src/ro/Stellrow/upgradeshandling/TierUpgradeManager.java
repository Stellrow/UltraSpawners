package ro.Stellrow.upgradeshandling;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import ro.Stellrow.upgradeshandling.upgrades.EntityTierUpgrade;
import ro.Stellrow.utils.CustomConfig;

import java.util.HashMap;

public class TierUpgradeManager {
    private final CustomConfig upgradesConfig;
    public TierUpgradeManager(CustomConfig upgradesConfig) {
        this.upgradesConfig = upgradesConfig;
    }

    private HashMap<EntityType, EntityTierUpgrade> entityUpgrades = new HashMap<>();

    public void buildUpgrades(){
        entityUpgrades.clear();
        FileConfiguration config = upgradesConfig.getConfig();
        if(config.contains("Upgrades")){
            for(String type : config.getConfigurationSection("Upgrades").getKeys(false)){
                EntityTierUpgrade entityTierUpgrade = new EntityTierUpgrade();
                try{
                    EntityType entityType = EntityType.valueOf(type.toUpperCase());
                    for(String entityTier : config.getConfigurationSection("Upgrades."+type).getKeys(false)){
                        try{
                            Integer tier = Integer.parseInt(entityTier);
                            Integer upgradeCost = config.getInt("Upgrades."+type+"."+entityTier);
                            entityTierUpgrade.addUpgradeCost(tier,upgradeCost);

                        }catch (IllegalArgumentException ex){
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Found wrong tier number in upgrades.yml!");
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]While this will only get skipped please");
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]recheck the file for possible mistakes");
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]The error occured while trying to load "+ChatColor.GREEN+type+ChatColor.RED+
                                    " at tier: "+ChatColor.GREEN+entityTier
                                    );
                        }
                    }
                    entityUpgrades.put(entityType,entityTierUpgrade);
                }catch (IllegalArgumentException ex){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Found wrong Entity Name in upgrades.yml!");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]While this will only get skipped please");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]recheck the file for possible mistakes");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]The error occured while trying to load "+ChatColor.GREEN+type);
                }
            }
        }

    }

    public EntityTierUpgrade getEntityUpgrade(EntityType type){
        if(entityUpgrades.containsKey(type)){
            return entityUpgrades.get(type);
        }
        return new EntityTierUpgrade();
    }




}
