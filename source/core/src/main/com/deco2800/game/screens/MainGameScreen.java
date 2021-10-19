package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.*;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.components.pause.PauseInputComponent;
import com.deco2800.game.components.pause.PauseMenuActions;
import com.deco2800.game.components.pause.PauseMenuDisplay;
import com.deco2800.game.components.player.PlayerWin;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.CutsceneScreen;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import com.deco2800.game.ui.textbox.TextBox;
import com.deco2800.game.ui.textbox.TextBoxDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
    private static final String[] mainGameTextures = {
            "images/heart.png",
            "lowHealthImages/BloodScreenDarkRepositioned.png",
            "images/textBoxDisplay/default_text_box.png",
            "images/textBoxDisplay/main_character_image.png",
            "images/textBoxDisplay/prisoner_image.png",
            "images/textBoxDisplay/black_bars.png",
            "images/textBoxDisplay/prison_text_box.png",
            "images/textBoxDisplay/loki_image.png",
            "images/textBoxDisplay/loki_text_box.png",
            "images/textBoxDisplay/enemy_default_text_box.png",
            "images/textBoxDisplay/elf_image.png",
            "images/textBoxDisplay/odin_image.png",
            "images/textBoxDisplay/thor_image.png",
            "images/textBoxDisplay/outdoor_text_box.png",

    };
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
    private static final String[] playerLowHealthSounds = {"sounds/heartBeat_placeholder.mp3"};
    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private GameArea gameArea;
    private boolean gameChange = false;
    private final TerrainFactory terrainFactory;

    public MainGameScreen(GdxGame game) {
        this.game = game;


        logger.debug("Initialising main game screen services");
        ServiceLocator.registerTimeSource(new GameTime());

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        physicsEngine = physicsService.getPhysics();

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        terrainFactory = new TerrainFactory(renderer.getCamera());
        loadAssets();
        createUI();
    }

    public MainGameScreen(GdxGame game, String world) {
        this(game);
        logger.debug("Initialising main game screen entities");

        switch (world) {
            case "game0":
                this.gameArea = new GameArea0(terrainFactory).create();
                break;
            case "game1":
                this.gameArea = new GameArea1(terrainFactory).create();
                break;
            case "game2":
                this.gameArea = new GameArea2(terrainFactory).create();
                break;
            case "game3":
                this.gameArea = new GameArea3(terrainFactory).create();
                break;
            case "game4":
                this.gameArea = new GameArea4(terrainFactory).create();
                break;
            case "game5":
                this.gameArea = new GameArea5(terrainFactory).create();
                break;
            default:
                break;
        }
        renderer.getCamera().setPlayer(this.gameArea.getPlayer());
    }

    /**
     * Use for teleport, track the current player health
     */
    public MainGameScreen(GdxGame game, String world, int currentHealth) {
        this(game);
        logger.debug("Initialising main game screen entities");

        switch (world) {
            case "game0":
                this.gameArea = new GameArea0(terrainFactory, currentHealth).create();
                break;
            case "game1":
                this.gameArea = new GameArea1(terrainFactory, currentHealth).create();
                break;
            case "game2":
                this.gameArea = new GameArea2(terrainFactory, currentHealth).create();
                break;
            case "game3":
                this.gameArea = new GameArea3(terrainFactory, currentHealth).create();
                break;
            case "game4":
                this.gameArea = new GameArea4(terrainFactory, currentHealth).create();
                break;
            default:
                this.gameArea = new TutorialGameArea(terrainFactory, currentHealth).create();
                break;
        }
        renderer.getCamera().setPlayer(this.gameArea.getPlayer());
    }


    /**
     * Runs when the player dies, causes the camera to zoom in.
     */
    private void isPlayerDead() {
        if (this.gameArea.getPlayer() != null
                && Boolean.TRUE.equals(this.gameArea.getPlayer().getComponent(CombatStatsComponent.class).isDead())) {
            zoomCamera();
        }
    }

    /**
     * Zooms the camera slightly, this will be called by renderer if the player is dead.
     */
    private void zoomCamera() {
        if (((OrthographicCamera) renderer.getCamera().getCamera()).zoom > 0.4) {
            ((OrthographicCamera) renderer.getCamera().getCamera()).zoom -= 0.008;
        } else if (Boolean.TRUE.equals(this.gameArea.getPlayer().getComponent(CombatStatsComponent.class).isDead())) {
            game.setScreen(GdxGame.ScreenType.DEATHSCREEN);
        } else if (this.gameArea.getPlayer().getComponent(PlayerWin.class).getHasWin()) {
            game.setScreen(GdxGame.ScreenType.END_SCREEN);
        }
    }

    /**
     * Use for teleport, get the leve change to ture
     */
    public void levelChange() {
        gameChange = true;
    }

    /**
     * render the map or new map if teleport trigger
     * get the player health form the previous map and keep it to next map
     * change the gameChange tate back to false;
     */
    @Override
    public void render(float delta) {
        if (gameChange) {
            if (gameArea.getLevel() == 9) {
                int currentHealth = gameArea.getPlayer().getComponent(CombatStatsComponent.class).getHealth();
                game.setScreen(GdxGame.ScreenType.GAMEAREA0, currentHealth);
                gameChange = false;
            } else if (gameArea.getLevel() == 0) {
                int currentHealth = gameArea.getPlayer().getComponent(CombatStatsComponent.class).getHealth();
                game.setScreen(GdxGame.ScreenType.GAMEAREA1, currentHealth);
                gameChange = false;
            } else if (gameArea.getLevel() == 1) {
                int currentHealth = gameArea.getPlayer().getComponent(CombatStatsComponent.class).getHealth();
                game.setScreen(GdxGame.ScreenType.GAMEAREA2, currentHealth);
                gameChange = false;
            } else if (gameArea.getLevel() == 2) {
                int currentHealth = gameArea.getPlayer().getComponent(CombatStatsComponent.class).getHealth();
                game.setScreen(GdxGame.ScreenType.GAMEAREA3, currentHealth);
                gameChange = false;
            } else if (gameArea.getLevel() == 3) {
                int currentHealth = gameArea.getPlayer().getComponent(CombatStatsComponent.class).getHealth();
                game.setScreen(GdxGame.ScreenType.GAMEAREA4, currentHealth);
                gameChange = false;
            } else if (gameArea.getLevel() == 4) {
                int currentHealth = gameArea.getPlayer().getComponent(CombatStatsComponent.class).getHealth();
                game.setScreen(GdxGame.ScreenType.GAMEAREA5, currentHealth);
                gameChange = false;
            }
        } else {
            physicsEngine.update();
            ServiceLocator.getEntityService().update();
            renderer.render();
            isPlayerDead();
            playerWin();
        }
    }

    /**
     * check the player win state and give the END_SCREEN if player win
     */
    private void playerWin() {
        if (this.gameArea.getPlayer().getComponent(PlayerWin.class).getHasWin()) {
            zoomCamera();
        }
    }

    /**
     * resize the object
     * @param width new width
     * @param height new hegiht
     */
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    /**
     * pause the game
     */
    @Override
    public void pause() {
        logger.info("Game paused");
    }

    /**
     * resume the game
     */
    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    /**
     * dispose the game screen that no longer need it
     */
    @Override
    public void dispose() {
        logger.debug("Disposing main game screen");

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    /**
     * load the assets the current game screen need it
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mainGameTextures);
        resourceService.loadSounds(playerLowHealthSounds);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * unload the assets the current game screen need it
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mainGameTextures);
        resourceService.unloadAssets(playerLowHealthSounds);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();
        InputComponent textBoxInput = ServiceLocator.getInputService().getInputFactory().createForTextBox();

        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new TextBox())
                .addComponent(textBoxInput)
                .addComponent(new TextBoxDisplay())
                .addComponent(new CutsceneScreen())
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                //.addComponent(new MainGameExitDisplay())
                .addComponent(new PauseMenuActions(game))
                .addComponent(new PauseMenuDisplay())
                .addComponent(new PauseInputComponent())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());

        ServiceLocator.getEntityService().registerUI(ui);
    }
}
