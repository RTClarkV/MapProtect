package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import dev.corestone.mapprotect.utilities.DataBook;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BoundingBox;


public class RegionBox implements Listener, RegionInterface {

    private MapProtect plugin;
    private RegionManager manager;
    private BoundingBox box;
    private RegionState state;

    private String name;
    private BukkitScheduler scheduler;

    public RegionBox(MapProtect plugin, RegionManager manager, String name){

        this.plugin = plugin;
        this.name = name;
        this.manager = manager;
        this.scheduler = plugin.getServer().getScheduler();
        //create managers

        //other logic
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.box = plugin.getLocationData().getBox(name);
        //state = RegionState.valueOf(DataBook.getRegionDataPath(name, "state"));


        scheduler.runTaskTimer(plugin, ()->{
            Bukkit.broadcastMessage("Region "+ name + " is alive");
        }, 0L, 60L);
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
