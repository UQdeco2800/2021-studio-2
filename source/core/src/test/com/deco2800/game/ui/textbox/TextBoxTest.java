package com.deco2800.game.ui.textbox;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class TextBoxTest {

    @Test
    void shouldSetOpenClosed() {
        TextBox textBox = new TextBox();
        textBox.setNewCharactersOff();

        textBox.setClosed();
        assertFalse(textBox.isOpen());

        textBox.setOpen();
        assertTrue(textBox.isOpen());

        textBox.setClosed();
        assertFalse(textBox.isOpen());
    }

    @Test
    void shouldToggleIsOpen() {
        TextBox textBox = new TextBox();
        textBox.setNewCharactersOff();

        textBox.setClosed();

        textBox.toggleIsOpen();
        assertTrue(textBox.isOpen());
        textBox.toggleIsOpen();
        assertFalse(textBox.isOpen());
    }

    @Test
    void shouldNotOpenOnDialogue() {
        TextBox textBox = new TextBox();

        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();

        assertFalse(textBox.isOpen());
    }

    @Test
    void shouldDisplayMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();

        assertEquals("", textBox.getMessage());
    }

    @Test
    void shouldGoNextMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();
        textBox.nextMessage();

        assertEquals("Test 1 Message 1", textBox.getMessage());
    }

    @Test
    void shouldGoNextMessages() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.nextMessage();

        assertEquals("Test 1 Message 2", textBox.getMessage());
    }

    @Test
    void initialSubMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();

        assertEquals("", textBox.getSubMessage());
    }

    @Test
    void shouldLoadCharacter() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setOpen();
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.setSubMessage();

        assertEquals("T", textBox.getSubMessage());
    }

    @Test
    void shouldLoadMultipleCharacters() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setOpen();
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.setSubMessage();
        textBox.setSubMessage();
        textBox.setSubMessage();

        assertEquals("Tes", textBox.getSubMessage());
    }

    @Test
    void shouldGoNextMessageWhenSkipped() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setOpen();
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.setSkip();
        textBox.setSkip();

        assertEquals("", textBox.getSubMessage());
    }

    @Test
    void shouldCloseWhenSkippingMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();
        textBox.setSkip();
        textBox.setSubMessage();
        textBox.setClosed();

        assertFalse(textBox.isOpen());
    }

    @Test
    void shouldHandleEscapeTest() {
        TextBox textBox = new TextBox();
        textBox.setClosed();
        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();
        textBox.setOpen();
        textBox.handleEscape();

        assertFalse(textBox.isOpen());
    }

    @Test
    void setRandomFirstEncounterDialogueSetTest() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setRandomFirstEncounter(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();

        String message = textBox.getMessage();

        assertEquals(message, Dialogue.TEST_1.getMessage(0));
    }

    @Test
    void setRandomBeatenDialogueSetTest() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setRandomBeatenDialogueSet(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();

        String message = textBox.getMessage();

        assertEquals(message, Dialogue.TEST_1.getMessage(0));
    }

    @Test
    void setRandomDefeatedDialogueSetTest2() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setRandomBeatenDialogueSet(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();

        String message = textBox.getMessage();

        assertTrue(message.equals(Dialogue.TEST_1.getMessage(0)) ||
                message.equals(Dialogue.TEST_1.getMessage(1)));
    }

    @Test
    void setOrderedDialogueSetTest() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();

        String message = textBox.getMessage();

        assertEquals(message, Dialogue.TEST_1.getMessage(0));
    }

    @Test
    void setOrderedDialogueSetTest2() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();

        String message = textBox.getMessage();

        assertEquals(message, Dialogue.TEST_2.getMessage(0));
    }

    @Test
    void setOrderedDialogueSetTest3() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.nextMessage();

        String message = textBox.getMessage();

        assertEquals(message, Dialogue.TEST_1.getMessage(1));
    }

    @Test
    void setOrderedDialogueSetTest4() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.nextMessage();

        String message = textBox.getMessage();

        assertEquals(message, Dialogue.TEST_2.getMessage(2));
    }

    @Test
    void isMainCharacterShowingTest() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.nextMessage();

        assertTrue(textBox.isMainCharacterShowing());
    }

    @Test
    void isMainCharacterShowingTest2() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.nextMessage();

        assertFalse(textBox.isMainCharacterShowing());
    }

    @Test
    void isMainCharacterShowingTest3() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setOrderedDialogue(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.nextMessage();

        assertFalse(textBox.isMainCharacterShowing());
    }
}
