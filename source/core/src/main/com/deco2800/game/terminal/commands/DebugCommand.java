package com.deco2800.game.terminal.commands;

import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

/**
 * A command for toggling debug mode on and off.
 */
public class DebugCommand implements Command {
  /**
   * Toggles debug mode on or off if the corresponding argument is received.
   * @param args command arguments
   */
  public boolean action(ArrayList<String> args) {
    if (!isValid(args)) {
      return false;
    }

    String arg = args.get(0);
    switch (arg) {
      case "on":
        ServiceLocator.getRenderService().getDebug().setActive(true);
        return true;
      case "off":
        ServiceLocator.getRenderService().getDebug().setActive(false);
        return true;
      default:
        return false;
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
