package com.deco2800.game.ecs;

import com.badlogic.gdx.utils.Array;

public class EntityService {
  private static final int INITIAL_CAPACITY = 16;

  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);

  public void register(Entity entity) {
    entities.add(entity);
    entity.create();
  }

  public void unregister(Entity entity) {
    entities.removeValue(entity, true);
  }

  public void update() {
    for (Entity entity : entities) {
      entity.earlyUpdate();
      entity.update();
    }
  }

  public void dispose() {
    for (Entity entity : entities) {
      entity.dispose();
    }
  }
}
