package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.tasks.loki.FirePillarBaseTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class FirePillarBaseTaskTest {

    @Mock
    GameArea gameArea;

    @Mock
    GameTime gameTime;

    private FirePillarBaseTask firePillar;

    private static final String[] forestTextureAtlases = {
            "images/hellViking.atlas", "images/lokiBoss.atlas", "images/firePillar.atlas"
    };

    private static final String[] forestTextures = {
            "images/arrow_normal.png",
            "images/health_left.png",
            "images/health_middle.png",
            "images/health_right.png",
            "images/health_frame_left.png",
            "images/health_frame_middle.png",
            "images/health_frame_right.png",
            "images/enemy_health_bar.png",
            "images/enemy_health_border.png",
            "images/enemy_health_bar_decrease.png",
            "images/boss_health_middle.png",
            "images/boss_health_left.png",
            "images/boss_health_right.png",
            "images/hellViking.png",
            "images/lokiBoss.png",
            "images/firePillar.png"
    };

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        gameArea = mock(GameArea.class);
        ServiceLocator.registerGameArea(gameArea);

        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadTextures(forestTextures);
        resourceService.loadAll();

        gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
    }

    @Test
    void spawnPillarTest() {
        when(gameTime.getTime()).thenReturn(2000L).thenReturn(0L);
        Entity boss = createSpawner();

        firePillar.create(() -> boss);
        assertEquals(10, firePillar.getPriority());
    }

    @Test
    void spawnPillarFailTest() {
        when(gameTime.getTime()).thenReturn(0L).thenReturn(2000L);
        Entity boss = createSpawner();

        firePillar.create(() -> boss);
        assertEquals(-1, firePillar.getPriority());
    }


    private Entity createSpawner() {

        AITaskComponent AI = new AITaskComponent();
        firePillar = new FirePillarBaseTask();

        AI.addTask(firePillar);

        return new Entity()
                .addComponent(AI)
                .addComponent(mock(PhysicsMovementComponent.class))
                .addComponent(new HitboxComponent())
                .addComponent(new CombatStatsComponent(100, 10))
                .addComponent(new PhysicsComponent());
    }
}
