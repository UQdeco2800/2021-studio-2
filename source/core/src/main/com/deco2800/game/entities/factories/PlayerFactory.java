package com.deco2800.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.weapons.Axe;
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
import com.deco2800.game.rendering.TextureRenderComponent;
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
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForPlayer();

    TextureAtlas atlas= new TextureAtlas("images/my_trash/player.atlas");
    AnimationRenderComponent animator =
            new AnimationRenderComponent(atlas);
    animator.addAnimation("BackAxe_Attack", 0.1f);
    animator.addAnimation("FrontAxe_Attack", 0.1f);
    animator.addAnimation("RightAxe_Attack", 0.1f);
    animator.addAnimation("LeftAxe_Attack", 0.1f);


    Entity player =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(new InventoryComponent(stats.gold))
            .addComponent(inputComponent)
            .addComponent(new Axe(PhysicsLayer.NPC, 10, 50, new Vector2(1.25f, 0.5f)))
            .addComponent(new WeaponHitboxComponent().setLayer(PhysicsLayer.WEAPON))
            .addComponent(new PlayerStatsDisplay())
            .addComponent(animator);

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);

    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
