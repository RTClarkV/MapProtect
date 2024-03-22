package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.dataessentials.DataFile;
import dev.corestone.mapprotect.data.dataessentials.DataManager;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageData implements DataFile {

    private DataManager data;
    private MapProtect plugin;

    public LanguageData(MapProtect plugin){
        this.plugin = plugin;
        data = new DataManager(plugin, this, "language.yml");
        update(data.getInternalConfig());
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
    public String getLang(String path){
        return data.getConfig().getString(path);
    }

    @Override
    public void update(YamlConfiguration internalYamlConfig) {
        data.set("version", internalYamlConfig.get("version"));
        for(String path : internalYamlConfig.getKeys(true)){
            if(data.getInternalConfig().contains(path)){
                set(path, internalYamlConfig.get(path));
            }
        }
    }
}
