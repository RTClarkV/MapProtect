package dev.corestone.mapprotect.regions.regionmanagers.block_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.UUID;

public class ExplosionDamageHandler implements RegionHandler, Listener {
    private MapProtect plugin;
    private RegionBox region;
    private boolean canExplode;
    private String explosionDamageDenyMessage;


    public ExplosionDamageHandler(MapProtect plugin, RegionBox region) {
        this.plugin = plugin;
        this.region = region;
        this.canExplode = (boolean) plugin.getRegionData().getBlockData(region.getName(), "explosion-damage");
        explosionDamageDenyMessage = plugin.getLanguageData().getLang("explosion-damage-deny");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void explosionDamageListenerEntity(EntityExplodeEvent event) {
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        for (Block explodedBlock : event.blockList()){
            if (region.getBox().contains(explodedBlock.getLocation().toVector())){
                event.setCancelled(true);
                event.getLocation().getWorld().playSound(event.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 1.0f, 1.0f);
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
                event.getBlock().getLocation().getWorld().playSound(event.getBlock().getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 1.0f, 1.0f);
                break;
            }
        }
    }

    @EventHandler
    public void explosionDamageByEntityListener(EntityDamageEvent event){
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)return;
        if (!region.getBox().contains(event.getEntity().getLocation().toVector()))return;
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