package dev.corestone.mapprotect;

import dev.corestone.mapprotect.commands.*;
import dev.corestone.mapprotect.data.DefaultData;
import dev.corestone.mapprotect.data.LocationData;
import dev.corestone.mapprotect.data.RegionData;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapProtect extends JavaPlugin {
    private LocationData locationData;
    private RegionData regionData;
    private DefaultData defaultData;
    private RegionManager manager;

    //commands
    private HelpCommand helpCommand;
    private MapListCommand mapListCommand;
    private MapManageCommand mapManageCommand;
    private CreateNewDefaultCommand createNewDefaultCommand;
    private MapDeleteCommand mapDeleteCommand;
    private ReloadCommand reloadCommand;
    @Override
    public void onEnable() {
        // Plugin startup logic

        this.locationData = new LocationData(this);
        this.defaultData = new DefaultData(this);
        this.regionData = new RegionData(this);
        //commands
        this.helpCommand = new HelpCommand(this);
        this.mapListCommand = new MapListCommand(this);
        this.mapManageCommand = new MapManageCommand(this);
        this.createNewDefaultCommand = new CreateNewDefaultCommand(this);
        this.reloadCommand = new ReloadCommand(this);

        //manager
        this.manager = new RegionManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        manager.shutDown();

        locationData = null;
        defaultData = null;
        regionData = null;
        manager = null;

    }

    public void reloadPlugin(){
        manager.shutDown();
        this.locationData = new LocationData(this);
        this.defaultData = new DefaultData(this);
        this.regionData = new RegionData(this);
        manager.startUp();
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
