package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import dev.corestone.mapprotect.data.LocationData;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WandManager implements Listener, CommandExecutor {
    private MapProtect plugin;

    private RegionManager manager;
    private NamespacedKey wandKey;
    private HashMap<UUID, Location> leftClickStash = new HashMap<>();
    private HashMap<UUID, Location> rightClickStash = new HashMap<>();


    private ItemStack wandItem = new ItemStack(Material.BLAZE_ROD);

    public WandManager(MapProtect plugin, RegionManager manager){
        this.plugin = plugin;
        this.manager = manager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("mpwand").setExecutor(this);
        plugin.getCommand("mpcreate").setExecutor(this);
        this.wandKey = new NamespacedKey(plugin,"swag-key-name");
        wandItem.getItemMeta().setDisplayName(Colorize.format("&bMapProtect Wand"));
        wandItem.getItemMeta().getPersistentDataContainer().set(wandKey, PersistentDataType.INTEGER, 1);
    }


    @EventHandler
    public void onLeftCLick(PlayerInteractEvent e){
        if(!e.getAction().isLeftClick())return;
        if(!e.getPlayer().hasPermission("mp.wand"))return;
        if(e.getClickedBlock() == null)return;
        e.getPlayer().sendMessage(Colorize.format("&bSet location &l1 &r&bat: " + e.getClickedBlock().getLocation().toVector()));
        e.setCancelled(true);
        if(leftClickStash.containsKey(e.getPlayer().getUniqueId())){
            leftClickStash.replace(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
            return;
        }
        leftClickStash.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
    }
    @EventHandler
    public void onRightCLick(PlayerInteractEvent e){
        if(!e.getAction().isRightClick())return;
        if(e.getClickedBlock() == null)return;
        if(!e.getPlayer().hasPermission("mp.wand"))return;
        e.getPlayer().sendMessage(Colorize.format("&bSet location &l2 &r&bat: " + e.getClickedBlock().getLocation().toVector()));
        e.setCancelled(true);
        if(rightClickStash.containsKey(e.getPlayer().getUniqueId())){
            rightClickStash.replace(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
            return;
        }
        rightClickStash.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return true;
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("mpwand")){
            player.sendMessage(Colorize.format("&cBehold! A magic Wand!"));
            player.getInventory().addItem(wandItem);
            return true;
        }
        if(command.getName().equalsIgnoreCase("mpcreate")){
            String regionName = args[0];
            if(regionName.equals("")){
                player.sendMessage(Colorize.format("&cPlease give the region a name."));
                player.sendMessage(Colorize.format("&cCommand usage: /mpcreate <name>"));
                return true;
            }
            if(rightClickStash.containsKey(player.getUniqueId()) || leftClickStash.containsKey(player.getUniqueId())){
                player.sendMessage(Colorize.format("&cPlease enter all the locations. It's like world edit, you can do it :)"));
                return true;
            }
            if(LocationData.getRegionNames().contains(regionName)){
                player.sendMessage(Colorize.format("&cA region with the name +&b"+regionName+" &calready exists."));
                return true;
            }
            manager.newBox(regionName, leftClickStash.get(player.getUniqueId()), rightClickStash.get(player.getUniqueId()));
        }

        return true;

    }
}
