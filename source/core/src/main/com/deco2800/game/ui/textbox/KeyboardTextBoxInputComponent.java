package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.Input;
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
     * At the moment, this is used to trigger the text box to appear on the screen but
     * this will be changed to be triggered on events within the game.
     */
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
     * Handles input if the text box is open. If 'ESCAPE' is typed, then the text box will close.
     * Any other keys will finish the line or it will move to the next line if the line has
     * already been finished.
     *
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        if (!textBox.isOpen()) {
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
        return textBox.isOpen();
    }
}
