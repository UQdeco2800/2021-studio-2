package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.input.InputComponent;

/**
 * Input handler for the debug terminal for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 *
 * <p>The debug terminal can be opened and closed by scrolling vertically and a message can be entered via
 * the keyboard.
 */
public class TouchTextBoxInputComponent extends InputComponent {
    private TextBox textBox;

    public TouchTextBoxInputComponent() {
        super(9);
    }

    public TouchTextBoxInputComponent(TextBox textBox) {
        this();
        this.textBox = textBox;
    }


    @Override
    public void create() {
        super.create();
        textBox = entity.getComponent(TextBox.class);
    }

    /**
     * Handles input if the text vox is open. This is because keyDown events are
     * triggered alongside keyTyped events. If the user is typing in the text box, the input shouldn't
     * trigger any other input handlers.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        return textBox.isOpen();
    }

    /**
     * Handles input if the text box is open. If 'enter' is typed, the entered message will be
     * processed, otherwise the message will be updated with the new character.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyTyped(char)
     */
    @Override
    public boolean keyTyped(char character) {
        if (textBox.isOpen()) {
            return true;
        }
        return false;
    }

    /**
     * Handles input if the text box is open. This is because keyUp events are
     * triggered alongside keyTyped events. If the user is typing in the text box, the input shouldn't
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
