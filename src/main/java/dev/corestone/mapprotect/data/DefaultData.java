package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.dataessentials.DataFile;
import dev.corestone.mapprotect.data.dataessentials.DataManager;

import dev.corestone.mapprotect.utilities.DataBook;
import org.bukkit.configuration.file.YamlConfiguration;



public class DefaultData implements DataFile {
    MapProtect plugin;
    DataManager data;

    public DefaultData(MapProtect plugin){
        this.plugin = plugin;
        this.data = new DataManager(plugin,this, "default-profile-data.yml");
        //update();
        update(data.getInternalConfig());
    }

    @Override
    public void update(YamlConfiguration internalConfig){

        for(String path : internalConfig.getKeys(false)){
            if(path.equals("version")){
                set(path, DataBook.version);
            }
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
}
