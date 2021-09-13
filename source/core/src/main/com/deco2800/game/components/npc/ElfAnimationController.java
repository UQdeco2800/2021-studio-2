package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ElfAnimationController extends Component {
    AnimationRenderComponent animator;

    /**
     * Create the animation
     * add listener on entity with wander and chase task
     * play animation on corresponding atlas
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener("LeftStart", this::animateLeft);
        entity.getEvents().addListener("RightStart", this::animateRight);
        entity.getEvents().addListener("UpStart", this::animateUp);
        entity.getEvents().addListener("DownStart", this::animateDown);

        entity.getEvents().addListener("rangedLeftShoot", this::animateRangerLeft);
        entity.getEvents().addListener("rangedRightShoot", this::animateRangerRight);
        entity.getEvents().addListener("rangedUpShoot", this::animateRangerUp);
        entity.getEvents().addListener("rangedDownShoot", this::animateRangerDown);

        entity.getEvents().addListener("assassinLeftShoot", this::animateAssassinLeft);
        entity.getEvents().addListener("assassinRightShoot", this::animateAssassinRight);
        entity.getEvents().addListener("assassinUpShoot", this::animateAssassinUp);
        entity.getEvents().addListener("assassinDownShoot", this::animateAssassinDown);
   }

    public void animateLeft() {
        animator.startAnimation("moveLeft");
    }

    public void animateRight() {
        animator.startAnimation("moveRight");
    }

    public void animateUp() {
        animator.startAnimation("moveUp");
    }

    public void animateDown() {
        animator.startAnimation("moveDown");
    }

    public void animateRangerLeft() {
        animator.startAnimation("rangerLeft");
    }

    public void animateRangerRight() {
        animator.startAnimation("rangerRight");
    }

    public void animateRangerUp() {
        animator.startAnimation("rangerUp");
    }

    public void animateRangerDown() {
        animator.startAnimation("rangerDown");
    }

    public void animateAssassinLeft() {
        animator.startAnimation("assassinLeft");
    }

    public void animateAssassinRight() {
        animator.startAnimation("assassinRight");
    }

    public void animateAssassinUp() {
        animator.startAnimation("assassinUp");
    }

    public void animateAssassinDown() {
        animator.startAnimation("assassinDown");
    }

  }