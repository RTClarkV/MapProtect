package dev.corestone.mapprotect.regions.regionmanagers;

import java.util.UUID;

public interface RegionHandler {

    void delete();
    void playerEntry(UUID uuid);
    void playerExit(UUID uuid);

}
