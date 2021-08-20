package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.input.InputComponent;

/**
 * Input handler for the text box for keyboard input.
 * This input handler only uses keyboard input.
 */
public class KeyboardTextBoxInputComponent extends InputComponent {
    private static final int SKIP_KEY = Input.Keys.ESCAPE;

    private TextBox textBox;

    public KeyboardTextBoxInputComponent() {
        super(9);
    }

    public KeyboardTextBoxInputComponent(TextBox textBox) {
        this();
        this.textBox = textBox;
    }

    @Override
    public void create() {
        super.create();
        textBox = entity.getComponent(TextBox.class);
    }

    /**
     * If the toggle key is pressed, the terminal will open / close.
     *
     * <p>Otherwise, handles input if the terminal is open. This is because keyDown events are
     * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
     * trigger any other input handlers.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
//    @Override
//    public boolean keyDown(int keycode) {
//
//        if (textBox.isOpen()) {
//            //Finishes the line of dialogue in the text box.
//        }
//
//        return textBox.isOpen();
//    }

    @Override
    public boolean keyDown(int keycode) {
        // handle open and close terminal
        if (keycode == SKIP_KEY) {
            textBox.toggleIsOpen();
            return true;
        }

        return textBox.isOpen();
    }

    /**
     * Handles input if the terminal is open. If 'enter' is typed, the entered message will be
     * processed, otherwise the message will be updated with the new character.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyTyped(char)
     */
    @Override
    public boolean keyTyped(char character) {
        if (!textBox.isOpen()) {
            return false;
        }

        if (character == SKIP_KEY) {
            textBox.handleEscape();
            return true;
        }
        return false;
    }

    /**
     * Handles input if the terminal is open. This is because keyUp events are
     * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
     * trigger any other input handlers.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        return textBox.isOpen();
    }
}
