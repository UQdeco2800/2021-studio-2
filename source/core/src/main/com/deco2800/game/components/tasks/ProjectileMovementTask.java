package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class ProjectileMovementTask extends MovementTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(ProjectileMovementTask.class);

  public ProjectileMovementTask(Vector2 target, Vector2 moveSpeed) {
    super(target, moveSpeed);
  }

  @Override
  public void update() {
    super.update();
  }

  public int getPriority() {
    if (stoppedMoving()) {
      //Arrows disappears when at destination to stop it from looping in the same place
      owner.getEntity().prepareDispose();
      return (-1);
    } else {
      return (10);
    }
  }

  @Override
  public void stop() {
    super.stop();
    owner.getEntity().prepareDispose();
  }

  public boolean stoppedMoving() {
    return (isAtTarget() || checkIfStuck());
  }
}
