package com.deco2800.game.components;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class HealthBarComponentTest {
    @Test
    void checkComponentCreate() {

        Entity entity = new Entity();
        entity.addComponent(new CombatStatsComponent(10, 10));
        HealthBarComponent component = mock(HealthBarComponent.class);
        entity.addComponent(component);

        entity.create();

        verify(component).create();
    }

    @Test
    void checkComponentDispose() {

        Entity entity = new Entity();
        entity.addComponent(new CombatStatsComponent(10, 10));
        HealthBarComponent component = mock(HealthBarComponent.class);
        entity.addComponent(component);
        entity.create();
        component.dispose();
        verify(component).dispose();

    }
}
