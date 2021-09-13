package com.deco2800.game.components.crate;

import com.deco2800.game.components.CombatStatsComponent;
import org.junit.jupiter.api.Test;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class TransformBarrelComponentTest {
    @Spy
    TransformBarrelComponent barrel;

    CombatStatsComponent combat;

    @Spy
    Entity entity;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerEntityService(new EntityService());
        combat = new CombatStatsComponent(100, 20);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void afterEach() {
        combat.dispose();
        ServiceLocator.clear();
    }

    @Test
    void configureComponentsWorks() {
        //add event listeners
        doCallRealMethod().when(barrel).create();
        entity.addComponent(combat);
        entity.addComponent(barrel);
        entity.create();

        //events added successfully
        assertTrue(entity.getEvents().hasEvent("transformEntity"));
        doNothing().when(barrel).configureComponents();
        doNothing().when(barrel).transform();

        //trigger barrel transformation
        combat.hit(new CombatStatsComponent(10, 100000));

        //confirm event trigger works
        verify(barrel).transform();
        verify(entity, never()).prepareDispose();
    }

}
