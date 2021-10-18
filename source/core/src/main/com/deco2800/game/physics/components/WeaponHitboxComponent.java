package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.deco2800.game.components.weapons.MeleeWeapon;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Represents the hit box used by weapons.
 * Main difference from a normal hit box is that it can be 'resized', which
 * is done by destroying and recreating the fixture.
 */
public class WeaponHitboxComponent extends ColliderComponent {

    @Override
    public void create() {
        setSensor(true);
        // Weapon hit boxes aren't set by default.
    }

    /**
     * Set physics as a box with a given size. Box is centered around the entity.
     *
     * @param size      size of the box, relative to the entity's size.
     *                  NOTE: Pass cpy() to avoid bugs.
     * @param direction the direction of the hit box, relative to the entity.
     * @return self
     */


    @SuppressWarnings("UnusedReturnValue")
    public ColliderComponent set(Vector2 size, int direction) {

        Vector2 position = new Vector2(0.5f, 0.5f); // center around entity

        /*
        From the origin, defined at the entity's center (0.5f, 0.5f), we want to change
        the location of the box's center such that it is just outside the entity.
         e.g. RIGHT = [entity][box]. We do this by adding half the entity's size, and
         half the size of the weapon box, both in the direction we want the box to be
         located. We also (sometimes) want to switch the axis of the weapon box, such
          that its dimensions are preserved for UP & DOWN vs LEFT & RIGHT. */
        switch (direction) {
            case MeleeWeapon.UP:
                // add half entity size (1f / 2) + half size of box (size/2)
                position.add(0, (1f + size.y) / 2);
                break;
            case MeleeWeapon.DOWN:
                position.add(0, (1f + size.y) / -2);
                break;
            case MeleeWeapon.RIGHT:
                Vector2Utils.swapAxis(size);
                position.add((1f + size.x) / 2, 0);
                break;
            case MeleeWeapon.LEFT:
                Vector2Utils.swapAxis(size);
                position.add((1f + size.x) / -2, 0);
                break;
            case MeleeWeapon.CENTER:
                // do nothing if its MeleeWeapon.CENTER (already centered at entity)
                break;
            default:
                return null; // all other directions are invalid.
        }
        return setAsBox(size, position);
    }

    /**
     * Sets the hit box shape, but also allows for resizing. This is called by
     * all setBox methods (defined in ColliderComponent)
     *
     * @param shape shape, default = bounding box the same size as the entity
     * @return self
     * @see ColliderComponent
     */
    @Override
    public ColliderComponent setShape(Shape shape) {
        fixtureDef.shape = shape;
        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        if (fixture != null) { // destroy fixture if one exists
            logger.debug("{} Added weapon hit box without destroying it first.", this);
            physBody.destroyFixture(fixture);
        }
        this.fixture = physBody.createFixture(fixtureDef);
        return this;
    }

    /**
     * Destroys the hit box connected to the entities body, if one exists.
     */
    public void destroy() {
        if (fixture == null) {
            logger.debug("{} Tried to destroy an already unset weapon hit box", this);
            return;
        }
        Body physBody = entity.getComponent(PhysicsComponent.class).getBody();
        physBody.destroyFixture(fixture);
        fixture = null;
    }
} 
