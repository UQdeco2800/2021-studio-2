package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Chases a target entity until they get too far away or line of sight is lost */
public class RangedChaseTask extends ChaseTask implements PriorityTask {

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public RangedChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    super(target, priority, viewDistance, maxChaseDistance);
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(calculatePos());
    movementTask.create(owner);
    movementTask.start();
    
    this.owner.getEntity().getEvents().trigger("chaseStart");
  }


  @Override
  public void update() {
    movementTask.setTarget(calculatePos());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  @Override
  public int getPriority() {
    return (super.getPriority());
  }

  /**
   *
   * @return position that is close enough to attack but staying as far away as possible
   */
  private Vector2 calculatePos() {
    Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);
    Vector2 v1 = owner.getEntity().getPosition().cpy();
    Vector2 v2 = target.getPosition().cpy();
    Vector2 v3 = v2.cpy().sub(v1); //heading relative to entity
    float range = owner.getEntity().getAttackRange();
    if (Math.abs(v3.x) > Math.abs(v3.y)) {
      if (Math.abs(v3.x - range) < Math.abs(v3.x + range)) {
        v3.x -= range;
      } else {
        v3.x += range;
      }
    } else {
      if (Math.abs(v3.y - range) < Math.abs(v3.y + range)) {
        v3.y -= range;
      } else {
        v3.y += range;
      }
    }
    return v3.add(v1);
  }
}
