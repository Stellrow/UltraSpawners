package ro.Stellrow.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import ro.Stellrow.UltraSpawners;

public class EconomyHandler {
    private final UltraSpawners pl;
    private boolean hasEco;
    public static Economy economy = null;


    public EconomyHandler(UltraSpawners pl) {
        this.pl = pl;
    }
    public void init(){
        findVault();
    }

    private void findVault() {
        if(pl.getServer().getPluginManager().getPlugin("Vault")!=null&&pl.getServer().getPluginManager().isPluginEnabled("Vault")) {
            setupEconomy();
            return;
        }
        pl.getServer().getConsoleSender().sendMessage("[UltraSpawners]"+ChatColor.RED+" Vault wasnt found! Level up using money feature is disabled!");
        return;
    }
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = pl.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            hasEco=true;
        }
        return (economy != null);
    }
}
