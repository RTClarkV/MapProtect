package dev.corestone.mapprotect.regions;

import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.UUID;

public interface RegionInterface {


    public ArrayList<UUID> getPlayers();
    void clearBlocks();
    String getName();
    BoundingBox getBox();
}
