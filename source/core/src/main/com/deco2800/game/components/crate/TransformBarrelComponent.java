package com.deco2800.game.components.crate;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchHealComponent;
import com.deco2800.game.physics.components.ColliderComponent;

/**
 * allows entities to disable and enable different components depending on what you
 * want to achieve. This only works when entities are about to die and you want them
 * to transform into a different kind of entity without disposing it.
 * e.g enemies die and drop items
 */
public class TransformBarrelComponent extends TransformEntityComponent {
    @Override
    protected void transform() {
        //break animation into a health potion
        entity.getEvents().trigger("barrelDeath");
        //disable the combatStat so that it doesn't get disposed over there
        configureComponents();
    }

    private void configureComponents() {
        entity.getComponent(CombatStatsComponent.class).setEnabled(false);

        //this component is where player can pick-up item and disposes it
        entity.getComponent(TouchHealComponent.class).setEnabled(true);
        entity.getComponent(ColliderComponent.class).setSensor(true);
    }
}
