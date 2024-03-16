package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MapDeleteCommand implements CommandExecutor, TabExecutor {
    private MapProtect plugin;
    private RegionManager manager;
    public MapDeleteCommand(MapProtect plugin, RegionManager manager){
        this.plugin = plugin;
        this.manager = manager;
        plugin.getCommand("mpremovemap").setExecutor(this);
        plugin.getCommand("mpdeletemap").setExecutor(this);

        plugin.getCommand("mpremovedefault").setExecutor(this);
        plugin.getCommand("mpdeletedefault").setExecutor(this);


    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {



        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("mpremovemap") || command.getName().equalsIgnoreCase("mpdeletemap")) {
            return plugin.getRegionData().getRegionList();
        }
        if (command.getName().equalsIgnoreCase("mpremovedefault") || command.getName().equalsIgnoreCase("mpdeletedefault")) {
            return plugin.getDefaultData().getDefaultList();
        }
        return null;
    }
}
