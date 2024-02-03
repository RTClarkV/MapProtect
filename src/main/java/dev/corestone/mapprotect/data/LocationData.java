package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.util.ArrayList;

public class LocationData {

    private static MapProtect plugin;
    private static File file;
    private static YamlConfiguration config;
    private static ArrayList<String> regionNames = new ArrayList<>();

    public LocationData(MapProtect plugin){
        this.plugin = plugin;
        load();
    }

    public static void load(){
        file = new File(plugin.getDataFolder(), "location-data.yml");
        if(!file.exists()){
            plugin.saveResource("location-data.yml", false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(false);

        try {
            config.load(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(config.getConfigurationSection("locations") == null)return;
        for(String name : config.getConfigurationSection("locations").getKeys(false)){
            regionNames.add(name);
        }
    }
    public static void save(){
        try {
            config.save(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void set(String path, Object object){
        config.set(path, object);
        save();
    }
    public static YamlConfiguration getConfig() {
        return config;
    }

    public static void addBox(String name, Location loc1, Location loc2){
        config.set("locations."+name+".loc1", loc1);
        config.set("locations."+name+".loc2", loc2);
        regionNames.add(name);
    }

    public static BoundingBox getBox(String name){
        Location loc1 = config.getLocation("locations."+name+".loc1");
        Location loc2 = config.getLocation("locations."+name+".loc2");
        return new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
    }

    public static void removeBox(String name){
        config.set("locations."+name, null);
        regionNames.remove(name);
    }
    public static ArrayList<String> getRegionNames(){
        return regionNames;
    }

}
