package com.deco2800.game.entities;

import com.badlogic.gdx.utils.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 * <p>
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
@SuppressWarnings("GDXJavaUnsafeIterator")
public class EntityService {
    private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
    private static final int INITIAL_CAPACITY = 16;
    private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);
    /**
     * Stores a UI entity so the UI can be updated from other locations.
     */
    private Entity ui;

    /**
     * Register a new entity with the entity service. The entity will be created and start updating.
     *
     * @param entity new entity.
     */
    public void register(Entity entity) {
        logger.debug("Registering {} in entity service", entity);
        entities.add(entity);
        entity.create();
    }

    /**
     * Register a UI entity with the entity service. The entity will be created and start updating.
     *
     * @param entity new entity.
     */
    public void registerUI(Entity entity) {
        register(entity);
        ui = entity;
    }

    /**
     * Returns the UI entity of the game so the UI can be changed from any location.
     *
     * @return UI entity that displays items on the screen
     */
    public Entity getUIEntity() {
        return this.ui;
    }

    /**
     * Unregister an entity with the entity service. The entity will be removed and stop updating.
     *
     * @param entity entity to be removed.
     */
    public void unregister(Entity entity) {
        logger.debug("Unregistering {} in entity service", entity);
        entities.removeValue(entity, true);
    }

    /**
     * Update all registered entities. Should only be called from the main game loop.
     */
    public void update() {
        for (Entity entity : entities) {
            entity.earlyUpdate();
            entity.update();
        }
    }

    /**
     * Dispose all entities.
     */
    public void dispose() {
        for (Entity entity : entities) {
            entity.dispose();
        }
    }

    public Array<Entity> getEntities() {
        return new Array<>(entities);
    }
}
