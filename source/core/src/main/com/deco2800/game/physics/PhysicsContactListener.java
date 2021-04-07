package com.deco2800.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.deco2800.game.events.EventHandler;

/**
 * Box2D collision events fire globally on the physics world, not per-object. The contact listener
 * receives these events, finds the entities involved in the collision, and triggers events on them.
 *
 * <p>On contact start: evt = "collisionStart", params = ({@link Fixture} thisFixture, {@link
 * Fixture} otherFixture)
 *
 * <p>On contact end: evt = "collisionEnd", params = ({@link Fixture} thisFixture, {@link Fixture}
 * otherFixture)
 */
public class PhysicsContactListener implements ContactListener {
  private final EventHandler eventHandler;

  public PhysicsContactListener() {
    this(new EventHandler());
  }

  public PhysicsContactListener(EventHandler eventHandler) {
    this.eventHandler = eventHandler;
  }

  public EventHandler getEvents() {
    return eventHandler;
  }

  @Override
  public void beginContact(Contact contact) {
    triggerEventOn(contact.getFixtureA(), "collisionStart", contact.getFixtureB());
    triggerEventOn(contact.getFixtureB(), "collisionStart", contact.getFixtureA());
  }

  @Override
  public void endContact(Contact contact) {
    triggerEventOn(contact.getFixtureA(), "collisionEnd", contact.getFixtureB());
    triggerEventOn(contact.getFixtureB(), "collisionEnd", contact.getFixtureA());
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}

  private void triggerEventOn(Fixture fixture, String evt, Fixture otherFixture) {
    BodyUserData userData = (BodyUserData) fixture.getBody().getUserData();
    if (userData != null && userData.entity != null) {
      userData.entity.getEvents().trigger(evt, fixture, otherFixture);
    }
  }
}
