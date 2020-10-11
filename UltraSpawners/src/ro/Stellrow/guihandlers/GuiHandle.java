package ro.Stellrow.guihandlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.Utils;
import ro.Stellrow.utils.EconomyHandler;
import ro.Stellrow.utils.SpawnerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiHandle implements Listener {
    private final UltraSpawners pl;
    private NamespacedKey priceKey;
    private NamespacedKey hologramKey;
    public GuiHandle(UltraSpawners pl) {
        this.pl = pl;
         priceKey = new NamespacedKey(pl,"priceKey");
         hologramKey = new NamespacedKey(pl,"hologramKey");
    }

    private ItemStack type,tier,stack,hologram,filler,upgrade,maxUpgrade;

    private HashMap<Inventory,CreatureSpawner> activeInventories = new HashMap<>();
    private HashMap<CreatureSpawner,GuiAccessor> openedSpawners = new HashMap<>();


    public void init(){
        pl.getServer().getPluginManager().registerEvents(this,pl);

        //Type Itemstack
        type = buildConfigItem("type");

        //Tier ItemStack
        tier = buildConfigItem("tier");

        //Stack ItemStack
        stack = buildConfigItem("stack");

        //Upgrade ItemStack
        upgrade = buildConfigItem("upgrade");

        //MaxUpgrade ItemStack
        maxUpgrade = buildConfigItem("maxUpgrade");

        //Hologram ItemStack
        hologram = buildConfigItem("hologram");


        //Filler used for the rest of the inventory
        Material fillerM;
            try{
                fillerM = Material.valueOf(pl.getConfig().getString("GuiConfig.filling-material"));
                filler = new ItemStack(fillerM);
            }catch (IllegalArgumentException ex){
                filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            }
            ItemMeta fillerMe = filler.getItemMeta();
            fillerMe.setDisplayName(Utils.asColor(pl.getConfig().getString("GuiConfig.filling-name")));
            filler.setItemMeta(fillerMe);



    }

    //Open inventory method for creating a gui using the clicked spawner and the player involved
    //This handle everything including creating and handling events related to the inventory
    public void openInventory(CreatureSpawner spawner, Player player){
        if(openedSpawners.containsValue(spawner)){
            //TODO config message for already opened spawner
            player.sendMessage(pl.messagesHandler.spawner_already_opened);
            return;
        }
        Inventory created = createInventory(spawner,player);
        activeInventories.put(created,spawner);
        openedSpawners.put(spawner,new GuiAccessor(player,created));
    }
    //TODO MAKE BETTER SYSTEM

    private Inventory createInventory(CreatureSpawner spawner,Player p){
        Inventory i = Bukkit.createInventory(null,27,"UltraSpawner");
        SpawnerData spawnerData = spawner.getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
        //11,13,15(Center slots for GUI)
        i.setItem(10,changeValue(type,"%type",spawnerData.getType().toString()));
        i.setItem(12,changeValue(tier,"%tier",spawnerData.getTier()+""));
        i.setItem(14,changeValue(stack,"%stack",spawnerData.getStack()+""));
        i.setItem(16,changeValue(setHologramPDC(hologram),"%hologramStatus",returnHologramStatus(spawnerData.hasHologram())));

        if(pl.getTierUpgradeManager().getEntityUpgrade(spawnerData.getType()).getUpgradeCost(spawnerData.getTier()+1)>=0){
            if(pl.getEconomyHandler().hasEco){


            int cost = pl.getTierUpgradeManager()
                    .getEntityUpgrade(spawnerData.getType())
                    .getUpgradeCost(spawnerData.getTier()+1)*spawnerData.getStack();

           i.setItem(21,setPrice(changeLoreValue(changeLoreValue(upgrade,"%nextTier",spawnerData.getTier()+1+""),"%upgradeCost",
                    cost+""),cost));
            }
        }else{
            i.setItem(21,maxUpgrade);
        }

        if(pl.getConfig().getBoolean("GuiConfig.fill-empty-slots")){
            for(int x = 0;x<27;x++){
                if(i.getItem(x)==null){
                    i.setItem(x,filler);
                }
            }
        }

        p.openInventory(i);
        return i;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(activeInventories.containsKey(event.getInventory())){
            if(event.getCurrentItem()!=null&&event.getCurrentItem().hasItemMeta()){
                if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(priceKey,PersistentDataType.INTEGER)){
                    Player whoClicked = (Player) event.getWhoClicked();
                    int price = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(priceKey,PersistentDataType.INTEGER);
                    if(canUpgrade(whoClicked,price)){
                        SpawnerData spawnerData = activeInventories.get(event.getClickedInventory()).getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                        spawnerData.setTier(spawnerData.getTier()+1);
                        activeInventories.get(event.getClickedInventory()).getPersistentDataContainer().set(pl.ultraSpawnerKey,pl.persistentSpawnerData,spawnerData);
                        activeInventories.get(event.getClickedInventory()).update();
                        whoClicked.sendMessage(pl.messagesHandler.successsfully_upgraded_spawner);
                        pl.getHologramsManager().updateHologram(activeInventories.get(event.getClickedInventory()));
                        whoClicked.closeInventory();
                    }else{
                        whoClicked.sendMessage(pl.messagesHandler.not_enough_money);
                        whoClicked.closeInventory();
                    }
                }
                //Check if hologram item was clicked
                if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(hologramKey,PersistentDataType.STRING)){
                    //Get spawner and spawnerdata
                    CreatureSpawner spawner = activeInventories.get(event.getClickedInventory());
                    SpawnerData spawnerData = spawner.getPersistentDataContainer().get(pl.ultraSpawnerKey,pl.persistentSpawnerData);
                    //Check hologram status
                    if(spawnerData.hasHologram()){
                        //Remove hologram
                        pl.getHologramsManager().removeHologram(spawner);
                        //Change hologram status inside the spawnerData
                        spawnerData.setHasHologram(false);
                        //Set the new spawnerdata to the spawner
                        changeHologramStatus(spawner,spawnerData);
                        //Close inventory of who clicked
                        event.getWhoClicked().closeInventory();
                    }else {
                        pl.getHologramsManager().addHologram(spawner, spawnerData);
                        spawnerData.setHasHologram(true);
                        changeHologramStatus(spawner,spawnerData);
                        event.getWhoClicked().closeInventory();
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(activeInventories.containsKey(event.getInventory())){
            openedSpawners.remove(activeInventories.get(event.getInventory()));
            activeInventories.remove(event.getInventory());
        }

    }
    //Possible break while inventory open check
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(event.getBlock().getType()==Material.SPAWNER){
            CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
            if(spawner.getPersistentDataContainer().has(pl.ultraSpawnerKey,pl.persistentSpawnerData)){
                if(activeInventories.containsValue(spawner)){
                    openedSpawners.get(spawner).getWhoAccesed().sendMessage(pl.messagesHandler.spawner_broken_while_watching);
                    openedSpawners.get(spawner).getWhoAccesed().closeInventory();
                    forceRemove(openedSpawners.get(spawner).getCreatedInventory());
                }
            }
        }
    }


    //Util methods
    private ItemStack buildConfigItem(String path){
        ItemStack toReturn;
        Material defMat;
        try {
            defMat = Material.valueOf(pl.getConfig().getString("GuiConfig."+path+".material"));
            toReturn = new ItemStack(defMat);
            ItemMeta itemMeta = toReturn.getItemMeta();
            itemMeta.setDisplayName(Utils.asColor(pl.getConfig().getString("GuiConfig."+path+".name")));
            itemMeta.setLore(Utils.loreAsColor(pl.getConfig().getStringList("GuiConfig."+path+".lore")));
            toReturn.setItemMeta(itemMeta);
        }catch (IllegalArgumentException ex){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Wound wrong material when creating GUI item: "+ChatColor.GREEN+path);
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Please check the config file at the GuiConfig category!");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[UltraSpawners]Setting default value!");
            ItemStack illegalItem = new ItemStack(Material.BOOK);
            ItemMeta illegalItemMeta = illegalItem.getItemMeta();
            illegalItemMeta.setDisplayName(Utils.asColor("&cWrong material name,please check console!"));
            illegalItem.setItemMeta(illegalItemMeta);
            return illegalItem;
        }

        return toReturn;
    }

    private ItemStack changeValue(ItemStack item,String toReplace,String replaceWith){
        ItemStack toReturn = item.clone();
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(im.getDisplayName().replaceAll(toReplace,replaceWith));
        toReturn.setItemMeta(im);
        return toReturn;
    }
    private ItemStack changeLoreValue(ItemStack item,String toReplace,String replaceWith){
        ItemStack toReturn = item.clone();
        ItemMeta im = item.getItemMeta();
        List<String> currLore = im.getLore();
        List<String> translatedLore = new ArrayList<>();
        for(String s : currLore){
            if(s.contains(toReplace)){
                translatedLore.add(s.replaceAll(toReplace,replaceWith));
            }else {
                translatedLore.add(s);
            }
        }
        im.setLore(translatedLore);
        toReturn.setItemMeta(im);
        return toReturn;
    }
    private ItemStack setPrice(ItemStack item,int price){
        ItemStack toReturn = item.clone();
        ItemMeta im = item.getItemMeta();
        im.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER,price);
        toReturn.setItemMeta(im);
        return toReturn;
    }
    private ItemStack setHologramPDC(ItemStack item){
        ItemStack toReturn = item.clone();
        ItemMeta im = item.getItemMeta();
        im.getPersistentDataContainer().set(hologramKey,PersistentDataType.STRING,"hologramButton");
        toReturn.setItemMeta(im);
        return toReturn;
    }
    private String returnHologramStatus(boolean status){
        if(status){
            return Utils.asColor(pl.getConfig().getString("GuiConfig.hologramStatus.enabled"));
        }
        return Utils.asColor(pl.getConfig().getString("GuiConfig.hologramStatus.disabled"));
    }
    private void forceRemove(Inventory involved){
        if(activeInventories.containsKey(involved)){
            openedSpawners.remove(activeInventories.get(involved));
            activeInventories.remove(involved);
        }
    }

    private boolean canUpgrade(Player whoUpgrades,int price){
        if(EconomyHandler.economy.getBalance(whoUpgrades)>=price){
            EconomyHandler.economy.withdrawPlayer(whoUpgrades,price);
            return true;
        }
        return false;
    }

    //Spawner Utility
    private void changeHologramStatus(CreatureSpawner spawner,SpawnerData spawnerData){
        spawner.getPersistentDataContainer().set(pl.ultraSpawnerKey,pl.persistentSpawnerData,spawnerData);
        spawner.update();
    }










}
