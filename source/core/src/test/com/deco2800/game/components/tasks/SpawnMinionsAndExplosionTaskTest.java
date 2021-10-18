package com.deco2800.game.components.tasks;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
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
class SpawnMinionsAndExplosionTaskTest {

    @Mock
    GameTime gameTime;

    private static final String[] forestTextureAtlases = {
            "images/rangedElf.atlas", "images/meleeElf.atlas",
            "images/meleeFinal.atlas", "images/rangedAllFinal.atlas", "images/explosion/explosion.atlas"
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
            "images/vortex.png",
            "images/explosion/explosion.png"
    };

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        GameArea gameArea = mock(GameArea.class);
        ServiceLocator.registerGameArea(gameArea);

        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadTextures(forestTextures);
        resourceService.loadAll();
    }

    @Test
    void inactivePriority() {
        Entity boss = createBoss();
        Entity target = new Entity();
        SpawnMinionsAndExplosionTask spawn =
                new SpawnMinionsAndExplosionTask(target);

        spawn.create(() -> boss);

        // inactive when boss health not < 50%
        assertEquals(-1, spawn.getPriority());
    }

    @Test
    void activePriority() {
        gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        when(gameTime.getTime()).thenReturn(0L);

        Entity boss = createBoss();
        Entity target = new Entity();
        SpawnMinionsAndExplosionTask spawn =
                new SpawnMinionsAndExplosionTask(target);

        spawn.create(() -> boss);

        boss.create();
        // inactive when boss health not < 50%


        // active when boss health < 50%
        boss.getComponent(CombatStatsComponent.class).setHealth(40);

        // active
        assertEquals(20, spawn.getPriority());

        spawn.update();
        // inactive after the task is update
        assertEquals(-1, spawn.getPriority());

        boss.getComponent(CombatStatsComponent.class).setHealth(24);
        // active again if health is reduce to 25%
        assertEquals(20, spawn.getPriority());

        // boss can only spawn at most two wave of enemy - one at < 50%, and one at < 25%

    }

    private Entity createBoss() {
        return new Entity()
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent())
                .addComponent(new CombatStatsComponent(100, 10))
                .addComponent(new PhysicsComponent());
    }
}
