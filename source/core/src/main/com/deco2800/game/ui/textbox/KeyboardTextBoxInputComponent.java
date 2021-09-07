package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;

/**
 * Input handler for the text box for keyboard input.
 * This input handler only uses keyboard input.
 */
public class KeyboardTextBoxInputComponent extends InputComponent {
    private static final int SKIP_KEY = Input.Keys.ESCAPE;

    /** Instance of the Text Box that will be manipulated by input. */
    private TextBox textBox;

    /** Constructor to create the input with a priority of 9, under terminal. */
    public KeyboardTextBoxInputComponent() {
        super(9);
    }

    /** Instantiates the InputComponent and stores the TextBox instance within the class. */
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
     * At the moment, this is used to trigger the text box to appear on the screen but
     * this will be changed to be triggered on events within the game.
     */
    @Override
    public boolean keyDown(int keycode) {
        // handle open and close terminal
        if (keycode == SKIP_KEY) {
            textBox.setClosed();
            return true;
        }

        return textBox.isOpen();
    }

    /**
     * Handles input if the text box is open. If 'ESCAPE' is typed, then the text box will close.
     * Any other keys will finish the line or it will move to the next line if the line has
     * already been finished.
     *
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        if (!textBox.isOpen() || !textBox.isAcceptingInput()) {
            return false;
        }

        if (character == SKIP_KEY) {
            textBox.handleEscape();
        } else {
            textBox.setSkip();
        }
        return true;
    }

    /**
     * Handles input if the text box is open. This is because keyUp events are
     * triggered alongside keyTyped events. If the user is typing buttons, the input shouldn't
     * trigger any other input handlers.
     *
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        if (textBox.isOpen()) {
            textBox.acceptInput();
        }
        return textBox.isOpen();
    }
}
 
