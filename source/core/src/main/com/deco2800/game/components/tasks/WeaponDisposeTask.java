package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Wait for the weapon break animation to run
 */
public class WeaponDisposeTask extends ProjectileMovementTask implements PriorityTask {
    /**
     * Wait duration
     */
    private final float duration;
    /**
     * Start time
     */
    private double start;
    /**
     * Start end
     */
    private boolean declareEnd;

    /**
     * Pause the weapon in midair to wait for animation to run
     *
     * @param targetLoc target position
     * @param speed     speed
     * @param duration  pause duration
     */
    public WeaponDisposeTask(Vector2 targetLoc, Vector2 speed, float duration) {
        super(targetLoc, speed);
        this.duration = duration;
        this.declareEnd = true;
    }

    /**
     * call wait for death animation
     */
    @Override
    public void update() {
        waitForDeathAnimation();
    }

    /**
     * Dispose weapon hit box and wait for the weapon break animation to run
     */
    public void waitForDeathAnimation() {
        if (this.declareEnd) {
            this.start = System.currentTimeMillis();
            owner.getEntity().getComponent(HitboxComponent.class).dispose();
            this.declareEnd = false;
        } else {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            if ((System.currentTimeMillis() - start) / 500 >= duration) {
                stop();
                owner.getEntity().prepareDispose();
                status = Status.FINISHED;
            }
        }
    }

    /**
     * override all other task if the weapon is dead (health = 0);
     *
     * @return 100 if weapon is dead, 0, otherwise
     */
    @Override
    public int getPriority() {
        if (owner.getEntity().getComponent(CombatStatsComponent.class).isDead()) {
            return 100;
        } else {
            return 0;
        }
    }
}
