package dev.corestone.mapprotect.regions;

import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.UUID;

public interface RegionInterface {
    void shutDown();
    RegionState getState();
    void setState(RegionState state);
    String getName();
    BoundingBox getBox();
}
