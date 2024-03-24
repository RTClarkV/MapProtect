package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
public class ExplosionDamageHandler implements RegionHandler, Listener {
    private MapProtect plugin;
    private RegionBox region;
    private boolean canExplode;

    public ExplosionDamageHandler(MapProtect plugin, RegionBox region) {
        this.plugin = plugin;
        this.region = region;
        this.canExplode = (boolean) plugin.getRegionData().getBlockData(region.getName(), "explosion-damage");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void explosionDamageListenerEntity(EntityExplodeEvent event) {
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        for (Block explodedBlock : event.blockList()){
            if (region.getBox().contains(explodedBlock.getLocation().toVector())){
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void explosionDamageListenerBlock(BlockExplodeEvent event) {
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        for (Block explodedBlock : event.blockList()){
            if (region.getBox().contains(explodedBlock.getLocation().toVector())){
                event.setCancelled(true);
                break;
            }
        }
    }

    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }
}