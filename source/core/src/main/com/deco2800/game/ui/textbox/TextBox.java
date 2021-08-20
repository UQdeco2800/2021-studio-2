package com.deco2800.game.ui.textbox;

import com.deco2800.game.components.Component;
import com.deco2800.game.ui.terminal.commands.Command;
import com.deco2800.game.ui.terminal.commands.DebugCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TextBox extends Component {
    private static final Logger logger = LoggerFactory.getLogger(TextBox.class);
    private final Map<String, Command> commands;
    private String enteredMessage = "";
    private boolean isOpen = false;

    public TextBox() {
        this(new HashMap<>());
    }

    public TextBox(Map<String, Command> commands) {
        this.commands = commands;

        addCommand("debug", new DebugCommand());
    }

    /** @return text box is open */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Opens the text box.
     */
    public void setOpen() {
        logger.debug("Opening text box");
        isOpen = true;
    }

    /**
     * Toggles between the terminal being open and closed.
     */
    public void toggleIsOpen() {
        if (isOpen) {
            this.setClosed();
        } else {
            this.setOpen();
        }
    }

    /**
     * Closes the terminal and clears the stored message.
     */
    public void setClosed() {
        logger.debug("Closing text box");
        isOpen = false;
    }

    public void handleEscape() {
        logger.debug("Handling escape key press");
        setClosed();
    }

    /**
     * Adds a command to the list of valid text box commands.
     *
     * @param name command name
     * @param command command
     */
    public void addCommand(String name, Command command) {
        logger.debug("Adding command: {}", name);
        if (commands.containsKey(name)) {
            logger.error("Command {} is already registered", name);
        }
        commands.put(name, command);
    }
}