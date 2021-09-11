package com.deco2800.game.components.tasks;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.services.ServiceLocator;

import java.util.concurrent.TimeUnit;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 */
public class VortexSpawnTask extends DefaultTask implements PriorityTask {
    private final Vector2 scale;

    private final Vector2 factor;

    private final float rotateAngle;

    private boolean reverse = false;

    private static float rotateFactor = 1;

    private static float offset = 0;

    private long time = 0;

    private boolean max = false;

    public VortexSpawnTask(Vector2 desiredScale, float rotateAngle) {
        this.scale = desiredScale;
        this.rotateAngle = rotateAngle;
        factor = new Vector2(this.scale.x / 10, this.scale.y / 10);
    }

    public void flipReverse() {
        this.reverse = !this.reverse;
    }

    /**
     * Update the arrow position on the screen.
     */
    @Override
    public void update() {
        if (owner.getEntity().getScale().x > this.scale.x
                && owner.getEntity().getScale().y > this.scale.y) {
            owner.getEntity().setScale(this.scale);
        }
        if (owner.getEntity().getScale().x < this.scale.x
                && owner.getEntity().getScale().y < this.scale.y && !max) {
            owner.getEntity().setScale(factor.scl(1.05f));
            owner.getEntity().setAngle(rotateAngle + rotateFactor);
            /*
            owner.getEntity().setPosition(
                    owner.getEntity().getPosition().x - owner.getEntity().getCenterPosition().x,
                    owner.getEntity().getPosition().y - owner.getEntity().getCenterPosition().y);
             */
            time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        } else {
            max = true;
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - time >= 800
                    && owner.getEntity().getScale().x > 0.1f
                    && owner.getEntity().getScale().y > 0.1f) {
                owner.getEntity().setScale(this.scale.scl(0.95f));
                owner.getEntity().setAngle(rotateAngle + rotateFactor);
            } else if (owner.getEntity().getScale().x <= 0.1f
                    && owner.getEntity().getScale().y <= 0.1f) {
                owner.getEntity().prepareDispose();
            }
        }
//            else {
//
//                owner.getEntity().prepareDispose();
//            }
//        else {
//            if (owner.getEntity().getScale().x > 0.1f && owner.getEntity().getScale().y > 0.1f) {
//                owner.getEntity().setScale(this.scale.scl(0.99f));
//                owner.getEntity().setAngle(rotateAngle + rotateFactor);
//            } else {
//                owner.getEntity().prepareDispose();
//            }
//     }
        offset += 0.000001f;
        rotateFactor++;

        super.update();
    }

    /**
     * return the priority of the arrow
     * If arrow is in moving, return 10, else return -1 and dispose the arrow
     *
     * @return int 10 if arrow is moving, -1 if arrow is not
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
     *
     * @return boolean true reach desired scale
     */
    private boolean desiredScale() {
        return owner.getEntity().getScale().cpy().dst(this.scale) < 0;
    }
}
