package com.deco2800.game.input;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.terminal.KeyboardTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyboardInputFactory creates input handlers that process keyboard and touch support.
 */
public class KeyboardInputFactory extends InputFactory {
    private final static Logger logger = LoggerFactory.getLogger(KeyboardInputFactory.class);

    /**
     * Creates an input handler for the player
     * @return Player input handler
     */
    @Override
    public InputComponent createForPlayer() {
        return new KeyboardPlayerInputComponent();
    }

    /**
     * Creates an input handler for the terminal
     *
     * @return Terminal input handler
     */
    public InputComponent createForTerminal() {
        return new KeyboardTerminalInputComponent();
    }
}
