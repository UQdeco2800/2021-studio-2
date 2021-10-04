package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CombatStatsComponent;
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
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.game.files.PlayerSave;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
@SuppressWarnings("SuspiciousNameCombination")
public class ForestGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final int NUM_CRATES = 3;
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
            "portal.png",
            "Odin/odin.png",
            "Assets/gametile-127.png",
            "images/boss_health_middle.png",
            "images/boss_health_left.png",
            "images/boss_health_right.png",
            "images/outdoorArcher.png",
            "images/outdoorWarrior.png",
            "images/hellWarrior.png",
            "images/viking.png",
            "images/hellViking.png",
            "images/outdoorArcher.png",
            "images/asgardWarrior.png",
            "images/lokiBoss.png"
    };
    public static final String[] healthRegenTextures = {
            "healthRegen/healthPotion_placeholder.png",
            "crate/crateHitBreak.png"
    };
    private static final String[] forestTextureAtlases = {
            "images/outdoorArcher.atlas", "images/terrain_iso_grass.atlas", "crate/crateHitBreak.atlas", "images/elf.atlas",
            "images/player.atlas", "images/bossAttack.atlas", "images/meleeElf.atlas",
            "images/guardElf.atlas", "images/rangedElf.atlas", "images/fireball/fireballAinmation.atlas",
            "end/portal.atlas", "Odin/odin.atlas",
            "images/player_scepter.atlas", "images/player_hammer.atlas", "images/outdoorWarrior.atlas",
            "images/hellWarrior.atlas",
            "images/guardElf.atlas", "images/rangedElf.atlas", "images/fireball/fireballAnimation.atlas",
            "images/player_scepter.atlas", "images/player_hammer.atlas", "images/arrow_broken/arrowBroken.atlas",
            "images/viking.atlas", "images/hellViking.atlas", "images/outdoorArcher.atlas", "images/asgardWarrior.atlas",
            "images/lokiBoss.atlas"
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
    private int playerHealth = 300;

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
     * Use for teleport, track the current playerHealth
     */
    public ForestGameArea(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
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
        //spawnTrees();
        spawnPlayer();
        spawnCrate();
//        spawnMeleeElf();
//        spawnElfGuard();
//        spawnRangedElf();
//        spawnAssassinElf();
//        spawnAnchoredElf();
//        spawnBoss();
//        spawnVikingMelee();
//        spawnHellVikingMelee();
//        spawnAsgardWarriorMelee();
//        spawnOutdoorArcher();
//        spawnLokiBoss();

        playMusic();
        setDialogue();
        spawnOdin();

        player.getComponent(CombatStatsComponent.class).setHealth(this.playerHealth);

    }

    @Override
    public int getLevel() {
        return 0;
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
        Entity newPlayer = PlayerFactory.createPlayer("Scepter");
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        player = newPlayer;
    }

    private void spawnOdin() {
        Entity odin = NPCFactory.createOdin(player);
        spawnEntityAt(odin, new GridPoint2(20, 20), true, true);
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
     * Randomly spawn viing on a random position of the terrain, the number of vikings limit to 2
     */
    private void spawnVikingMelee() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createMeleeViking(player);
            incNum();
            spawnEntityAt(elf, randomPos, true, true);
        }
    }

    /**
     * Randomly spawn viking on a random position of the terrain, the number of vikings limit to 2
     */
    private void spawnHellVikingMelee() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createMeleeHellViking(player);
            incNum();
            spawnEntityAt(elf, randomPos, true, true);
        }
    }

    /**
     * Randomly spawn warrior on a random position of the terrain, the number of warriors limit to 2
     */
    private void spawnAsgardWarriorMelee() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createMeleeAsgardViking(player);
            incNum();
            spawnEntityAt(elf, randomPos, true, true);
        }
    }

    /**
     * Spawn range archer on terrain, ranged archers can shoot target
     */
    private void spawnOutdoorArcher() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity archer = NPCFactory.createOutdoorArcher(player, 0.1f);
            incNum();
            spawnEntityAt(archer, randomPos, true, true);
        }
    }

    /**
     * spawn boss - only spawn on the map if other enemies are killed
     */
    private void spawnLokiBoss() {
        /*GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);*/
        GridPoint2 bossPos = new GridPoint2(100, 100);
        Entity boss = NPCFactory.createLokiBossNPC(player);
        spawnEntityAt(boss, bossPos, true, true);
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
            incNum();
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
     * spawns the crate with the potion entity inside.
     * the crate is placed on top of the potion entity.
     */
    public void spawnCrate() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_CRATES; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity crate = ObstacleFactory.createHealthCrate();
            spawnEntityAt(crate, randomPos, true, true);
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
        resourceService.loadTextures(healthRegenTextures);
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
        resourceService.unloadAssets(healthRegenTextures);
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(forestSounds);
        resourceService.unloadAssets(forestMusic);
    }

    /**
     * Sets the dialogue for when the game first loads.
     */
    private void setDialogue() {
        PlayerSave.Save pSave = PlayerSave.load();


        if(pSave.lokiEnc < 2 || pSave.lokiEnc >= 4){
            TextBox textBox = ServiceLocator.getEntityService()
                    .getUIEntity().getComponent(TextBox.class);
            textBox.setRandomFirstEncounter(RandomDialogueSet.LOKI_OPENING);

            pSave.lokiEnc += 1;

        }else if(pSave.lokiEnc >= 1 && pSave.lokiEnc < 3){
            TextBox textBox = ServiceLocator.getEntityService()
                    .getUIEntity().getComponent(TextBox.class);
            textBox.setRandomFirstEncounter(RandomDialogueSet.GARMR);

            pSave.lokiEnc += 1;
        }

        PlayerSave.write(pSave);

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