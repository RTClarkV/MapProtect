package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.utilities.Colorize;
import dev.corestone.mapprotect.utilities.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ReloadCommand implements CommandExecutor {

    private MapProtect plugin;

    public ReloadCommand(MapProtect plugin){
        this.plugin = plugin;
        plugin.getCommand("mpreload").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(Colorize.format("&cReloading &3Map&bProtect&c."));
        double time = System.currentTimeMillis();
        plugin.reloadPlugin();
        time = System.currentTimeMillis()- time;
        sender.sendMessage(Colorize.format("&3Map&bProtect &creload complete. Time: " + time + "ms."));
        return true;
    }
}
