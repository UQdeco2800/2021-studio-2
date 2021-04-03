package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

public class WanderTask implements PriorityTask {
  private final float wanderRadius;
  private Entity entity;
  private Vector3 targetPosition;

  public WanderTask(float wanderRadius) {
    this.wanderRadius = wanderRadius;
  }

  @Override
  public int getPriority() {
    return 1;
  }

  @Override
  public void start(Entity entity) {
    this.entity = entity;
  }

  @Override
  public void update() { }

  @Override
  public void stop() { }

  @Override
  public Status getStatus() {
    // Wandering never finishes
    return Status.Active;
  }
}
