package com.deco2800.game.components.tasks;

import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;

/**
 * An 'AI Task' that sets an entity to destroy itself after a set duration.
 */
public class LifespanComponent extends Component {
    private final long lifeSpan;
    private long timeAtCreation;

    public LifespanComponent(long lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    @Override
    public void create() {
        this.timeAtCreation = ServiceLocator.getTimeSource().getTime();
    }

    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - timeAtCreation >= lifeSpan) {
            entity.prepareDispose();
        }
    }
}
