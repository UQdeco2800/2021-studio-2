package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.weapons.MeleeWeapon;
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

    // my trash: TODO: delete this later
    TextureAtlas atlas= new TextureAtlas("images/my_trash/example.atlas");
    AnimationRenderComponent animator =
            new AnimationRenderComponent(atlas);
    animator.addAnimation("sprite", 0.2f);

    Entity player =
        new Entity()
            //.addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(new InventoryComponent(stats.gold))
            .addComponent(inputComponent)
                .addComponent(new MeleeWeapon(PhysicsLayer.NPC, 10, 10, new Vector2(2f, 2f),600L))
                .addComponent(new WeaponHitboxComponent().setLayer(PhysicsLayer.WEAPON))
            .addComponent(new PlayerStatsDisplay())
                .addComponent(animator); // remove this later

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
   // player.getComponent(WeaponHitboxComponent.class).setAsBox(new Vector2(1f, 1f));
   // PhysicsUtils.setScaledCollider(player, 1.5f, 1.5f, WeaponHitboxComponent.class);

    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    //player.getComponent(TextureRenderComponent.class).scaleEntity();
    return player;
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
