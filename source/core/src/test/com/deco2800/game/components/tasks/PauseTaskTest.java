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
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class PauseTaskTest {

    @Mock
    GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        GameTime gameTime = mock(GameTime.class);
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        this.gameTime = gameTime;
    }

    @Test
    void shouldNotPause() {
        when(gameTime.getTime()).thenReturn(0L).thenReturn(500L);
        Entity taskRunner = new Entity();
        taskRunner.addComponent(new CombatStatsComponent(100, 10));

        PauseTask pauseTask = new PauseTask();

        pauseTask.create(() -> taskRunner);

        //The priority of the task should be -1 when the game is not paused.
        assertEquals(-1, pauseTask.getPriority());
    }

    @Test
    void cutscenesPausedTimePaused() {
        when(gameTime.getTime()).thenReturn(0L).thenReturn(500L);
        Entity taskRunner = new Entity();
        taskRunner.addComponent(new CombatStatsComponent(100, 10));

        PauseTask pauseTask = new PauseTask();

        pauseTask.create(() -> taskRunner);
        gameTime.pause();

        //The priority of the task should be -1 when the game is not paused.
        assertEquals(-1, pauseTask.getPriority());
    }

    @Test
    void cutscenesPausedTimeNotPaused() {
        when(gameTime.getTime()).thenReturn(0L).thenReturn(0L);
        Entity taskRunner = new Entity();
        taskRunner.addComponent(new CombatStatsComponent(100, 10));

        PauseTask pauseTask = new PauseTask();

        pauseTask.create(() -> taskRunner);
        gameTime.pause();

        //The priority of the task should be -1 when the game is not paused.
        assertEquals(25, pauseTask.getPriority());
    }

    @Test
    void shouldPause() {
        Entity taskRunner = new Entity();
        taskRunner.addComponent(new CombatStatsComponent(100, 10));

        PauseTask pauseTask = new PauseTask();

        pauseTask.create(() -> taskRunner);

        //The priority of the task should be 25 when the game has been paused.
        gameTime.pauseEnemies();
        assertEquals(25, pauseTask.getPriority());
    }
}
