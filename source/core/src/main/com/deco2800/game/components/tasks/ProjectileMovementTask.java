package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.ServiceLocator;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 */
public class ProjectileMovementTask extends MovementTask implements PriorityTask {

  public ProjectileMovementTask(Vector2 target, Vector2 moveSpeed) {
    super(target, moveSpeed);
  }

  @Override
  public void update() {
    super.update();
    ServiceLocator.getRenderService().getDebug().drawLine(owner.getEntity().getCenterPosition(), target);
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
    //Arrows disappears when at destination to stop it from looping in the same place
    owner.getEntity().prepareDispose();
  }

  public boolean stoppedMoving() {
    return (isAtTarget() || checkIfStuck());
  }
}
