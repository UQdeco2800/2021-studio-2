package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;

/**
 * Lets an entity be controlled by physics. Do not directly modify the position of a physics-enabled
 * entity. Instead, use forces to move it.
 */
public class PhysicsComponent extends Component {
  private static final float GROUND_FRICTION = 5f;
  private final PhysicsEngine physics;
  private final Body body;

  /** Create a physics component with default settings. */
  public PhysicsComponent() {
    this(ServiceLocator.getPhysicsService().getPhysics());
  }

  /**
   * Create a physics component
   *
   * @param engine The physics engine to attach the component to
   */
  public PhysicsComponent(PhysicsEngine engine) {
    this.physics = engine;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.fixedRotation = true;
    bodyDef.linearDamping = GROUND_FRICTION;
    bodyDef.angle = 0f;
    bodyDef.active = false;
    body = physics.createBody(bodyDef);
  }

  /**
   * Set body type
   *
   * @param bodyType body type, default = dynamic
   * @return self
   */
  public PhysicsComponent setBodyType(BodyType bodyType) {
    body.setType(bodyType);
    return this;
  }

  /**
   * Get the physics body.
   *
   * @return physics body if entity has been created, null otherwise.
   */
  public Body getBody() {
    return body;
  }

  @Override
  public void create() {
    body.setTransform(entity.getPosition(), 0f);
    body.setActive(true);

    BodyUserData userData = new BodyUserData();
    userData.entity = entity;
    body.setUserData(userData);

//    // Start listening to entity position changes
//    entity.getEvents().addListener("setPosition", this::onSetPosition);
  }

  /**
   * Entity position needs to be updated to match the new physics position. This should happen
   * before other updates, which may use the new position.
   */
  @Override
  public void earlyUpdate() {
    Vector2 bodyPos = body.getPosition();
    entity.setPosition(bodyPos);
  }

  @Override
  public void dispose() {
    physics.destroyBody(body);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    body.setActive(enabled);
  }

  public enum AlignX {
    Left,
    Center,
    Right
  }

  public enum AlignY {
    Bottom,
    Center,
    Top
  }
}
