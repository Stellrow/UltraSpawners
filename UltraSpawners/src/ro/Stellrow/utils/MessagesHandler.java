package ro.Stellrow.utils;

import ro.Stellrow.Utils;

public class MessagesHandler {
    private final CustomConfig messagesConfig;

    public MessagesHandler(CustomConfig messagesConfig) {
        this.messagesConfig = messagesConfig;
    }

    public String prefix;
    public String no_permission;
    public String spawner_already_opened;
    public String spawner_broken_while_watching;
    public String not_enough_money;
    public String successsfully_upgraded_spawner;


    public void reload(){
            prefix = Utils.asColor(messagesConfig.getConfig().getString("Messages.prefix",
                    "&e[UltraSpawners]"
                    ) + " ");
            no_permission = prefix + Utils.asColor(messagesConfig.getConfig().getString("Messages.no-permission",
                    "&cYou do not have permission to use this command!"
                    ));
            spawner_already_opened = prefix + Utils.asColor(messagesConfig.getConfig().getString("Messages.spawner-already-opened",
                    "&cSomebody is looking at this spawner already!"
                    ));
            spawner_broken_while_watching = prefix + Utils.asColor(messagesConfig.getConfig().getString("Messages.spawner-broken-while-watching",
                    "&cSomebody destroyed the spawner you are looking at!"
                    ));
            not_enough_money = prefix+ Utils.asColor(messagesConfig.getConfig().getString("Messages.not-enough-money",
                    "&cYou dont have enough money to upgrade the spawner!"
            ));
            successsfully_upgraded_spawner = prefix+ Utils.asColor(messagesConfig.getConfig().getString("Messages.successsfully-upgraded-spawner",
                    "&aYou successfully upgraded the spawner!"
            ));
    }
}
