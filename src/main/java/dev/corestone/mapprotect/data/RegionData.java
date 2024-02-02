package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class RegionData {

    private MapProtect plugin;

    private File file;
    private YamlConfiguration config;
    private ArrayList<String> regionList = new ArrayList<>();
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
        if(config.getConfigurationSection("regions") == null)return;
        for(String name : config.getConfigurationSection("regions").getKeys(false)){
            regionList.add(name);
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

    public YamlConfiguration getConfig(){
        return config;
    }

    public BoundingBox getRegion(String name){
        Location loc1 = config.getLocation("regions."+name+".location-1");
        Location loc2 = config.getLocation("regions."+name+".location-1");
        return new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
    }
    public void addRegion(String name, Location loc1, Location loc2){
        config.set("regions."+name+".location-1", loc1);
        config.set("regions."+name+".location-1", loc1);
        regionList.add(name);
    }
    public void removeRegion(String name){
        config.set("regions."+name, null);
        regionList.remove(name);
    }
    public ArrayList<String> getRegionList(){
        return regionList;
    }


}
