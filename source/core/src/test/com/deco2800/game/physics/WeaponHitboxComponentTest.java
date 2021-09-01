package com.deco2800.game.physics;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.components.WeaponHitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class WeaponHitboxComponentTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldBeSensor() {
        Entity entity = new Entity();
        entity.addComponent(new PhysicsComponent());
        WeaponHitboxComponent component = new WeaponHitboxComponent();
        entity.addComponent(component);

        component.setShape(new PolygonShape());
        entity.create();
        assertTrue(component.getFixture().isSensor());
    }

    @Test
    void shouldSetShape() {
        Entity entity = new Entity();
        entity.addComponent(new PhysicsComponent());
        WeaponHitboxComponent component = new WeaponHitboxComponent();
        entity.addComponent(component);

        Shape shape = new CircleShape();
        component.setShape(shape);
        entity.create();
        assertEquals(shape.getType(), component.getFixture().getShape().getType());
    }

    @Test
    void shouldResizeShape() {
        Entity entity = new Entity();
        entity.addComponent(new PhysicsComponent());
        WeaponHitboxComponent component = new WeaponHitboxComponent();
        entity.addComponent(component);

        Shape circle = new CircleShape();
        Shape polygon = new PolygonShape();
        component.setShape(circle);
        component.setShape(polygon);
        entity.create();
        assertEquals(polygon.getType(), component.getFixture().getShape().getType());
    }

    @Test
    void shouldDestroy() {
        Entity entity = new Entity();
        entity.addComponent(new PhysicsComponent());
        WeaponHitboxComponent component = new WeaponHitboxComponent();
        entity.addComponent(component);

        Shape shape = new CircleShape();
        component.setShape(shape);
        component.destroy();
        assertNull(component.getFixture());
    }
}