package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallDamageHandler implements RegionHandler, Listener {

    private MapProtect plugin;
    private RegionBox regionBox;
    private boolean fallDamage;

    public PlayerFallDamageHandler(MapProtect plugin, RegionBox regionBox){
        this.plugin = plugin;
        this.regionBox = regionBox;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.fallDamage = (boolean) plugin.getRegionData().getPlayerData(regionBox.getName(), "player-fall-damage");
    }

    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onFall(EntityDamageEvent e){
        if(regionBox.getState().equals(RegionState.IDLE))return;
        if(!e.getEntity().getType().equals(EntityType.PLAYER))return;
        if(fallDamage)return;
        if(!regionBox.getBox().contains(e.getEntity().getLocation().toVector()))return;
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL){
            e.setCancelled(true);
        }

    }
}
