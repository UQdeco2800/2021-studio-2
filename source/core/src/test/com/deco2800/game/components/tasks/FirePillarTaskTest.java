package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.tasks.loki.FirePillarTask;
import com.deco2800.game.components.tasks.loki.SpawnLokiDecoyTask;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class FirePillarTaskTest {

    @Mock
    GameArea gameArea;

    @Mock
    GameTime gameTime;

    private FirePillarTask firePillar;

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
    }

    @Test
    void inactivePriority() {
        Entity target = new Entity();
        Entity boss = createSpawner(target);

        firePillar.create(() -> boss);

        // inactive when boss health not < 50%
        assertEquals(-1, firePillar.getPriority());
    }

    @Test
    void canAttackTest() {
        Entity target = new Entity();
        Entity boss = createSpawner(target);
        target.setPosition(0f, 0f);
        boss.setPosition(0f, 0f);
        boss.setAttackRange(5f);

        firePillar.create(() -> boss);

        // inactive when boss health not < 50%
        assertEquals(20, firePillar.getPriority());
    }

    @Test
    void transformedAttack() {
        Entity target = new Entity();
        Entity boss = createSpawner(target);
        target.setPosition(0f, 0f);
        boss.setPosition(0f, 0f);
        boss.setAttackRange(5f);

        firePillar.create(() -> boss);

        // inactive when boss health not < 50%
        assertEquals(20, firePillar.getPriority());
        boss.setEntityType("transformed");
        assertEquals(-1, firePillar.getPriority());
    }

    @Test
    void spawnPillarTest() {
        Entity target = new Entity();
        Entity boss = createSpawner(target);
        target.setPosition(0f, 0f);
        boss.setPosition(0f, 0f);
        boss.setAttackRange(5f);

        gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        when(gameTime.getTime()).thenReturn(0L);

        firePillar.create(() -> boss);
        assertEquals(20, firePillar.getPriority());
        firePillar.start();
        firePillar.start();

        // inactive when boss health not < 50%
    }


    private Entity createSpawner(Entity target) {

        AITaskComponent AI = new AITaskComponent();
        firePillar = new FirePillarTask(target, 0, 0);

        AI.addTask(firePillar);

        return new Entity()
                .addComponent(AI)
                .addComponent(mock(PhysicsMovementComponent.class))
                .addComponent(new HitboxComponent())
                .addComponent(new CombatStatsComponent(100, 10))
                .addComponent(new PhysicsComponent());
    }
}