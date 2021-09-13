package com.deco2800.game.components;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {

    CombatStatsComponent combat;
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
    void shouldNotHaveEventWithNoCSC() {
        entity.create();
        assertFalse(entity.getEvents().hasEvent("healEntity"), "entity should not have healEntity" +
                " if it doesn't have CombatStatComponents");
    }

    @Test
    void shouldHaveEventListener() {
        entity.addComponent(combat);
        entity.create();
        assertTrue(entity.getEvents().hasEvent("healEntity"),
                "entity should have 'healEntity' event if it has CombatStatsComponents");
        entity.dispose();
    }

    @Test
    void triggerAddHealth() {
        entity.addComponent(combat);
        entity.create();
        assertEquals(100, combat.getHealth());
        combat.setHealth(50);
        assertEquals(50, combat.getHealth());
        entity.getEvents().trigger("healEntity", 100);
        assertEquals(100, combat.getHealth(), " 'healEntity' event should have triggered the " +
                "addHealth() method in CombatStatComponets");
    }

    @Test
    void disposeOnSetHealthNegativeHp() {
        //dispose of the entity when setting hp to a negative number hp
        Entity spy = spy(entity);
        spy.addComponent(combat);
        spy.create();
        combat.setHealth(-99);
        verify(spy).prepareDispose();
    }

    @Test
    void disposeOnSetHealthExactly0Hp() {
        //dispose of the entity when it first reaches exactly 0 hp
        Entity spy = spy(entity);
        spy.addComponent(combat);
        spy.create();
        combat.setHealth(0);
        verify(spy).prepareDispose();
    }

    @Test
    void NotdisposeOnSetHealth0Hp() {
        //since it has a 'transformEntity' event, entity will not dispose in CSC
        CombatStatsComponent combatSpy = spy(combat);
        Entity entitySpy = spy(entity);

        //simulate 'transfromEntity' event
        doReturn(true).when(combatSpy).checkTransformation(anyInt());

        entitySpy.addComponent(combatSpy);
        entitySpy.create();

        //make sure that the entity is left with <= 0 hp
        combatSpy.hit(new CombatStatsComponent(100,99999));

        //make sure that prepareDispose is never called since we have a 'transformEntity' event
        verify(entitySpy, never()).prepareDispose();
    }

    @Test
    void disableComponentHitDoesNothing() {
        entity.addComponent(combat);
        entity.getComponent(CombatStatsComponent.class).setEnabled(false);
        combat.hit(new CombatStatsComponent(100, 10000));
        assertEquals(100, combat.getHealth(), "hit() should do nothing when CombatStatComponents " +
                "is disabled");
    }

    @Test
    void shouldSetGetHealth() {

        assertEquals(100, combat.getHealth());

        combat.setHealth(150);
        assertEquals( 100, combat.getHealth(), "health cannot go over the max Health");

        combat.setHealth(-50);
        assertEquals(0, combat.getHealth(), "health cannot be negative");
    }

    @Test
    void shouldChangeMaxHealth() {
        assertEquals(100, combat.getHealth());
        assertEquals(100, combat.getMaxHealth(), "max Health should be the same as the first time" +
                " player set health");

        combat.setMaxHealth(150);
        assertEquals(100, combat.getHealth(), "current health should not change when changing " +
                "setMaxhHealth()");
        assertEquals(150, combat.getMaxHealth(), "setMaxHealth() should change the max Health of " +
                "the player");
    }

    @Test
    void shouldSetHealthToMax() {
        combat.setMaxHealth(200);
        combat.setHealth(200);
        assertEquals(200, combat.getHealth());
    }


    @Test
    void shouldCheckIsDead() {
        assertFalse(combat.isDead());

        combat.setHealth(0);
        assertTrue(combat.isDead());
    }

    @Test
    void shouldAddHealth() {
        combat.addHealth(-500);
        assertEquals(0, combat.getHealth());

        combat.addHealth(100);
        combat.addHealth(-20);
        assertEquals(80, combat.getHealth());
    }

    @Test
    void shouldSetGetBaseAttack() {
        assertEquals(20, combat.getBaseAttack());

        combat.setBaseAttack(150);
        assertEquals(150, combat.getBaseAttack());

        combat.setBaseAttack(-50);
        assertEquals(150, combat.getBaseAttack());
    }
}
