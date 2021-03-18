package com.deco2800.game.ecs;

public class Component {
  protected Entity entity;
  protected boolean enabled = true;

  public void create() {}

  public void earlyUpdate() {}

  public void update() {}

  public void dispose() {}

  public final void triggerUpdate() {
    if (enabled) {
      update();
    }
  }

  public final void triggerEarlyUpdate() {
    if (enabled) {
      earlyUpdate();
    }
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
