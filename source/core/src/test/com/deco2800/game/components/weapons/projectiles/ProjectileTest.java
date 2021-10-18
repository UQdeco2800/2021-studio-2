package com.deco2800.game.components.weapons.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("StatementWithEmptyBody")
class ProjectileTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerResourceService(new ResourceService());
        ResourceService resourceService = ServiceLocator.getResourceService();
        // wait for assets to load
        while (resourceService.loadForMillis(10)) {
            // wait for assets to load
        }
    }

    @Test
    void shouldTerminate() {
        Vector2 target = new Vector2(1f, 1f);
        Entity entity = createBlast(target);
        entity.getComponent(BlastController.class).hit = true;
        boolean deleted = false;
        try {
            entity.getComponent(BlastController.class).update();
            entity.getComponent(BlastController.class);
        } catch (Exception e) {
            deleted = true;
        }
        if (!deleted) {
            assertTrue(false);
        }
    }

    Entity createBlast(Vector2 target) {
        float speed = 8f;
        PhysicsMovementComponent movingComponent = new PhysicsMovementComponent();
        movingComponent.setMoving(true);
        movingComponent.setTarget(target);
        movingComponent.setMaxSpeed(new Vector2(speed, speed));
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(movingComponent)
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.MELEEWEAPON))
                .addComponent(new BlastController());
    }
}
