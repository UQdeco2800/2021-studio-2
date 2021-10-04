package com.deco2800.game.components.tasks;

import com.deco2800.game.components.CombatStatsComponent;
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
class DeathPauseTaskTest {

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
    void getPriority() {
        Entity taskRunner = new Entity();
        taskRunner.addComponent(new CombatStatsComponent(100, 10));

        DeathPauseTask deathPauseTask = new DeathPauseTask(taskRunner, 0, 100, 100, 1.5f);

        deathPauseTask.create(() -> taskRunner);

        //only run if the entity die
        assertEquals(0, deathPauseTask.getPriority());

        //the entity die
        taskRunner.getComponent(CombatStatsComponent.class).setHealth(0);
        assertEquals(100, deathPauseTask.getPriority());

    }

}