package com.deco2800.game.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Player save information
 * Ways to save and load it
 */
public class PlayerSave {
    private static final String SAVE_FILE = "playersave.save";
    private static final Logger logger = LoggerFactory.getLogger(PlayerSave.class);

    private PlayerSave() {
        logger.warn("Instantiating of a class that is not suppose be an instance.");
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Function which takes in a PLayerSave.Save object
     * and writes it to the save file as is.
     *
     * @param pSave Takes a PlayerSave.Save to be written to file
     */
    public static void write(Save pSave) {

        try (FileWriter saveWrite = new FileWriter((SAVE_FILE))){

            saveWrite.write(String.valueOf(pSave.hasPlayed) + '\n');
            saveWrite.write(String.valueOf(pSave.lokiEnc) + '\n');
            saveWrite.write(String.valueOf(pSave.thorEnc) + '\n');
            saveWrite.write(String.valueOf(pSave.odinEnc) + '\n');

            saveWrite.write(String.valueOf(pSave.lokiWins) + '\n');
            saveWrite.write(String.valueOf(pSave.thorWins) + '\n');
            saveWrite.write(String.valueOf(pSave.odinWins) + '\n');

            logger.debug("Player Save File correctly updated.");

        } catch (IOException e) {
            logger.warn("Player Save File has not correctly written save status");
        }

    }

    /**
     * Function which returns a default save file object with
     * default values as though the game had never been reset
     *
     * @return Returns a PlayerSave.Save object with default values
     */
    public static Save initial() {
        return new Save();
    }

    /**
     * Function which reads and then returns a save object from
     * the file at location in SAVE_FILE
     *
     * @return Returns the PlayerSave.Save object as stored in the written
     * save file
     */
    public static Save load() {
        Save playersave = new Save();


        try {
            File saveFile = new File(SAVE_FILE);
            Scanner saveRead = new Scanner(saveFile);

            playersave.hasPlayed = Boolean.parseBoolean(saveRead.nextLine());
            playersave.lokiEnc = Integer.parseInt(saveRead.nextLine());
            playersave.thorEnc = Integer.parseInt(saveRead.nextLine());
            playersave.odinEnc = Integer.parseInt(saveRead.nextLine());

            playersave.lokiWins = Integer.parseInt(saveRead.nextLine());
            playersave.thorWins = Integer.parseInt(saveRead.nextLine());
            playersave.odinWins = Integer.parseInt(saveRead.nextLine());

            logger.debug("Player Save File correctly loaded into new game");

        } catch (FileNotFoundException e) {
            logger.warn("Player Save File has not correctly written save status");
        } catch (NumberFormatException numE) {
            logger.warn("Player Save File has not correctly written save status");
        }
        return playersave;
    }

    /**
     * Stores player game progress
     * Values are defaulted to no progress at all
     */
    public static class Save {
        // whether or not the player has played the tutorial/game before
        public boolean hasPlayed = false;

        // number of times the player has encountered a specific boss
        public static int lokiEnc = 0;
        public static int thorEnc = 0;
        public static int odinEnc = 0;

        // number of times the player has defeated a specific boss
        public static int lokiWins = 0;
        public static int thorWins = 0;
        public static int odinWins = 0;
    }
}
