package dev.corestone.mapprotect.regions.regionmanagers.block_managers;

import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class BlockHandler implements Listener, RegionHandler {
    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }
}
