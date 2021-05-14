package com.deco2800.game.terminal;

import com.deco2800.game.components.Component;
import com.deco2800.game.terminal.commands.Command;
import com.deco2800.game.terminal.commands.DebugCommand;
import java.util.Arrays;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * State tracker for a debug terminal. Any commands to be actioned through the terminal input should
 * be added to the map of commands.
 */
public class Terminal extends Component {
  private static final Logger logger = LoggerFactory.getLogger(Terminal.class);
  private final Map<String, Command> commands;
  private String enteredMessage = "";
  private boolean isOpen = false;

  public Terminal() {
    this(new HashMap<>());
  }

  public Terminal(Map<String, Command> commands) {
    this.commands = commands;

    addCommand("debug", new DebugCommand());
  }

  /** @return message entered by user */
  public String getEnteredMessage() {
    return enteredMessage;
  }

  /** @return console is open */
  public boolean isOpen() {
    return isOpen;
  }

  /**
   * Toggles between the terminal being open and closed. When closed, the terminal message will be
   * cleared.
   */
  public void toggleIsOpen() {
    isOpen = !isOpen;
    if (!isOpen) {
      setEnteredMessage("");
    }
  }

  /**
   * Adds a command to the list of valid terminal commands.
   *
   * @param name command name
   * @param command command
   */
  public void addCommand(String name, Command command) {
    if (commands.containsKey(name)) {
      logger.error("Command {} is already registered", name);
    }
    commands.put(name, command);
  }

  /**
   * Processes the completed message entered by the user. If the message corresponds to a valid
   * command, the command will be actioned.
   */
  public void processMessage() {
    // strip leading and trailing whitespace
    String message = enteredMessage.trim();

    // separate command from args
    String[] sections = message.split(" ");
    String command = sections[0];

    ArrayList<String> args = new ArrayList<>(Arrays.asList(sections).subList(1, sections.length));

    if (commands.containsKey(command)) {
      commands.get(command).action(args);
    }

    setEnteredMessage("");
  }

  /**
   * Appends the character to the end of the entered message.
   *
   * @param character character to append
   */
  public void appendToMessage(char character) {
    enteredMessage = enteredMessage + character;
  }

  /** Removes the last character of the entered message. */
  public void handleBackspace() {
    int messageLength = enteredMessage.length();
    if (messageLength != 0) {
      enteredMessage = enteredMessage.substring(0, messageLength - 1);
    }
  }

  /** Sets the entered message to the empty string. */
  public void setEnteredMessage(String text) {
    enteredMessage = text;
  }
}
