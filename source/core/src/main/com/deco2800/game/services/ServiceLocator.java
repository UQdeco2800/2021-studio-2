package com.deco2800.game.services;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.screens.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services. Warning: global access is a trap and should
 * be used extremely sparingly. Read the README for details.
 */
@SuppressWarnings({"GDXJavaStaticResource"})
public class ServiceLocator {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
    private static EntityService entityService;
    private static RenderService renderService;
    private static PhysicsService physicsService;
    private static GameTime timeSource;
    private static InputService inputService;
    private static ResourceService resourceService;
    private static GameArea gameAreaService;
    private static MainGameScreen gameScreen;


    private ServiceLocator() {
        throw new IllegalStateException("Instantiating static util class");
    }

    public static EntityService getEntityService() {
        return entityService;
    }

    public static RenderService getRenderService() {
        return renderService;
    }

    public static PhysicsService getPhysicsService() {
        return physicsService;
    }

    public static GameTime getTimeSource() {
        return timeSource;
    }

    public static InputService getInputService() {
        return inputService;
    }

    /**
     * Allows entities to access gameArea and spawn entities.
     *
     * @return current game area
     */
    public static ResourceService getResourceService() {
        return resourceService;
    }

    public static GameArea getGameAreaService() {
        return gameAreaService;
    }

    public static MainGameScreen getGameScreen() {
        return gameScreen;
    }

    public static void registerEntityService(EntityService service) {
        logger.debug("Registering entity service {}", service);
        entityService = service;
    }

    public static void registerRenderService(RenderService service) {
        logger.debug("Registering render service {}", service);
        renderService = service;
    }

    public static void registerPhysicsService(PhysicsService service) {
        logger.debug("Registering physics service {}", service);
        physicsService = service;
    }

    public static void registerTimeSource(GameTime source) {
        logger.debug("Registering time source {}", source);
        timeSource = source;
    }

    public static void registerInputService(InputService source) {
        logger.debug("Registering input service {}", source);
        inputService = source;
    }

    public static void registerResourceService(ResourceService source) {
        logger.debug("Registering resource service {}", source);
        resourceService = source;
    }

    public static void registerGameScreen(MainGameScreen gameScreen) {
        ServiceLocator.gameScreen = gameScreen;
    }

    /**
     * Allows entities to access gameArea and spawn entities.
     *
     * @param source current game area
     */
    public static void registerGameArea(GameArea source) {
        logger.debug("Registering GameArea service {}", source);
        gameAreaService = source;
    }

    public static void clear() {
        entityService = null;
        renderService = null;
        physicsService = null;
        timeSource = null;
        inputService = null;
        resourceService = null;
    }

}
