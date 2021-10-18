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
        tileTextures = map.TileRefsArray();

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

    private void spawnPTraps() {
        GridPoint2 fixedPos = new GridPoint2(15, 15);
        Entity trap = ObstacleFactory.createPhysicalTrap();
        spawnEntityAt(trap, fixedPos, true, true);
    }

    private void spawnTraps() {
        GridPoint2 fixedPos = new GridPoint2(8, 8);
        Entity trap = ObstacleFactory.createNonePhysicalTrap();
        spawnEntityAt(trap, fixedPos, true, true);
    }

    private void spawnTeleport() {
        Entity teleport = ObstacleFactory.createTeleport();
        HashMap<String, Float>[] teleportObjects = map.getTeleportObjects();
        if (teleportObjects != null) {
            for (HashMap<String, Float> teleportObject : teleportObjects) {
                int x = teleportObject.get("x").intValue();
                int y = teleportObject.get("y").intValue();
                float width = teleportObject.get("width");
                float height = teleportObject.get("height");
                int unitHeight = (int) ((height / 32f));

                spawnEntityAt(teleport,
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - (y + unitHeight)),
                        false,
                        false);

                //spawnEntityAt(teleport, fixedPos, true, true);
            }
        }
        //boss= 1;
    }

    private void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer("Hammer");
        HashMap<String, Float> spawn = map.getInitTeleportObjects()[0];
        int height = map.getDimensions().get("n_tiles_height");
        spawnEntityAt(newPlayer, new GridPoint2(spawn.get("x").intValue(),height - spawn.get("y").intValue()),
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

    private void spawnHealthCrateObject() {
        HashMap<String, Float>[] crates = map.getHealthCrateObjects();
        if (crates != null) {
            for (HashMap<String, Float> crate : crates) {
                int x = crate.get("x").intValue();
                int y = crate.get("y").intValue();

                spawnEntityAt(
                        ObstacleFactory.createHealthCrate(),
                        new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Use for teleport, track the current map player in
     */
    @Override
    public int getLevel() {
        return 0;
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
                Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.normalArrow, 0.1f);
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
                Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.fastArrow, 0);
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
                Entity boss = NPCFactory.createBossNPC(player);
                incNum();
                spawnEntityAt(
                        boss,
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