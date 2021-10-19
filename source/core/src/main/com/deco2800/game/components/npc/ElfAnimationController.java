package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ElfAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean death;
    private static final String ASSASSIN_TYPE = "assassin";
    private static final String RANGED_TYPE = "ranged";

    /**
     * Create the animation
     * add listener on entity with wander and chase task
     * play animation on corresponding atlas
     */
    @Override
    public void create() {
        super.create();
        death = false;
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener("LeftStart", this::animateLeft);
        entity.getEvents().addListener("RightStart", this::animateRight);
        entity.getEvents().addListener("UpStart", this::animateUp);
        entity.getEvents().addListener("DownStart", this::animateDown);

        entity.getEvents().addListener("attackLeft", this::animateLeftAttack);
        entity.getEvents().addListener("attackRight", this::animateRightAttack);
        entity.getEvents().addListener("attackUp", this::animateUpAttack);
        entity.getEvents().addListener("attackDown", this::animateDownAttack);

        entity.getEvents().addListener("stunLeft", this::animateLeftStun);
        entity.getEvents().addListener("stunRight", this::animateRightStun);
        entity.getEvents().addListener("stunUp", this::animateUpStun);
        entity.getEvents().addListener("stunDown", this::animateDownStun);

        animateDown();
    }

    public void setDeath() {
        death = true;
    }

    private void animate(String modifier) {
        if (!death) {
            switch (entity.getEntityType()) {
                case ASSASSIN_TYPE:
                    animator.startAnimation(ASSASSIN_TYPE + modifier);
                    break;
                case RANGED_TYPE:
                    animator.startAnimation(RANGED_TYPE + modifier);
                    break;
                default:
                    animator.startAnimation("move" + modifier);
                    break;
            }
        } else {
            if (modifier.equals("Up")) {
                modifier = "Front";
            }
            if (modifier.equals("Down")) {
                modifier = "Back";
            }
            if (!entity.getEntityType().equals("elfBoss")) {
                if (entity.getEntityType().equals(ASSASSIN_TYPE)) {
                    animator.startAnimation(ASSASSIN_TYPE + modifier + "Death");
                } else {
                    animator.startAnimation(modifier.toLowerCase() + "Death");
                }
            } else {
                animator.startAnimation(modifier.toLowerCase() + "BossDeath");
            }
            //Add death animations for Assassin, guard, boss
        }
    }

    public void animateLeft() {
        animate("Left");
    }

    public void animateRight() {
        animate("Right");
    }

    public void animateUp() {
        animate("Up");
    }

    public void animateDown() {
        animate("Down");
    }

    public void animateLeftAttack() {
        animator.startAnimation("attackLeft");
    }

    public void animateRightAttack() {
        animator.startAnimation("attackRight");
    }

    public void animateUpAttack() {
        animator.startAnimation("attackUp");
    }

    public void animateDownAttack() {
        animator.startAnimation("attackDown");
    }

    private void stun(String modifier) {
        switch (entity.getEntityType()) {
            case ASSASSIN_TYPE:
                animator.startAnimation("assassinStun" + modifier);
                break;
            case RANGED_TYPE:
                animator.startAnimation("rangedStun" + modifier);
                break;
            default:
                animator.startAnimation("stun" + modifier);
                break;
        }
    }

    public void animateLeftStun() {
        stun("Left");
    }

    public void animateRightStun() {
        stun("Right");
    }

    public void animateUpStun() {
        stun("Up");
    }

    public void animateDownStun() {
        stun("Down");
    }


}
