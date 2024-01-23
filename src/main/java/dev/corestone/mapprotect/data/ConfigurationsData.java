package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigurationsData {

    private MapProtect plugin;
    private File file;
    private YamlConfiguration config;

    public ConfigurationsData(MapProtect plugin){
        this.plugin = plugin;
    }

    public void load(){
        file = new File(plugin.getDataFolder(), "configurations.yaml");
        if(!file.exists()){
            plugin.saveResource("configurations.yaml", false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(false);

        try {
            config.load(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void save(){
        try {
            config.save(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void set(String path, Object object){
        config.set(path, object);
        save();
    }
    public Object get(String path){
        return config.get(path);
    }

    public int getBlockPlaceTimer(){
        return config.getInt("block-place-timer");
    }
}
