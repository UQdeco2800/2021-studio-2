package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class OdinAnimationController extends Component {
    AnimationRenderComponent odinAnimator;

    @Override
    public void create() {
        super.create();
        odinAnimator = entity.getComponent(AnimationRenderComponent.class);
        addEvents();

    }

    private void addEvents() {
        //walk animations
        entity.getEvents().addListener("moveRight", this::animateRight);
        entity.getEvents().addListener("moveLeft", this::animateLeft);
        entity.getEvents().addListener("moveUp", this::animateUp);
        entity.getEvents().addListener("moveDown", this::animateDown);

        //attack animations
        entity.getEvents().addListener("LeftAttack", this::animateRightAttack);
        entity.getEvents().addListener("RightAttack", this::animateLeftAttack);
        entity.getEvents().addListener("UpAttack", this::animateUpAttack);
        entity.getEvents().addListener("DownAttack", this::animateDownAttack);
    }

    private void animateRight() {
        odinAnimator.startAnimation("moveRight");
    }

    private void animateLeft() {
        odinAnimator.startAnimation("moveLeft");
    }

    private void animateUp() {
        odinAnimator.startAnimation("moveUp");
    }

    private void animateDown() {
        odinAnimator.startAnimation("moveDown");
    }

    private void animateLeftAttack() {
        odinAnimator.startAnimation("LeftAttack");
    }

    private void animateRightAttack() {
        odinAnimator.startAnimation("RightAttack");
    }

    private void animateUpAttack() {
        odinAnimator.startAnimation("UpAttack");
    }

    private void animateDownAttack() {
        odinAnimator.startAnimation("DownAttack");
    }
}
