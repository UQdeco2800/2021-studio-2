package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.tasks.ShootProjectileTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

import java.util.HashMap;

/**
 * Dungeon Level with an Elf Mage as the boss
 */
public class GameArea0 extends GameArea {

    public GameArea0(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    public GameArea0(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        map = FileLoader.readClass(Map.class, "maps/lvl_1.json");
        tileTextures = map.tileRefsArray();

        super.create();
        loadAssets();
        displayUI("Level 1");

        spawnTerrain();
        spawnPlayer();

        spawnMeleeElf();
        spawnMovementCutscenes();
        spawnMeleeElf();
        spawnElfGuard();
        spawnRangedElf();
        spawnAssassinElf();
        spawnAnchoredElf();
        spawnBoss();

        spawnDialogueCutscenes();
        setInitialDialogue();

        spawnObstacles();
        spawnLights();

        spawnSpikeTraps();
        spawnLavaTraps();
        spawnHealthCrateObject();

        spawnHealthCrateObject();

        spawnTraps();
        spawnPTraps();

        playMusic();
        spawnTeleport();
        decBossNum();
        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
    }

    private void spawnDialogueCutscenes() {
        RandomDialogueSet dialogueSet = RandomDialogueSet.ELF_ENCOUNTER;
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

    private void spawnMeleeElf() {
        HashMap<String, Float>[] objects = map.getMeleeObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createMeleeElf(player);
                incNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Spawn range elf on terrain, range elf can shoot target
     */
    private void spawnRangedElf() {
        HashMap<String, Float>[] objects = map.getRangeObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.NORMAL_ARROW, 0.1f);
                incNum();
                elf.setEntityType("ranged");
                elf.getEvents().trigger("rangerLeft");
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Spawn Assassin on terrain, range can shoot from far away with high damage
     */
    private void spawnAssassinElf() {
        HashMap<String, Float>[] objects = map.getAssassinObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.FAST_ARROW, 0);
                elf.getEvents().trigger("assassinLeft");
                incNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    /**
     * spawn boss - only spawn on the map if other enemies are killed
     */
    private void spawnBoss() {
        HashMap<String, Float>[] objects = map.getBossObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createBossNPC(player);
                incNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    private void spawnElfGuard() {
        HashMap<String, Float>[] objects = map.getGuardObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createElfGuard(player);
                incNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Spawn anchored elf, elf only move at the certain anchored
     */
    private void spawnAnchoredElf() {
        HashMap<String, Float>[] objects = map.getAnchoredObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity anchor = ObstacleFactory.createAnchor();
                Entity elf = NPCFactory.createAnchoredElf(player, anchor, 3f);
                incNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Sets the dialogue for when the game first loads.
     */
    private void setInitialDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        PlayerSave.Save.setHasPlayed(true);
        if (PlayerSave.Save.getElfEnc() == 0) {
            textBox.setRandomFirstEncounter(RandomDialogueSet.ELF_INTRODUCTION);
        } else {
            if (PlayerSave.Save.getElfWins() == 0) {
                //If getWins() returns 0, that means the most recent game has resulted in a loss
                textBox.setRandomDefeatDialogueSet(RandomDialogueSet.ELF_INTRODUCTION);
            } else {
                // When it returns 1, then the player has beaten the boss before
                textBox.setRandomBeatenDialogueSet(RandomDialogueSet.ELF_INTRODUCTION);
            }
        }
        PlayerSave.Save.setElfWins(0);
        PlayerSave.write();
    }


}