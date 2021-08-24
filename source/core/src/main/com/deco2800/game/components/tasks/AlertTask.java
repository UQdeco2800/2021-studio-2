package com.deco2800.game.components.tasks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;

/** Chases a target entity but stays far enough away so it can attack safely */
public class AlertTask extends MovementTask {

  private long timeDiscoveredTarget;
  /**
   * @param target The entity to chase.
   */
  public AlertTask(Vector2 target, long timeDiscoveredTarget) {
    super(target, timeDiscoveredTarget);
  }

  @Override
  public void start() {
    //Deadzone
    if (this.discover()) {
      owner.getEntity().getEvents().trigger("chaseStart");
    }
  }


  @Override
  public void update() {
    this.update();
    if (this.getStatus() != Status.ACTIVE) {
      this.start();
    }
  }
}
