package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
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
class VortexSpawnTaskTest {

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
    void increaseSizeBy5Percent() {
        Entity taskRunner = new Entity();

        VortexSpawnTask vortexSpawnTask = new VortexSpawnTask(taskRunner, new Vector2(10f, 10f), 20f);

        vortexSpawnTask.create(() -> taskRunner);

        // initially the size is 10% of the expected size -> upscale
        assertEquals(10, vortexSpawnTask.getPriority());

        Vector2 scale = new Vector2(1f, 1f);
        for (int i = 0; i < 45; i++) {
            vortexSpawnTask.update();
            assertEquals(scale.scl(1.05f), taskRunner.getScale());
        }

        // the priority is always 10,
        assertEquals(10, vortexSpawnTask.getPriority());

    }

}