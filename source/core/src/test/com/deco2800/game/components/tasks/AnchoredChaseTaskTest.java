package com.deco2800.game.components.tasks;

import com.deco2800.game.entities.Entity;
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
class AnchoredChaseTaskTest {

    @BeforeEach
    void setUp() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    /**
     * Idea of anchored chase test is entity only chase target if target is within anchor range
     */
    @Test
    void isTargetAtBase() {

        Entity target = new Entity();
        target.setPosition(6f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        Entity anchor = new Entity();
        // entity can chase
        AnchoredChaseTask anchoredChaseTask = new AnchoredChaseTask(target, 10f, 10f,
                anchor, 3f);
        anchoredChaseTask.create(() -> entity);

        // Entity is not start so return -1 by default
        assertEquals(-1, anchoredChaseTask.getPriority());

        // Target is outside of anchor protect radius so ignore
        anchoredChaseTask.start();
        assertEquals(-1, anchoredChaseTask.getPriority());

        target.setPosition(2f, 2f);
        // Target is within anchor so chase target
        entity.setPosition(6f, 6f);
        anchoredChaseTask.update();
        // When active, should not chase outside chase distance
        assertEquals(10, anchoredChaseTask.getPriority());

        target.setPosition(4f, 4f);
        assertEquals(-1, anchoredChaseTask.getPriority());

        // entity can be next to target, but won't chase target unless target approach anchor
        entity.setPosition(4f, 4f);
        assertEquals(-1, anchoredChaseTask.getPriority());
    }

    @Test
    void entityLessViewDistance() {

        Entity target = new Entity();
        target.setPosition(6f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        Entity anchor = new Entity();
        // entity can chase
        AnchoredChaseTask anchoredChaseTask = new AnchoredChaseTask(target, 3f, 4f,
                anchor, 10f);
        anchoredChaseTask.create(() -> entity);

        // Entity is not start so return -1 by default
        assertEquals(-1, anchoredChaseTask.getPriority());

        // Target is outside of entity viewDistance so ignore, even if target within anchor radius
        anchoredChaseTask.start();
        assertEquals(-1, anchoredChaseTask.getPriority());

        target.setPosition(0f, 3f);
        // Target is within anchor protect radius, and entity can see target
        anchoredChaseTask.update();
        // When active, should not chase outside chase distance
        assertEquals(10, anchoredChaseTask.getPriority());

    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
} 
