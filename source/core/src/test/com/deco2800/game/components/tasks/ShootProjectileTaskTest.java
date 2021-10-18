package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShootProjectileTaskTest {

    @BeforeEach
    void setUp() {
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void checkShootPossible() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setMultishotChance(0.1);
        AITaskComponent ai = new AITaskComponent().addTask(shootProjectileTask);
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setAttackRange(5);
        entity.setPosition(0f, 0f);
        // can see enemy
        assertEquals(20, shootProjectileTask.getPriority());
        shootProjectileTask.start();
        // need to wait 2 seconds (cool down) before can shoot
        long time = System.currentTimeMillis();
        //noinspection StatementWithEmptyBody
        while (System.currentTimeMillis() - time <= 2000) {
            // Wait
        }
        // can see enemy
        assertEquals(20, shootProjectileTask.getPriority());

        // need to count the number of spawn entity
        for (int i = 0; i < 5; i++) {
            entity.earlyUpdate();
            //entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        assertEquals(20, shootProjectileTask.getPriority());
    }

    @Test
    void updateCooldown() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setMultishotChance(0.1);
        AITaskComponent ai = new AITaskComponent().addTask(shootProjectileTask);
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setAttackRange(5);
        entity.setPosition(0f, 0f);
        shootProjectileTask.setCooldownMS(500);
        // can see enemy
        assertEquals(20, shootProjectileTask.getPriority());
        shootProjectileTask.start();
        // need to wait 2 seconds (cool down) before can shoot
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time <= 700) {
            // Wait
            entity.earlyUpdate();
        }
        // can see enemy
        assertEquals(20, shootProjectileTask.getPriority());

        // need to count the number of spawn entity
        for (int i = 0; i < 5; i++) {
            entity.earlyUpdate();
            //entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        assertEquals(20, shootProjectileTask.getPriority());
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
} 
