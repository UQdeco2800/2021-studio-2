package com.deco2800.game.files;

import com.deco2800.game.files.FileLoader.Location;

import java.io.File;

/**
 * Player save information
 * Ways to save and load it
 */
public class PlayerSave {
    private static final String ROOT_DIR = "DECO2800Game";
    private static final String SAVE_FILE = "save.json";


    public static Save get() {
        String path = ROOT_DIR + File.separator + SAVE_FILE;
        Save playerSave = FileLoader.readClass(Save.class, path, Location.EXTERNAL);
        // Use default values if file doesn't exist
        return playerSave != null ? playerSave : new Save();
    }

    public static void set(Save pSave) {
        String path = ROOT_DIR + File.separator + SAVE_FILE;
        FileLoader.writeClass(pSave, path, Location.EXTERNAL);
    }

    /**
     * Stores player game progress
     * Values are defaulted to no progress at all
     */
    public static class Save {
        // whether or not the player has played the tutorial/game
        public boolean hasPlayed = false;
        // how many materials the player has
        public int playerMaterials = 0;

        // number of times the player has encountered a specific boss
        public int firstBoss = 0;
        public int secondBoss = 0;
        public int thirdBoss = 0;

        // number of times the player has defeated a specific boss
        public int firstBossWins = 0;
        public int secondBossWins = 0;
        public int thirdBossWins = 0;
    }
}
