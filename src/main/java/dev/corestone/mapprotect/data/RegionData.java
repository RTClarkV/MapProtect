package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.dataessentials.DataFile;
import dev.corestone.mapprotect.data.dataessentials.DataManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RegionData implements DataFile {

    private MapProtect plugin;
    private DataManager data;

    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<String> defaultProfiles = new ArrayList<>();
    public RegionData(MapProtect plugin){
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, "region-data.yml");
    }
    @Override
    public YamlConfiguration getConfig() {
        return data.getConfig();
    }

    @Override
    public void set(String path, Object object) {
        data.set(path, object);
    }

    @Override
    public void remove(String path) {
        data.remove(path);
    }

    @Override
    public void update(YamlConfiguration internalConfig){
    }


//    public void load(){
//        file = new File(plugin.getDataFolder(), "region-data.yml");
//        //config.options().parseComments(false);
//        if(!file.exists()){
//            plugin.saveResource("region-data.yml", false);
//        }
//        config = new YamlConfiguration();
//        config.options().parseComments(true);
//
//        try{
//            config.load(file);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        if(config.getConfigurationSection("regions") == null)return;
//        regionList.addAll(config.getConfigurationSection("regions").getKeys(false));
//        defaultProfiles.addAll(config.getConfigurationSection("default-profiles").getKeys(false));
//    }
//    public void save(){
//        try {
//            config.save(file);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public void set(String path, Object value){
//        config.set(path, value);
//        save();
//    }
//
//    public YamlConfiguration getConfig(){
//        return config;
//    }

    public BoundingBox getRegion(String name){
        Location loc1 = data.getConfig().getLocation("regions."+name+".location-1");
        Location loc2 = data.getConfig().getLocation("regions."+name+".location-1");
        return new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
    }
//    public void addNewRegion(String name, String defaultName){
//        for(String path : data.getConfig().getConfigurationSection("default-profiles."+defaultName).getKeys(false)){
//            set("regions."+name+"."+path, data.getConfig().get("default-profiles."+defaultName+"."+path));
//        }
//        regionList.add(name);
//    }
    public void removeRegion(String name){
        set("regions."+name, null);
        regionList.remove(name);
    }
    public ArrayList<String> getRegionList(){
        return regionList;
    }

    public List<Material> getBreakableBlocks(String name){
        List<String> somelist = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        somelist = data.getConfig().getStringList("regions."+name+".breakable-blocks");
        for (String type : somelist){
            materials.add(Material.valueOf(type));
        }
        return materials;
    }

    public List<Material> getPlaceableBlocks(String name){
        List<String> somelist = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        somelist = data.getConfig().getStringList("regions."+name+".placeable-blocks");
        for (String type : somelist){
            materials.add(Material.valueOf(type));
        }
        return materials;
    }

    public int getBlockBreakTimer(String name){
        return data.getConfig().getInt("regions."+name+".block-break-timer");
    }
    public List<String> getDefaultProfileList(){
        return defaultProfiles;
    }
    public void addDefaultProfile(String profileName, String existingProfile){
        if(defaultProfiles.contains(profileName))return;
        defaultProfiles.add(profileName);
        for(String path : data.getConfig().getConfigurationSection("regions."+existingProfile).getKeys(false)){
            set("default-profiles."+profileName+"."+path, data.getConfig().get("regions."+existingProfile+"."+path));
        }
    }
    public void removeDefaultProfile(String profileName){
        set("default-profiles."+profileName, null);
        defaultProfiles.remove(profileName);
    }


}
