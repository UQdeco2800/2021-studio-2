package com.deco2800.game.components.tasks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;

/**
 * Chases a target entity but stays far enough away so it can attack safely
 */
public class RangedChaseTask extends ChaseTask implements PriorityTask {

    /**
     * @param target           The entity to chase.
     * @param priority         Task priority when chasing (0 when not chasing).
     * @param viewDistance     Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public RangedChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
        super(target, priority, viewDistance, maxChaseDistance);
    }

    /**
     * Start the range chase task
     */
    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(calculatePos());
        movementTask.create(owner);
        movementTask.start();
        //Deadzone
        if (super.getDistanceToTarget() < owner.getEntity().getAttackRange() * 8 / 10) {
            this.owner.getEntity().getEvents().trigger("wanderStart");
        } else if (super.getDistanceToTarget() > owner.getEntity().getAttackRange()) {
            //this.owner.getEntity().getEvents().trigger("fleeStart");
            this.owner.getEntity().getEvents().trigger("chaseStart");
        }
    }

    /**
     * Update the ranged chase task
     */
    @Override
    public void update() {
        movementTask.setTarget(calculatePos());
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
    }

    /**
     * Get the priority of ranged chase task - the priority is 10 if the distance to target is
     * less than 0.8 times of the attack range of entity
     * or distance greater than attack range
     *
     * @return 10 if satisfy the requirement or -1 otherwise
     */
    @Override
    public int getPriority() {
        //Deadzone
        if (super.getDistanceToTarget() < owner.getEntity().getAttackRange() * 8 / 10) {
            return (super.getPriority());
        } else if (super.getDistanceToTarget() > owner.getEntity().getAttackRange()) {
            return (super.getPriority());
        }
        return (-1);

    }

    /**
     * @return position that is close enough to attack but staying as far away as possible
     */
    private Vector2 calculatePos() {
        Vector2 v1 = owner.getEntity().getPosition().cpy();
        Vector2 v2 = target.getPosition().cpy();
        Vector2 v3 = v1.cpy().sub(v2); //heading relative to entity
        float range = owner.getEntity().getAttackRange();
        float distanceFrom = v3.len();
        if (range / v3.len() < 1) {
            v3.scl(range / v3.len());
            v3.add(v2);
        } else {
            //invert v3 direction
            v3 = v2.cpy().sub(v1);
            v3.rotate90(1).rotate90(1); //heading relative to target
            //v3.scl(v3.len() / range);
            if (range / (v3.len() + distanceFrom) < 1) {
                v3.scl(range / (v3.len() + distanceFrom));
            }
            v3.add(v1);
        }
        Vector2 bodyOffset = owner.getEntity().getCenterPosition().cpy().sub(owner.getEntity().getPosition());
        ServiceLocator.getRenderService().getDebug().drawLine(owner.getEntity().getCenterPosition(), v3.cpy().add(bodyOffset), Color.WHITE, 1);
        return v3;
    }
}
