package com.deco2800.game.areas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.components.player.PlayerActionComponent;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityFactory;
import com.deco2800.game.math.RandomUtils;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.rendering.TextureRenderComponent;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final int NUM_TREES = 5;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);

  private final TerrainFactory terrainFactory;

  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  public void create() {
    // Make terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) terrain.getMap().getLayers().get(0);

    // Spawn Trees
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = new GridPoint2(terrainLayer.getWidth() - 2, terrainLayer.getHeight() - 2);
    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = EntityFactory.createTree();
      System.out.println("Spawning tree at " + randomPos);
      spawnEntityAt(tree, randomPos, false);
    }

    // Spawn entities
    Entity player = EntityFactory.createPlayer();
    System.out.println("Spawning player at " + PLAYER_SPAWN);
    spawnEntityAt(player, PLAYER_SPAWN, true);
  }
}
