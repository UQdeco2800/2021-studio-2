package com.deco2800.game.terminal;

import com.deco2800.game.components.Component;
import com.deco2800.game.terminal.commands.Command;
import com.deco2800.game.terminal.commands.DebugCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class Terminal extends Component {
  private static final Logger logger = LoggerFactory.getLogger(Terminal.class);
  private HashMap<String, Command> commands = new HashMap<>();
  private String incompleteMessage = "";
  private Boolean isOpen = false;

  @Override
  public void create() {
    super.create();
    addCommand("debug", new DebugCommand());
  }

  public String getIncompleteMessage() {
    return incompleteMessage;
  }

  public boolean isOpen() {
    return isOpen;
  }

  public void toggleIsOpen() {
    isOpen = !isOpen;
    if (!isOpen) {
      resetIncompleteMessage();
    }
  }

  public void addCommand(String name, Command command) {
    if (commands.containsKey(name)) {
      logger.error("Command {} is already registered", name);
    }
    commands.put(name, command);
  }

  public void processMessage() {
      // strip leading and trailing whitespace
      String message = incompleteMessage.trim();

      // separate command from args
      String[] sections = message.split(" ");
      String command = sections[0];
      String[] args = Arrays.copyOfRange(sections, 1, sections.length);

      if (commands.containsKey(command)) {
        commands.get(command).action(args);
      }

    resetIncompleteMessage();
  }

  public void appendToMessage(char character) {
    incompleteMessage = incompleteMessage + character;
  }

  public void handleBackspace() {
    int messageLength = incompleteMessage.length();
    if (messageLength != 0) {
      incompleteMessage = incompleteMessage.substring(0, messageLength - 1);
    }
  }

  private void resetIncompleteMessage() {
    incompleteMessage = "";
  }
}
