package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.deco2800.game.screens.MainGameScreen;

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
