package ro.Stellrow;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import ro.Stellrow.commands.UltraSpawnerCommandManager;
import ro.Stellrow.guihandlers.GuiHandle;
import ro.Stellrow.holograms.HologramsManager;
import ro.Stellrow.itemhandling.ItemHandler;
import ro.Stellrow.relatedevents.UltraSpawnerEventsManager;
import ro.Stellrow.upgradeshandling.TierDropManager;
import ro.Stellrow.upgradeshandling.TierUpgradeManager;
import ro.Stellrow.utils.*;

public class UltraSpawners extends JavaPlugin {

    //Nsk Identifiers
    public NamespacedKey ultraSpawnerKey = new NamespacedKey(this,"ultraspawner");






    //Item Handler
    private ItemHandler itemHandler = new ItemHandler(this);
    //Events Manager
    private UltraSpawnerEventsManager eventsManager = new UltraSpawnerEventsManager(this);
    //Commands
    private UltraSpawnerCommandManager commandManager = new UltraSpawnerCommandManager(this);
    //GuiHandler
    private GuiHandle guiHandle = new GuiHandle(this);


    //Custom Configs
    private CustomConfig messageConfig = new CustomConfig("messages",this);
    private CustomConfig dropsConfig = new CustomConfig("drops",this);
    private CustomConfig upgradesConfig = new CustomConfig("upgrades",this);
    //Message Handler
    public MessagesHandler messagesHandler;
    //TierDrops Manager
    private TierDropManager tierDropManager = new TierDropManager(dropsConfig);
    //Upgrades Manager
    private TierUpgradeManager tierUpgradeManager = new TierUpgradeManager(upgradesConfig);
    //Holograms Manager
    private HologramsManager hologramsManager = new HologramsManager(this);

    //Handlers
    private SpawnerStackingHandler spawnerStackingHandler;
    private EconomyHandler economyHandler = new EconomyHandler(this);

    public PersistentSpawnerData persistentSpawnerData = new PersistentSpawnerData();


    public void onEnable(){
        loadConfig();


        eventsManager.init();
        commandManager.init();
        guiHandle.init();
        economyHandler.init();


        messagesHandler = new MessagesHandler(messageConfig);
        messagesHandler.reload();
        tierDropManager.buildDrops();
        tierUpgradeManager.buildUpgrades();

        //Stacking handling
        new EntityStackingHandler(this,
                getConfig().getInt("GeneralConfig.max-stack-size",200),
                getConfig().getBoolean("GeneralConfig.keep-stacking",true)
                ).startRunnable();
        spawnerStackingHandler=new SpawnerStackingHandler(this,getConfig().getBoolean("GeneralConfig.chunk-spawner-stacking",true));
    }

    public void onDisable(){
        hologramsManager.removeAll();
    }

    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }





    public ItemHandler getItemHandler() {
        return itemHandler;
    }
    public GuiHandle getGuiHandle(){return guiHandle;}
    public TierUpgradeManager getTierUpgradeManager() {
        return tierUpgradeManager;
    }
    public TierDropManager getTierDropManager(){return tierDropManager;}

    public SpawnerStackingHandler getSpawnerStackingHandler() {
        return spawnerStackingHandler;
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }
    public HologramsManager getHologramsManager() {
        return hologramsManager;
    }
}
