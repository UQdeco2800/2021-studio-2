package com.deco2800.game.components.gamearea;

import java.util.HashMap;

public class Map {
    private HashMap<String, Integer> dimensions;
    private int[][] mapTilePlacement;
    private HashMap<String, String> tileRefs;
    private HashMap<String, Integer>[] wallObjects;

    public HashMap<String, Integer> getDimensions() {
        return dimensions;
    }

    public int[][] getMapTiles() {
        return mapTilePlacement;
    }

    public HashMap<String, String> getTileRefs() {
        return tileRefs;
    }

    public HashMap<String, Integer>[] getWallObjects() {
        return wallObjects;
    }
}
