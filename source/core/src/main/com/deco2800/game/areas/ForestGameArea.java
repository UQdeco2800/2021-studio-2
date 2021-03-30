package com.deco2800.game.areas;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityFactory;
import com.deco2800.game.math.RandomUtils;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;

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
      spawnEntityAt(tree, randomPos, true, false);
    }

    // Spawn entities
    Entity player = EntityFactory.createPlayer();
    spawnEntityAt(player, PLAYER_SPAWN, true, true);
  }
}
