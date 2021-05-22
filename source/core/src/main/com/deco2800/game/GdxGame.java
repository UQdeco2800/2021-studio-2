package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.screens.MainMenuScreen;
import com.deco2800.game.screens.SettingsScreen;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {

  @Override
  public void create() {
    loadSettings();
    setScreen(ScreenType.MainMenu);
  }

  private void loadSettings() {
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  public void setScreen(ScreenType screenType) {
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
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
      case Settings:
        return new SettingsScreen(this);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MainMenu, MainGame, Settings
  }

  public void exit() {
    app.exit();
  }
}
