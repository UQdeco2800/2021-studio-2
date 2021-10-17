package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.components.tasks.ShootProjectileTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
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
 * Forest area for the demo game with trees, a player, and some enemies.
 */
@SuppressWarnings("SuspiciousNameCombination")
public class ForestGameArea extends GameArea {
    private static final int NUM_CRATES = 3;
    private static final int NUM_TREES = 7;
    private static final int NUM_MELEE_ELF = 2;
    private static final int NUM_ANCHORED_ELF = 1;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);

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
     * Creates the current game area with the terrain factory required to produce the map
     * as well as the health of the player.
     *
     * @param terrainFactory Base game engine terrain factory to produce the map assets
     * @param currentHealth  the current health of the main character as an integer
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
        spawnPlayer();
        spawnCrate();

        spawnMeleeElf();
        spawnElfGuard();
        spawnRangedElf();
        playMusic();
        setInitialDialogue();
        playMusic();

        player.getComponent(CombatStatsComponent.class).setHealth(this.playerHealth);
    }

    private void setInitialDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        RandomDialogueSet dialogueSet = RandomDialogueSet.THOR_ENCOUNTER;

        PlayerSave.Save.setHasPlayed(true);
        if (PlayerSave.Save.getOdinEnc() == 0) {
            textBox.setRandomFirstEncounter(dialogueSet);
        } else {
            if (PlayerSave.Save.getOdinWins() == 0) {
                //If getWins() returns 0, that means the most recent game has resulted in a loss
                textBox.setRandomDefeatDialogueSet(dialogueSet);
            } else {
                // When it returns 1, then the player has beaten the boss before
                textBox.setRandomBeatenDialogueSet(dialogueSet);
            }
        }
        PlayerSave.Save.setThorWins(1);
        PlayerSave.Save.setOdinWins(0);
        PlayerSave.write();
    }

    private void spawnDialogueCutscenes() {
        RandomDialogueSet dialogueSet = RandomDialogueSet.THOR_ENCOUNTER;
        DialogueSet set;
        if (PlayerSave.Save.getElfEnc() == 0) {
            set = DialogueSet.FIRST_ENCOUNTER;
        } else {
            if (PlayerSave.Save.getElfWins() == 0) {
                //If getWins() returns 0, that means the most recent game has resulted in a loss
                set = DialogueSet.PLAYER_DEFEATED_BEFORE;
            } else {
                // When it returns 1, then the player has beaten the boss before
                set = DialogueSet.BOSS_DEFEATED_BEFORE;
            }
        }

        HashMap<String, Float>[] dialogues = map.getCutsceneObjects();
        if (dialogues == null) {
            return;
        }
        for (HashMap<String, Float> dialogue : dialogues) {
            int x = dialogue.get("x").intValue();
            int y = dialogue.get("y").intValue();

            spawnEntityAt(
                    CutsceneTriggerFactory.createDialogueTrigger(dialogueSet, set, 1),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                    false,
                    false);
        }
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
        Entity newPlayer = PlayerFactory.createPlayer("Longsword");
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
            Entity archer = NPCFactory.createOutdoorArcher(player);
            incNum();
            spawnEntityAt(archer, randomPos, true, true);
        }
    }

    /**
     * spawn boss - only spawn on the map if other enemies are killed
     */
    private void spawnLoki() {
        Entity boss = NPCFactory.createLoki(player);
        incBossNum();
        spawnEntityAt(boss, new GridPoint2(10, 10), true, true);
    }

    private void spawnThor() {
        Entity boss = NPCFactory.createThor(player);
        incBossNum();
        spawnEntityAt(boss, new GridPoint2(15, 15), true, true);
    }

    /**
     * Spawn range elf on terrain, range elf can shoot target
     */
    private void spawnRangedElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.normalArrow, 0.1f);
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
            Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.fastArrow, 0);
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
        GridPoint2 bossPos = new GridPoint2(100, 100);
        Entity boss = NPCFactory.createBossNPC(player);
        spawnEntityAt(boss, bossPos, true, true);
    }

    private void spawnElfGuard() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
        Entity elfGuard = NPCFactory.createElfGuard(player);
        incNum();
        spawnEntityAt(elfGuard, randomPos, true, true);
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
}