package com.deco2800.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.Task.Status;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class MovementTaskTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  @Test
  void shouldMoveOnStart() {
    Vector2 target = new Vector2(10f, 10f);
    MovementTask task = new MovementTask(target);
    Entity entity = new Entity().addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.create();

    task.start(entity);
    assertTrue(movementComponent.getMoving());
    assertEquals(target, movementComponent.getTarget());
    assertEquals(Status.Active, task.getStatus());
  }

  @Test
  void shouldStopWhenClose() {
    MovementTask task = new MovementTask(new Vector2(10f, 10f), 2f);
    Entity entity = new Entity().addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.setPosition(5f, 5f);
    entity.create();

    task.start(entity);
    task.update();
    assertTrue(movementComponent.getMoving());
    assertEquals(Status.Active, task.getStatus());

    entity.setPosition(10f, 9f);
    task.update();
    assertFalse(movementComponent.getMoving());
    assertEquals(Status.Finished, task.getStatus());
  }
}