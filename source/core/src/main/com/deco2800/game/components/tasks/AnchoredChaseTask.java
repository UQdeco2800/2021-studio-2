package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

/**
 * Chases a target entity until they get too far away, leaves the base anchor, or line of sight is lost
 */
public class AnchoredChaseTask extends ChaseTask implements PriorityTask {

    private final Entity base;
    private final float protectRadius;
    private final float protectX;
    private final float protectY;

    /**
     * @param target           The entity to chase.
     * @param viewDistance     Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     * @param anchor           - entity to retreat to
     * @param protectRadius    - bounds around the object to stay in
     */
    public AnchoredChaseTask(Entity target, float viewDistance, float maxChaseDistance, Entity anchor, float protectRadius) {
        super(target, 10, viewDistance, maxChaseDistance);
        this.base = anchor;
        this.protectRadius = protectRadius;
        this.protectX = 0;
        this.protectY = 0;
    }

    /**
     * @param target           The entity to chase.
     * @param viewDistance     Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     * @param anchor           - entity to retreat to
     * @param protectX         - bounds around the object to stay in on the x axis
     * @param protectY         - bounds around the object to stay in on the y axis
     */
    public AnchoredChaseTask(Entity target, float viewDistance, float maxChaseDistance, Entity anchor, float protectX, float protectY) {
        super(target, 10, viewDistance, maxChaseDistance);
        this.base = anchor;
        this.protectRadius = 0;
        this.protectX = protectX;
        this.protectY = protectY;
    }

    /**
     * @return -1 to deactivate if the target is outside of the base
     * or it passes the call to its super to calculate if the target is in range and visible
     */
    @Override
    public int getPriority() {
        if (!isTargetInBase()) {
            return (-1);
        }
        return (super.getPriority());
    }

    /**
     * @return true if the target is currently in the anchored base,
     * false if the target is outside of the base
     */
    private boolean isTargetInBase() {
        Vector2 targetLoc = target.getCenterPosition();
        Vector2 min;
        Vector2 max;
        // Set up anchored boundary
        if (protectRadius > 0) {
            min = base.getCenterPosition().cpy().sub(protectRadius, protectRadius);
            max = base.getCenterPosition().cpy().add(protectRadius, protectRadius);
        } else {
            min = base.getCenterPosition().cpy().sub(protectX, protectY);
            max = base.getCenterPosition().cpy().add(protectX, protectY);
        }
        // Is the target in the boundary
        return ((targetLoc.x >= min.x && targetLoc.y >= min.y) && (targetLoc.x <= max.x && targetLoc.y <= max.y));
    }
}
