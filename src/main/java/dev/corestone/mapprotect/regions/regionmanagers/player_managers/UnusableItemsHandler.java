package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.UUID;

public class UnusableItemsHandler implements Listener, RegionHandler {
    private MapProtect plugin;
    private RegionBox region;
    private String denyMessage;
    private ArrayList<Material> unusableItems = new ArrayList<>();
    public UnusableItemsHandler(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.denyMessage = plugin.getLanguageData().getLang("player-item-use-deny");
        for(String material : plugin.getRegionData().getStringArrayPlayerData(region.getName(), "unusable-items")){
            unusableItems.add(Material.valueOf(material.toUpperCase()));
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(region.getState()  == RegionState.IDLE)return;
        if(event.getItem() == null)return;
        if(!unusableItems.contains(event.getItem().getType()))return;
        if(event.getClickedBlock() != null && region.getBox().contains(event.getClickedBlock().getLocation().toVector())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(Colorize.format(denyMessage));
            return;
        }
        if(event.getInteractionPoint() != null && region.getBox().contains(event.getInteractionPoint().toVector())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(Colorize.format(denyMessage));
            return;
        }
        if(!region.getBox().contains(event.getPlayer().getLocation().toVector()))return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(Colorize.format(denyMessage));
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
