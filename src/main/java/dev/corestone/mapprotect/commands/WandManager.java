package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WandManager implements Listener, CommandExecutor, TabExecutor {
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
        plugin.getCommand("mpcreate").setTabCompleter(this);
        this.wandKey = new NamespacedKey(plugin,"swag-key-name");
        ItemMeta meta = wandItem.getItemMeta();
        meta.setDisplayName(Colorize.format("&bMapProtect Wand"));
        meta.getPersistentDataContainer().set(wandKey, PersistentDataType.INTEGER, 1);
        wandItem.setItemMeta(meta);
    }


    @EventHandler
    public void onLeftCLick(PlayerInteractEvent e){
        if(!e.getAction().isLeftClick())return;
        if(!e.getPlayer().hasPermission("mp.wand"))return;
        if(e.getClickedBlock() == null)return;
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null)return;
        if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(wandKey))return;

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
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null)return;
        if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(wandKey))return;

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
            ArrayList<String> list = new ArrayList<>();
            for(int j = 0; args.length > j; j++){
                list.add(args[j]);
            }
            if(list.isEmpty()){
                player.sendMessage(Colorize.format("&cPlease give the region a name."));
                player.sendMessage(Colorize.format("&cCommand usage: /mpcreate <name>"));
                return true;
            }
            String regionName = args[0];
            String defaultProfile = "master-default";
            if(list.size() > 1){
                defaultProfile = args[1];
            }
            if(!rightClickStash.containsKey(player.getUniqueId()) || !leftClickStash.containsKey(player.getUniqueId())){
                player.sendMessage(Colorize.format("&cPlease enter all the locations. It's like world edit, you can do it :)"));
                return true;
            }
            if(plugin.getLocationData().getRegionNames().contains(regionName)){
                player.sendMessage(Colorize.format("&cA region with the name &3"+regionName+" &calready exists."));
                return true;
            }
            player.sendMessage(Colorize.format("&3Creating a protection zone for &3&l" + regionName));
            manager.createNewRegion(regionName, defaultProfile, leftClickStash.get(player.getUniqueId()), rightClickStash.get(player.getUniqueId()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2 && command.getName().equalsIgnoreCase("mpcreate")){
            //return plugin.getDefaultData().get();
        }
        return new ArrayList<>();
    }
}
