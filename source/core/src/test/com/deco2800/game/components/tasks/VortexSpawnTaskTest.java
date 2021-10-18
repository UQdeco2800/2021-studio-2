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
        ServiceLocator.registerEntityService(new EntityService());

    }

    @Test
    void increaseSizeBy5Percent() {
        Entity taskRunner = new Entity();

        VortexSpawnTask vortexSpawnTask = new VortexSpawnTask(taskRunner, new Vector2(10f, 10f), 20f);

        vortexSpawnTask.create(() -> taskRunner);

        // ensure that the priority is 10
        assertEquals(10, vortexSpawnTask.getPriority());


        for (int i = 0; i < 45; i++) { //update a task couple of times
            vortexSpawnTask.update();
            assertEquals(taskRunner.getScale().x, taskRunner.getScale().y);
        }

        Vector2 scale = new Vector2(8.203527f, 8.203527f);
        // check the new scale of the entity after updating the vortex spawn task
        assertEquals(scale, taskRunner.getScale());

        Vector2 scale2 = new Vector2(10f, 10f);

        while (taskRunner.getScale().x != scale2.x) { //x scale and y scale always the same
            vortexSpawnTask.update();
        }

        assertEquals(scale2, taskRunner.getScale());

        long timeStart = System.currentTimeMillis();
        long timeWait = 0;

        while (taskRunner.getScale().x == scale2.x) {
            vortexSpawnTask.update();
            if (System.currentTimeMillis() - timeStart >= 1) {
                timeWait++;
                timeStart = System.currentTimeMillis();
            }
        }

        // ensure the priority always return 10
        assertEquals(10, vortexSpawnTask.getPriority());
    }

}