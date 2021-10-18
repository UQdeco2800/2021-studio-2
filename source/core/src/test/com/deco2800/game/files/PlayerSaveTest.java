package com.deco2800.game.files;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerSaveTest {

    @Test
    void testDefaultSave() {
        assertFalse(PlayerSave.Save.getHasPlayed());

        assertEquals(0, PlayerSave.Save.getElfEnc());
        assertEquals(0, PlayerSave.Save.getLokiEnc());
        assertEquals(0, PlayerSave.Save.getLoki2Enc());
        assertEquals(0, PlayerSave.Save.getThorEnc());
        assertEquals(0, PlayerSave.Save.getOdinEnc());

        assertEquals(0, PlayerSave.Save.getElfWins());
        assertEquals(0, PlayerSave.Save.getLokiWins());
        assertEquals(0, PlayerSave.Save.getLoki2Wins());
        assertEquals(0, PlayerSave.Save.getThorWins());
        assertEquals(0, PlayerSave.Save.getOdinWins());
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

        assertFalse(PlayerSave.Save.getHasPlayed());
        assertEquals(0, PlayerSave.Save.getElfEnc());
        assertEquals(0, PlayerSave.Save.getLokiEnc());
        assertEquals(0, PlayerSave.Save.getLoki2Enc());
        assertEquals(0, PlayerSave.Save.getThorEnc());
        assertEquals(0, PlayerSave.Save.getOdinEnc());

        assertEquals(0, PlayerSave.Save.getElfWins());
        assertEquals(0, PlayerSave.Save.getLokiWins());
        assertEquals(0, PlayerSave.Save.getLoki2Wins());
        assertEquals(0, PlayerSave.Save.getThorWins());
        assertEquals(0, PlayerSave.Save.getOdinWins());
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

        assertTrue(PlayerSave.Save.getHasPlayed());

        assertEquals(1, PlayerSave.Save.getElfEnc());
        assertEquals(2, PlayerSave.Save.getLokiEnc());
        assertEquals(3, PlayerSave.Save.getLoki2Enc());
        assertEquals(4, PlayerSave.Save.getThorEnc());
        assertEquals(5, PlayerSave.Save.getOdinEnc());

        assertEquals(6, PlayerSave.Save.getElfWins());
        assertEquals(7, PlayerSave.Save.getLokiWins());
        assertEquals(8, PlayerSave.Save.getLoki2Wins());
        assertEquals(9, PlayerSave.Save.getThorWins());
        assertEquals(10, PlayerSave.Save.getOdinWins());
    }
}
