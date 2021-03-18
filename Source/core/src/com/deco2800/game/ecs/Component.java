package com.deco2800.game.ecs;

/**
 * Core component class from which all components inherit. Contains logic for creating, updating,
 * and disposing. Components can be attached to an entity to give it specific behaviour. It is
 * unlikely that changes will need to be made here.
 */
public class Component {
  protected Entity entity;
  protected boolean enabled = true;

  /**
   * Called when the entity is created and registered. Initial logic such as calls to GetComponent
   * should be made here, not in the constructor which is called before an entity is finished.
   */
  public void create() {}

  /**
   * Early update called once per frame of the game, before update(). Use this only for logic that
   * must run before other updates, such as physics. Not called if component is disabled.
   */
  public void earlyUpdate() {}

  /**
   * Called once per frame of the game, and should be used for most component logic. Not called if
   * component is disabled.
   */
  public void update() {}

  /** Called when the component is disposed. Dispose of any internal resources here. */
  public void dispose() {}

  /**
   * Set the entity to which this component belongs. This is called by the Entity, and should not be
   * set manually.
   *
   * @param entity The entity to which the component is attached.
   */
  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  /**
   * Enable or disable the component. While disabled, a component does not run update() or
   * earlyUpdate(). Other events inside the component may still fire. The component can still be
   * disposed while disabled.
   *
   * @param enabled Should component be enabled
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /** Used to trigger the component to update itself. This should not need to be called manually. */
  public final void triggerUpdate() {
    if (enabled) {
      update();
    }
  }

  /**
   * Used to trigger the component to early-update itself. This should not need to be called
   * manually.
   */
  public final void triggerEarlyUpdate() {
    if (enabled) {
      earlyUpdate();
    }
  }
}
