package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.UUID;

public class MapGameModeHandler implements Listener, RegionHandler {
    private MapProtect plugin;

    private RegionBox region;
    private GameMode gameMode;
    private boolean toggleGameModes;
    public MapGameModeHandler(MapProtect plugin, RegionBox region){
        this.region = region;
        this.plugin = plugin;
        this.gameMode = GameMode.valueOf(plugin.getRegionData().getMasterData(region.getName(), "game-mode").toString().toUpperCase());
        this.toggleGameModes = (boolean) plugin.getRegionData().getMasterData(region.getName(), "toggle-game-mode");
    }
    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void playerEntry(UUID uuid) {
        if(!toggleGameModes)return;
        if(Bukkit.getPlayer(uuid).hasPermission("mp.admin"))return;
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).setGameMode(gameMode);
    }

    @Override
    public void playerExit(UUID uuid) {
        if(!toggleGameModes)return;
        if(Bukkit.getPlayer(uuid).hasPermission("mp.admin"))return;
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).setGameMode(Bukkit.getDefaultGameMode());
    }
}
