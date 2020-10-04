package ro.Stellrow.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ro.Stellrow.UltraSpawners;
import ro.Stellrow.Utils;

public class UltraSpawnerCommandManager implements CommandExecutor {
    private UltraSpawners pl;
    public UltraSpawnerCommandManager(UltraSpawners pl) {
        this.pl = pl;
    }


    public void init(){
        pl.getCommand("ultraspawners").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String sa, String[] args) {
        if(args.length==1&&args[0].equalsIgnoreCase("debug")){
            sender.sendMessage(pl.getHologramsManager().getActiveHolograms().size()+" <Size");
            return true;
        }
        if(args.length==5&&args[0].equalsIgnoreCase("give")){
            if(sender.hasPermission("ultraspawners.give")){

                //Target check
                Player target = Bukkit.getPlayer(args[1]);
                if(target==null){sender.sendMessage(Utils.asColor("&cPlayer offline or not existing!"));
                return true;
                }

                //EntityType check
                EntityType type;
                try{
                    type = EntityType.valueOf(args[2].toUpperCase());
                }catch (IllegalArgumentException ex){
                    sender.sendMessage(Utils.asColor("&cWrong EntityType!"));
                    return true;
                }

                //Tier check
                int tier;
                try{
                    tier = Integer.valueOf(args[3]);
                }catch (IllegalArgumentException ex){
                    sender.sendMessage(Utils.asColor("&cWrong tier number!"));
                    return true;
                }

                //Amount check
                int amount;
                try{
                    amount = Integer.valueOf(args[4]);
                }catch (IllegalArgumentException ex){
                    sender.sendMessage(Utils.asColor("&cWrong amount number!"));
                    return true;
                }

                addItem(target,pl.getItemHandler().getSpawner(type,tier,amount));
                return true;


            }
        }



        sender.sendMessage(Utils.asColor("&7Usage: /ultraspawners give <player> <type> <tier> <amount>"));
        return true;
    }

    private void addItem(Player target, ItemStack toGive){
        if(target.getInventory().firstEmpty()==-1){
            target.getWorld().dropItemNaturally(target.getLocation(),toGive);
            return;
        }
        target.getInventory().addItem(toGive);
    }
}
