package com.deco2800.game.components.tasks;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 * todo: may need to offset the target by the sprites center size to stop it approaching with the corner
 */
public class BlastTask extends MovementTask implements PriorityTask {
    private PhysicsMovementComponent movementComponent;
    private Vector2 moveSpeed = Vector2Utils.ONE;

    public BlastTask(Vector2 target) {
        super(target, Vector2Utils.ONE);
    }

    @Override
    public void update() {
        super.update();
        if (isAtTarget()) {
            owner.getEntity().prepareDispose();
        }
    }

    @Override
    public int getPriority() {
        return 10;
    }
}