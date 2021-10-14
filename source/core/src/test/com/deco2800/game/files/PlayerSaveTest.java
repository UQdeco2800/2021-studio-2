package com.deco2800.game.files;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerSaveTest {

    @Test
    void testDefaultSave() {
        assertEquals(PlayerSave.Save.getHasPlayed(), false);

        assertEquals(PlayerSave.Save.getElfEnc(), 0);
        assertEquals(PlayerSave.Save.getLokiEnc(), 0);
        assertEquals(PlayerSave.Save.getLoki2Enc(), 0);
        assertEquals(PlayerSave.Save.getThorEnc(), 0);
        assertEquals(PlayerSave.Save.getOdinEnc(), 0);

        assertEquals(PlayerSave.Save.getElfWins(), 0);
        assertEquals(PlayerSave.Save.getLokiWins(), 0);
        assertEquals(PlayerSave.Save.getLoki2Wins(), 0);
        assertEquals(PlayerSave.Save.getThorWins(), 0);
        assertEquals(PlayerSave.Save.getOdinWins(), 0);
    }

    @Test
    void testSaveReset() {
        PlayerSave.Save.setHasPlayed(true);

        PlayerSave.Save.setElfEnc(1);
        PlayerSave.Save.setLokiEnc(2);
        PlayerSave.Save.setLoki2Enc(3);
        PlayerSave.Save.setThorEnc(4);
        PlayerSave.Save.setOdinEnc(5);

        PlayerSave.Save.setElfWins(6);
        PlayerSave.Save.setLokiWins(7);
        PlayerSave.Save.setLoki2Wins(8);
        PlayerSave.Save.setThorWins(9);
        PlayerSave.Save.setOdinWins(10);

        PlayerSave.write();
        PlayerSave.Save.resetSave();

        assertEquals(PlayerSave.Save.getHasPlayed(), false);
        assertEquals(PlayerSave.Save.getElfEnc(), 0);
        assertEquals(PlayerSave.Save.getLokiEnc(), 0);
        assertEquals(PlayerSave.Save.getLoki2Enc(), 0);
        assertEquals(PlayerSave.Save.getThorEnc(), 0);
        assertEquals(PlayerSave.Save.getOdinEnc(), 0);

        assertEquals(PlayerSave.Save.getElfWins(), 0);
        assertEquals(PlayerSave.Save.getLokiWins(), 0);
        assertEquals(PlayerSave.Save.getLoki2Wins(), 0);
        assertEquals(PlayerSave.Save.getThorWins(), 0);
        assertEquals(PlayerSave.Save.getOdinWins(), 0);
    }

    @Test
    void testSaveChange() {
        PlayerSave.Save.setHasPlayed(true);

        PlayerSave.Save.setElfEnc(1);
        PlayerSave.Save.setLokiEnc(2);
        PlayerSave.Save.setLoki2Enc(3);
        PlayerSave.Save.setThorEnc(4);
        PlayerSave.Save.setOdinEnc(5);

        PlayerSave.Save.setElfWins(6);
        PlayerSave.Save.setLokiWins(7);
        PlayerSave.Save.setLoki2Wins(8);
        PlayerSave.Save.setThorWins(9);
        PlayerSave.Save.setOdinWins(10);

        PlayerSave.write();
        PlayerSave.Save.resetSave();

        PlayerSave.load();

        assertEquals(PlayerSave.Save.getHasPlayed(), true);

        assertEquals(PlayerSave.Save.getElfEnc(), 1);
        assertEquals(PlayerSave.Save.getLokiEnc(), 2);
        assertEquals(PlayerSave.Save.getLoki2Enc(), 3);
        assertEquals(PlayerSave.Save.getThorEnc(), 4);
        assertEquals(PlayerSave.Save.getOdinEnc(), 5);

        assertEquals(PlayerSave.Save.getElfWins(), 6);
        assertEquals(PlayerSave.Save.getLokiWins(), 7);
        assertEquals(PlayerSave.Save.getLoki2Wins(), 8);
        assertEquals(PlayerSave.Save.getThorWins(), 9);
        assertEquals(PlayerSave.Save.getOdinWins(), 10);
    }
}
