package com.deco2800.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.deco2800.game.components.Component;
import com.deco2800.game.events.EventHandler;

public class PhysicsContactListener extends Component implements ContactListener {
  private final EventHandler eventHandler;

  public PhysicsContactListener() {
    eventHandler = new EventHandler();
  }

  public EventHandler getEvents() {
    return eventHandler;
  }

  @Override
  public void beginContact(Contact contact) {
    triggerEventOn(contact.getFixtureA(), "beginContact", contact);
    triggerEventOn(contact.getFixtureB(), "beginContact", contact);
  }

  @Override
  public void endContact(Contact contact) {
    triggerEventOn(contact.getFixtureA(), "endContact", contact);
    triggerEventOn(contact.getFixtureB(), "endContact", contact);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}

  private void triggerEventOn(Fixture fixture, String evt, Contact contact) {
    BodyUserData userData = (BodyUserData) fixture.getBody().getUserData();
    if (userData != null && userData.entity != null) {
      userData.entity.getEvents().trigger(evt, contact);
    }
  }
}
