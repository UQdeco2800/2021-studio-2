package com.deco2800.game.terminal.commands;

import java.util.ArrayList;

/**
 * A generic command class.
 */
public interface Command {
  void action(ArrayList<String> args);
}
