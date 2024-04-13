package dev.corestone.mapprotect.regions.regionmanagers.block_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import java.util.UUID;

public class BlockBurnHandler implements Listener, RegionHandler {
    private MapProtect plugin;
    private RegionBox region;
    private boolean canBurn;
    public BlockBurnHandler(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.canBurn = (boolean) plugin.getRegionData().getBlockData(region.getName(), "block-burn");
    }
    @EventHandler
    public void onBurn(BlockBurnEvent event){
        if(canBurn)return;
        if(region.getState() == RegionState.IDLE)return;
        if(!region.getBox().contains(event.getBlock().getLocation().toVector()))return;
        event.setCancelled(true);
    }
    @EventHandler
    public void onSpread(BlockIgniteEvent event){
        if(canBurn)return;
        if(region.getState() == RegionState.IDLE)return;
        if(!region.getBox().contains(event.getBlock().getLocation().toVector()))return;
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
