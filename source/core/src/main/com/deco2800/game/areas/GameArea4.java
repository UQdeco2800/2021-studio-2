package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Smaller room based level
 */
public class GameArea4 extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(GameArea4.class);
    private static final int NUM_MELEE_ELF = 2;
    private static final int NUM_ANCHORED_ELF = 1;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(60, 70);
    private static final GridPoint2 TEST_TRIGGER = new GridPoint2(20, 21);
    private static final float WALL_WIDTH = 0.1f;
    private static final String[] forestTextures = {
            "images/tree.png",
            "images/trap.png",
            "images/test.png",
            "images/arrow_normal.png",
            "images/crown.png",
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
            "images/player_axe.png",
            "images/player_hammer.png",
            "images/player_scepter.png",
            "images/blast.png",
            "images/health_left.png",
            "images/health_middle.png",
            "images/health_right.png",
            "images/health_frame_left.png",
            "images/health_frame_middle.png",
            "images/health_frame_right.png",
            "images/hp_icon.png",
            "images/dash_icon.png",
            "images/prisoner.png",
            "images/rock.png",
            "images/enemy_health_bar.png",
            "images/enemy_health_border.png",
            "images/enemy_health_bar_decrease.png",
            "images/vortex.png",
            "images/aiming_line.png",
            "images/bossAttack.png",
            "images/meleeElf.png",
            "images/guardElf.png",
            "images/rangedElf.png",
            "images/fireball/fireballAinmation.png",
            "player_scepter.png",
            "player_hammer.png",
            "images/boss_health_middle.png",
            "images/boss_health_left.png",
            "images/boss_health_right.png",
            "images/hammer_projectile.png",
            "images/meleeFinal.png"
    };
    private static String[] tileTextures = null;
    public static final String[] healthRegenTextures = {
            "healthRegen/healthPotion_placeholder.png",
            "crate/crateHitBreak.png"
    };
    private static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "crate/crateHitBreak.atlas", "images/elf.atlas",
            "images/player.atlas", "images/bossAttack.atlas", "images/meleeElf.atlas",
            "images/guardElf.atlas", "images/rangedElf.atlas", "images/fireball/fireballAinmation.atlas",
            "images/player_scepter.atlas", "images/player_hammer.atlas", "images/hammer_projectile.atlas",
            "images/meleeFinal.atlas"
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
    private int playerHealth = 300;

    public GameArea4(TerrainFactory terrainFactory, GdxGame game) {
        super();
        this.game = game;
        this.terrainFactory = terrainFactory;
    }

    public GameArea4(TerrainFactory terrainFactory, GdxGame game, int currentHealth) {
        super();
        this.game = game;
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        map = FileLoader.readClass(Map.class, "maps/lvl_0.json");
        tileTextures = map.TileRefsArray();

        super.create();
        loadAssets();
        displayUI();

        spawnTerrain();
        spawnPlayer();

        spawnObstacles();
        spawnLights();

        spawnSpikeTraps();
        spawnLavaTraps();
        spawnTutorialObstacles();
        spawnDialogueCutscenes();
        setInitialDialogue();
        decBossNum();
        spawnTeleport();
        spawnEnemy();

        playMusic();
        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Level 5"));
        spawnEntity(ui);
    }

    private void spawnDialogueCutscenes() {
        Entity trigger = CutsceneTriggerFactory.createPrisonerCutscene(RandomDialogueSet.TUTORIAL,
                DialogueSet.ORDERED, 3);
        spawnEntityAt(trigger, new Vector2(25f, 40f), true, true);

        Entity trigger2 = CutsceneTriggerFactory.createPrisonerCutscene(RandomDialogueSet.TUTORIAL,
                DialogueSet.ORDERED, 1);
        spawnEntityAt(trigger2, new Vector2(34.5f, 40f), true, true);

        Entity trigger3 = CutsceneTriggerFactory.createPrisonerCutscene(RandomDialogueSet.TUTORIAL,
                DialogueSet.ORDERED, 2);
        spawnEntityAt(trigger3, new Vector2(43.2f, 40f), true, true);

        Entity trigger4 = CutsceneTriggerFactory.createPrisonerCutscene(RandomDialogueSet.TUTORIAL,
                DialogueSet.ORDERED, 1);
        spawnEntityAt(trigger4, new Vector2(51.2f, 41.5f), true, true);
    }

    private void spawnTutorialObstacles() {
        Entity crate1 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate1, new Vector2(20, 35.8f), true, true);
        Entity crate2 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate2, new Vector2(20.8f, 35.8f), true, true);
        Entity crate3 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate3, new Vector2(20, 36.3f), true, true);
        Entity crate4 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate4, new Vector2(27.5f, 35.8f), true, true);
        Entity crate5 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate5, new Vector2(26.8f, 35.8f), true, true);

        Entity crate6 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate6, new Vector2(43f, 35.8f), true, true);
        Entity crate7 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate7, new Vector2(43.8f, 35.8f), true, true);
        Entity crate8 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate8, new Vector2(40f, 35.8f), true, true);

        Entity crate9 = ObstacleFactory.createHealthCrate();
        spawnEntityAt(crate9, new Vector2(50f, 42.4f), true, true);
    }

    private void spawnEnemy() {
        Entity enemy = NPCFactory.createMeleeElf(player);
        spawnEntityAt(enemy, new Vector2(50f, 37.5f), true, true);
    }

    private void spawnTeleport() {
        logger.info("Spawning the teleport object");
        Entity teleport = ObstacleFactory.createTeleport();
        HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
        GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(),
                (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 1));
        this.spawnEntityAt(teleport, fixedPos, true, true);

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

        //Imported Map Walls
        HashMap<String, Float>[] walls = map.getWallObjects();
        for (HashMap<String, Float> wall : walls) {
            int x = wall.get("x").intValue();
            int y = wall.get("y").intValue();
            float width = wall.get("width");
            float height = wall.get("height");

            int unitHeight = (int) ((height / 32f));
            spawnEntityAt(
                    ObstacleFactory.createWall((width / 32f) * 0.5f, (height / 32f) * 0.5f),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - (y + unitHeight)),
                    false,
                    false);
        }
    }

    private void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer("Hammer");
        HashMap<String, Float> spawn = map.getInitTeleportObjects()[0];
        int height = map.getDimensions().get("n_tiles_height");
        spawnEntityAt(newPlayer, new GridPoint2(spawn.get("x").intValue(), height - spawn.get("y").intValue()),
                true, true);
        player = newPlayer;
    }

    private void spawnObstacles() {
        int[][] obstacles = map.getTransObstacles();
        HashMap<String, String> tileRefs = map.getTileRefs();
        if (obstacles != null) {
            GridPoint2 min = new GridPoint2(0, 0);
            GridPoint2 max = new GridPoint2(map.getDimensions().get("n_tiles_width") - 1,
                    map.getDimensions().get("n_tiles_height") - 1);

            for (int y = min.y; y <= max.y; y++) {
                for (int x = min.y; x <= max.x; x++) {
                    if (obstacles[y][x] != 0) {

                        Entity obstacle = ObstacleFactory.createObstacle(tileRefs.get(String.valueOf(obstacles[y][x])));
                        GridPoint2 pos = new GridPoint2(x, max.y - y);

                        spawnEntityAt(obstacle, pos, true, false);
                    }
                }
            }
        }
    }

    private void spawnLights() {
        int[][] lights = map.getLightTiles();
        HashMap<String, String> tileRefs = map.getTileRefs();
        if (lights != null) {
            GridPoint2 min = new GridPoint2(0, 0);
            GridPoint2 max = new GridPoint2(map.getDimensions().get("n_tiles_width") - 1,
                    map.getDimensions().get("n_tiles_height") - 1);

            for (int y = min.y; y <= max.y; y++) {
                for (int x = min.y; x <= max.x; x++) {
                    if (lights[y][x] != 0) {

                        Entity obstacle = ObstacleFactory.createObstacle(tileRefs.get(String.valueOf(lights[y][x])));
                        GridPoint2 pos = new GridPoint2(x, max.y - y);

                        spawnEntityAt(obstacle, pos, true, false);
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

            int unitHeight = (int) ((height / 32f));
            spawnEntityAt(
                    ObstacleFactory.createRSPhysicalTrap((width / 32f) * 0.5f, (height / 32f) * 0.5f),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - (y + unitHeight)),
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

            int unitHeight = (int) ((height / 32f));
            spawnEntityAt(
                    ObstacleFactory.createRSNonePhysicalTrap((width / 32f) * 0.5f, (height / 32f) * 0.5f),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - (y + unitHeight)),
                    false,
                    false);
        }
    }

    /**
     * Use for teleport, track the current map player in
     */
    @Override
    public int getLevel() {
        return 9;
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
        resourceService.loadTextures(healthRegenTextures);
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
        resourceService.unloadAssets(healthRegenTextures);
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(forestSounds);
        resourceService.unloadAssets(forestMusic);
        resourceService.unloadAssets(arrowSounds);
    }

    private void setInitialDialogue() {
        PlayerSave.write();
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        RandomDialogueSet dialogueSet = RandomDialogueSet.TUTORIAL;

        textBox.setRandomFirstEncounter(dialogueSet);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}