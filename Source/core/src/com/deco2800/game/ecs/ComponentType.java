package com.deco2800.game.ecs;

import com.badlogic.gdx.utils.ObjectMap;

public class ComponentType {
  private static final ObjectMap<Class<? extends Component>, ComponentType> componentTypes =
      new ObjectMap<>();
  private static int nextId = 0;

  private final int id;

  public static ComponentType getFrom(Class<? extends Component> type) {
    ComponentType componentType = componentTypes.get(type);
    if (componentType == null) {
      componentType = new ComponentType();
      componentTypes.put(type, componentType);
    }
    return componentType;
  }

  public int getId() {
    return id;
  }

  private ComponentType() {
    id = nextId;
    nextId++;
  }
}
