package com.deco2800.game.components.player;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.weapons.Axe;
import com.deco2800.game.components.weapons.MeleeWeapon;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private long lockDuration;
  private long timeSinceStopped;
  private boolean moving = false;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("mouseAttack", this::mouseAttack);
    entity.getEvents().addListener("lockMovement", this::lockMovement);
    entity.getEvents().addListener("dash", this::dash);
  }

  /**
   * Updates speed of player if moving.
   * Also keeps track of lockMovement() duration.
   */
  @Override
  public void update() {
    if (moving) {
      updateSpeed();
      // lock movement if a duration is specified.
    } else if (lockDuration != 0) {
      long currentTime = ServiceLocator.getTimeSource().getTime();
      // determine whether lock duration has passed
      if ((currentTime - timeSinceStopped) >= lockDuration) {
        // unlock movement
        lockDuration = 0;
        moving = true;
      }
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    if (lockDuration == 0) {
      moving = true;
    }
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack. Player currently only uses an axe.
   * @param keycode - the last pressed player key.
   */
  void attack(int keycode) {
    System.out.println(keycode);
    MeleeWeapon weapon = entity.getComponent(Axe.class);
    if (weapon == null) {
      return;
    }
    // determine direction of attack based on last pressed key
    int attackDirection = 0;
    switch (keycode) {
      case Input.Keys.W:
        attackDirection = MeleeWeapon.UP;
        break;
      case Input.Keys.S:
        attackDirection = MeleeWeapon.DOWN;
        break;
      case Input.Keys.A:
        attackDirection = MeleeWeapon.LEFT;
        break;
      case Input.Keys.D:
        attackDirection = MeleeWeapon.RIGHT;
        break;
    }
    weapon.attack(attackDirection);
    lockMovement(weapon.getTotalAttackTime());
  }

  /**
   * Makes the player attack using a mouse click.
   * Currently, this function is only used to test AOE attacks, and
   * can't determine direction of attack based on click.
   * @param coordinates the mouse coordinates of the click
   */
  void mouseAttack(Vector2 coordinates) {
    System.out.println("called in mouseattack");
    MeleeWeapon weapon = entity.getComponent(Axe.class);
    if (weapon != null) {
      System.out.println("called in player actions");
      weapon.attack(MeleeWeapon.CENTER);
    }
  }

  /**
   * Locks player movement for a specified duration.
   * @param duration - the time the movement lock will last, measured in milliseconds.
   */
  void lockMovement(long duration) {
    timeSinceStopped = ServiceLocator.getTimeSource().getTime();
    lockDuration = duration;
    moving = false;
  }

  /**
   * The player dashes in the direction that they are currently moving in.
   */
  void dash(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
  }
}
