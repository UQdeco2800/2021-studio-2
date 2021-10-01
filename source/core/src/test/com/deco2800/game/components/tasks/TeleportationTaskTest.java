package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.WeaponFactory;
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
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TeleportationTaskTest {

    private final String[] forestTextures = {
            "images/vortex.png"
    };

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        TerrainFactory terrain;
        terrain = mock(TerrainFactory.class);
        ServiceLocator.registerGameArea(new GameArea() {
            @Override
            public void create() {
                super.create();
            }
        });
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(forestTextures);
        resourceService.loadAll();

    }

    // need to mock terrain, image, combat stat component, ...
    /*
    @Test
    void outsideOfAreaBound() {
        Entity taskRunner = makePhysicsEntity();
        taskRunner.setPosition(100, 100);
        System.out.println(taskRunner.getPosition());
        AITaskComponent aiTaskComponent = new AITaskComponent();
        aiTaskComponent.addTask(new WanderTask(new Vector2(2f, 2f), 2f));
        TeleportationTask teleportationTask = new TeleportationTask(taskRunner, 1000);

        aiTaskComponent.addTask(teleportationTask);

        taskRunner.addComponent(aiTaskComponent);

        taskRunner.create();

        teleportationTask.update();
        System.out.println(taskRunner.getPosition());

        // ensure that the priority is 10
        assertEquals(100, teleportationTask.getPriority());
    }

     */

    @Test
    void insideOfAreaBound() {
        Entity taskRunner = makePhysicsEntity();

        TeleportationTask teleportationTask = new TeleportationTask(taskRunner, 1000);

        teleportationTask.create(() -> taskRunner);

        System.out.println(taskRunner.getPosition());
        taskRunner.setPosition(2f,2f);

        // ensure that the priority is 100 (initial priority is 100
        assertEquals(100, teleportationTask.getPriority());

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