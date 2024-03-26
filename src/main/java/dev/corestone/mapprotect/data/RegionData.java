package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.dataessentials.DataFile;
import dev.corestone.mapprotect.data.dataessentials.DataManager;
import dev.corestone.mapprotect.regions.RegionBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RegionData implements DataFile {

    private MapProtect plugin;
    private DataManager data;

    private ArrayList<String> regionList = new ArrayList<>();
    public RegionData(MapProtect plugin){
        this.plugin = plugin;
        this.data = new DataManager(this.plugin, this, "region-data.yml");
        update(data.getInternalConfig());
        loadRegions();
    }

    public void loadRegions(){
        if(data.getConfig().getConfigurationSection("regions") == null)return;
        for(String name : data.getConfig().getConfigurationSection("regions").getKeys(false)){
            regionList.add(name);
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

        set("version", internalConfig.get("version"));
        if(data.getConfig().getConfigurationSection("regions") == null)return;

        for(String path : plugin.getDefaultData().getConfig().getConfigurationSection("master-default").getKeys(true)){
            for(String regionName : data.getConfig().getConfigurationSection("regions").getKeys(false)){
                if(!data.getConfig().contains("regions."+regionName+"."+path)){
                    set("regions."+regionName+"."+path, plugin.getDefaultData().getConfig().get("master-default."+path));
                }
            }
        }
        for(String regionName : data.getConfig().getConfigurationSection("regions").getKeys(false)){
              for(String regionPath : data.getConfig().getConfigurationSection("regions."+regionName).getKeys(true)){
                  if(!plugin.getDefaultData().getConfig().getConfigurationSection("master-default").contains(regionPath)){
                      data.remove("regions."+regionName+"."+regionPath);
                  }
              }
        }
    }




    public BoundingBox getRegionBox(String name){
        Location loc1 = data.getConfig().getLocation("regions."+name+".location-1");
        Location loc2 = data.getConfig().getLocation("regions."+name+".location-1");
        return new BoundingBox(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
    }
    public void removeRegion(String name){
        remove("regions."+name);
        regionList.remove(name);
    }
    public ArrayList<String> getRegionList(){
        return regionList;
    }

    public void addRegion(String name, String defaultProfileName){
        if(regionList.contains(name))return;
        if(defaultProfileName.isEmpty()){
            defaultProfileName = "master-default";
        }
        if(!defaultProfileName.equals("master-default")){
            defaultProfileName = "default-profiles."+defaultProfileName;
        }
        set("regions."+name, plugin.getDefaultData().getConfig().get(defaultProfileName));
        regionList.add(name);
    }

    public Object getBlockData(String regionName, String dataPiece){
        return getConfig().get("regions."+regionName+".block-managers."+dataPiece);
    }
    public Object getPlayerData(String regionName, String dataPiece){
        return getConfig().get("regions."+regionName+".player-managers."+dataPiece);
    }
    public Object getMobData(String regionName, String dataPiece){
        return getConfig().get("regions."+regionName+".mob-managers."+dataPiece);
    }

    public Object getMasterData(String regionName, String dataPiece){
        return getConfig().get("regions."+regionName+".map-master."+dataPiece);

    }


}
