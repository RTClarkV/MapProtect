package dev.corestone.mapprotect.utilities;

import org.bukkit.Location;
import org.bukkit.Sound;

public class BroadcastSound {
    public static void playSound(Location location, Sound sound, int volume, int pitch){
        location.getWorld().playSound(location, sound, volume, pitch);
        System.out.println("amogus");
    }
}
