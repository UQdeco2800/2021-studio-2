package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
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

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class ZigChaseTaskTest {
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
        target.setPosition(3f, 3f);

        AITaskComponent ai = new AITaskComponent()
                .addTask(new ZigChaseTask(target, 10, 5f, 6f, 3));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        long time = System.nanoTime();
        long timeCompare = System.nanoTime();
        int count = 0;
        // Can't really test the zig zag movement, just test the enemies always move toward target
        while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time) < 2000) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
            if (initialDistance - entity.getPosition().dst(target.getPosition()) == 0
                    && entity.getPosition().dst(target.getPosition()) > 1f) {
                // distance > 1 because if less than 1, the entity is approach target
                count++;
            }
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeCompare) > 500) {
                assertNotEquals(0, initialDistance - entity.getPosition().dst(target.getPosition()));
                break;
            }
        }
    }

    @Test
    void shouldChaseOnlyWhenInDistance() {
        Entity target = new Entity();
        target.setPosition(0f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        ZigChaseTask zigChaseTask = new ZigChaseTask(target, 10, 5, 10, 3);
        zigChaseTask.create(() -> entity);

        // Not currently active, target is too far, should have negative priority
        assertTrue(zigChaseTask.getPriority() < 0);

        // When in view distance, should give higher priority
        target.setPosition(0f, 4f);
        assertEquals(10, zigChaseTask.getPriority());

        // When active, should chase if within chase distance
        target.setPosition(0f, 8f);
        zigChaseTask.start();
        assertEquals(10, zigChaseTask.getPriority());

        // When active, should not chase outside chase distance
        target.setPosition(0f, 12f);
        assertTrue(zigChaseTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new CombatStatsComponent(100, 10));
    }
}
