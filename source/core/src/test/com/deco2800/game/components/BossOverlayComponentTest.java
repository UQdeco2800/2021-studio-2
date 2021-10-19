package com.deco2800.game.components;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class BossOverlayComponentTest {
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
    };

    @Mock
    GameArea gameArea;

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
        resourceService.loadTextures(forestTextures);
        resourceService.loadAll();
    }

    @Test
    void checkComponentCreate() {

        Entity entity = new Entity();
        entity.addComponent(new CombatStatsComponent(10, 10));
        BossOverlayComponent component = mock(BossOverlayComponent.class);
        entity.addComponent(component);

        entity.create();

        verify(component).create();
    }

    @Test
    void checkComponentDispose() {

        Entity entity = new Entity();
        entity.addComponent(new CombatStatsComponent(10, 10));
        BossOverlayComponent component = mock(BossOverlayComponent.class);
        entity.addComponent(component);
        entity.create();
        component.dispose();
        verify(component).dispose();

    }

    @Test
    void checkEnemySpawnCondition() {
        Entity boss = mock(Entity.class);
        BossOverlayComponent bossOverlayComponent = new BossOverlayComponent();
        boss.addComponent(bossOverlayComponent);

        boss.create();
        when(gameArea.getNumEnemy()).thenReturn(0);
        bossOverlayComponent.update();
        assertTrue(bossOverlayComponent.hasEnemySpawned());

    }

}
