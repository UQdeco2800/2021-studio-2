package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class ProjectileMovementTaskTest {

    private static final String[] sounds = {
            "sounds/arrow_disappear.mp3",
            "sounds/beam_disappear"
    };

    @BeforeEach
    void beforeEach() {
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
    }

    @Test
    void shouldMoveTowardsTarget() {
        Entity target = new Entity();
        target.setPosition(4f, 4f);

        ProjectileMovementTask projectileMovementTask = new ProjectileMovementTask(target.getPosition(), new Vector2(4f, 4f));
        AITaskComponent ai = new AITaskComponent().addTask(projectileMovementTask);
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 78; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance < initialDistance);
        // can't check because the item is already dispose - so does not exist
    }

    @Test
    void shouldChaseOnlyWhenInDistance() {
        Entity target = new Entity();
        target.setPosition(6f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        ProjectileMovementTask projectileMovementTask =
                new ProjectileMovementTask(target.getPosition(), new Vector2(2f, 2f));
        projectileMovementTask.create(() -> entity);

        // Arrow should travel toward target
        assertEquals(10, projectileMovementTask.getPriority());

        projectileMovementTask.start();
        entity.earlyUpdate();
        entity.update();
        // When active, should chase if within chase distance
        assertEquals(10, projectileMovementTask.getPriority());

    }

    @Test
    void inactivePriorityArrow() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        Entity target = new Entity();
        target.setPosition(2f, 2f);
        Entity projectile = makePhysicsEntity();
        projectile.setPosition(10f, 10f);
        ProjectileMovementTask movementTask = new ProjectileMovementTask(
                target.getPosition(), new Vector2(2f, 2f));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask);

        projectile.setEntityType("arrow");
        projectile.addComponent(aiComponent);
        projectile.create();
        projectile.update();
        projectile.setPosition(target.getPosition());
        movementTask.update();
        assertEquals(-1, movementTask.getPriority());
    }


    @Test
    void inactiveFireBall() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        Entity target = new Entity();
        target.setPosition(2f, 2f);
        Entity projectile = makePhysicsEntity();
        projectile.data.put("fireBallMovement", true);
        projectile.setPosition(10f, 10f);
        ProjectileMovementTask movementTask = new ProjectileMovementTask(
                target.getPosition(), new Vector2(2f, 2f));
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(movementTask);

        projectile.addComponent(aiComponent);
        projectile.create();
        projectile.update();
        assertEquals(10, movementTask.getPriority()); // fireball can move
        projectile.setPosition(target.getPosition());
        movementTask.update();
        assertEquals(-1, movementTask.getPriority());
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new CombatStatsComponent(100, 10));
    }
} 
