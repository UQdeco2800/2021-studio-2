package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class ExplosionSpawnTaskTest {

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
        ServiceLocator.registerEntityService(new EntityService());

    }

    @Test
    void activePriority() {
        Entity taskRunner = new Entity();

        ExplosionSpawnTask explosionSpawnTask = new ExplosionSpawnTask(taskRunner, new Vector2(10f, 10f));

        explosionSpawnTask.create(() -> taskRunner);

        // ensure that the priority is 10
        assertEquals(10, explosionSpawnTask.getPriority());
    }

    @Test
    void inactivePriority() {
        Entity taskRunner = new Entity();

        ExplosionSpawnTask explosionSpawnTask = new ExplosionSpawnTask(taskRunner, new Vector2(3f, 3f));

        taskRunner.setScale(1f, 1f);
        explosionSpawnTask.create(() -> taskRunner);

        for (int i = 0; i < 42; i++) {
            explosionSpawnTask.update();
        }

        // ensure that the priority is 10
        assertEquals(-1, explosionSpawnTask.getPriority());
    }
}