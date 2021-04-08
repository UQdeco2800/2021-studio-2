package com.deco2800.game.ai.tasks;

import com.deco2800.game.entities.Entity;

/**
 * A default task implementation that stores the associated entity and updates status when
 * starting/stopping a task. Removes some boilerplate code from each task.
 */
public abstract class DefaultTask implements Task {
  protected Entity entity;
  protected Status status = Status.Inactive;

  @Override
  public void start(Entity entity) {
    this.entity = entity;
    status = Status.Active;
  }

  @Override
  public void update() {}

  @Override
  public void stop() {
    status = Status.Inactive;
  }

  @Override
  public Status getStatus() {
    return status;
  }
}
