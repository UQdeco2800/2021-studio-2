package com.deco2800.game.input;

import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.ui.terminal.KeyboardTerminalInputComponent;
import com.deco2800.game.ui.textbox.TextBoxInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyboardInputFactory creates input handlers that process keyboard and touch support.
 */
public class KeyboardInputFactory extends InputFactory {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardInputFactory.class);

    /**
     * Creates an input handler for the player.
     *
     * @return Player input handler
     */
    @Override
    public InputComponent createForPlayer() {
        logger.debug("Creating player input handler");
        return new KeyboardPlayerInputComponent();
    }

    /**
     * Creates an input handler for the terminal.
     *
     * @return Terminal input handler
     */
    public InputComponent createForTerminal() {
        logger.debug("Creating terminal input handler");
        return new KeyboardTerminalInputComponent();
    }

    /**
     * Creates an input handler for the text box.
     *
     * @return TextBox input handler
     */
    public InputComponent createForTextBox() {
        logger.debug("Creating text box input handler");
        return new TextBoxInputComponent();
    }
}
