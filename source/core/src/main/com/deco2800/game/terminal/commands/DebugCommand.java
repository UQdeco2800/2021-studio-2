package com.deco2800.game.terminal.commands;

import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(DebugCommand.class);

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

  boolean isValid(String[] args) {
    return args.length == 1;
  }
}
