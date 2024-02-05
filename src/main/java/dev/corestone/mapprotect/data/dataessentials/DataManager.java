package dev.corestone.mapprotect.data.dataessentials;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.utilities.DataBook;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DataManager {
    private File file;
    private YamlConfiguration config;
    private MapProtect plugin;
    DataFile manager;
    private String fileName;

    public DataManager(MapProtect pluign, DataFile manager, String fileName){
        this.plugin = pluign;
        this.manager = manager;
        this.fileName = fileName;
        load();
    }
    private void load(){
        file = new File(plugin.getDataFolder(), fileName);

        if(!file.exists()){
            plugin.saveResource(fileName, false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(file.exists()){
            update();
        }
    }
    public void update(){

        InputStreamReader internalConfigFileStream = new InputStreamReader(plugin.getResource(fileName), StandardCharsets.UTF_8);
        YamlConfiguration internalYamlConfig = YamlConfiguration.loadConfiguration(internalConfigFileStream);

        for(String path : internalYamlConfig.getKeys(false)){
            if(path.equals("version")){
                set(path, internalYamlConfig.get(path));
            }
            if(path.equals("master-default")){
                for(String externalPath : config.getConfigurationSection("master-default").getKeys(true)){
                    if(!internalYamlConfig.getConfigurationSection("master-default").contains(externalPath)){
                        remove("master-default."+externalPath);
                    }
                }
                for(String internalPath : internalYamlConfig.getConfigurationSection("master-default").getKeys(true)){
                    if(!config.getConfigurationSection("master-default").contains(internalPath)){
                        set("master-default."+internalPath, internalYamlConfig.get("master-default."+internalPath));
                    }
                }

            }
        }
        save();
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
    public void remove(String path){
        config.set(path, null);
        save();
    }
    public YamlConfiguration getConfig(){
        return config;
    }
    public String getFileName(){
        return fileName;
    }
}
