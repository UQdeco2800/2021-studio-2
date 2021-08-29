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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class AnchoredRetreatTaskTest {
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

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }

    /**
     * Main idea of anchored retreat task is updating the priority based on the distance from anchor
     */
    @Test
    void shouldRetreatToBase() {

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        Entity anchor = new Entity();
        // entity can chase
        AnchoredRetreatTask anchoredRetreatTask = new AnchoredRetreatTask(anchor, 3f, 3f);
        anchoredRetreatTask.create(() -> entity);

        // Entity is not start so return -1 by default
        assertEquals(-1, anchoredRetreatTask.getPriority());

        // When active, entity should start return to anchor
        anchoredRetreatTask.start();
        assertEquals(5, anchoredRetreatTask.getPriority());


        // Distance from entity to anchor is to far away, begin to retreat
        entity.setPosition(6f, 6f);
        anchoredRetreatTask.update();
        // When active, should not chase outside chase distance
        assertEquals(15, anchoredRetreatTask.getPriority());

        entity.setPosition(6f, 6f);
        anchoredRetreatTask.start();
        anchoredRetreatTask.update();
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time <= 2000) {
            //wait
        }
        entity.setPosition(2f, 2f);
        anchoredRetreatTask.stop();

        // no longer retreat
        if (System.currentTimeMillis() - time > 2000) {
            assertEquals(-1, anchoredRetreatTask.getPriority());
        }

    }
}