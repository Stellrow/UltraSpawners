package ro.Stellrow.itemhandling;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.Utils;
import ro.Stellrow.utils.SpawnerData;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {
    private UltraSpawners pl;

    public ItemHandler(UltraSpawners pl) {
        this.pl = pl;
    }


    //Main method to build a spawner ItemStack
    public ItemStack getSpawner(EntityType type,int tier,int amount){
        ItemStack i = new ItemStack(Material.SPAWNER,amount);
        ItemMeta im = i.getItemMeta();
        String name = Utils.asColor(pl.getConfig().getString("ItemConfig.general-name-format"));
        im.setDisplayName(name.replaceAll("%type",type.toString()).replaceAll("%tier",tier+""));

        SpawnerData spawnerData = new SpawnerData(type,tier,1);
        im.getPersistentDataContainer().set(pl.ultraSpawnerKey, pl.persistentSpawnerData,spawnerData);
        im.setLore(Utils.loreAsColor(pl.getConfig().getStringList("ItemConfig.lore")));
        i.setItemMeta(im);
        return i;
    }


    //Method used to return a placed spawner based off the stack amount
    public List<ItemStack> getSpawner(CreatureSpawner creatureSpawner){
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack i = new ItemStack(Material.SPAWNER);
        ItemMeta im = i.getItemMeta();
        PersistentDataContainer spawnerPDC = creatureSpawner.getPersistentDataContainer();
        SpawnerData spawnerData = spawnerPDC.get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
        spawnerData.setHasHologram(false);
        EntityType type = spawnerData.getType();
        int stack = spawnerData.getStack();
        spawnerData.setStack(1);

        im.getPersistentDataContainer().set(pl.ultraSpawnerKey,pl.persistentSpawnerData,spawnerData);

        String name = Utils.asColor(pl.getConfig().getString("ItemConfig.general-name-format"));
        im.setDisplayName(name.replaceAll("%type",type.toString()).replaceAll("%tier",spawnerData.getTier()+""));
        im.setLore(Utils.loreAsColor(pl.getConfig().getStringList("ItemConfig.lore")));

        i.setItemMeta(im);
        if(stack>64) {
            while (stack > 64) {
                stack -= 64;
                i.setAmount(64);
                itemStacks.add(i.clone());
            }
            i.setAmount(stack);
            itemStacks.add(i.clone());
        }else{
            i.setAmount(stack);
            itemStacks.add(i);
            return itemStacks;
        }


        return itemStacks;
    }

}
