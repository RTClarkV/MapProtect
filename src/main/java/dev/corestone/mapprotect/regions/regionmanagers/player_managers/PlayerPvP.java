package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class PlayerPvP implements RegionHandler, Listener {

    private MapProtect plugin;
    private RegionBox region;
    private boolean pvp;
    private String pvpDenyMessage;

    public PlayerPvP(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        pvp = (boolean) plugin.getRegionData().getPlayerData(region.getName(), "player-pvp");
        pvpDenyMessage = plugin.getLanguageData().getLang("player-pvp-deny");
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(pvp)return;
        if(!event.getEntity().getType().equals(EntityType.PLAYER))return;
        if(region.getState() == RegionState.IDLE)return;
        if(!region.getBox().contains(event.getEntity().getLocation().toVector()))return;
        event.setCancelled(true);
        event.getEntity().sendMessage(Colorize.format(pvpDenyMessage));
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
