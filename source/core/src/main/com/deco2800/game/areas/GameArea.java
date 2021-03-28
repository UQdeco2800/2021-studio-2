package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.TerrainComponent;
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
  protected TerrainComponent terrain;
  protected List<Entity> areaEntities;

  public GameArea() {
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

  /**
   * Spawn entity at its current position
   * @param entity Entity (not yet registered)
   */
  protected void spawnEntity(Entity entity) {
    areaEntities.add(entity);
    ServiceLocator.getEntityService().register(entity);
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   * @param entity Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param center true to center entity on the tile, false to align the bottom left corner
   */
  protected void spawnEntityAt(Entity entity, GridPoint2 tilePos, boolean center) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
    if (center) {
      float tileSize = terrain.getTileSize();
      worldPos.add(tileSize / 2, tileSize / 2).sub(entity.getCenterPosition());
    }
    entity.setPosition(worldPos);
    spawnEntity(entity);
  }
}
