package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractHandler implements RegionHandler, Listener {
    private MapProtect plugin;
    private RegionBox region;
    private boolean canInteract;
    private String playerInteractDenyMessage;

    public PlayerInteractHandler(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        this.canInteract = (boolean)plugin.getRegionData().getPlayerData(region.getName(), "player-interact");
        playerInteractDenyMessage = plugin.getLanguageData().getLang("player-interact-deny");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerInteractListener(PlayerInteractEvent event){
        if(canInteract)return;
        if(region.getState() == RegionState.IDLE)return;
        if(event.getClickedBlock() == null)return;
        if(!region.getBox().contains(event.getClickedBlock().getLocation().toVector()))return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(Colorize.format(playerInteractDenyMessage));
    }
    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }
}
