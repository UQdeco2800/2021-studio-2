package com.deco2800.game.ecs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.deco2800.game.services.ServiceLocator;

public class Entity {
  private static int nextId = 0;

  private final int id;
  private final IntMap<Component> components;

  private boolean enabled = true;
  private boolean created = false;
  private Vector2 position = Vector2.Zero;
  private Vector2 scale = new Vector2(1, 1);
  private Array<Component> createdComponents;

  public Entity() {
    components = new IntMap<>(4);
    id = nextId;
    nextId++;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Vector2 getPosition() {
    return position;
  }

  public void setPosition(Vector2 position) {
    this.position = position;
  }

  public void setPosition(float x, float y) {
    this.position.x = x;
    this.position.y = y;
  }

  public Vector2 getScale() {
    return scale;
  }

  public void setScale(Vector2 scale) {
    this.scale = scale;
  }

  public void setScale(float x, float y) {
    this.scale.x = x;
    this.scale.y = y;
  }

  @SuppressWarnings("unchecked")
  public <T extends Component> T getComponent(Class<T> type) {
    ComponentType componentType = ComponentType.getFrom(type);
    return (T) components.get(componentType.getId());
  }

  public Entity addComponent(Component component) {
    if (created) {
      throw new RuntimeException("Attempted to add component after entity creation");
    }
    ComponentType componentType = ComponentType.getFrom(component.getClass());
    if (components.containsKey(componentType.getId())) {
      throw new RuntimeException("Attempted to add two components of the same type to entity");
    }
    components.put(componentType.getId(), component);

    return this;
  }

  public void create() {
    if (created) {
      throw new RuntimeException("Entity was created twice");
    }
    createdComponents = components.values().toArray();
    for (Component component : createdComponents) {
      component.setEntity(this);
      component.create();
    }
    created = true;
  }

  public void earlyUpdate() {
    if (!enabled) {
      return;
    }
    for (Component component : createdComponents) {
      component.triggerEarlyUpdate();
    }
  }

  public void update() {
    if (!enabled) {
      return;
    }
    for (Component component : createdComponents) {
      component.triggerUpdate();
    }
  }

  public void dispose() {
    for (Component component : createdComponents) {
      component.dispose();
    }
    ServiceLocator.getEntityService().unregister(this);
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Entity && ((Entity) obj).id == this.id);
  }
}
