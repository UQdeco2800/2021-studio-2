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

    /**
     * Returns an array of the tile textures locations for easier loading
     *
     * @return
     */
    public String[] TileRefsArray() {
        String[] s = new String[tileRefs.size()];

        for (int i = 1; i <= tileRefs.size(); i++) {
            s[i - 1] = tileRefs.get(String.valueOf(i));
        }

        return s;
    }
}
