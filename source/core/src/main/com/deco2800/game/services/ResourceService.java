package com.deco2800.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceService {
  private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);
  private static final int loadingUpdateInterval = 10; //adjust for desired loading interval
  private static final String[] forestTextures = {
    "box_boy_leaf.png",
    "ghost_1.png",
    "ghost_2.png",
    "ghost_3.png",
    "grass_1.png",
    "grass_2.png",
    "grass_3.png"
  };
  private final AssetManager assetManager = new AssetManager();

  public ResourceService() {
    loadTextures();

    while (!assetManager.update(loadingUpdateInterval)) {
      int progress = (int) (assetManager.getProgress() * 100);
      logger.info("Loading... {}%", progress);
    }
  }

  public <T> T getResource(String filename, Class<T> type) {
    return assetManager.get(filename, type);
  }

  private void loadTextures() {
    try {
      for (String texture : forestTextures) {
        assetManager.load(texture, Texture.class);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  private void loadAudio() {}
}
