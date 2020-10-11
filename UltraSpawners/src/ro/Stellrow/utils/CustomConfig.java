package ro.Stellrow.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.Stellrow.UltraSpawners;

import java.io.File;
import java.io.IOException;

public class CustomConfig {
    private File file;
    private FileConfiguration fileConfiguration;

    public CustomConfig(String name, UltraSpawners main){
        if(!main.getDataFolder().exists()){
            main.getDataFolder().mkdirs();
        }
            file = new File(main.getDataFolder(),name+".yml");
        if(!file.exists()){
            try {
                file.createNewFile();
                if(main.getResource(name+".yml")!=null){
                    main.saveResource(name+".yml",true);
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

    }

    public FileConfiguration getConfig(){
        return fileConfiguration;
    }

    public void save(){
        try {
            fileConfiguration.save(file);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

}
