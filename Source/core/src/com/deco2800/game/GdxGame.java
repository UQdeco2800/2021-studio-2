package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.deco2800.game.screens.MainGameScreen;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  MainGameScreen mainGameScreen;

  @Override
  public void create() {
    mainGameScreen = new MainGameScreen(this);
    setScreen(mainGameScreen);
  }

  @Override
  public void dispose() {
    mainGameScreen.dispose();
  }
}
