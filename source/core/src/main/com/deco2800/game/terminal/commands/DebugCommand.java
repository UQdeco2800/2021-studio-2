package com.deco2800.game.terminal.commands;

import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A command for toggling debug mode on and off.
 */
public class DebugCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(DebugCommand.class);

  /**
   * Toggles debug mode on or off if the corresponding argument is received.
   * @param args command arguments
   */
  public void action(ArrayList<String> args) {
    if (!isValid(args)) {
      return;
    }

    String arg = args.get(0);
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
  boolean isValid(ArrayList<String> args) {
    return args.size() == 1;
  }
}
