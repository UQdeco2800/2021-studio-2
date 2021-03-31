package com.deco2800.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceService {
  private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);
  private static final int loadingUpdateInterval = 10; // adjust for desired loading interval

  private final AssetManager assetManager = new AssetManager();

  public <T> T getAsset(String filename, Class<T> type) {
    return assetManager.get(filename, type);
  }

  public boolean containsAsset(String resourceName) {
    return assetManager.contains(resourceName);
  }

  public int getProgress() {
    return (int) (assetManager.getProgress() * 100);
  }

  public boolean loadAll() {
    return assetManager.update();
  } // blocking load

  public boolean loadForMillis(int duration) {
    return assetManager.update(duration);
  }

  public void clearAllAssets() {
    assetManager.clear();
  }

  private <T> void loadAsset(String textureName, Class<T> type) {
    try {
      assetManager.load(textureName, type);
    } catch (Exception e) {
      logger.error("Could not load {}: {}", type.getSimpleName(), textureName);
    }
  }

  private <T> void loadAssets(String[] assetNames, Class<T> type) {
    for (String resource : assetNames) {
      loadAsset(resource, type);
    }
  }

  public void loadTextures(String[] textureNames) {
    loadAssets(textureNames, Texture.class);
  }

  public void loadTextureAtlases(String[] textureAtlases) {
    loadAssets(textureAtlases, TextureAtlas.class);
  }

  public void loadSounds(String[] soundNames) {
    loadAssets(soundNames, Sound.class);
  }

  public void loadMusic(String[] musicNames) {
    loadAssets(musicNames, Music.class);
  }
}
