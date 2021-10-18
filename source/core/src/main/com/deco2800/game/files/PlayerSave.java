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

    /**
     * Function which takes in a PLayerSave.Save object
     * and writes it to the save file as is.
     */
    public static void write() {

        try (FileWriter saveWrite = new FileWriter((SAVE_FILE))) {

            saveWrite.write(String.valueOf(Save.getHasPlayed()) + '\n');
            saveWrite.write(String.valueOf(Save.getElfEnc()) + '\n');
            saveWrite.write(String.valueOf(Save.getLokiEnc()) + '\n');
            saveWrite.write(String.valueOf(Save.getLoki2Enc()) + '\n');
            saveWrite.write(String.valueOf(Save.getThorEnc()) + '\n');
            saveWrite.write(String.valueOf(Save.getOdinEnc()) + '\n');

            saveWrite.write(String.valueOf(Save.getElfWins()) + '\n');
            saveWrite.write(String.valueOf(Save.getLokiWins()) + '\n');
            saveWrite.write(String.valueOf(Save.getLoki2Wins()) + '\n');
            saveWrite.write(String.valueOf(Save.getThorWins()) + '\n');
            saveWrite.write(String.valueOf(Save.getOdinWins()) + '\n');

            logger.debug("Player Save File correctly updated.");

        } catch (IOException e) {
            logger.warn("Player Save File has not correctly written save status");
        }

    }

    /**
     * Saves a completely reset file to the player save file.
     */
    public static void resetFile() {
        Save.resetSave();
        write();
    }

    /**
     * Function which reads and then returns a save object from
     * the file at location in SAVE_FILE
     */
    public static void load() {
        File saveFile = new File(SAVE_FILE);
        try {

            Scanner saveRead = new Scanner(saveFile);

            Save.setHasPlayed(Boolean.parseBoolean(saveRead.nextLine()));
            Save.setElfEnc(Integer.parseInt(saveRead.nextLine()));
            Save.setLokiEnc(Integer.parseInt(saveRead.nextLine()));
            Save.setLoki2Enc(Integer.parseInt(saveRead.nextLine()));
            Save.setThorEnc(Integer.parseInt(saveRead.nextLine()));
            Save.setOdinEnc(Integer.parseInt(saveRead.nextLine()));

            Save.setElfWins(Integer.parseInt(saveRead.nextLine()));
            Save.setLokiWins(Integer.parseInt(saveRead.nextLine()));
            Save.setLoki2Wins(Integer.parseInt(saveRead.nextLine()));
            Save.setThorWins(Integer.parseInt(saveRead.nextLine()));
            Save.setOdinWins(Integer.parseInt(saveRead.nextLine()));

            logger.debug("Player Save File correctly loaded into new game");
            saveRead.close();

        } catch (FileNotFoundException | NumberFormatException e) {
            logger.warn("Player Save File has not correctly written save status");
            Save.resetSave();
        }

    }

    /**
     * Stores player game progress
     * Values are defaulted to no progress at all
     */
    public static class Save {
        // whether or not the player has played the tutorial/game before
        private static boolean hasPlayed = false;

        // number of times the player has encountered a specific boss

        private static int elfEnc = 0;
        private static int lokiEnc = 0;

        private static int loki2Enc = 0;
        private static int thorEnc = 0;
        private static int odinEnc = 0;

        // number of times the player has defeated a specific boss

        private static int elfWins = 0;
        private static int lokiWins = 0;

        private static int loki2Wins = 0;
        private static int thorWins = 0;
        private static int odinWins = 0;

        public static void setHasPlayed(boolean played) {
            hasPlayed = played;
        }

        public static boolean getHasPlayed() {
            return hasPlayed;
        }

        public static int getElfEnc() {
            return elfEnc;
        }

        public static int getLokiEnc() {
            return lokiEnc;
        }

        public static int getLoki2Enc() {
            return loki2Enc;
        }

        public static int getThorEnc() {
            return thorEnc;
        }

        public static int getOdinEnc() {
            return odinEnc;
        }

        public static int getElfWins() {
            return elfWins;
        }

        public static int getLokiWins() {
            return lokiWins;
        }

        public static int getLoki2Wins() {
            return loki2Wins;
        }

        public static int getThorWins() {
            return thorWins;
        }

        public static int getOdinWins() {
            return odinWins;
        }

        public static void setElfEnc(int num) {
            elfEnc = num;
        }

        public static void setLokiEnc(int num) {
            lokiEnc = num;
        }

        public static void setLoki2Enc(int num) {
            loki2Enc = num;
        }

        public static void setThorEnc(int num) {
            thorEnc = num;
        }

        public static void setOdinEnc(int num) {
            odinEnc = num;
        }

        public static void setElfWins(int num) {
            elfWins = num;
        }

        public static void setLokiWins(int num) {
            lokiWins = num;
        }

        public static void setLoki2Wins(int num) {
            loki2Wins = num;
        }

        public static void setThorWins(int num) {
            thorWins = num;
        }

        public static void setOdinWins(int num) {
            odinWins = num;
        }

        public static void resetSave() {
            hasPlayed = false;

            // number of times the player has encountered a specific boss

            elfEnc = 0;
            lokiEnc = 0;
            loki2Enc = 0;
            thorEnc = 0;
            odinEnc = 0;

            // number of times the player has defeated a specific boss

            elfWins = 0;
            lokiWins = 0;
            loki2Wins = 0;
            thorWins = 0;
            odinWins = 0;

        }

    }
}
