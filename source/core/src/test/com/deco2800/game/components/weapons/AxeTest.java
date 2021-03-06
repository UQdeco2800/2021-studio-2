package com.deco2800.game.components.weapons;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.WeaponHitboxComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("StatementWithEmptyBody")
@ExtendWith(GameExtension.class)
class AxeTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerResourceService(new ResourceService());
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadSounds(new String[]{"sounds/impact.ogg", "sounds/swish.ogg"});
        while (resourceService.loadForMillis(10)) {
            // wait for assets to load
        }
    }

    @Test
    void shouldAttack() {
        short targetLayer = (1 << 3);
        Entity entity = createAttacker(targetLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = entity.getComponent(WeaponHitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(0, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldCreateWeaponHitbox() {
        short targetLayer = (1 << 3);
        Entity entity = createAttacker(targetLayer);

        Axe weapon = entity.getComponent(Axe.class); // arbitrary
        weapon.setAttackFrameDuration(100L);
        weapon.attack(MeleeWeapon.UP);
        weapon.triggerAttackStage(150L); // attack frame

        assertNotEquals(null,
                entity.getComponent(WeaponHitboxComponent.class).getFixture());
    }

    @Test
    void shouldDestroyWeaponHitbox() {
        short targetLayer = (1 << 3);
        Entity entity = createAttacker(targetLayer);

        Axe weapon = entity.getComponent(Axe.class); // arbitrary
        weapon.attack(MeleeWeapon.UP);
        weapon.triggerAttackStage(weapon.getTotalAttackTime());

        assertNull(entity.getComponent(WeaponHitboxComponent.class).getFixture());
    }

    @Test
    void shouldGetTotalAttackTime() {
        short targetLayer = (1 << 3);
        Axe weapon = (new Axe(targetLayer,
                0, 0, new Vector2(0f, 0f)));
        weapon.setAttackFrames(100L, 3, 1);
        // assumes 3 frames in attack
        assertEquals(100L * 3, weapon.getTotalAttackTime());
    }

    @Test
    void shouldNotAttackOtherLayer() {
        short targetLayer = (1 << 3);
        short attackLayer = (1 << 4);
        Entity entity = createAttacker(attackLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = entity.getComponent(WeaponHitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldNotAttackWithoutCombatComponent() {
        short targetLayer = (1 << 3);
        Entity entity = createAttacker(targetLayer);
        // Target does not have a combat component
        Entity target =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(targetLayer));
        target.create();

        Fixture entityFixture = entity.getComponent(WeaponHitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

        // This should not cause an exception, but the attack should be ignored
        try {
            entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    Entity createAttacker(short targetLayer) {

        Entity entity =
                new Entity()
                        .addComponent(new WeaponHitboxComponent().setLayer(targetLayer))
                        .addComponent(new Axe(targetLayer, 0, 0,
                                new Vector2(0f, 0f)))
                        .addComponent(new CombatStatsComponent(0, 10))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent());
        entity.getComponent(Axe.class).setAttackFrames(100L, 3, 1);
        entity.create();
        return entity;
    }

    Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer));
        target.create();
        return target;
    }
}
