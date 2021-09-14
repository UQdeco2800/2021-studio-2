package com.deco2800.game.components.crate;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class CrateAnimationControllerTest {
    CombatStatsComponent combat;
    @Mock
    CrateAnimationController animate;
    Entity entity;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerEntityService(new EntityService());
        entity = new Entity();
        combat = new CombatStatsComponent(100, 20);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void afterEach() {
        combat.dispose();
        ServiceLocator.clear();
    }

    @Test
    void hitCallsFromCSC() {
        //hit animation occurs when hit() is called from CombatStatComponent if the entity has a
        // 'hit' event
        entity.getEvents().addListener("hit", animate::hitBarrel);
        entity.getEvents().addListener("barrelDeath", animate::breakBarrel);
        entity.addComponent(combat);

        combat.hit(new CombatStatsComponent(100, 100));
        verify(animate).hitBarrel();
        verify(animate, never()).breakBarrel();
    }

    @Test
    void breakBarrelCalledFromTransformBarrelComponent() {
        entity.getEvents().addListener("hit", animate::hitBarrel);
        entity.getEvents().addListener("barrelDeath", animate::breakBarrel);
        TransformBarrelComponent barrel = spy(TransformBarrelComponent.class);

        entity.addComponent(barrel);
        entity.create();
        doNothing().when(barrel).configureComponents();
        barrel.transform();
        verify(animate).breakBarrel();
    }
}