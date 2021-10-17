package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

import java.util.concurrent.TimeUnit;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 */
public class VortexSpawnTask extends DefaultTask implements PriorityTask {
    /**
     * desired scale
     */
    private final Vector2 scale;
    /**
     * upscale factor
     */
    private final Vector2 factor;
    /**
     * angle to rotate
     */
    private final float rotateAngle;
    /**
     * reverse spawn the vortex
     */
    private boolean reverse = false;
    /**
     * rotate factor
     */
    private float rotateFactor = 0;
    /**
     * time pause when vortex at desired scale - the start down scale
     */
    private long time = 0;
    /**
     * check if vortex is at max scale (desire)
     */
    private boolean max = false;

    /**
     * owner of the entity that run this task
     */
    private final Entity ownerRunner;


    /**
     * Spawn the vortex
     *
     * @param ownerRunner  owner entity.
     * @param desiredScale upper end of the scale (margin to upscale to)
     * @param rotateAngle  angle to rotate
     */
    public VortexSpawnTask(Entity ownerRunner, Vector2 desiredScale, float rotateAngle) {
        this.scale = desiredScale;
        this.rotateAngle = rotateAngle;
        factor = new Vector2(this.scale.x / 50, this.scale.y / 50);
        this.ownerRunner = ownerRunner;
    }

    /**
     * reverse the upscale and down scale
     */
    public void flipReverse() {
        this.reverse = !this.reverse;
    }

    /**
     * Update the vortex position on the screen. Upscale the vortex
     */
    @Override
    public void update() {
        Vector2 bodyOffset = owner.getEntity().getCenterPosition().cpy().sub(
                owner.getEntity().getPosition());
        Vector2 position = ownerRunner.getCenterPosition().sub(bodyOffset);
        owner.getEntity().setAngle(rotateAngle + rotateFactor);
        if (owner.getEntity().getScale().x > this.scale.x
                && owner.getEntity().getScale().y > this.scale.y) {
            owner.getEntity().setScale(this.scale);
            owner.getEntity().setPosition(position);
        }
        if (owner.getEntity().getScale().x < this.scale.x
                && owner.getEntity().getScale().y < this.scale.y && !max) {
            owner.getEntity().setScale(owner.getEntity().getScale().add(factor.scl(0.99f)));
            owner.getEntity().setPosition(position);
            time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        } else {
            max = true;
            //Let target teleport
            if (owner.getEntity().data.containsKey("teleportID")
                    && (int) owner.getEntity().data.get("teleportID") == 1) {
                if (!owner.getEntity().data.containsKey("teleportTarget")) {
                    //Add body offset
                    owner.getEntity().data.put("teleportTarget", true);
                }
            }
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - time >= 800
                    && owner.getEntity().getScale().x > 0.1f
                    && owner.getEntity().getScale().y > 0.1f) {
                //Stop target teleporting
                if (owner.getEntity().data.containsKey("teleportID")
                        && (int) owner.getEntity().data.get("teleportID") == 1) {
                    if ((boolean) owner.getEntity().data.get("teleportTarget")) {
                        owner.getEntity().data.put("teleportTarget", false);
                    }
                }
                owner.getEntity().setScale(owner.getEntity().getScale().sub(factor.scl(1.01f)));
                owner.getEntity().setPosition(position);
            } else if (owner.getEntity().getScale().x <= 0.1f
                    && owner.getEntity().getScale().y <= 0.1f) {
                owner.getEntity().prepareDispose();
            }
        }
        rotateFactor++;

        super.update();
    }

    /**
     * return the priority of the vortex spawn
     * If vortex can be spawn, return 10, else return -1
     *
     * @return int 10 if vortex is not at desired scale, -1 if vortex is at desired scale
     */
    public int getPriority() {
        if (desiredScale()) {
            // dispose if the entity spawn at desired size
            return (-1);
        } else {
            return (10);
        }
    }

    /**
     * check if vortex upscale to desire scale
     *
     * @return boolean true reach desired scale
     */
    private boolean desiredScale() {
        return owner.getEntity().getScale().cpy().dst(this.scale) < 0;
    }
}
