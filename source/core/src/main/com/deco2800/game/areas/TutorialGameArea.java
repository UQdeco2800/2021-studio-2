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
import com.deco2800.game.components.tasks.ShootProjectileTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Tutorial Level where player learns mechanics of the game.
 */
public class TutorialGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(TutorialGameArea.class);
    private static final int NUM_MELEE_ELF = 2;
    private static final int NUM_ANCHORED_ELF = 1;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(20, 370);
    private static final GridPoint2 TEST_TRIGGER = new GridPoint2(20, 21);
    private static Map map;

    public TutorialGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    public TutorialGameArea(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        map = FileLoader.readClass(Map.class, "maps/MapObjects.json");
        tileTextures = map.TileRefsArray();

        super.create();
        loadAssets();
        displayUI("map Test");

        spawnTerrain();
        spawnPlayer();

        spawnMeleeElf();
        spawnElfGuard();
        spawnRangedElf();
        spawnAssassinElf();
        spawnAnchoredElf();
        spawnBoss();

        spawnObstacles();

        spawnSpikeTraps();
        spawnLavaTraps();

        spawnTraps();
        spawnPTraps();

        playMusic();
        spawnTeleport();
        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
    }

    /**
     * Use for teleport, track the current map player in
     */
    @Override
    public int getLevel() {
        return 9;
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
}