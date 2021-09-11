package com.deco2800.game.components.tasks;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultMultiTask;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.WeaponConfigs;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * Spawns an arrow to shoot at a target
 */
public class SpawnMinionsTask extends DefaultTask implements PriorityTask {

    private final Entity target;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private final GameArea gameArea;
    private static int spawn = 0;

    /**
     * @param target     The entity to chase.
     */
    public SpawnMinionsTask(Entity target) {
        this.target = target;
        this.gameArea = ServiceLocator.getGameAreaService();
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * update the arrow - check whether the entity can shoot the arrow or not
     */
    @Override
    public void update() {
        if (canSpawn()) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            spawn();
            spawn++;
        }
    }



    /**
     * Spawns in an arrow according to the classes variables
     */
    public void spawn() {
        Entity ghost = NPCFactory.createGhost(target);
        //Entity ghost2 = NPCFactory.createRangedGhost(target);
        //Entity ghost3 = NPCFactory.createGhostKing(target);
        Entity ghost4 = NPCFactory.createRangedGhost(target);

        gameArea.spawnEntityAt(ghost, owner.getEntity().getCenterPosition(), true, true);
        //gameArea.spawnEntityAt(ghost2, owner.getEntity().getCenterPosition(), true, true);
        //gameArea.spawnEntityAt(ghost3, owner.getEntity().getCenterPosition(), true, true);
        gameArea.spawnEntityAt(ghost4, owner.getEntity().getCenterPosition(), true, true);

    }

    /**
     * return the priority of arrow task
     *
     * @return highest priority if can shoot, else -1
     */
    @Override
    public int getPriority() {
        if (canSpawn()) {
            return 20;
        }
        return -1;
    }

    public boolean mapBound() {
        return owner.getEntity().getPosition().x < 0
                && owner.getEntity().getPosition().y < 0
                && owner.getEntity().getPosition().x > 30
                && owner.getEntity().getPosition().y > 30;
    }

    /**
     * check if target can shoot based on given cooldown of the shooting and target is visible
     *
     * @return true if can shoot, false otherwise
     */
    private boolean canSpawn() {
        float maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        float health = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        float ratio = health / maxHealth;

        if (ServiceLocator.getGameAreaService().getNumEnemy() == 0 && ratio < 0.5 && mapBound()) {
            return true;
        }

        if (ratio < 0.5 && spawn < 1) {
            return true;
        }
        return ratio < 0.25 && spawn < 2;
    }
}
