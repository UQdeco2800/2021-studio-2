package com.deco2800.game.components.tasks;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.ai.tasks.AITaskComponent;
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
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

import java.security.SecureRandom;
import java.util.TreeMap;

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

    private void playElfDead() {
        SecureRandom random = new SecureRandom();
        int numCase = random.nextInt(2);
        if (numCase == 1) {
            ServiceLocator.getResourceService().getAsset(
                    "sounds/death_1.mp3", Sound.class).play(0.5f);
        } else {
            ServiceLocator.getResourceService().getAsset(
                    "sounds/death_2.mp3", Sound.class).play(0.5f);
        }
    }

    private void playElfBossDeath() {
        ServiceLocator.getResourceService().getAsset(
                "sounds/boss_death.mp3", Sound.class).play(0.5f);
    }

    @Override
    public void update() {
        if (Boolean.TRUE.equals(owner.getEntity().getComponent(CombatStatsComponent.class).isDead())) {
            waitForDeathAnimation();
        }
    }

    public void waitForDeathAnimation() {

        if (this.declareEnd) {
            this.start = System.currentTimeMillis();
            if (owner.getEntity().getEntityType().equals("melee")
                    || owner.getEntity().getEntityType().equals("assassin")
                    || owner.getEntity().getEntityType().equals("ranged")
                    || owner.getEntity().getEntityType().equals("AlertCaller")
                    || owner.getEntity().getEntityType().equals("viking")
                    || owner.getEntity().getEntityType().equals("archer")) {
                playElfDead();
            }
            if (owner.getEntity().getEntityType().equals("elfBoss")) {
                playElfBossDeath();
            }
            if (owner.getEntity().getEntityType().equals("odin")) {
                showDialogue();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        spawnWin();
                    }
                }, 4.5f);
                owner.getEntity().getComponent(HumanAnimationController.class).setDeath();
            } else if (owner.getEntity().getComponent(HumanAnimationController.class) != null) {
                owner.getEntity().getComponent(HumanAnimationController.class).setDeath();
            } else {
                if (owner.getEntity().getComponent(ElfAnimationController.class) != null) {
                    owner.getEntity().getComponent(ElfAnimationController.class).setDeath();
                }
            }
            PlayerSave.Save.setOdinWins(1);
            PlayerSave.write();
            this.declareEnd = false;
            try {
                owner.getEntity().getComponent(HealthBarComponent.class).dispose();
            } catch (Exception e) {
                //ignore
            }
            owner.getEntity().getComponent(ColliderComponent.class).dispose();
            owner.getEntity().getComponent(HitboxComponent.class).dispose();
            owner.getEntity().getComponent(TouchAttackComponent.class).dispose();
            TreeMap<String, Object> data = owner.getEntity().data;
            if (data.containsKey("fireBalls")) {
                for (Entity fireBall : (Entity[]) data.get("fireBalls")) {
                    if (fireBall != null) {
                        fireBall.dispose();
                    }
                }
            }
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
                TreeMap<String, Object> data = owner.getEntity().data;
                if (data.containsKey("fireBalls")) {
                    for (Entity fireBall : (Entity[]) data.get("fireBalls")) {
                        if (fireBall != null) {
                            fireBall.dispose();
                        }
                    }
                }
            }
        }
    }

    private void showDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        RandomDialogueSet dialogueSet = RandomDialogueSet.ODIN_KILLED;

        PlayerSave.Save.setHasPlayed(true);
        if (PlayerSave.Save.getOdinEnc() == 0) {
            textBox.setRandomFirstEncounter(dialogueSet);
        } else {
            if (PlayerSave.Save.getOdinWins() == 0) {
                //If getWins() returns 0, that means the most recent game has resulted in a loss
                textBox.setRandomDefeatDialogueSet(dialogueSet);
            } else {
                // When it returns 1, then the player has beaten the boss before
                textBox.setRandomBeatenDialogueSet(dialogueSet);
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
        if (Boolean.TRUE.equals(owner.getEntity().getComponent(CombatStatsComponent.class).isDead())) {
            return 100;
        } else {
            return 0;
        }
    }
}
