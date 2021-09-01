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
    void shouldOpenOnDialogue() {
        TextBox textBox = new TextBox();

        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();

        assertTrue(textBox.isOpen());
    }

    @Test
    void shouldDisplayMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();

        assertEquals("Message 1", textBox.getMessage());
    }

    @Test
    void shouldGoNextMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.nextMessage();

        assertEquals("Message 2", textBox.getMessage());
    }

    @Test
    void shouldGoNextMessages() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.nextMessage();
        textBox.nextMessage();

        assertEquals("Message 3", textBox.getMessage());
    }

    @Test
    void initialSubMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();

        assertEquals("", textBox.getSubMessage());
    }

    @Test
    void shouldSkipMessageLoading() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.setSkip();
        textBox.setSubMessage();

        assertEquals("Message 1", textBox.getSubMessage());
    }

    @Test
    void shouldLoadCharacter() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.setSubMessage();

        assertEquals("M", textBox.getSubMessage());
    }

    @Test
    void shouldLoadMultipleCharacters() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.setSubMessage();
        textBox.setSubMessage();
        textBox.setSubMessage();

        assertEquals("Mes", textBox.getSubMessage());
    }

    @Test
    void shouldGoNextMessageWhenSkipped() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.setSkip();
        textBox.setSkip();

        assertEquals("", textBox.getSubMessage());
    }

    @Test
    void shouldCloseWhenSkippingMessage() {
        TextBox textBox = new TextBox();
        textBox.setClosed();

        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.setSkip();
        textBox.setSubMessage();
        textBox.setClosed();

        assertFalse(textBox.isOpen());
    }
}