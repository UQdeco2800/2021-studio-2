package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class WeaponDisposeTaskTest {
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
    void getInactivePriority() {
        Entity target = new Entity();
        WeaponDisposeTask weaponDisposeTask = new WeaponDisposeTask(target.getPosition(),
                new Vector2(10, 10), 0.8f);

        Entity owner = makePhysicsEntity();

        weaponDisposeTask.create(() -> owner);

        assertEquals(0, weaponDisposeTask.getPriority());
    }

    @Test
    void getActivePriority() {
        Entity target = new Entity();
        WeaponDisposeTask weaponDisposeTask = new WeaponDisposeTask(target.getPosition(),
                new Vector2(10, 10), 0.8f);

        Entity owner = makePhysicsEntity();

        weaponDisposeTask.create(() -> owner);

        owner.getComponent(CombatStatsComponent.class).setHealth(0);

        weaponDisposeTask.update();

        assertEquals(100, weaponDisposeTask.getPriority());
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent())
                .addComponent(new CombatStatsComponent(100, 0));
    }
}
