package com.deco2800.game.components.tasks;


import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;

/** Chases a target entity if they've been alerted or can see the target */
public class AlertableChaseTask extends ChaseTask implements PriorityTask {
  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public AlertableChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    super(target, priority, viewDistance, maxChaseDistance);
  }

  @Override
  public void update() {
    super.update();
    for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
      //get event triggered
      //if (entity.getEvents().addListener();)
    }
  }
}
