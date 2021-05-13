package com.deco2800.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class UserSettings {
  private static final String SETTINGS_ID = "Settings";

  public static Preferences get() {
    return Gdx.app.getPreferences(SETTINGS_ID);
  }
}
