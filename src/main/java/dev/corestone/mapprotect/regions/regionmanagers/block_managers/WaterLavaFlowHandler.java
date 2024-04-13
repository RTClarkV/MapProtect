package dev.corestone.mapprotect.regions.regionmanagers.block_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.UUID;

public class WaterLavaFlowHandler implements RegionHandler, Listener {
    private MapProtect plugin;
    private RegionBox region;
    private boolean waterLavaCanFlow;
    private boolean waterLavaFlowFromOutside;

    public WaterLavaFlowHandler(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.waterLavaCanFlow = (boolean) plugin.getRegionData().getBlockData(region.getName(), "water-lava-flow");
        this.waterLavaFlowFromOutside = (boolean) plugin.getRegionData().getBlockData(region.getName(), "water-lava-flow-from-outside");
    }

    @EventHandler
    public void onFlowInside(BlockFromToEvent event){
        if(waterLavaCanFlow)return;
        if(region.getState() == RegionState.IDLE)return;
        if(!region.getBox().contains(event.getToBlock().getLocation().toVector()))return;
        event.setCancelled(true);
    }
    @EventHandler
    public void onFlowFromOutside(BlockFromToEvent event){
        if(waterLavaFlowFromOutside)return;
        if(region.getState() == RegionState.IDLE)return;
        if(region.getBox().contains(event.getBlock().getLocation().toVector()))return;
        if(!region.getBox().contains(event.getToBlock().getLocation().toVector()))return;
        event.setCancelled(true);
    }
    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void playerEntry(UUID uuid) {

    }

    @Override
    public void playerExit(UUID uuid) {

    }
}
