package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.ElfAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class DeathPauseTask extends ChaseTask implements PriorityTask {
    private final float duration;
    private final GameTime timeSource;
    private double start;
    private int priority;
    private boolean declareEnd;
    private long endTime;

    public DeathPauseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float duration) {
        super(target, priority, viewDistance, maxChaseDistance);
        this.duration = duration;
        this.timeSource = ServiceLocator.getTimeSource();
        this.declareEnd = true;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {
        if (owner.getEntity().getComponent(CombatStatsComponent.class).isDead()) {
            waitForDeathAnimation();
        }
    }

    public void waitForDeathAnimation() {

        if (this.declareEnd) {
            this.start = System.currentTimeMillis();
            owner.getEntity().getComponent(ElfAnimationController.class).setDeath();
            this.declareEnd = false;
            owner.getEntity().getComponent(HealthBarComponent.class).dispose();
            //owner.getEntity().getComponent(PhysicsComponent.class).dispose();
            owner.getEntity().getComponent(ColliderComponent.class).dispose();
            owner.getEntity().getComponent(HitboxComponent.class).dispose();
            owner.getEntity().getComponent(TouchAttackComponent.class).dispose();
        } else {
            movementTask.stop();
            if ((System.currentTimeMillis() - start) / 1000 >= duration) {
                if (owner.getEntity().getEntityType().equals("elfBoss")) {
                    ServiceLocator.getGameAreaService().decBossNum();
                } else {
                    ServiceLocator.getGameAreaService().decNum();
                }
                owner.getEntity().prepareDispose();
                status = Status.FINISHED;
            }
        }
    }

    @Override
    public int getPriority() {
        if (owner.getEntity().getComponent(CombatStatsComponent.class).isDead()) {
            return 100;
        } else {
            return 0;
        }
    }
}
