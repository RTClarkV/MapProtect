package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;

import dev.corestone.mapprotect.regions.regionmanagers.PlayerFallDamageHandler;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.DataBook;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;


public class RegionBox implements Listener, RegionInterface {

    private MapProtect plugin;
    private RegionManager manager;
    private BoundingBox box;
    private RegionState state;

    private String name;
    private BukkitScheduler scheduler;
    private ArrayList<RegionHandler> regionHandlers = new ArrayList<>();

    public RegionBox(MapProtect plugin, RegionManager manager, String name){

        this.plugin = plugin;
        this.name = name;
        this.manager = manager;
        this.scheduler = plugin.getServer().getScheduler();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.box = plugin.getLocationData().getBox(name);
        state = RegionState.valueOf(plugin.getRegionData().getConfig().getString("regions."+name+".map-master.map-state"));


        //create managers
        regionHandlers.add(new PlayerFallDamageHandler(plugin, this));


        //other logic

//        scheduler.runTaskTimer(plugin, (f)->{
//            if(state == RegionState.DELETED){
//                f.cancel();
//            }
//            Bukkit.broadcastMessage("Region " + name + " is alive. + " + state);
//        }, 0L, 80L);
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
                case DELETED:

                    for(RegionHandler handler : regionHandlers){
                        handler.delete();
                    }

                    HandlerList.unregisterAll(this);
                    break;
            }
    }

    @Override
    public void shutDown() {
        this.scheduler = null;
        HandlerList.unregisterAll(this);
        setState(RegionState.DELETED);
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
