package dev.corestone.mapprotect.data;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.dataessentials.DataFile;
import dev.corestone.mapprotect.data.dataessentials.DataManager;

import org.bukkit.configuration.file.YamlConfiguration;




public class DefaultData implements DataFile {
    MapProtect plugin;
    DataManager data;

    public DefaultData(MapProtect plugin){
        this.plugin = plugin;
        this.data = new DataManager(plugin,this, "default-profile-data.yml");
        //update();
    }

    @Override
    public void update(){

//        for(String path : data.getConfig().getConfigurationSection(DataBook.defaultKey).getKeys(false)){
//            for(String defaultPath : data.getConfig().getConfigurationSection(DataBook.defaultKey+".master-default").getKeys(false)){
//                String replacePath = defaultPath;
//                defaultPath = DataBook.defaultKey+DataBook.dot+defaultPath;
//                if(!data.getConfig().getConfigurationSection(DataBook.defaultKey+"."+path).contains(replacePath)){
//                    data.set(DataBook.defaultKey+"."+path, defaultPath);
//                }
//            }
//        }

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
