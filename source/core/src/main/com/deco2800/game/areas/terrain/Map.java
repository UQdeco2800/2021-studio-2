package com.deco2800.game.areas.terrain;

import java.util.HashMap;

/**
 * A class that stores the information read in from a map JSON file
 */
public class Map {
    // Variables representing the types of data stored in the JSON file
    // Every element in the JSON must be represented with the same variable name and type
    private HashMap<String, Integer> dimensions;
    private int[][] mapTilePlacement;
    private HashMap<String, String> tileRefs;
    private HashMap<String, Float>[] wallObjects;
    private int[][] transObstacles;
    private HashMap<String, Float>[] spikeObjects;
    private HashMap<String, Float>[] lavaObjects;
    private HashMap<String, Float>[] teleportObjects;
    private HashMap<String, Float>[] initTeleportObjects;
    private int[][] LightTiles;
    private HashMap<String, Float>[] bossBounds;
    private HashMap<String, Float>[] healthCrateObjects;
    private HashMap<String, Float>[] meleeObjects;
    private HashMap<String, Float>[] rangeObjects;
    private HashMap<String, Float>[] guardObjects;
    private HashMap<String, Float>[] assassinObjects;
    private HashMap<String, Float>[] bossObjects;
    private HashMap<String, Float>[] anchoredObjects;
    private HashMap<String, Float>[] asgardMeleeObjects;
    private HashMap<String, Float>[] outdoorMeleeObjects;
    private HashMap<String, Float>[] hellMeleeObjects;
    private HashMap<String, Float>[] moveRightObjects;
    private HashMap<String, Float>[] moveLeftObjects;
    private HashMap<String, Float>[] moveUpObjects;
    private HashMap<String, Float>[] moveDownObjects;
    private HashMap<String, Float>[] cutsceneObjects;

    public HashMap<String, Float>[] getCutsceneObjects() {
        return cutsceneObjects;
    }

    public HashMap<String, Float>[] getMoveRightObjects() {
        return moveRightObjects;
    }

    public HashMap<String, Float>[] getMoveDownObjects() {
        return moveDownObjects;
    }

    public HashMap<String, Float>[] getMoveLeftObjects() {
        return moveLeftObjects;
    }

    public HashMap<String, Float>[] getMoveUpObjects() {
        return moveUpObjects;
    }

    public HashMap<String, Float>[] getHellMeleeObjects() {
        return hellMeleeObjects;
    }

    public HashMap<String, Float>[] getAsgardMeleeObjects() {
        return asgardMeleeObjects;
    }

    public HashMap<String, Float>[] getOutdoorMeleeObjects() {
        return outdoorMeleeObjects;
    }

    public HashMap<String, Float>[] getMeleeObjects() {
        return meleeObjects;
    }

    public HashMap<String, Float>[] getGuardObjects() {
        return guardObjects;
    }

    public HashMap<String, Float>[] getRangeObjects() {
        return rangeObjects;
    }

    public HashMap<String, Float>[] getAssassinObjects() {
        return assassinObjects;
    }

    public HashMap<String, Float>[] getBossObjects() {
        return bossObjects;
    }
    public HashMap<String, Float>[] getAnchoredObjects() {
        return anchoredObjects;
    }

    public HashMap<String, Float>[] getHealthCrateObjects() {
        return healthCrateObjects;
    }

    public HashMap<String, Integer> getDimensions() {
        return dimensions;
    }

    public int[][] getMapTiles() {
        return mapTilePlacement;
    }

    public HashMap<String, String> getTileRefs() {
        return tileRefs;
    }

    public HashMap<String, Float>[] getWallObjects() {
        return wallObjects;
    }

    public int[][] getTransObstacles() {
        return transObstacles;
    }

    public HashMap<String, Float>[] getSpikeObjects() {
        return spikeObjects;
    }

    public HashMap<String, Float>[] getLavaObjects() {
        return lavaObjects;
    }

    public HashMap<String, Float>[] getInitTeleportObjects() {
        return initTeleportObjects;
    }

    public int[][] getLightTiles() {
        return LightTiles;
    }

    public HashMap<String, Float>[] getBossBounds() {
        return bossBounds;
    }

    public HashMap<String, Float>[] getTeleportObjects() {
        return teleportObjects;
    }

    /**
     * Returns an array of the tile textures locations for easier loading
     */
    public String[] TileRefsArray() {
        String[] s = new String[tileRefs.size()];

        for (int i = 1; i <= tileRefs.size(); i++) {
            s[i - 1] = tileRefs.get(String.valueOf(i));
        }

        return s;
    }
}
