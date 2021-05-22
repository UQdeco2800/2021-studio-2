package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.math.Vector2Utils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
class PhysicsUtilsTest {
  Entity entity;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerPhysicsService(new PhysicsService());

    entity =
        new Entity().addComponent(new ColliderComponent()).addComponent(new PhysicsComponent());
  }

  @Test
  void shouldScaleFullCollider() {
    setAndCheckScale(new Vector2(3.5f, 4.5f), Vector2Utils.ONE);
  }

  @Test
  void shouldScaleHalfCollider() {
    setAndCheckScale(Vector2Utils.ONE, new Vector2(0.5f, 0.5f));
  }

  @Test
  void shouldScaleBigCollider() {
    setAndCheckScale(new Vector2(0.5f, 0.5f), new Vector2(2f, 2f));
  }

  private void setAndCheckScale(Vector2 entityScale, Vector2 colliderScale) {
    entity.setScale(entityScale);
    PhysicsUtils.setScaledCollider(entity, colliderScale.x, colliderScale.y);
    ServiceLocator.getEntityService().register(entity);

    checkCollider(entity, entityScale.cpy().scl(colliderScale));
  }

  private void checkCollider(Entity entity, Vector2 scale) {
    ColliderComponent collider = entity.getComponent(ColliderComponent.class);
    Shape shape = collider.getFixture().getShape();
    assertTrue(shape instanceof PolygonShape);

    Vector2 bounds = getColliderBounds((PolygonShape) shape);
    assertTrue(bounds.epsilonEquals(scale));
  }

  private Vector2 getColliderBounds(PolygonShape shape) {
    Vector2 min = Vector2Utils.MAX.cpy();
    Vector2 max = Vector2Utils.MIN.cpy();
    Vector2 vect = new Vector2();

    for (int i = 0; i < shape.getVertexCount(); i++) {
      shape.getVertex(i, vect);
      min.x = Math.min(min.x, vect.x);
      min.y = Math.min(min.y, vect.y);

      max.x = Math.max(max.x, vect.x);
      max.y = Math.max(max.y, vect.y);
    }

    return max.sub(min);
  }
}
