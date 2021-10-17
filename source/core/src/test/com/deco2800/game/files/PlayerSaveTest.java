package com.deco2800.game.files;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlayerSaveTest {

    @Test
    void testDefaultSave() {
        PlayerSave.Save testSave = PlayerSave.initial();

        assertFalse(testSave.hasPlayed);

        assertEquals(testSave.lokiEnc, 0);
        assertEquals(testSave.thorEnc, 0);
        assertEquals(testSave.odinEnc, 0);

        assertEquals(testSave.lokiWins, 0);
        assertEquals(testSave.thorWins, 0);
        assertEquals(testSave.odinWins, 0);
    }

    @Test
    void testSaveChange() {
        PlayerSave.Save testSave = PlayerSave.initial();
        PlayerSave.Save backupSave = PlayerSave.load();

        testSave.hasPlayed = true;

        testSave.lokiEnc = 8;
        testSave.thorEnc = 7;
        testSave.odinEnc = 5;

        testSave.lokiWins = 5;
        testSave.thorWins = 4;
        testSave.odinWins = 3;

        PlayerSave.write(testSave);

        PlayerSave.Save loadedSave = PlayerSave.load();

        assertEquals(testSave.hasPlayed, loadedSave.hasPlayed);

        assertEquals(testSave.lokiEnc, loadedSave.lokiEnc);
        assertEquals(testSave.thorEnc, loadedSave.thorEnc);
        assertEquals(testSave.odinEnc, loadedSave.odinEnc);

        assertEquals(testSave.lokiWins, loadedSave.lokiWins);
        assertEquals(testSave.thorWins, loadedSave.thorWins);
        assertEquals(testSave.odinWins, loadedSave.odinWins);

        PlayerSave.write(backupSave);
    }
}
