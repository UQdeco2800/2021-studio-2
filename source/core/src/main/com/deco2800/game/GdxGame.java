package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.screens.MainMenuScreen;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {

  @Override
  public void create() {
    setScreen(new MainMenuScreen(this));
  }

  public void setScreen(ScreenType screenType) {
    getScreen().dispose();
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    getScreen().dispose();
  }

  private Screen newScreen(ScreenType screenType) {
    switch (screenType) {
      case MainMenu:
        return new MainMenuScreen(this);
      case MainGame:
        return new MainGameScreen(this);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MainMenu, MainGame
  }

  public void exit() {
    app.exit();
  }
}
