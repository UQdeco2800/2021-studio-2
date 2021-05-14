package com.deco2800.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.files.FileLoader.Location;
import java.io.File;

public class UserSettings {
  private static final String ROOT_DIR = "DECO2800Game";
  private static final String SETTINGS_FILE = "settings.json";

  private static final int WINDOW_WIDTH = 1280;
  private static final int WINDOW_HEIGHT = 800;

  public static Settings get() {
    String path = ROOT_DIR + File.separator + SETTINGS_FILE;
    Settings fileSettings = FileLoader.readClass(Settings.class, path, Location.External);
    // Use default values if file doesn't exist
    return fileSettings != null ? fileSettings : new Settings();
  }

  public static void set(Settings settings, boolean applyImmediate) {
    String path = ROOT_DIR + File.separator + SETTINGS_FILE;
    FileLoader.writeClass(settings, path, Location.External);

    if (applyImmediate) {
      applySettings(settings);
    }
  }

  public static void applySettings(Settings settings) {
    Gdx.graphics.setForegroundFPS(settings.fps);
    Gdx.graphics.setVSync(settings.vsync);

    if (settings.fullscreen) {
      DisplayMode displayMode = findMatching(settings.displayMode);
      if (displayMode == null) {
        displayMode = Gdx.graphics.getDisplayMode();
      }
      Gdx.graphics.setFullscreenMode(displayMode);
    } else {
      Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
    }
  }

  private static DisplayMode findMatching(DisplaySettings desiredSettings) {
    if (desiredSettings == null) {
      return null;
    }
    for (DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
      if (displayMode.refreshRate == desiredSettings.refreshRate
          && displayMode.height == desiredSettings.height
          && displayMode.width == desiredSettings.width) {
        return displayMode;
      }
    }

    return null;
  }

  public static class Settings {
    public int fps = 60;
    public boolean fullscreen = true;
    public boolean vsync = true;
    public float uiScale = 1f;
    public DisplaySettings displayMode = null;
  }

  public static class DisplaySettings {
    public int width;
    public int height;
    public int refreshRate;

    public DisplaySettings() {}

    public DisplaySettings(DisplayMode displayMode) {
      this.width = displayMode.width;
      this.height = displayMode.height;
      this.refreshRate = displayMode.refreshRate;
    }
  }

  //  public class SerializedDisplayMode implements Json.Serializable {
  //    public DisplayMode displayMode;
  //
  //    @Override
  //    public void write(Json json) {
  //      json.writeValue("width", displayMode.width);
  //      json.writeValue("height", displayMode.height);
  //      json.writeValue("refreshRate", displayMode.refreshRate);
  //    }
  //
  //    @Override
  //    public void read(Json json, JsonValue jsonData) {
  //      int width = jsonData.getInt("width");
  //      int height = jsonData.getInt("height");
  //      int refreshRate = jsonData.getInt("refreshRate");
  //
  //      // Find the matching displayMode
  //    }
  //  }
}
