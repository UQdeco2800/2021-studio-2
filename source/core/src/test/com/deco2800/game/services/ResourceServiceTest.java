package com.deco2800.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class ResourceServiceTest {
  @Test
  void loadAllShouldLoadAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String texture3 = "test/files/heart.png";
    String[] textures = {texture1, texture2, texture3};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    resourceService.loadAll();

    verify(assetManager).load(texture1, Texture.class);
    verify(assetManager).load(texture2, Texture.class);
    verify(assetManager).load(texture3, Texture.class);

    assertTrue(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertTrue(assetManager.contains(texture3, Texture.class));
  }

  @Test
  void loadForMillisShouldLoadAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String texture3 = "test/files/heart.png";
    String[] textures = {texture1, texture2, texture3};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    while (!resourceService.loadForMillis(1)) {
      ;
    }

    verify(assetManager).load(texture1, Texture.class);
    verify(assetManager).load(texture2, Texture.class);
    verify(assetManager).load(texture3, Texture.class);

    assertTrue(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertTrue(assetManager.contains(texture3, Texture.class));
  }
}
