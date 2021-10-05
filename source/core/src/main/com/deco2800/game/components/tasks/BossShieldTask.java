package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.Random;

public class BossShieldTask extends ChaseTask implements PriorityTask {
    private final GameTime timeSource;
    private final float duration;
    private long endTime;
    private float maxChaseDistance;
    private int priority;
    private float viewDistance;
    private boolean shield;
    private int currentHealth;

    public BossShieldTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float duration) {
        super(target, priority, viewDistance, maxChaseDistance);
        this.maxChaseDistance = maxChaseDistance;
        this.viewDistance = viewDistance;
        this.priority = priority;
        this.timeSource = ServiceLocator.getTimeSource();
        this.duration = duration;
    }


    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }


    private int getActivePriority() {
        float dst = super.getDistanceToTarget();
        if (dst > maxChaseDistance || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    public void shieldController() {
        float health = (float) owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        float maxHealth = (float) owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        if ((health / maxHealth) <= 0.5 && timeSource.getTime() >= endTime) {
            endTime = timeSource.getTime() + (int) (duration * 1000);
            Random rand = new Random();
            if (rand.nextDouble() > 0.5) {
                this.shield = true;
                currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
            } else {
                this.shield = false;
            }
        }
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    @Override
    public void start() {
        super.start();
        endTime = 0;
    }

    @Override
    public void update() {
        if (shield) {
            super.movementTask.stop();
            owner.getEntity().getComponent(CombatStatsComponent.class).setHealth(currentHealth);
        }
    }

}