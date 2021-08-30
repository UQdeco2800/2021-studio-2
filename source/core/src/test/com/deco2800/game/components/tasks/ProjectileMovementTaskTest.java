package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectileMovementTaskTest {

    @BeforeEach
    void beforeEach() {
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
        target.setPosition(4f, 4f);

        ProjectileMovementTask projectileMovementTask = new ProjectileMovementTask(target.getPosition(), new Vector2(4f, 4f));
        AITaskComponent ai = new AITaskComponent().addTask(projectileMovementTask);
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 78; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance < initialDistance);
        // can't check because the item is already dispose - so does not exist
    }

    @Test
    void shouldChaseOnlyWhenInDistance() {
        Entity target = new Entity();
        target.setPosition(6f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        ProjectileMovementTask projectileMovementTask =
                new ProjectileMovementTask(target.getPosition(), new Vector2(2f, 2f));
        projectileMovementTask.create(() -> entity);

        // Arrow should travel toward target
        assertEquals(10, projectileMovementTask.getPriority());

        projectileMovementTask.start();
        entity.earlyUpdate();
        entity.update();
        // When active, should chase if within chase distance
        assertEquals(10, projectileMovementTask.getPriority());

    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}