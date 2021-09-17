package com.deco2800.game.ui.textbox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DialogueTest {

    @Test
    void getMainCharacterTest() {
        assertTrue(Dialogue.TEST_1.isMainCharacter(0));
    }

    @Test
    void getMainCharacterTest2() {
        assertTrue(Dialogue.TEST_1.isMainCharacter(2));
    }

    @Test
    void getNPCCharacterTest() {
        assertFalse(Dialogue.TEST_1.isMainCharacter(1));
    }

    @Test
    void getMessageTest() {
        assertEquals("Test 1 Message 1", Dialogue.TEST_1.getMessage(0));
    }

    @Test
    void getMessageTest2() {
        assertEquals("Test 2 Message 3", Dialogue.TEST_2.getMessage(2));
    }

    @Test
    void getSizeTest() {
        assertEquals(4, Dialogue.TEST_1.size());
    }

    @Test
    void getSizeTest2() {
        assertEquals(3, Dialogue.TEST_2.size());
    }
}
