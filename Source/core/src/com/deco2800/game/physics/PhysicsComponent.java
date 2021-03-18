package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.deco2800.game.ecs.Component;
import com.deco2800.game.services.ServiceLocator;

/**
 * Lets an entity be controlled by physics.
 * Do not directly modify the position of a physics-enabled entity. Instead,
 * use forces to move it.
 */
public class PhysicsComponent extends Component {
    private final PhysicsEngine physics;
    private final BodyDef.BodyType bodyType;
    private final FixtureDef fixtureDef;
    private Body body;

    public PhysicsComponent(
        BodyDef.BodyType bodyType,
        Vector2 size
    ) {
        this.physics = ServiceLocator.getPhysicsService().getPhysics();
        this.bodyType = bodyType;
        fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x, size.y);
        fixtureDef.shape = shape;
        shape.dispose();
    }

    public PhysicsComponent(
        BodyDef.BodyType bodyType,
        Shape shape
    ) {
        this.physics = ServiceLocator.getPhysicsService().getPhysics();
        this.bodyType = bodyType;
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
    }

    public Body getBody() {
        return body;
    }

    public void setFriction(float friction) {
        fixtureDef.friction = friction;
    }

    public void setSensor(boolean isSensor) {
        fixtureDef.isSensor = isSensor;
    }

    public void setDensity(float density) {
        fixtureDef.density = density;
    }

    public void setRestitution(float restitution) {
        fixtureDef.restitution = restitution;
    }

    public void setShape(Shape shape) {
        fixtureDef.shape = shape;
    }

    @Override
    public void create() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(entity.getPosition());
        body = physics.createBody(bodyDef);
        body.createFixture(fixtureDef);
    }

    /**
     * Entity position needs to be updated to match the new physics position.
     * This should happen before other updates, which may use the new position.
     */
    @Override
    public void earlyUpdate() {
        Vector2 bodyPos = body.getPosition();
        entity.setPosition(bodyPos);
    }

    @Override
    public void dispose() {
        physics.destroyBody(body);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        body.setActive(enabled);
    }
}
