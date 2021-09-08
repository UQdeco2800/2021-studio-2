package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class BossTeleportationController extends Component {
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

        entity.getEvents().addListener("teleportLeft", this::teleportLeft);
        entity.getEvents().addListener("teleportRight", this::teleportRight);
        entity.getEvents().addListener("teleportFront", this::teleportFront);
        entity.getEvents().addListener("teleportBack", this::teleportBack);

        entity.getEvents().addListener("appearLeft", this::appearLeft);
        entity.getEvents().addListener("appearRight", this::appearRight);
        entity.getEvents().addListener("appearFront", this::appearFront);
        entity.getEvents().addListener("appearBack", this::appearBack);

    }

    private void teleportLeft() {
        animator.startAnimation("teleportLeft");
    }

    private void teleportRight() {
        animator.startAnimation("teleportRight");
    }

    private void teleportFront() {
        animator.startAnimation("teleportFront");
    }

    private void teleportBack() {
        animator.startAnimation("teleportBack");
    }


    /**
     * animation of enemy appear facing left
     */
    private void appearLeft() {
        animator.startAnimation("appearLeft");
    }

    /**
     * animation of enemy appear facing right
     */
    private void appearRight() {
        animator.startAnimation("appearRight");
    }

    /**
     * animation of enemy appear facing front
     */
    private void appearFront() {
        animator.startAnimation("appearFront");
    }

    /**
     * animation of enemy appear facing back
     */
    private void appearBack() {
        animator.startAnimation("appearBack");
    }

}
