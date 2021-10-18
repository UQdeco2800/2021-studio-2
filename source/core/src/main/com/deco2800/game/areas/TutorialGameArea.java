package com.deco2800.game.areas;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.components.tasks.ShootProjectileTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.utils.math.RandomUtils;

/**
 * Tutorial Level where player learns mechanics of the game.
 */
public class TutorialGameArea extends GameArea {
    private static final int NUM_MELEE_ELF = 2;
    private static final int NUM_ANCHORED_ELF = 1;

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
    public GameArea create() {
        levelInt = 9;
        playerWeaponType = "Scepter";
        super.create("maps/MapObjects.json", "Map Test");

        spawnMeleeElf();
        spawnElfGuard();
        spawnRangedElf();
        spawnAssassinElf();
        spawnAnchoredElf();

        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
        return this;
    }

    private void spawnCutsceneTrigger() {
        Entity trigger = CutsceneTriggerFactory.createDialogueTrigger(RandomDialogueSet.TUTORIAL,
                DialogueSet.ORDERED, 0);
        spawnEntityAt(trigger, new Vector2(11f, 181.3f), true, true);

        Entity trigger3 = CutsceneTriggerFactory.createLokiTrigger(RandomDialogueSet.LOKI_INTRODUCTION,
                DialogueSet.BOSS_DEFEATED_BEFORE);
        spawnEntityAt(trigger3, new Vector2(21f, 177f), true, true);

        Entity moveTrigger3 = CutsceneTriggerFactory.createAttackTrigger(3, Input.Keys.D);
        spawnEntityAt(moveTrigger3, new Vector2(21f, 181.3f), true, true);

        Entity moveTrigger4 = CutsceneTriggerFactory.createMoveTrigger(new Vector2(1f, 0f), 20, 0);
        spawnEntityAt(moveTrigger4, new Vector2(14.6f, 180.2f), true, true);

        Entity moveTrigger5 = CutsceneTriggerFactory.createMoveTrigger(new Vector2(0f, -1f), 0, -10);
        spawnEntityAt(moveTrigger5, new Vector2(14.7f, 184.5f), true, true);


        Entity moveTrigger6 = CutsceneTriggerFactory.createMoveTrigger(new Vector2(1f, 0f), 4, 0);
        spawnEntityAt(moveTrigger6, new Vector2(11.5f, 184.5f), true, true);
    }

    /**
     * Randomly spawn elf on a random position of the terrain, the number of elf limit to 2
     */
    @Override
    protected void spawnMeleeElf() {
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
    @Override
    protected void spawnRangedElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.NORMAL_ARROW, 0.1f);
            incNum();
            elf.setEntityType("ranged");
            elf.getEvents().trigger("rangerLeft");
            spawnEntityAt(elf, randomPos, true, true);
        }
    }

    /**
     * Spawn Assassin on terrain, range can shoot from far away with high damage
     */
    @Override
    protected void spawnAssassinElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_MELEE_ELF; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity elf = NPCFactory.createRangedElf(player, ShootProjectileTask.projectileTypes.FAST_ARROW, 0);
            elf.setEntityType("assassin");
            elf.getEvents().trigger("assassinLeft");
            spawnEntityAt(elf, randomPos, true, true);
            incNum();
        }
    }

    @Override
    protected void spawnElfGuard() {
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
    @Override
    protected void spawnAnchoredElf() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_ANCHORED_ELF; i++) {
            GridPoint2 basePos = RandomUtils.random(minPos, maxPos);

            GridPoint2 elfPos = RandomUtils.random(basePos.cpy().sub(3, 3), basePos.cpy().add(3, 3));
            Entity anchor = ObstacleFactory.createAnchor();
            Entity anchoredelf = NPCFactory.createAnchoredElf(player, anchor, 3f);
            spawnEntityAt(anchor, basePos, true, true);
            incNum();
            spawnEntityAt(anchoredelf, elfPos, true, true);
        }
    }
}
