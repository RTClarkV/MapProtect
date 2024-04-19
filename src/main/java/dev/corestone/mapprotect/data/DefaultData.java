package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.dataessentials.DataFile;
import dev.corestone.mapprotect.data.dataessentials.DataManager;

import dev.corestone.mapprotect.utilities.DataBook;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;


public class DefaultData implements DataFile {
    MapProtect plugin;
    DataManager data;

    private ArrayList<String> defaultList = new ArrayList<>();

    public DefaultData(MapProtect plugin){
        this.plugin = plugin;
        this.data = new DataManager(plugin,this, "default-profile-data.yml");
        //update();
        update(data.getInternalConfig());
        loadDefaults();
    }
    public void loadDefaults(){
        if(data.getConfig().getConfigurationSection("default-profiles") == null)return;
        for(String name : data.getConfig().getConfigurationSection("default-profiles").getKeys(false)){
            defaultList.add(name);
        }
    }

    @Override
    public void update(YamlConfiguration internalConfig){
        data.set("version", internalConfig.get("version"));
        for(String path : internalConfig.getKeys(false)){
            if(path.equals("master-default")){
                for(String manager : internalConfig.getConfigurationSection("master-default").getKeys(false)){
                 for(String setting : internalConfig.getConfigurationSection("master-default."+manager).getKeys(false)){
                     String dirPath = "master-default."+manager+"."+setting;
                     if(!data.getConfig().contains(dirPath)){
                         data.set(dirPath, internalConfig.get(dirPath));}}}
                for(String manager : data.getConfig().getConfigurationSection("master-default").getKeys(false)){
                    for(String setting : data.getConfig().getConfigurationSection("master-default."+manager).getKeys(false)){
                        String dirPath = "master-default."+manager+"."+setting;
                        if(!internalConfig.contains(dirPath)){
                            data.remove(dirPath);}}}
            }
        }
        if(data.getConfig().getConfigurationSection("default-profiles") == null)return;
        for(String path : data.getConfig().getConfigurationSection("master-default").getKeys(true)){
            for(String defaultName : data.getConfig().getConfigurationSection("default-profiles").getKeys(false)){
                if(!data.getConfig().contains("default-profiles."+defaultName+"."+path) && !path.contains("potion-effects")){
                    set("default-profiles."+defaultName+"."+path, data.getConfig().get("master-default."+path));
                }
            }
        }
        for(String regionName : data.getConfig().getConfigurationSection("default-profiles").getKeys(false)){
            for(String regionPath : data.getConfig().getConfigurationSection("default-profiles."+regionName).getKeys(true)){
                if(!data.getConfig().getConfigurationSection("master-default").contains(regionPath) && !regionPath.contains("potion-effects")){
                    data.remove("default-profiles."+regionName+"."+regionPath);
                }
            }
        }
    }

    public void createNewDefaultProfile(String name, String existingProfile){

        data.set("default-profiles."+name, plugin.getRegionData().getConfig().get("regions."+existingProfile));

    }
    public ArrayList<String> getDefaultList(){
        return defaultList;
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

    public boolean masterDefaultContainsSetting(String manager, String setting){
        return data.getConfig().contains("master-default."+manager+"."+setting);
    }
    public String getDefaultPath(String defaultName){
        if(defaultName.equals("master-default")){
            return "master-default";
        }
        return "default-profiles."+defaultName;
    }
}
