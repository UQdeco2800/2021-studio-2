package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.weapons.Hammer;
import com.deco2800.game.components.weapons.MeleeWeapon;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;

    Hammer mjolnir;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        mjolnir = this.entity.getComponent(Hammer.class);
        entity.getEvents().addListener("walkForward", this::animateForwardWalk);
        entity.getEvents().addListener("walkRight", this::animateRightWalk);
        entity.getEvents().addListener("walkLeft", this::animateLeftWalk);
        entity.getEvents().addListener("walkBackward", this::animateBackwardWalk);
        entity.getEvents().addListener("stopForward", this::stopForwardWalk);
        entity.getEvents().addListener("stopRight", this::stopRightWalk);
        entity.getEvents().addListener("stopLeft", this::stopLeftWalk);
        entity.getEvents().addListener("stopBackward", this::stopBackwardWalk);
        stopForwardWalk();
    }

    /**
     * Determines whether the player is currently holding mjolnir
     * @return whether character is holding mjolnir.
     */
    boolean hasMjolnir() {
        return false; //!(mjolnir != null && mjolnir.isEquipped());
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk forward.
     */
    void animateForwardWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("walk_down_mjolnir");
        } else {
            animator.startAnimation("walk_down");
        }
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk to the right.
     */
    void animateRightWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("walk_right_mjolnir");
        } else {
            animator.startAnimation("walk_right");
        }
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk to the left.
     */
    void animateLeftWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("walk_left_mjolnir");
        } else {
            animator.startAnimation("walk_left");
        }
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk backwards.
     */
    void animateBackwardWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("walk_up_mjolnir");
        } else {
            animator.startAnimation("walk_up");
        }
    }

    /**
     * Once triggered, it changes the animation of the sprite to be shown in the default frame.
     */
    void stopForwardWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("default_mjolnir");
        } else {
            animator.startAnimation("default");
        }
    }

    /**
     * Once triggered, it changes the animation of the sprite to default frame facing backwards.
     */
    void stopBackwardWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("default_backward_mjolnir");
        } else {
            animator.startAnimation("default_backward");
        }
    }

    /**
     * Once triggered, it changes the animation of the sprite to the default frame facing to the left.
     */
    void stopLeftWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("default_left_mjolnir");
        } else {
            animator.startAnimation("default_left");
        }
    }

    /**
     * Once triggered, it changes the animation of the sprite to the default frame facing to the right.
     */
    void stopRightWalk() {
        if (hasMjolnir()) {
            animator.startAnimation("default_right_mjolnir");
        } else {
            animator.startAnimation("default_right");
        }
    }
}
