package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.Input;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class KeyboardTextBoxInputComponentTest {

    @Test
    void shouldNotClose() {
        TextBox textBox = new TextBox();
        KeyboardTextBoxInputComponent textBoxInput = new KeyboardTextBoxInputComponent(textBox);
        textBox.setNewCharactersOff();

        textBoxInput.keyDown(Input.Keys.SPACE);
        assertTrue(textBox.isOpen());

        textBoxInput.keyDown(Input.Keys.SPACE);
        assertTrue(textBox.isOpen());
    }

    @Test
    void shouldBeClosed() {
        TextBox textBox = new TextBox();
        KeyboardTextBoxInputComponent textBoxInput = new KeyboardTextBoxInputComponent(textBox);
        textBox.setNewCharactersOff();

        textBoxInput.keyDown(Input.Keys.SPACE);
        assertTrue(textBox.isOpen());

        textBoxInput.keyDown(Input.Keys.ESCAPE);
        assertFalse(textBox.isOpen());
    }

    @Test
    void shouldFlushMessage() {
        TextBox textBox = new TextBox();
        KeyboardTextBoxInputComponent textBoxInput = new KeyboardTextBoxInputComponent(textBox);
        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.keyTyped(' ');
        textBox.setSubMessage();

        assertEquals("Message 1", textBox.getSubMessage());
    }

    @Test
    void shouldNotDisplay() {
        TextBox textBox = new TextBox();
        KeyboardTextBoxInputComponent textBoxInput = new KeyboardTextBoxInputComponent(textBox);
        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();

        textBoxInput.keyTyped(' ');
        textBoxInput.keyTyped(' ');

        assertEquals("", textBox.getSubMessage());
    }

    @Test
    void shouldGoNextMessage() {
        TextBox textBox = new TextBox();
        KeyboardTextBoxInputComponent textBoxInput = new KeyboardTextBoxInputComponent(textBox);
        textBox.setDialogue(Dialogue.TEST);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.keyTyped(' ');
        textBoxInput.keyTyped(' ');

        assertEquals("Message 2", textBox.getMessage());

        textBoxInput.keyTyped(' ');
        textBoxInput.keyTyped(' ');

        assertEquals("Message 3", textBox.getMessage());
    }
}
