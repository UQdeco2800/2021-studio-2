package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;


@ExtendWith(GameExtension.class)
class ZigChaseTaskTest {
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
    void shouldMoveTowardsTarget() {
        Entity target = new Entity();
        target.setPosition(3f, 3f);

        AITaskComponent ai = new AITaskComponent()
                .addTask(new ZigChaseTask(target, 10, 5f, 6f, 1f));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        long time = System.nanoTime();
        long timeCompare = System.nanoTime();
        int count = 0;
        // Can't really test the zig zag movement, just test the enemies always move toward target
        while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time) < 2000) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
            if (initialDistance - entity.getPosition().dst(target.getPosition()) == 0
                    && entity.getPosition().dst(target.getPosition()) > 1f) {
                // distance > 1 because if less than 1, the entity is approach target
                count++;
            }
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeCompare) > 500) {
                assertTrue(initialDistance - entity.getPosition().dst(target.getPosition()) != 0);
                break;
            }
        }
        //System.out.println(count);
    }

    @Test
    void shouldChaseOnlyWhenInDistance() {
        Entity target = new Entity();
        target.setPosition(0f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        ZigChaseTask zigChaseTask  = new ZigChaseTask(target, 10, 5, 10, 1f);
        zigChaseTask.create(() -> entity);

        // Not currently active, target is too far, should have negative priority
        assertTrue(zigChaseTask.getPriority() < 0);

        // When in view distance, should give higher priority
        target.setPosition(0f, 4f);
        assertEquals(10, zigChaseTask.getPriority());

        // When active, should chase if within chase distance
        target.setPosition(0f, 8f);
        zigChaseTask.start();
        assertEquals(10, zigChaseTask.getPriority());

        // When active, should not chase outside chase distance
        target.setPosition(0f, 12f);
        assertTrue(zigChaseTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}
