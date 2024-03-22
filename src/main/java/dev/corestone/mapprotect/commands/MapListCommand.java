package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MapListCommand implements CommandExecutor {
    private MapProtect plugin;
    public MapListCommand(MapProtect plugin) {
        this.plugin = plugin;
        plugin.getCommand("mplist").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return true;

        Player player = (Player) sender;
        player.sendMessage(Colorize.format("&cMapProtect areas:"));
        if(plugin.getRegionData() == null)return true;
        for(String mapName : plugin.getRegionData().getRegionList()){
            player.sendMessage(Colorize.format("&3"+mapName));
        }
        return true;
    }
}
