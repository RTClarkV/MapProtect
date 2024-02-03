package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionData {

    private static MapProtect plugin;

    private static File file;
    private static YamlConfiguration config;
    private static ArrayList<String> regionList = new ArrayList<>();
    public RegionData(MapProtect plugin){
        this.plugin = plugin;
        load();
    }

    public static void load(){
        file = new File(plugin.getDataFolder(), "region-data.yml");

        config = new YamlConfiguration();
        config.options().parseComments(false);

        try{
            config.load(file);
        }catch (Exception e){
            e.printStackTrace();
        }


        if(config.getConfigurationSection("regions") == null)return;
        regionList.addAll(config.getConfigurationSection("regions").getKeys(false));
    }
    public static void save(){
        try {
            config.save(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void set(String path, Object value){
        config.set(path, value);
        save();
    }

    public static YamlConfiguration getConfig(){
        return config;
    }

    public static BoundingBox getRegion(String name){
        Location loc1 = config.getLocation("regions."+name+".location-1");
        Location loc2 = config.getLocation("regions."+name+".location-1");
        return new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
    }
    public static void addNewRegion(String name){
        config.set("regions."+name+".breakable-blocks", config.getStringList("default.breakable-blocks"));
        config.set("regions."+name+".placeable-blocks", config.getStringList("default.placeable-blocks"));
        config.set("regions."+name+".block-break-timer", config.getInt("default.block-break-timer"));
        regionList.add(name);
    }
    public static void removeRegion(String name){
        config.set("regions."+name, null);
        regionList.remove(name);
    }
    public static ArrayList<String> getRegionList(){
        return regionList;
    }

    public static List<Material> getBreakableBlocks(String name){
        List<String> somelist = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        somelist = config.getStringList("regions."+name+".breakable-blocks");

        for (String type : somelist){
            materials.add(Material.valueOf(type));
        }

        return materials;
    }

    public static List<Material> getPlaceableBlocks(String name){
        List<String> somelist = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        somelist = config.getStringList("regions."+name+".placeable-blocks");

        for (String type : somelist){
            materials.add(Material.valueOf(type));
        }

        return materials;
    }

    public static int getBlockBreakTimer(String name){
        return config.getInt("regions."+name+".block-break-timer");
    }
}
