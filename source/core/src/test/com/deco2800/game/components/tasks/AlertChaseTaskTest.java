package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class AlertChaseTaskTest {

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void ShouldSeeTheEnemyIfIsAlerted() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        AlertChaseTask alertChaseTask = new AlertChaseTask(target, 10, 3, 4);
        AITaskComponent ai = new AITaskComponent().addTask(alertChaseTask);
        Entity entity = makePhysicsEntity().addComponent(ai);
        AlertableChaseTask alertableChaseTask = new AlertableChaseTask(target, 10, 5, 10);
        AITaskComponent aiAlertable = new AITaskComponent().addTask(alertableChaseTask);
        Entity alertableEntity = makePhysicsEntity().addComponent(aiAlertable);
        alertableChaseTask.create(() -> alertableEntity);
        alertChaseTask.create(() -> entity);
        entity.setPosition(0f, 0f);
        alertableEntity.setPosition(8f, 8f);
        entity.setEntityType("AlertCaller");
        EntityService entityService = new EntityService();
        entityService.register(entity);
        entityService.register(alertableEntity);
        ServiceLocator.registerEntityService(entityService);

        float oldDistance = alertableEntity.getPosition().dst(target.getPosition());

        for (int i = 0; i < 3; i++) {
            alertableEntity.update();
        }
        float newDistance1 = alertableEntity.getPosition().dst(target.getPosition());

        // target is too far from enemy
        assertEquals(oldDistance - newDistance1, 0);

        for (int i = 0; i < 3; i++) {
            entity.update();
            alertableEntity.earlyUpdate();
            alertableEntity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        float newDistance2 = alertableEntity.getPosition().dst(target.getPosition());
        // fail because the enemy is not alerted - alert task fail
        assertEquals(newDistance2 - oldDistance, 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}
