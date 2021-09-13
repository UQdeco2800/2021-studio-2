package com.deco2800.game.components.crate;

import com.deco2800.game.components.Component;

/**
 * abstract class to allow entities to transform into a different entity by
 * disabling/disposing unwanted components and enabling/configure existing components
 * that were added when the entity was created.
 * Ideally this class is used if entities drop items and you don't want to create a
 * separate entity for that.
 * disposal will occur in a touchComponent, so when the player walks up to pick-up the item
 */
public abstract class TransformEntityComponent extends Component {
    //implement a timer for transformation animation and then player can interact with the entity

    public void create() {
        super.create();
        entity.getEvents().addListener("transformEntity", this::transform);
    }

    /**
     * enabling and disabling of components occur here
     */
    public abstract void transform();

    //add other methods in the subclasses if you need further functionality
}
