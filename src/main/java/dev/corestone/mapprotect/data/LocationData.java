package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.dataessentials.DataFile;
import dev.corestone.mapprotect.data.dataessentials.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationData implements DataFile {

    private MapProtect plugin;
    private DataManager data;
    private ArrayList<String> regionNames = new ArrayList<>();
    private HashMap<String, Location> regionSpawns = new HashMap<>();
    public LocationData(MapProtect plugin){
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, "location-data.yml");
        load();
    }
    public void load(){
        if(data.getConfig().getConfigurationSection("locations") == null)return;
        for(String name : data.getConfig().getConfigurationSection("locations").getKeys(false)){
            regionNames.add(name);
            regionSpawns.put(name, data.getConfig().getLocation("locations."+name+".spawn-loc"));
        }
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


    public void addBox(String name, Location loc1, Location loc2){
        set("locations."+name+".loc1", loc1);
        set("locations."+name+".loc2", loc2);
        set("locations."+name+".spawn-loc", loc1.clone().add(loc2.clone()).multiply(.5));
        regionNames.add(name);
        regionSpawns.put(name, data.getConfig().getLocation("locations."+name+".spawn-loc"));
    }
    public void setSpawnLoc(String name, Location location){
        set("locations."+name+".spawn-loc", location);
        regionSpawns.replace(name, location);
    }

    public BoundingBox getBox(String name){
        Location loc1 = data.getConfig().getLocation("locations."+name+".loc1");
        Location loc2 = data.getConfig().getLocation("locations."+name+".loc2");
        return new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
    }

    public void removeBox(String name){
        set("locations."+name, null);
        regionNames.remove(name);
        regionSpawns.remove(name);
    }
    public ArrayList<String> getRegionNames(){
        return regionNames;
    }

    public Location getMapSpawn(String name){
        return regionSpawns.get(name);
    }
    public HashMap<String ,Location> getMapSpawnList(){
        return regionSpawns;
    }
}
