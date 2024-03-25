package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class UnusableItemsHandler implements RegionHandler, Listener {
    private MapProtect plugin;
    private RegionBox region;
    private boolean canUseItem;
    private String useItemDenyMessage;

    public UnusableItemsHandler(MapProtect plugin, RegionBox regionBox){
        this.plugin = plugin;
        this.region = regionBox;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.canUseItem = (boolean) plugin.getRegionData().getPlayerData(regionBox.getName(), "unusable-item-message");
    }


    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }
}
