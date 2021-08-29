package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)

class RangedChaseTaskTest {
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldMoveTowardsTarget() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);


        AITaskComponent ai = new AITaskComponent().addTask(new RangedChaseTask(target, 10, 5, 10));
        Entity entity = makePhysicsEntity().addComponent(ai);

        entity.create();
        entity.setPosition(0f, 0f);
        entity.setAttackRange(2);

        // move toward target
        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance < initialDistance);

        entity.setAttackRange(2);

        //Move back if target is too close
        entity.setAttackRange(4);
        entity.setPosition(1f, 1f);

        float initialDistance2 = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance2 = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance2 > initialDistance2);
    }

    @Test
    void shouldChaseOnlyWhenInDistance() {
        Entity target = new Entity();
        target.setPosition(10f, 10f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        RangedChaseTask rangedChaseTask = new RangedChaseTask(target, 10, 15f, 20f);
        rangedChaseTask.create(() -> entity);
        entity.setAttackRange(5);

        // Not currently active, target is too far, should have negative priority
        assertTrue(rangedChaseTask.getPriority() > 0);

        // When in view distance, should give higher priority
        target.setPosition(10f, 4f);
        assertEquals(10, rangedChaseTask.getPriority());

        // Blind spot - distance to target is <= entity.getAttackRange
        // and distance to target is >= entity.getAttackRange * 8/10
        target.setPosition(0f, 4f);
        assertEquals(-1, rangedChaseTask.getPriority());
        target.setPosition(5f, 0f);
        assertEquals(-1, rangedChaseTask.getPriority());

        // When active, should chase if within chase distance
        target.setPosition(4f, 8f);
        rangedChaseTask.start();
        assertEquals(10, rangedChaseTask.getPriority());

        // When active, should not chase outside chase distance
        target.setPosition(20f, 12f);
        assertTrue(rangedChaseTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}