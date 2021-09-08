package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class TestGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(TestGameArea.class);
    private static final int NUM_GHOSTS = 1;
    private static final int NUM_ANCHORED_GHOSTS = 1;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
    private static final GridPoint2 TEST_TRIGGER = new GridPoint2(20, 21);
    private static final float WALL_WIDTH = 0.1f;
    private static final String[] forestTextures = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/trap.png",
            "images/test.png",
            "images/arrow_normal.png",
            "images/ghost_king.png",
            "images/ghost_crown.png",
            "images/ghost_1.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/mud.png",
            "images/player.png",
            "images/health_left.png",
            "images/health_middle.png",
            "images/health_right.png",
            "images/health_frame_left.png",
            "images/health_frame_middle.png",
            "images/health_frame_right.png",
            "images/hp_icon.png",
            "images/dash_icon.png",
            "images/rock.png"
    };
    private static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/player.atlas"
    };
    private static final String[] forestSounds = {
            "sounds/Impact4.ogg", "sounds/impact.ogg", "sounds/swish.ogg"
    };
    private static final String[] arrowSounds = {
            "sounds/arrow_disappear.mp3",
            "sounds/arrow_shoot.mp3"
    };
    private static final String backgroundMusic = "sounds/RAGNAROK_MAIN_SONG_76bpm.mp3";
    private static final String[] forestMusic = {backgroundMusic};
    private static String[] tileTextures = null;
    private final TerrainFactory terrainFactory;

    public TestGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        Map m = FileLoader.readClass(Map.class, "maps/test_map.json");
        tileTextures = m.TileRefsArray();

        super.create();
        loadAssets();
        displayUI();

        spawnTerrain();
        spawnPlayer();
        spawnGhosts();
        spawnCutsceneTrigger();
        spawnGhostKing();
        spawnRangedGhosts();
        spawnGhostKing(); //use this later to make evil assassins with different sprites
        spawnAnchoredGhosts();
        spawnTraps();

        playMusic();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Map Test"));
        spawnEntity(ui);
    }

    /**
     * Spawn anchored ghost, ghost only move at the certain anchored
     */
    private void spawnAnchoredGhosts() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_ANCHORED_GHOSTS; i++) {
            GridPoint2 basePos = RandomUtils.random(minPos, maxPos);
            GridPoint2 ghostPos = RandomUtils.random(basePos.cpy().sub(3, 3), basePos.cpy().add(3, 3));
            Entity anchor = ObstacleFactory.createAnchor();
            Entity AnchoredGhost = NPCFactory.createAnchoredGhost(player, anchor, 3f);
            spawnEntityAt(anchor, basePos, true, true);
            spawnEntityAt(AnchoredGhost, ghostPos, true, true);
        }
    }

    /**
     * Spawn range ghost on terrain, range ghost can shoot target
     */
    private void spawnRangedGhosts() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_GHOSTS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity ghost = NPCFactory.createRangedGhost(player);
            spawnEntityAt(ghost, randomPos, true, true);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.TEST);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);

        //Walls imported from JSON (Not working as intended, leave for sprint 2)
    /*Map m = FileLoader.readClass(Map.class, "maps/test_map.json");
    HashMap<String, Integer>[] walls = m.getWallObjects();
    int X = 0;
    int Y = 2;
    int WIDTH = 1;
    int HEIGHT = 3;

    for (HashMap<String, Integer> wall : walls) {
      String wallString = wall.values().toString();
      String wallNoBracket = wallString.substring(1, wallString.length() - 1);
      String[] wallValues = wallNoBracket.split(", ");

      float xFloat = Float.parseFloat(wallValues[X]);
      int x = (int) xFloat;

      float yFloat = Float.parseFloat(wallValues[Y]);
      int y = (int) yFloat;

      float width = Float.parseFloat(wallValues[WIDTH]);

      float height = Float.parseFloat(wallValues[HEIGHT]);

      int unitHeight = (int) ((height/32f));
      spawnEntityAt(
              ObstacleFactory.createWall((width/32f)*0.5f, (height/32f)*0.5f),
              new GridPoint2(x, 25 - (y + unitHeight)),
              false,
              false);
    }
     */

        //Manually placed walls, will be deleted in next sprint
        //Left Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 6f),
                new GridPoint2(3, 7),
                false,
                false);

        //Bottom-Left Wall
        spawnEntityAt(ObstacleFactory.createWall(5.5f, 0.5f),
                new GridPoint2(3, 6),
                false,
                false);

        //Bottom-Right Wall
        spawnEntityAt(ObstacleFactory.createWall(3.5f, 0.5f),
                new GridPoint2(14, 10),
                false,
                false);

        //Right Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 4.5f),
                new GridPoint2(21, 10),
                false,
                false);

        //Top-Right Wall
        spawnEntityAt(ObstacleFactory.createWall(3.5f, 0.5f),
                new GridPoint2(14, 19),
                false,
                false);

        //Top-Left Wall
        spawnEntityAt(ObstacleFactory.createWall(5.5f, 0.5f),
                new GridPoint2(3, 16),
                false,
                false);

        //Middle-Top Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 3f),
                new GridPoint2(13, 16),
                false,
                false);

        //Middle-Bottom Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 2f),
                new GridPoint2(14, 7),
                false,
                false);
    }

    private void spawnTraps() {
        GridPoint2 fixedPos = new GridPoint2(15, 15);
        Entity trap = ObstacleFactory.createPhysicalTrap();
        spawnEntityAt(trap, fixedPos, true, true);
    }

    private void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        player = newPlayer;
    }

    private void spawnGhosts() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_GHOSTS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity ghost = NPCFactory.createGhost(player);
            spawnEntityAt(ghost, randomPos, true, true);
        }
    }

    private void spawnGhostKing() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        Entity ghostKing = NPCFactory.createGhostKing(player);
        spawnEntityAt(ghostKing, randomPos, true, true);
    }

    private void spawnCutsceneTrigger() {
        Entity trigger = CutsceneTriggerFactory.createTrigger();
        spawnEntityAt(trigger, TEST_TRIGGER, true, true);
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(tileTextures);
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(forestSounds);
        resourceService.loadMusic(forestMusic);
        resourceService.loadSounds(arrowSounds);

        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(forestTextures);
        resourceService.unloadAssets(tileTextures);
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(forestSounds);
        resourceService.unloadAssets(forestMusic);
        resourceService.unloadAssets(arrowSounds);
    }


    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}
