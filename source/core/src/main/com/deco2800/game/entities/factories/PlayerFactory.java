package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.weapons.Axe;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.player.*;
import com.deco2800.game.components.death.DeathActions;
import com.deco2800.game.components.death.DeathDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.WeaponHitboxComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create a player entity.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats =
          FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * @param game instance of the game
   * @return entity
   */
  public static Entity createPlayer(GdxGame game) {
    InputComponent inputComponent =
            ServiceLocator.getInputService().getInputFactory().createForPlayer();

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/player.atlas", TextureAtlas.class));

    animator.addAnimation("walk_right", 0.18f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_forward", 0.13f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_backward", 0.13f, Animation.PlayMode.LOOP);
    animator.addAnimation("walk_left", 0.18f, Animation.PlayMode.LOOP);
    animator.addAnimation("default", 1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("default_backward", 1, Animation.PlayMode.NORMAL);
    animator.addAnimation("default_right", 1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("default_left", 1f, Animation.PlayMode.NORMAL);
    animator.addAnimation("back_axe_attack", 0.1f);
    animator.addAnimation("front_axe_attack", 0.1f);
    animator.addAnimation("right_axe_attack", 0.1f);
    animator.addAnimation("left_axe_attack", 0.1f);

    Entity player =
            new Entity()
                    .addComponent(animator)
                    .addComponent(new PlayerAnimationController())
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                    //Remove the below lines when the player uses a separate weapon entity
                    .addComponent(new WeaponHitboxComponent().setLayer(PhysicsLayer.MELEEWEAPON))
                    .addComponent(new Axe(PhysicsLayer.NPC, 10, 50,
                            new Vector2(1f, 0.5f)))
                    
                    .addComponent(new PlayerActions())
                    .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                    .addComponent(new InventoryComponent(stats.gold))
                    .addComponent(inputComponent)
                    .addComponent(new PlayerStatsDisplay())
                    .addComponent(new PlayerLowHealthDisplay())
                    .addComponent(new DeathDisplay())
                    .addComponent(new DeathActions(game));

    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(AnimationRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
