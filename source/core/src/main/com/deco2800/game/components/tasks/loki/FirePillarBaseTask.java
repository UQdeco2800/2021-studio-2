package com.deco2800.game.components.tasks.loki;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.services.ServiceLocator;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 */
public class FirePillarBaseTask extends DefaultTask implements PriorityTask {

    /** Spawn time of the entity. */
    private long spawnTime;

    private GameArea gameArea;

    public FirePillarBaseTask() {

        this.spawnTime = ServiceLocator.getTimeSource().getTime();
        this.gameArea = ServiceLocator.getGameAreaService();
    }

    /**
     * return the priority of the arrow
     * If arrow is in moving, return 10, else return -1 and dispose the arrow
     *
     * @return int 10 if arrow is moving, -1 if arrow is not
     */
    public int getPriority() {
        if ((ServiceLocator.getTimeSource().getTime() - spawnTime) > 350) {
            Entity pillar = WeaponFactory.createFirePillar();
            Vector2 ownerPos = owner.getEntity().getPosition();

            gameArea.spawnEntityAt(pillar, new Vector2(ownerPos.x + 0.15f, ownerPos.y + 0.15f), true, true);
            owner.getEntity().prepareDispose();
            return -1;
        }
        return 10;
    }

}
