package com.deco2800.game.components.tasks;

import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.npc.ElfAnimationController;
import com.deco2800.game.components.npc.HumanAnimationController;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;

public class DeathPauseTask extends ChaseTask implements PriorityTask {
    private final float duration;
    private double start;
    private boolean declareEnd;

    private boolean dead = false;

    public DeathPauseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float duration) {
        super(target, priority, viewDistance, maxChaseDistance);
        this.duration = duration;
        this.declareEnd = true;
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
            if (owner.getEntity().getComponent(HumanAnimationController.class) != null) {
                owner.getEntity().getComponent(HumanAnimationController.class).setDeath();
            } else if (owner.getEntity().getEntityType().equals("odin")) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        spawnWin();
                    }
                }, 2f);
                owner.getEntity().getComponent(HumanAnimationController.class).setDeath();
            } else {
                if (owner.getEntity().getComponent(ElfAnimationController.class) != null) {
                    owner.getEntity().getComponent(ElfAnimationController.class).setDeath();
                }
            }
            PlayerSave.Save.setOdinWins(1);
            PlayerSave.write();
            this.declareEnd = false;
            owner.getEntity().getComponent(HealthBarComponent.class).dispose();
            owner.getEntity().getComponent(ColliderComponent.class).dispose();
            owner.getEntity().getComponent(HitboxComponent.class).dispose();
            owner.getEntity().getComponent(TouchAttackComponent.class).dispose();
        } else {
            movementTask.stop();
            if ((System.currentTimeMillis() - start) / 1000 >= duration) {
                if (!dead) {
                    if (owner.getEntity().getEntityType().equals("elfBoss")
                            || owner.getEntity().getEntityType().equals("loki")
                            || owner.getEntity().getEntityType().equals("odin")) {
                        ServiceLocator.getGameAreaService().decBossNum();
                    } else {
                        ServiceLocator.getGameAreaService().decNum();
                    }
                    status = Status.FINISHED;
                }
                dead = true;
            }
        }
    }

    private void spawnWin() {
        Entity win = ObstacleFactory.winCondition();
        ServiceLocator.getGameAreaService().spawnEntityAt(win,
                owner.getEntity().getCenterPosition(), true, true);
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
