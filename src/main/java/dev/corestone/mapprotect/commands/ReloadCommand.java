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
        plugin.reloadPlugin();
        return true;
    }
}
