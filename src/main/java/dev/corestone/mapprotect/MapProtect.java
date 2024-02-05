package dev.corestone.mapprotect;

import dev.corestone.mapprotect.commands.HelpCommand;
import dev.corestone.mapprotect.commands.MapListCommand;
import dev.corestone.mapprotect.commands.MapManageCommand;
import dev.corestone.mapprotect.data.DefaultData;
import dev.corestone.mapprotect.data.LocationData;
import dev.corestone.mapprotect.data.RegionData;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapProtect extends JavaPlugin {
    private LocationData locationData;
    private RegionData regionData;
    private DefaultData defaultData;
    private RegionManager manager;
    private HelpCommand helpCommand;
    private MapListCommand mapListCommand;
    private MapManageCommand mapManageCommand;
    @Override
    public void onEnable() {
        // Plugin startup logic
//        new RegionManager(this);
        this.locationData = new LocationData(this);
        this.regionData = new RegionData(this);
        this.defaultData = new DefaultData(this);
        this.helpCommand = new HelpCommand(this);
        this.mapListCommand = new MapListCommand(this);
        this.mapManageCommand = new MapManageCommand(this);

        this.manager = new RegionManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        manager.shutDown();
    }

    public void reloadPlugin(){

    }

    public RegionData getRegionData(){
        return regionData;
    }
    public LocationData getLocationData(){
        return locationData;
    }
    public DefaultData getDefaultData(){
        return defaultData;
    }
    public RegionManager getManager(){
        return manager;
    }
}
