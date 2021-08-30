package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/** Chases a target entity and alerts nearby enemies after 3 secinds */
public class AlertChaseTask extends ChaseTask implements PriorityTask {

  private long timeDiscoveredTarget;
  private boolean alert;
  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public AlertChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    super(target, priority, viewDistance, maxChaseDistance);
  }

  @Override
  public void start() {
    super.start();
    alert = false;
    timeDiscoveredTarget = System.nanoTime();
  }

  @Override
  public void stop() {
    super.start();
    alert = false;
    owner.getEntity().getEvents().trigger("unAlert");
  }

  @Override
  public void update() {
    super.update();
    if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeDiscoveredTarget) >= 3000) {
      /*
      Logger logger = LoggerFactory.getLogger(WanderTask.class);
      logger.info("found3 secs");
      */
      owner.getEntity().getEvents().trigger("alert");
      updateAlert();
    }
  }

  // set alert if other ghost found enemy

  public void updateAlert() {
    this.alert = true;
  }
}