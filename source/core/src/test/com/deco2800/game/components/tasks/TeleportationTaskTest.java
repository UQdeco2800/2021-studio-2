package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
//@ExtendWith(MockitoExtension.class)
class TeleportationTaskTest {
    @Mock
    GameArea gameArea;

    @Mock
    GameTime gameTime;
    private final String[] forestTextures = {
            "images/vortex.png"
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
        resourceService.loadTextures(forestTextures);
        resourceService.loadAll();

        gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
    }

    @Test
    void insideOfAreaBound() {
        Entity taskRunner = makePhysicsEntity();

        TeleportationTask teleportationTask = new TeleportationTask(taskRunner, 1000);

        teleportationTask.create(() -> taskRunner);

        taskRunner.setPosition(2f, 2f);

        // ensure that the priority is 100 (initial priority is 100
        assertEquals(-1, teleportationTask.getPriority());

    }

    @Test
    void outsideGameBound() {
        Entity taskRunner = makePhysicsEntity();

        TeleportationTask teleportationTask = new TeleportationTask(taskRunner, 1000);
        AITaskComponent ai = new AITaskComponent();
        ai.addTask(teleportationTask);
        taskRunner.addComponent(ai);
        taskRunner.setPosition(100f, 100f);
        ServiceLocator.getGameAreaService().incNum();

        taskRunner.create();
        teleportationTask.start();


        // outside the map
        assertTrue(teleportationTask.mapBound());

        taskRunner.setPosition(2f, 2f);
        assertFalse(teleportationTask.mapBound());
    }

    @Test
    void activeCanSpawn() {
        Entity taskRunner = makePhysicsEntity();

        Entity target = new Entity();
        target.setPosition(3f, 3f);
        TeleportationTask teleportationTask = new TeleportationTask(taskRunner, 1000);
        AITaskComponent ai = new AITaskComponent();
        ai.addTask(teleportationTask);
        taskRunner.addComponent(ai);
        taskRunner.setAttackRange(5);
        taskRunner.setPosition(2f, 2f);
        taskRunner.create();
        teleportationTask.start();
        when(gameArea.getNumEnemy()).thenReturn(1);
        teleportationTask.update();
        taskRunner.update();
        taskRunner.getComponent(CombatStatsComponent.class).setHealth(40);
        taskRunner.update();
        assertEquals(30, teleportationTask.getPriority());
    }

    @Test
    void inactivePriority() {
        Entity taskRunner = makePhysicsEntity();

        Entity target = new Entity();
        target.setPosition(3f, 3f);
        TeleportationTask teleportationTask = new TeleportationTask(taskRunner, 1000);
        AITaskComponent ai = new AITaskComponent();
        ai.addTask(teleportationTask);
        taskRunner.addComponent(ai);
        taskRunner.setAttackRange(5);
        taskRunner.setPosition(2f, 2f);
        taskRunner.create();
        teleportationTask.start();
        when(gameArea.getNumEnemy()).thenReturn(1);
        teleportationTask.update();
        taskRunner.update();
        assertEquals(-1, teleportationTask.getPriority()); // health greater than 50%
    }

    @Test
    void randomTeleport() {
        Entity taskRunner = makePhysicsEntity();

        Entity target = new Entity();
        target.setPosition(3f, 3f);
        TeleportationTask teleportationTask = new TeleportationTask(taskRunner, 1000);
        AITaskComponent ai = new AITaskComponent();
        ai.addTask(teleportationTask);
        taskRunner.addComponent(ai);
        taskRunner.setAttackRange(5);
        taskRunner.setPosition(2f, 2f);
        taskRunner.create();
        teleportationTask.start();
        when(gameArea.getNumEnemy()).thenReturn(1);
        teleportationTask.update();
        taskRunner.update();
        teleportationTask.teleport();
        taskRunner.update();
        Vector2 position1 = new Vector2(3f, 3f); // almost impossible to spawn at the exact same position
        assertNotEquals(position1.x, taskRunner.getPosition().x, 0.0);
        teleportationTask.teleport();
        taskRunner.update();


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
