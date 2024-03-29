package dev.corestone.mapprotect.utilities;

import java.util.ArrayList;

public class DataBook {
    //version
    public static final String version = "1.1.6-IN-DEV";

    //get String path
    public static String getRegionDataPath(String regionName, String path){
        return regionKey +dot+ regionName +dot+ path;
    }
    public static String getRegionDataPath(String regionName){
        return regionKey +dot+ regionName;
    }

    public static String getDefaultProfilePath(String defaultName, String path){
        return defaultKey +dot+ defaultName +dot+ path;
    }
    public static String getDefaultProfilePath(String defaultName){
        return defaultKey +dot+ defaultName;
    }

    public static String getLocationDataPath(String regionName, String path){
        return locationKey +dot+ regionName +dot+ path;
    }
    public static String getLocationDataPath(String regionName){
        return locationKey +dot+ regionName;
    }

    //dot
    public static final String dot = ".";

    //keys
    public static final String regionKey = "regions";
    public static final String defaultKey = "default-profiles";
    public static final String locationKey = "locations";

    //Option names with variable.
    public static final String blockBreakTimer = "block-break-timer";

    //keys master list

    //mpteleportplayer
}
