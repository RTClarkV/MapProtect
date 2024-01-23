package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class RegionData {

    private MapProtect plugin;

    private File file;
    private YamlConfiguration config;
    private ArrayList<ArrayList<Location>> locationsList = new ArrayList<>();
    public RegionData(MapProtect plugin){
        this.plugin = plugin;
        load();
    }

    public void load(){
        file = new File(plugin.getDataFolder(), "region-data.yaml");

        config = new YamlConfiguration();
        config.options().parseComments(false);

        try{
            config.load(file);
        }catch (Exception e){
            e.printStackTrace();
        }

        for(String path : config.getConfigurationSection("warps").getKeys(false)){

        }
    }
    public void save(){
        try {
            config.save(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set(String path, Object value){
        config.set(path, value);
        save();
    }

    public Object get(String path){
        return config.get(path);
    }


}
