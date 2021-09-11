package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
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
import com.deco2800.game.areas.terrain.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class TestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(TestGameArea.class);
  private static final int NUM_GHOSTS = 1;
  private static final int NUM_ANCHORED_GHOSTS = 1;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(20, 370);
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
  private static String[] tileTextures = null;
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

  private final TerrainFactory terrainFactory;
  private final GdxGame game;
  private static Map map;

  public TestGameArea(TerrainFactory terrainFactory, GdxGame game) {
    super();
    this.game = game;
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    map = FileLoader.readClass(Map.class, "maps/MapObjects.json");
    tileTextures = map.TileRefsArray();

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
    spawnObstacles();

    spawnSpikeTraps();
    spawnLavaTraps();

    spawnTraps();
    spawnPTraps();

    playMusic();
    spawnTeleport();
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
      GridPoint2 ghostPos = RandomUtils.random(basePos.cpy().sub(3,3), basePos.cpy().add(3,3));
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

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.TEST, map);
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

    //Walls imported from JSON
    HashMap<String, Float>[] walls = map.getWallObjects();
    for (HashMap<String, Float> wall : walls) {
      int x = wall.get("x").intValue();
      int y = wall.get("y").intValue();
      float width = wall.get("width");
      float height = wall.get("height");

      int unitHeight = (int) ((height/32f));
      spawnEntityAt(
              ObstacleFactory.createWall((width/32f)*0.5f, (height/32f)*0.5f),
              new GridPoint2(x, map.getDimensions().get("n_tiles_height")- (y + unitHeight)),
              false,
              false);
    }
  }

  private void spawnPTraps() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    GridPoint2 fixedPos = new GridPoint2(15,15);
    Entity trap = ObstacleFactory.createPhysicalTrap();
    spawnEntityAt(trap, fixedPos, true, true);
  }

  private void spawnTraps() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    GridPoint2 fixedPos = new GridPoint2(8,8);
    Entity trap = ObstacleFactory.createNonePhysicalTrap();
    spawnEntityAt(trap, fixedPos, true, true);
  }

  private void spawnTeleport() {
    Entity teleport = ObstacleFactory.creatTeleport(player);
    GridPoint2 fixedPos = new GridPoint2(15,10);
    spawnEntityAt(teleport, fixedPos, true,true);
  }

  private void spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer(game);
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    player = newPlayer;
    //player.setPosition(new Vector2(15, 8)); TESTING FOR TELEPORT
  }

  private void spawnObstacles(){
    int[][] obstacles = map.getTransObstacles();
    HashMap<String, String> tileRefs = map.getTileRefs();
    if (obstacles != null) {
      GridPoint2 min = new GridPoint2(0, 0);
      GridPoint2 max = new GridPoint2(map.getDimensions().get("n_tiles_width") - 1,
              map.getDimensions().get("n_tiles_height") - 1);

      for (int y = min.y; y <= max.y; y++) {
        for (int x = min.y; x <= max.x; x++){
          if (obstacles[y][x] != 0){

            Entity obstacle = ObstacleFactory.createObstacle(tileRefs.get(String.valueOf(obstacles[y][x])));
            GridPoint2 pos = new GridPoint2(x, max.y - y);

            spawnEntityAt(obstacle, pos, true,false);
          }
        }
      }
    }
  }

  private void spawnSpikeTraps() {
    HashMap<String, Float>[] spikeTraps = map.getSpikeObjects();
    for (HashMap<String, Float> spikeTrap : spikeTraps) {
      int x = spikeTrap.get("x").intValue();
      int y = spikeTrap.get("y").intValue();
      float width = spikeTrap.get("width");
      float height = spikeTrap.get("height");

      int unitHeight = (int) ((height/32f));
      spawnEntityAt(
              ObstacleFactory.createRSPhysicalTrap((width/32f)*0.5f, (height/32f)*0.5f),
              new GridPoint2(x, map.getDimensions().get("n_tiles_height")- (y + unitHeight)),
              false,
              false);
    }
  }

  private void spawnLavaTraps() {
    HashMap<String, Float>[] lavaTraps = map.getLavaObjects();
    for (HashMap<String, Float> lavaTrap : lavaTraps) {
      int x = lavaTrap.get("x").intValue();
      int y = lavaTrap.get("y").intValue();
      float width = lavaTrap.get("width");
      float height = lavaTrap.get("height");

      int unitHeight = (int) ((height/32f));
      spawnEntityAt(
              ObstacleFactory.createRSNonePhysicalTrap((width/32f)*0.5f, (height/32f)*0.5f),
              new GridPoint2(x, map.getDimensions().get("n_tiles_height")- (y + unitHeight)),
              false,
              false);
    }
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

    while (!resourceService.loadForMillis(10)) {
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
