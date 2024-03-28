package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import com.destroystokyo.paper.event.entity.TurtleLayEggEvent;
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
        Location explosionLocation = event.getEntity().getLocation();
        World world = explosionLocation.getWorld();
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        for (Block explodedBlock : event.blockList()){
            if (region.getBox().contains(explodedBlock.getLocation().toVector())){
                event.setCancelled(true);
                world.playSound(explosionLocation, Sound.ENTITY_ARMOR_STAND_BREAK, 1.0f, 1.0f);
                System.out.println("ExplosionDamageListenerEntity worked");
                break;
            }
        }
    }

    @EventHandler
    public void explosionDamageListenerBlock(BlockExplodeEvent event) {
        Location explosionLocation = event.getBlock().getLocation();
        World world = explosionLocation.getWorld();
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        for (Block explodedBlock : event.blockList()){
            if (region.getBox().contains(explodedBlock.getLocation().toVector())){
                event.setCancelled(true);
                world.playSound(explosionLocation, Sound.ENTITY_ARMOR_STAND_BREAK, 1.0f, 1.0f);
                System.out.println("ExplosionDamageListenerBlock worked");
                break;
            }
        }
    }

    @EventHandler
    public void explosionDamageByEntityListener(EntityDamageByEntityEvent event){
        Entity damagedEntity = event.getEntity();
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)return;
        if (!region.getBox().contains(damagedEntity.getLocation().toVector()))return;
        event.setCancelled(true);
        if (event.getEntity() instanceof Player) {event.getEntity().sendMessage(Colorize.format(explosionDamageDenyMessage));}

    }

    @EventHandler
    public void explosionDamageByBlockListener(EntityDamageByBlockEvent event){
        Entity damagedEntity = event.getEntity();
        if (canExplode)return;
        if (region.getState() == RegionState.IDLE)return;
        if (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)return;
        if (!region.getBox().contains(damagedEntity.getLocation().toVector()))return;
        event.setCancelled(true);
        if (event.getEntity() instanceof Player) {event.getEntity().sendMessage(Colorize.format(explosionDamageDenyMessage));}
    }

    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }
}

