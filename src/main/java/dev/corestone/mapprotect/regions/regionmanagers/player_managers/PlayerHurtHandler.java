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

import java.util.UUID;

public class PlayerHurtHandler implements Listener, RegionHandler {
    private MapProtect plugin;
    private RegionBox region;
    private boolean canHurt;

    public PlayerHurtHandler(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.canHurt = (boolean) plugin.getRegionData().getPlayerData(region.getName(), "player-hurt");
    }
    @EventHandler
    public void onHurt(EntityDamageEvent event){
        if(canHurt)return;
        if(region.getState() == RegionState.IDLE)return;
        if(event.getEntityType() != EntityType.PLAYER)return;
        if(!region.getBox().contains(event.getEntity().getLocation().toVector()))return;
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
