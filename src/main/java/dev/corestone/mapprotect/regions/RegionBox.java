package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import dev.corestone.mapprotect.regions.regionmanagers.BlockManager;
import dev.corestone.mapprotect.utilities.DataBook;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.UUID;

public class RegionBox implements Listener, RegionInterface {

    private MapProtect plugin;
    private RegionManager manager;
    private BlockManager blockManager;
    private BoundingBox box;
    private RegionState state;

    private String name;

    public RegionBox(MapProtect plugin, RegionManager manager, String name){

        this.plugin = plugin;
        this.name = name;
        this.manager = manager;

        //create managers
        this.blockManager = new BlockManager(plugin, this);

        //other logic
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.box = plugin.getLocationData().getBox(name);
        state = RegionState.valueOf(DataBook.getRegionDataPath(name, "state"));
    }
    @Override
    public void setState(RegionState state) {
            this.state = state;
            switch (state){
                case ACTIVE:
                    break;
                case IDLE:
                    break;
                case LOCKED:
                    break;
                case CLOSED:
                    break;
                case JAILED:
                    break;
            }
    }

    @Override
    public void shutDown() {

    }

    @Override
    public RegionState getState() {
        return state;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BoundingBox getBox() {
        return box;
    }
}
