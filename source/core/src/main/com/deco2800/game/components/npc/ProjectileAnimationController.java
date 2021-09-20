package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ProjectileAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean death;

    @Override
    public void create() {
        super.create();
        death = false;

        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("brokenArrow", this::broken);
    }


    public void broken() {
        if (!entity.getEntityType().equals("fireBall")) {
            animator.getEntity().setScale(animator.getEntity().getScale().x * 1.5f,
                    animator.getEntity().getScale().y * 5f);
            animator.startAnimation("brokenArrow");
        } else {
            animator.startAnimation("hit");
        }
    }

}
