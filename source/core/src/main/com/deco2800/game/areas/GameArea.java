package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.components.tasks.ShootProjectileTask;
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
import com.deco2800.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
@SuppressWarnings({"SuspiciousNameCombination", "CanBeFinal"})
public abstract class GameArea implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(GameArea.class);

    protected TerrainComponent terrain;
    protected List<Entity> areaEntities;
    protected Entity player;
    protected int numEnemy = 0;
    protected int numBoss = 0;
    protected Map map;
    protected static final float WALL_WIDTH_F = 0.1f;
    protected String[] tileTextures = null;
    protected static final String[] textures = {
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
            "images/player_longsword.png",
            "thor/lightning.png",
            "images/blast.png",
            "images/hammer_projectile",
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
            "images/fireball/fireballAnimation.png",
            "images/rangedFixed.png",
            "images/bossFixed.png",
            "images/meleeAnimationsTextured.png",
            "images/meleeFinal.png",
            "images/assassinFinal.png",
            "images/guardFinal.png",
            "images/rangedAllFinal.png",
            "images/bossFinal.png",
            "player_scepter.png",
            "player_hammer.png",
            "player_axe.png",
            "portal.png",
            "Odin/odinAttack.png",
            "Odin/odinDeath.png",
            "Odin/odinMovement.png",
            "Odin/OdinProjectile/beam_normal.png",
            "Odin/OdinProjectile/beamBroken.png",
            "Assets/gametile-127.png",
            "images/boss_health_middle.png",
            "images/boss_health_left.png",
            "images/boss_health_right.png",
            "images/viking.png",
            "images/explosion/explosion.png",
            "images/hellViking.png",
            "images/outdoorArcher.png",
            "images/asgardWarrior.png",
            "images/lokiBoss.png",
            "thor/aoe_attck.png",
            "thor/up_attck.png",
            "thor/down_attck.png",
            "thor/left_attck.png",
            "thor/right_attck.png",
            "thor/walk_left.png",
            "thor/walk_right.png",
            "images/firePillar.png",
            "healthRegen/healthPotion_placeholder.png",
            "crate/crateHitBreak.png",
            "rangedDeath.png",
            "guardDeath.png",
            "meleeDeath.png",
            "assassinDeath.png",
            "bossDeath.png",
            "assassinWalk.png",
            "rangedWalk.png",
            "guardMeleeAttack.png",
            "guardWalk.png",
            "guardWalkLeft.png"
    };
    protected static final String[] textureAtlases = {
            "images/outdoorArcher.atlas", "images/terrain_iso_grass.atlas", "crate/crateHitBreak.atlas", "images/elf.atlas",
            "images/player.atlas", "images/bossAttack.atlas", "images/meleeElf.atlas",
            "images/guardElf.atlas", "images/rangedElf.atlas", "images/fireball/fireballAnimation.atlas",
            "end/portal.atlas", "Odin/odin.atlas", "images/player_scepter.atlas", "images/player_hammer.atlas",
            "images/player_longsword.atlas", "images/hammer_projectile.atlas", "images/outdoorWarrior.atlas",
            "thor/lightning.atlas", "images/guardElf.atlas", "images/rangedElf.atlas", "images/fireball/fireballAnimation.atlas",
             "images/newArrowBroken/atlas/arrow.atlas", "images/player_axe.atlas",
            "images/viking.atlas", "images/meleeAnimationsTextured.atlas",
            "images/meleeFinal.atlas", "images/assassinFinal.atlas", "images/guardFinal.atlas", "images/rangedAllFinal.atlas", "images/bossFinal.atlas",
            "images/explosion/explosion.atlas", "images/hellViking.atlas", "images/outdoorArcher.atlas", "images/asgardWarrior.atlas",
            "images/lokiBoss.atlas", "thor/thor.atlas", "images/firePillar.atlas", "Odin" +
            "/OdinProjectile/beamBroken.atlas", "images/fireball/fireballAnimationBlue.atlas",
            "/OdinProjectile/beamBroken.atlas", "rangedDeath.atlas",  "guardDeath.atlas", "meleeDeath.atlas", "assassinDeath.atlas", "bossDeath.atlas", "assassinWalk.atlas",
            "rangedWalk.atlas", "guardMeleeAttack.atlas"
    };
    protected static final String[] sounds = {
            "sounds/Impact4.ogg", "sounds/impact.ogg", "sounds/swish.ogg",
            "sounds/lightning.mp3", "sounds/clank.mp3",
            "sounds/arrow_disappear.mp3",
            "sounds/arrow_shoot.mp3",
            "sounds/death_2.mp3",
            "sounds/death_1.mp3",
            "sounds/boss_death.mp3",
            "sounds/beam_shoot.mp3",
            "sounds/beam_disappear.mp3"
    };
    protected static String music = "sounds/RAGNAROK_MAIN_SONG_76bpm.mp3";

    protected TerrainFactory terrainFactory = null;
    protected int playerHealth = 300;
    protected int levelInt = 0;

    protected static final String TILES_HEIGHT = "n_tiles_height";
    protected static final String TILES_WIDTH = "n_tiles_width";
    protected static final String WALL_HEIGHT = "height";
    protected static final String WALL_WIDTH = "width";
    protected String playerWeaponType = "Axe";

    protected GameArea() {
        areaEntities = new ArrayList<>();
    }

    /**
     * Create the game area in the world.
     */
    public void create(String mapFile, String areaName) {
        ServiceLocator.registerGameArea(this);
        map = FileLoader.readClass(Map.class, mapFile);
        tileTextures = map.tileRefsArray();
        loadAssets();
        displayUI(areaName);

        spawnTerrain();
        spawnObstacles();
        spawnLights();
        spawnLavaTraps();
        spawnHealthCrateObject();
        spawnTraps();
        spawnPTraps();

        spawnPlayer();

        playMusic();

    }

    /**
     * Use for teleport, track the current map player in
     */
    public int getLevel() {
        return levelInt;
    }

    /**
     * increase number of enemy on the map (keep track) - when the enemy is create and spawn
     */
    public void incNum() {
        numEnemy++;
    }

    /**
     * decrease number of enemy on the map (keep track) - when the enemy died
     */
    public void decNum() {
        numEnemy--;
    }

    /**
     * increase the number of boss
     */
    public void incBossNum() {
        numBoss++;
    }

    /**
     * decrease number of boss - spawn the teleport portal to another map
     */
    public void decBossNum() {
        logger.info("Decreasing number of bosses in the map.");
        numBoss--;
        if (numBoss == 0) {
            logger.info("Number of Bosses is now at 0");
            logger.info("Spawning the teleport object");
            //gama area x
            Entity teleport = ObstacleFactory.createTeleport();
            HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
            GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(),
                    (map.getDimensions().get(TILES_HEIGHT) - teleportPos[0].get("y").intValue()) - 1);
            this.spawnEntityAt(teleport, fixedPos, true, true);
        }
    }

    protected void displayUI(String areaName) {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay(areaName));
        spawnEntity(ui);
    }

    protected void spawnDialogueCutscenes(RandomDialogueSet dialogueSet) {
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
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                    false,
                    false);
        }
    }

    /**
     * get the number of enemy on the map
     *
     * @return int number of enemy
     */
    public int getNumEnemy() {
        return numEnemy;
    }

    /**
     * Returns the player entity that is created.
     *
     * @return player entity - main character being controlled
     */
    public Entity getPlayer() {
        return player;
    }


    /**
     * Spawn entity at its current position
     *
     * @param entity Entity (not yet registered)
     */
    protected void spawnEntity(Entity entity) {
        areaEntities.add(entity);
        ServiceLocator.getEntityService().register(entity);
    }

    /**
     * Spawn entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity  Entity (not yet registered)
     * @param tilePos tile position to spawn at
     * @param centerX true to center entity X on the tile, false to align the bottom left corner
     * @param centerY true to center entity Y on the tile, false to align the bottom left corner
     */
    public void spawnEntityAt(
            Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
        float tileSize = terrain.getTileSize();

        if (centerX) {
            worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(worldPos);
        spawnEntity(entity);
    }

    /**
     * Spawns a create object which will reveal a health potion at the positions specified
     * within the Tiled JSON file.
     */
    protected void spawnHealthCrateObject() {
        HashMap<String, Float>[] crates = map.getHealthCrateObjects();
        if (crates == null) {
            return;
        }
        for (HashMap<String, Float> crate : crates) {
            int x = crate.get("x").intValue();
            int y = crate.get("y").intValue();

            spawnEntityAt(
                    ObstacleFactory.createHealthCrate(),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                    false,
                    false);
        }
    }

    /**
     * Spawn entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity    Entity (not yet registered)
     * @param entityPos world position to spawn at
     * @param centerX   true to center entity X on the tile, false to align the bottom left corner
     * @param centerY   true to center entity Y on the tile, false to align the bottom left corner
     */
    public void spawnEntityAt(
            Entity entity, Vector2 entityPos, boolean centerX, boolean centerY) {
        float tileSize = terrain.getTileSize();

        if (centerX) {
            entityPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            entityPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(entityPos);
        spawnEntity(entity);
    }

    protected void spawnHellWarriorObject() {
        HashMap<String, Float>[] warriors = map.getHellMeleeObjects();
        for (HashMap<String, Float> warrior : warriors) {
            int x = warrior.get("x").intValue();
            int y = warrior.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createMeleeHellViking(player),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                    false,
                    false);
        }
    }

    protected void spawnAsgardWarriorObject() {
        HashMap<String, Float>[] crates = map.getAsgardMeleeObjects();
        for (HashMap<String, Float> crate : crates) {
            int x = crate.get("x").intValue();
            int y = crate.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createMeleeAsgardViking(player),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                    false,
                    false);
        }
    }

    protected void spawnOutdoorWarriorObject() {
        HashMap<String, Float>[] warriors = map.getOutdoorMeleeObjects();
        for (HashMap<String, Float> warrior : warriors) {
            int x = warrior.get("x").intValue();
            int y = warrior.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createMeleeViking(player),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                    false,
                    false);
        }
    }

    protected void spawnOutdoorArcherObject() {
        HashMap<String, Float>[] crates = map.getRangeObjects();
        for (HashMap<String, Float> crate : crates) {
            int x = crate.get("x").intValue();
            int y = crate.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createOutdoorArcher(player),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                    false,
                    false);
        }
    }

    protected void spawnMovementCutscenes() {
        HashMap<String, Float>[] lefts = map.getMoveLeftObjects();
        if (lefts != null) {
            for (HashMap<String, Float> left : lefts) {
                int x = left.get("x").intValue();
                int y = left.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createLeftMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }

        HashMap<String, Float>[] rights = map.getMoveRightObjects();
        if (rights != null) {
            for (HashMap<String, Float> right : rights) {
                int x = right.get("x").intValue();
                int y = right.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createRightMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }

        HashMap<String, Float>[] downs = map.getMoveDownObjects();
        if (downs != null) {
            for (HashMap<String, Float> down : downs) {
                int x = down.get("x").intValue();
                int y = down.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createDownMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }

        HashMap<String, Float>[] ups = map.getMoveUpObjects();
        if (ups != null) {
            for (HashMap<String, Float> up : ups) {
                int x = up.get("x").intValue();
                int y = up.get("y").intValue();

                spawnEntityAt(
                        CutsceneTriggerFactory.createUpMoveTrigger(),
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    /**
     * spawn boss - only spawn on the map if other enemies are killed
     */
    protected void spawnLoki() {
        HashMap<String, Float>[] objects = map.getBossObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createLoki(player);
                incBossNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    /**
     * spawn boss - only spawn on the map if other enemies are killed
     */
    protected void spawnBoss() {
        HashMap<String, Float>[] objects = map.getBossObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createBossNPC(player);
                Entity thor = NPCFactory.createThor(player);
                incBossNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
                spawnEntityAt(
                        thor,
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    /**
     * spawn boss - only spawn on the map if other enemies are killed
     */
    protected void spawnThor() {
        HashMap<String, Float>[] objects = map.getBossObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity thor = NPCFactory.createThor(player);
                incBossNum();
                spawnEntityAt(
                        thor,
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    protected void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(map);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH_F, worldBounds.y), GridPoint2Utils.ZERO, false, false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH_F, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH_F),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH_F), GridPoint2Utils.ZERO, false, false);

        //Imported Map Walls
        HashMap<String, Float>[] walls = map.getWallObjects();
        for (HashMap<String, Float> wall : walls) {
            int x = wall.get("x").intValue();
            int y = wall.get("y").intValue();
            float width = wall.get(WALL_WIDTH);
            float height = wall.get(WALL_HEIGHT);

            int unitHeight = (int) (height / 32f);
            spawnEntityAt(
                    ObstacleFactory.createWall((width / 32f) * 0.5f, (height / 32f) * 0.5f),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - (y + unitHeight)),
                    false,
                    false);
        }
    }

    protected void spawnPTraps() {
        GridPoint2 fixedPos = new GridPoint2(15, 15);
        Entity trap = ObstacleFactory.createPhysicalTrap();
        spawnEntityAt(trap, fixedPos, true, true);
    }

    protected void spawnTraps() {
        GridPoint2 fixedPos = new GridPoint2(8, 8);
        Entity trap = ObstacleFactory.createNonePhysicalTrap();
        spawnEntityAt(trap, fixedPos, true, true);
    }

    protected void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer(playerWeaponType);
        HashMap<String, Float> spawn = map.getInitTeleportObjects()[0];
        int height = map.getDimensions().get(TILES_HEIGHT);
        spawnEntityAt(newPlayer, new GridPoint2(spawn.get("x").intValue(), height - spawn.get("y").intValue()),
                true, true);
        player = newPlayer;
    }

    protected void spawnObstacles() {
        int[][] obstacles = map.getTransObstacles();
        HashMap<String, String> tileRefs = map.getTileRefs();
        if (obstacles != null) {
            GridPoint2 min = new GridPoint2(0, 0);
            GridPoint2 max = new GridPoint2(map.getDimensions().get(TILES_WIDTH) - 1,
                    map.getDimensions().get(TILES_HEIGHT) - 1);

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

    protected void spawnLights() {
        int[][] lights = map.getlightTiles();
        HashMap<String, String> tileRefs = map.getTileRefs();
        if (lights != null) {
            GridPoint2 min = new GridPoint2(0, 0);
            GridPoint2 max = new GridPoint2(map.getDimensions().get(TILES_WIDTH) - 1,
                    map.getDimensions().get(TILES_HEIGHT) - 1);

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

    protected void spawnSpikeTraps() {
        HashMap<String, Float>[] spikeTraps = map.getSpikeObjects();
        for (HashMap<String, Float> spikeTrap : spikeTraps) {
            int x = spikeTrap.get("x").intValue();
            int y = spikeTrap.get("y").intValue();
            float width = spikeTrap.get(WALL_WIDTH);
            float height = spikeTrap.get(WALL_HEIGHT);

            int unitHeight = (int) (height / 32f);
            spawnEntityAt(
                    ObstacleFactory.createRSPhysicalTrap((width / 32f) * 0.5f, (height / 32f) * 0.5f),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - (y + unitHeight)),
                    false,
                    false);
        }
    }

    protected void spawnLavaTraps() {
        HashMap<String, Float>[] lavaTraps = map.getLavaObjects();
        for (HashMap<String, Float> lavaTrap : lavaTraps) {
            int x = lavaTrap.get("x").intValue();
            int y = lavaTrap.get("y").intValue();
            float width = lavaTrap.get(WALL_WIDTH);
            float height = lavaTrap.get(WALL_HEIGHT);

            int unitHeight = (int) (height / 32f);
            spawnEntityAt(
                    ObstacleFactory.createRSNonePhysicalTrap((width / 32f) * 0.5f, (height / 32f) * 0.5f),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - (y + unitHeight)),
                    false,
                    false);
        }
    }

    protected void spawnMeleeElf() {
        HashMap<String, Float>[] objects = map.getMeleeObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createMeleeElf(player);
                incNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Spawn range elf on terrain, range elf can shoot target
     */
    protected void spawnRangedElf() {
        HashMap<String, Float>[] objects = map.getRangeObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.NORMAL_ARROW, 0.15f * (levelInt + 1));
                incNum();
                elf.setEntityType("ranged");
                elf.getEvents().trigger("rangedLeft");
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Spawn Assassin on terrain, range can shoot from far away with high damage
     */
    protected void spawnAssassinElf() {
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
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    protected void spawnElfGuard() {
        HashMap<String, Float>[] objects = map.getGuardObjects();
        if (objects != null) {
            for (HashMap<String, Float> object : objects) {
                int x = object.get("x").intValue();
                int y = object.get("y").intValue();
                Entity elf = NPCFactory.createElfGuard(player);
                incNum();
                spawnEntityAt(
                        elf,
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Spawn anchored elf, elf only move at the certain anchored
     */
    protected void spawnAnchoredElf() {
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
                        new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                        false,
                        false);
            }
        }
    }

    /**
     * Load the texture from files
     */
    protected void loadAssets() {

        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(textures);
        resourceService.loadTextures(tileTextures);
        resourceService.loadTextureAtlases(textureAtlases);
        resourceService.loadSounds(sounds);
        resourceService.loadMusic(new String[]{music});
        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    /**
     * Unload the assets (include image and sound)
     */
    protected void unloadAssets() {
        logger.debug("Unloading assets");
        if (ServiceLocator.getResourceService() != null) {
            ResourceService resourceService = ServiceLocator.getResourceService();
            resourceService.unloadAssets(textures);
            resourceService.unloadAssets(tileTextures);
            resourceService.unloadAssets(textureAtlases);
            resourceService.unloadAssets(sounds);
            resourceService.unloadAssets(new String[]{music});
        }
    }

    public void dispose() {
        for (Entity entity : areaEntities) {
            entity.dispose();
        }
        if (ServiceLocator.getResourceService() != null
                && ServiceLocator.getResourceService().getAsset(music, Music.class) != null) {
            ServiceLocator.getResourceService().getAsset(music, Music.class).stop();
        }
        this.unloadAssets();
    }

    protected void playMusic() {
        Music gameMusic = ServiceLocator.getResourceService().getAsset(GameArea.music, Music.class);
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.3f);
        gameMusic.play();

    }
}
