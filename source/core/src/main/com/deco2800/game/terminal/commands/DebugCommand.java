package com.deco2800.game.terminal.commands;

import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command for toggling debug mode on and off.
 */
public class DebugCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(DebugCommand.class);

  /**
   * Toggles debug mode on or off if the corresponding argument is received.
   * @param args command arguments
   */
  public void action(String[] args) {
    if (!isValid(args)) {
      return;
    }

    String arg = args[0];
    switch (arg) {
      case "on":
        ServiceLocator.getRenderService().getDebug().setActive(true);
        break;
      case "off":
        ServiceLocator.getRenderService().getDebug().setActive(false);
        break;
    }
  }

  /**
   * Validates the command arguments.
   * @param args command arguments
   * @return is valid
   */
  boolean isValid(String[] args) {
    return args.length == 1;
  }
}
