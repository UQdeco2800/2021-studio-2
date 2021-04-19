package com.deco2800.game.terminal.commands;

import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(DebugCommand.class);

  public boolean action(String[] args) {
    if (!isValid(args)) {
      return false;
    }

    String arg = args[0];
    if ("on".equals(arg)) {
      ServiceLocator.getRenderService().getDebug().setActive(true);
      return true;
    } else if ("off".equals(arg)) {
      ServiceLocator.getRenderService().getDebug().setActive(false);
      return true;
    }

    return false;
  }

  boolean isValid(String[] args) {
    return args.length == 1;
  }
}
