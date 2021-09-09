package com.deco2800.game.ui.textbox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    /**
     * Message with main character speaking.
     */
    private Message messageMain;

    /**
     * Message with the NPC speaking.
     */
    private Message messageNPC;

    @BeforeEach
    void setUp() {
        messageMain = new Message(true, "This is the main character");
        messageNPC = new Message(false, "This is not the main character");
    }

    @Test
    void getMainCharacterTest() {
        assertTrue(messageMain.isMainCharacter());
    }

    @Test
    void getNPCCharacterTest() {
        assertFalse(messageNPC.isMainCharacter());
    }

    @Test
    void getMainCharacterMessageTest() {
        assertEquals("This is the main character", messageMain.getMessage());
    }

    @Test
    void getNPCMessageTest() {
        assertEquals("This is not the main character", messageNPC.getMessage());
    }
}
