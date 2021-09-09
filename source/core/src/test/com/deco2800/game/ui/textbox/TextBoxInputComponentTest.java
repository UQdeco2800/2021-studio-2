package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.Input;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
class TextBoxInputComponentTest {
    @Test
    void shouldCloseTextBox() {
        TextBox textBox = spy(TextBox.class);
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setNewCharactersOff();

        textBox.setClosed();

        textBoxInput.keyDown(Input.Keys.ESCAPE);
        assertFalse(textBox.isOpen());
    }

    @Test
    void shouldNotClose() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setNewCharactersOff();

        textBoxInput.keyDown(Input.Keys.SPACE);
        assertTrue(textBox.isOpen());

        textBoxInput.keyDown(Input.Keys.SPACE);
        assertTrue(textBox.isOpen());
    }

    @Test
    void shouldBeClosed() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setNewCharactersOff();

        textBoxInput.keyDown(Input.Keys.SPACE);
        assertTrue(textBox.isOpen());

        textBoxInput.keyDown(Input.Keys.ESCAPE);
        assertFalse(textBox.isOpen());
    }

    @Test
    void shouldNotDisplay() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();

        textBoxInput.keyTyped(' ');
        textBoxInput.keyTyped(' ');

        assertEquals("", textBox.getSubMessage());
    }

    @Test
    void shouldGoNextMessage() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.keyTyped(' ');
        textBoxInput.keyTyped(' ');

        assertEquals("Test 1 Message 1", textBox.getMessage());

        textBoxInput.keyTyped(' ');
        textBoxInput.keyTyped(' ');

        assertEquals("Test 1 Message 2", textBox.getMessage());
    }

    @Test
    void shouldFlushMessageKey() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setRandomFirstEncounter(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.keyTyped(' ');
        textBox.setSubMessage();

        assertEquals("Test 1 Message 1", textBox.getSubMessage());
    }

    @Test
    void shouldFlushMessageTouch() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setRandomFirstEncounter(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.touchDown(0, 0, 0, 0);
        textBox.setSubMessage();

        assertEquals("Test 1 Message 1", textBox.getSubMessage());
    }

    @Test
    void shouldFlushMessageTouchEmpty() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setRandomFirstEncounter(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.touchDown(0, 0, 0, 0);
        textBox.setSubMessage();

        textBoxInput.touchDown(0, 0, 0, 0);
        textBox.setSubMessage();

        assertEquals("T", textBox.getSubMessage());
    }

    @Test
    void shouldFlushMessageTouch2() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setRandomFirstEncounter(RandomDialogueSet.TEST);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.touchDown(0, 0, 0, 0);
        textBoxInput.touchDown(0, 0, 0, 0);

        textBoxInput.touchDown(0, 0, 0, 0);
        textBox.setSubMessage();

        assertEquals("Test 1 Message 2", textBox.getSubMessage());
    }

    @Test
    void shouldFlushMessageTouchKey() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.touchDown(0, 0, 0, 0);
        textBoxInput.keyTyped(' ');

        assertEquals("Test 1 Message 1", textBox.getMessage());

        textBoxInput.keyTyped(' ');
        textBoxInput.touchDown(0, 0, 0, 0);

        assertEquals("Test 1 Message 2", textBox.getMessage());
    }

    @Test
    void shouldGoNextMessageTouch() {
        TextBox textBox = new TextBox();
        TextBoxInputComponent textBoxInput = new TextBoxInputComponent(textBox);
        textBox.setDialogue(Dialogue.TEST_1);
        textBox.setNewCharactersOff();
        textBox.acceptInput();

        textBoxInput.touchDown(0, 0, 0, 0);
        textBoxInput.touchDown(0, 0, 0, 0);

        assertEquals("Test 1 Message 1", textBox.getMessage());

        textBoxInput.touchDown(0, 0, 0, 0);
        textBoxInput.touchDown(0, 0, 0, 0);

        assertEquals("Test 1 Message 2", textBox.getMessage());
    }
}
