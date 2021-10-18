package com.deco2800.game.components.tasks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class DeathPauseTaskTest {

    protected static final String[] sounds = {
            "sounds/death_2.mp3",
            "sounds/death_1.mp3",
            "sounds/boss_death.mp3",
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
    };

    @Mock
    GameArea gameArea;

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
//        GameTime gameTime = mock(GameTime.class);
//        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
//        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);


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

    @Test
    void deathEnemyTrigger() {
        gameArea = mock(GameArea.class);
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadTextures(forestTextures);
        ServiceLocator.getResourceService().loadAll();
        Sprite healthBar = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar.png", Texture.class));
        Sprite healthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_bar_decrease.png", Texture.class));
        Sprite healthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/enemy_health_border.png", Texture.class));
        Entity taskRunner = makePhysicsEntity();
        taskRunner.addComponent(new HealthBarComponent(healthBar, healthBarDecrease, healthBarFrame));
        taskRunner.setEntityType("melee");
        DeathPauseTask deathPauseTask = new DeathPauseTask(taskRunner, 0, 100, 100, 1.5f);

        deathPauseTask.create(() -> taskRunner);

        taskRunner.create();

        taskRunner.getComponent(CombatStatsComponent.class).setHealth(0);
        deathPauseTask.update();
        assertEquals(100, deathPauseTask.getPriority()); // test the image display and play death sound
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new CombatStatsComponent(100, 10))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.5f));
    }
}
