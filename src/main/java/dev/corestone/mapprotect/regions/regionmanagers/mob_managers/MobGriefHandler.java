package dev.corestone.mapprotect.regions.regionmanagers.mob_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;

import java.util.UUID;

public class MobGriefHandler implements Listener, RegionHandler {
    private MapProtect plugin;
    private RegionBox region;
    private boolean mobGrief;

    public MobGriefHandler(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.mobGrief = (boolean) plugin.getRegionData().getMobData(region.getName(), "monster-grief");
    }
    @EventHandler
    public void creeperExplode(EntityExplodeEvent event){
        if(mobGrief)return;
        if(!region.getBox().contains(event.getLocation().toVector()))return;
        if(event.getEntity() instanceof Monster){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onTake(EntityChangeBlockEvent event){
        if(mobGrief)return;
        if(!region.getBox().contains(event.getBlock().getLocation().toVector()))return;
        if(event.getEntity() instanceof Monster) event.setCancelled(true);
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
