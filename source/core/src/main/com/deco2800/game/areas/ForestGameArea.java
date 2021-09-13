package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
@SuppressWarnings("SuspiciousNameCombination")
public class ForestGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final int NUM_TREES = 7;
    private static final int NUM_MELEE_ELF = 2;
    private static final int NUM_ANCHORED_ELF = 1;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
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
            "images/minionEnemy.png",
            "images/meleeElf.png",
            "images/guardElf.png",
            "images/rangedElf.png"

    };
    private static final String[] forestTextureAtlases = {

            "images/terrain_iso_grass.atlas", "images/elf.atlas",
            "images/player.atlas", "images/bossEnemy.atlas", "images/bossAttack.atlas", "images/minionEnemy.atlas", "images/meleeElf.atlas",
            "images/guardElf.atlas", "images/rangedElf.atlas"
    };
    private static final String[] arrowSounds = {
            "sounds/arrow_disappear.mp3",
            "sounds/arrow_shoot.mp3"
    };
    private static final String[] forestSounds = {
            "sounds/Impact4.ogg", "sounds/impact.ogg", "sounds/swish.ogg"
    };
    private static final String backgroundMusic = "sounds/RAGNAROK_MAIN_SONG_76bpm.mp3";
    private static final String[] forestMusic = {backgroundMusic};

    private final TerrainFactory terrainFactory;

    /**
     * Intialise the forest game
     *
     * @param terrainFactory intialise the terrain factory
     */
    public ForestGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        super.create();
        loadAssets();
        displayUI();

        spawnTerrain();
        spawnTrees();
        spawnPlayer();
        spawnMeleeElf();
        spawnElfGuard();
        spawnRangedElf();
        spawnAssassinElf();
        spawnAnchoredElf();
        spawnBoss();
        playMusic();
        setDialogue();
    }

    /**
     * Display the UI
     */
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }

    /**
     * Spawn the terrain - spawn map entity on terrain
     */
    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
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
    }

    /**
     * Spawn tree on terrain
     */
    private void spawnTrees() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_TREES; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity tree = ObstacleFactory.createTree();
            spawnEntityAt(tree, randomPos, true, false);
        }
    }

    /**
     * Spawn player at the terrain, create the player
     */
    private void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        player = newPlayer;
    }

    /**
     * Randomly spawn elf on a random position of the terrain, the number of elf limit to 2
     */
    private void spawnMeleeElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createMeleeElf(player);
            incNum();
            spawnEntityAt(elf, randomPos, true, true);
        }
    }

    /**
     * Spawn range elf on terrain, range elf can shoot target
     */
    private void spawnRangedElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createRangedElf(player, "normalArrow", 0.1f);
            incNum();
            elf.setEntityType("ranged");
            elf.getEvents().trigger("rangerLeft");
            spawnEntityAt(elf, randomPos, true, true);
        }
    }

    /**
     * Spawn Assassin on terrain, range can shoot from far away with high damage
     */
    private void spawnAssassinElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createRangedElf(player, "fastArrow", 0);
            elf.setEntityType("assassin");
            elf.getEvents().trigger("assassinLeft");
            spawnEntityAt(elf, randomPos, true, true);
        }
    }

    /**
     * spawn boss - only spawn on the map if other enemies are killed
     */
    private void spawnBoss() {
        /*GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);*/
        GridPoint2 bossPos = new GridPoint2(100, 100);
        Entity boss = NPCFactory.createBossNPC(player);
        spawnEntityAt(boss, bossPos, true, true);
    }

    private void spawnElfGuard() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        Entity elfKing = NPCFactory.createElfGuard(player);
        incNum();
        spawnEntityAt(elfKing, randomPos, true, true);
    }

    /**
     * Spawn anchored elf, elf only move at the certain anchored
     */
    private void spawnAnchoredElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_ANCHORED_ELF; i++) {
            GridPoint2 basePos = RandomUtils.random(minPos, maxPos);

            GridPoint2 elfPos = RandomUtils.random(basePos.cpy().sub(3, 3), basePos.cpy().add(3, 3));
            Entity anchor = ObstacleFactory.createAnchor();
            Entity Anchoredelf = NPCFactory.createAnchoredElf(player, anchor, 3f);
            spawnEntityAt(anchor, basePos, true, true);
            incNum();
            spawnEntityAt(Anchoredelf, elfPos, true, true);
        }
    }

    /**
     * Play the music on the background of the game
     */
    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    /**
     * Load the texture from files
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
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

    /**
     * Unload the assets (include image and sound)
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(forestTextures);
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(forestSounds);
        resourceService.unloadAssets(forestMusic);
    }

    /**
     * Sets the dialogue for when the game first loads.
     */
    private void setDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);
        textBox.setRandomFirstEncounter(RandomDialogueSet.LOKI_OPENING);
    }


    /**
     * Dispose the asset (call unloadAssets).
     */
    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}
