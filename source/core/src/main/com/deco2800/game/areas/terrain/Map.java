package com.deco2800.game.areas.terrain;

import java.util.*;

/**
 * A class that stores the information read in from a map JSON file
 */
public class Map {
    private HashMap<String, Integer> dimensions;
    private int[][] mapTilePlacement;
    private HashMap<String, String> tileRefs;
    private HashMap<String, Integer>[] wallObjects;
    private int[][] transObstacles;

    public HashMap<String,Integer> getDimensions(){
        return dimensions;
    }

    public int[][] getMapTiles(){
        return mapTilePlacement;
    }

    public HashMap<String,String> getTileRefs(){
        return tileRefs;
    }

    public HashMap<String, Integer>[] getWallObjects(){
        return wallObjects;
    }

    public int[][] getTransObstacles(){return transObstacles;}

    /**
     * Returns an array of the tile textures locations for easier loading
     * @return
     */
    public String[] TileRefsArray(){
        String[] s = new String[tileRefs.size()];

        for(int i = 1; i <= tileRefs.size(); i++){
            s[i-1] = tileRefs.get(String.valueOf(i));
        }

        return s;
    }
}
