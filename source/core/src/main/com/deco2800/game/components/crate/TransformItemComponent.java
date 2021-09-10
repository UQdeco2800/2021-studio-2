package com.deco2800.game.components.crate;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchHealComponent;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * allows entities to disable and enable different components depending on what you
 * want to achieve -> this only works when entities are about to die and you want them
 * to transform into a different kind of entity without disposing it.
 * e.g enemies die and drop items
 */
public class TransformItemComponent extends TransformEntityComponent {

    @Override
    public void transform() {
        entity.getComponent(AnimationRenderComponent.class).startAnimation("break");
        disposeOtherComponents();
        entity.getComponent(TouchHealComponent.class).setEnabled(true);
        entity.getComponent(ColliderComponent.class).setSensor(true);

    }
    private void disposeOtherComponents() {
        entity.getComponent(CombatStatsComponent.class).dispose();
    }
}
