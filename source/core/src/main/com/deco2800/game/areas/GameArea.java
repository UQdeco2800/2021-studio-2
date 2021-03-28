package com.deco2800.game.areas;

import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.areas.terrain.TerrainFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea implements Disposable {
  protected final TerrainFactory terrainFactory;
  protected List<Entity> areaEntities;

  public GameArea(TerrainFactory terrainFactory) {
    this.terrainFactory = terrainFactory;
    areaEntities = new ArrayList<>();
  }

  /** Create the game area in the world. */
  public abstract void create();

  /** Dispose of all internal entities in the area */
  public void dispose() {
    for (Entity entity : areaEntities) {
      entity.dispose();
    }
  }

  protected void spawnEntity(Entity entity) {
    areaEntities.add(entity);
    ServiceLocator.getEntityService().register(entity);
  }
}
