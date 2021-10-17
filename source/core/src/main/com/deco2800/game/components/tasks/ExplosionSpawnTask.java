package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

/**
 * Controls the scale of a boss's explosion attack
 */
public class ExplosionSpawnTask extends DefaultTask implements PriorityTask {
    /**
     * desired scale
     */
    private final Vector2 scale;
    /**
     * upscale factor
     */
    private final Vector2 factor;

    private final Entity ownerRunner;

    /**
     * Spawn the explosion
     *
     * @param ownerRunner  owner entity.
     * @param desiredScale upper end of the scale (margin to upscale to)
     */
    public ExplosionSpawnTask(Entity ownerRunner, Vector2 desiredScale) {
        this.scale = desiredScale;
        factor = new Vector2(this.scale.x / 50, this.scale.y / 50);
        this.ownerRunner = ownerRunner;
    }

    /**
     * Update the explosion position on the screen. Upscale the explosion
     */
    @Override
    public void update() {
        Vector2 bodyOffset = owner.getEntity().getCenterPosition().cpy().sub(owner.getEntity().getPosition());
        Vector2 position = ownerRunner.getCenterPosition().sub(bodyOffset);
        if (owner.getEntity().getScale().x > this.scale.x
                && owner.getEntity().getScale().y > this.scale.y) {
            owner.getEntity().setScale(this.scale);
            owner.getEntity().setPosition(position);
        }
        if (owner.getEntity().getScale().x < this.scale.x
                && owner.getEntity().getScale().y < this.scale.y) {
            owner.getEntity().setScale(owner.getEntity().getScale().add(factor.scl(0.99f)));
            owner.getEntity().setPosition(position);
        } else {
            owner.getEntity().prepareDispose();
        }
        super.update();
    }

    /**
     * return the priority of the explosion spawn
     * If explosion can be spawn, return 10, else return -1
     *
     * @return int 10 if explosion is not at desired scale, -1 if explosion is at desired scale
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
     * check if explosion upscale to desire scale
     *
     * @return boolean true reach desired scale
     */
    private boolean desiredScale() {
        return owner.getEntity().getScale().cpy().dst(this.scale) <= 0;
    }
}
