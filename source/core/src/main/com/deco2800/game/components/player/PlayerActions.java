package com.deco2800.game.components.player;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.weapons.MeleeWeapon;
import com.deco2800.game.components.weapons.Weapon;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import java.util.concurrent.TimeUnit;

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
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
      // lock movement for a specified duration, if a lock duration is specified
    } else if (lockDuration != 0) {
      long currentTime = ServiceLocator.getTimeSource().getTime();
      // determine whether lock duration has passed
      if ((currentTime - timeSinceStopped) >= lockDuration) {
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
   * Makes the player attack.
   */
  void attack(int keycode) {
    Weapon weapon = entity.getComponent(MeleeWeapon.class);
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
    lockMovement(600L);
  }

  /**
   * Makes the player attack using a mouse click.
   * @param coordinates the mouse coordinates of the click
   */
  void mouseAttack(Vector2 coordinates) {
    System.out.println("called in mouseattack");
    Weapon weapon = entity.getComponent(MeleeWeapon.class);
    if (weapon != null) {
      System.out.println("called in player actions");
      weapon.attack(0);
      return;
    }
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
    entity.getComponent(AnimationRenderComponent.class).startAnimation("sprite");
  }

  void lockMovement(long duration) {
    timeSinceStopped = ServiceLocator.getTimeSource().getTime();
    lockDuration = duration;
    stopWalking();
  }
}
