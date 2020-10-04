package ro.Stellrow.relatedevents;

import ro.Stellrow.UltraSpawners;

public class UltraSpawnerEventsManager {
    private UltraSpawners pl;

    public UltraSpawnerEventsManager(UltraSpawners pl) {
        this.pl = pl;
    }

    public void init(){
    pl.getServer().getPluginManager().registerEvents(new PlacingBreakingEvents(pl),pl);
    pl.getServer().getPluginManager().registerEvents(new UseSpawnerEvent(pl),pl);
    pl.getServer().getPluginManager().registerEvents(new SpawnerActivate(pl,pl.getConfig().getBoolean("GeneralConfig.kill-on-hit",true)),pl);
    pl.getServer().getPluginManager().registerEvents(new SpawnerEntityKill(pl),pl);
    pl.getServer().getPluginManager().registerEvents(pl.getHologramsManager(),pl);
    }
}
