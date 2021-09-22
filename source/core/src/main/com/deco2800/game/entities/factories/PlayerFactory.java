package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.*;
import com.deco2800.game.components.weapons.Axe;
import com.deco2800.game.components.weapons.Hammer;
import com.deco2800.game.components.weapons.Scepter;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
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

    private PlayerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Create a player entity.
     *
     * @return entity
     */
    public static Entity createPlayer(String weapon) {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForPlayer();

        AnimationRenderComponent animator;

        if (weapon.equals("Scepter")) {
            animator = new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/player_scepter.atlas", TextureAtlas.class));
            // Sceptor animations
            animator.addAnimation("left_scepter_attack", 0.1f);
            animator.addAnimation("right_scepter_attack", 0.1f);
            animator.addAnimation("up_scepter_attack", 0.1f);
            animator.addAnimation("down_scepter_attack", 0.1f);

        } else if (weapon.equals("Hammer")) {
            animator = new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/player_hammer.atlas", TextureAtlas.class));
            // Hammer/Mjolnir Animations
            animator.addAnimation("hammer_aoe", 0.1f);
            animator.addAnimation("left_hammer_attack", 0.1f);
            animator.addAnimation("right_hammer_attack", 0.1f);
            animator.addAnimation("up_hammer_attack", 0.1f);
            animator.addAnimation("down_hammer_attack", 0.1f);

            // Axe (default)
        } else {
            animator = new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/player_axe.atlas", TextureAtlas.class));
            // Axe Animations (DEFAULT)
            animator.addAnimation("left_attack", 0.1f);
            animator.addAnimation("right_attack", 0.1f);
            animator.addAnimation("up_attack", 0.1f);
            animator.addAnimation("down_attack", 0.1f);
        }

        animator.addAnimation("walk_right", 0.18f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk_down", 0.13f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk_up", 0.13f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk_left", 0.18f, Animation.PlayMode.LOOP);
        animator.addAnimation("default", 1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("default_up", 1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("default_right", 1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("default_left", 1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("damaged_right", 0.3f, Animation.PlayMode.NORMAL);
        animator.addAnimation("damaged_up", 0.3f, Animation.PlayMode.NORMAL);
        animator.addAnimation("damaged_down", 0.3f, Animation.PlayMode.NORMAL);
        animator.addAnimation("damaged_left", 0.3f, Animation.PlayMode.NORMAL);
        animator.setAnimationScale(2f);

        Entity player = new Entity()
                .addComponent(animator)
                .addComponent(new PlayerAnimationController())
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new TrapComponent().setLayer(PhysicsLayer.TRAP))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                //Remove the below lines when the player uses a separate weapon entity
                .addComponent(new WeaponHitboxComponent().setLayer(PhysicsLayer.MELEEWEAPON))
                .addComponent(new PlayerActions())
                .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                .addComponent(new InventoryComponent(stats.gold))
                .addComponent(inputComponent)
                .addComponent(new PlayerStatsDisplay())
                .addComponent(new PlayerLowHealthDisplay());

        // Add equipped weapon.
        if (weapon.equals("Scepter")) {
            player.addComponent(new Scepter(PhysicsLayer.NPC, 10, 50,
                    new Vector2(0.5f, 1f)));
        } else if (weapon.equals("Hammer")) {
            player.addComponent(new Hammer(PhysicsLayer.NPC, 10, 50,
                    new Vector2(1f, 0.5f)));
        } else { // Axe is default
            player.addComponent(new Axe(PhysicsLayer.NPC, 10, 50,
                    new Vector2(1f, 0.75f)));
        }

        player.getComponent(TrapComponent.class).setAsBox(new Vector2(0.7f, 0.4f), new Vector2(0.5f, 0.2f));
        player.getComponent(ColliderComponent.class).setDensity(1.5f);
        player.getComponent(AnimationRenderComponent.class).scaleEntity();
        //player.setScale(new Vector2().scl(2));
        PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
        player.setEntityType("player");
        return player;
    }
}
