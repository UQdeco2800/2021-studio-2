package com.deco2800.game.components.tasks.thor;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * An 'AI Task' that sets an entity to destroy itself after a set duration.
 */
public class LightningProjectile extends Component {
    private final long lifeSpan;
    private long timeAtCreation;

    private final int attackStatus;
    public static final int AOE = 0;
    public static final int FOLLOW_PLAYER = 1;

    private AnimationRenderComponent animator;

    public LightningProjectile(long lifeSpan, int attackStatus) {
        this.lifeSpan = lifeSpan;
        this.attackStatus = attackStatus;
    }

    @Override
    public void create() {
        this.timeAtCreation = ServiceLocator.getTimeSource().getTime();
        this.animator = entity.getComponent(AnimationRenderComponent.class);

        if (attackStatus == AOE) {
            animator.startAnimation("lightening_attack");
        } else {
            animator.startAnimation("alt-lightening-attack");
        }
    }

    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - timeAtCreation >= lifeSpan) {
            entity.prepareDispose();
        }
    }
}
