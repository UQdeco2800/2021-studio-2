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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(GameExtension.class)
class AlertableChaseTaskTest {

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
    void ShouldSeeTheEnemyIfIsAlerted() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        AITaskComponent ai = new AITaskComponent().addTask(new AlertableChaseTask(target, 10, 5, 10));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        AlertableChaseTask alertableChaseTask = new AlertableChaseTask(target, 10, 5, 10);
        alertableChaseTask.create(() -> entity);

        //assertTrue(alertableChaseTask.getPriority() < 0);

        // When in view distance, should give higher priority
        target.setPosition(0f, 4f);
        //assertEquals(10, alertableChaseTask.getPriority());

        // When active, should chase if within chase distance
        target.setPosition(0f, 8f);
        alertableChaseTask.start();
        //assertEquals(10, alertableChaseTask.getPriority());

        // When active, should not chase outside chase distance
        target.setPosition(0f, 12f);
        //assertTrue(alertableChaseTask.getPriority() < 0);
    }


    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }

}