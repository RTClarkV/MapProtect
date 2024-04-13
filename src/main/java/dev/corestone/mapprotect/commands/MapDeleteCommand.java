package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import dev.corestone.mapprotect.utilities.Colorize;
import dev.corestone.mapprotect.utilities.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MapDeleteCommand implements CommandExecutor, TabExecutor {
    private MapProtect plugin;
    private RegionManager manager;
    public MapDeleteCommand(MapProtect plugin, RegionManager manager){
        this.plugin = plugin;
        this.manager = manager;
        plugin.getCommand("mpremovemap").setExecutor(this);
        plugin.getCommand("mpdeletemap").setTabCompleter(this);

        plugin.getCommand("mpremovedefault").setExecutor(this);
        plugin.getCommand("mpdeletedefault").setTabCompleter(this);


    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> argsList = new ArrayList<>();
        for(int j = 0; args.length > j; j++){
            argsList.add(args[j]);
        }
        if(argsList.isEmpty()){
            sender.sendMessage(Colorize.format(PlayerMessage.noArgs));
            return true;
        }
        String name = args[0];
        if (command.getName().equalsIgnoreCase("mpremovemap")){
            manager.removeRegion(name);
            sender.sendMessage(Colorize.format("&3Removing map &b" + name));
        }
        if(command.getName().equalsIgnoreCase("mpremovedefault")){
            manager.removeDefault(name);
            sender.sendMessage(Colorize.format("&3Removing default &b" + name));

        }
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
