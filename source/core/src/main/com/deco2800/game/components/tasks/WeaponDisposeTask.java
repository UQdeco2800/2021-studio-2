package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class WeaponDisposeTask extends ProjectileMovementTask implements PriorityTask {
    private final float duration;
    private double start;
    private boolean declareEnd;
    private final GameTime timeSource;


    public WeaponDisposeTask(Vector2 targetLoc, Vector2 speed, float duration) {
        super(targetLoc, speed);
        this.duration = duration;
        this.timeSource = ServiceLocator.getTimeSource();
        this.declareEnd = true;
    }

    @Override
    public void update() {
        waitForDeathAnimation();
    }

    public void waitForDeathAnimation() {
        if (this.declareEnd) {
            this.start = System.currentTimeMillis();
            owner.getEntity().getComponent(HitboxComponent.class).dispose();
            if (!owner.getEntity().getEntityType().equals("fireBall")) {
                owner.getEntity().getComponent(TextureRenderComponent.class).dispose();
            }
            this.declareEnd = false;
        } else {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            if ((System.currentTimeMillis() - start) / 1000 >= duration) {
                stop();
                owner.getEntity().prepareDispose();
                status = Status.FINISHED;
            }
        }
    }

    @Override
    public int getPriority() {
//        return 0;
        if (owner.getEntity().getComponent(CombatStatsComponent.class).isDead()) {
            return 100;
        } else {
            return 0;
        }
    }
}
