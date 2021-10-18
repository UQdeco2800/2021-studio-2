package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

import java.util.HashMap;

/**
 * Smaller room based level
 */
public class GameArea4 extends GameArea {
    public GameArea4(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    public GameArea4(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    public GameArea create() {
        levelInt = 9;
        super.create("maps/lvl_0.json", "Level 5");

        spawnEnemy();
        decBossNum();

        spawnDialogueCutscenes();
        setInitialDialogue();
        spawnTutorialObstacles();

        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
        return this;
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

        Entity teleport = ObstacleFactory.createTeleport();
        HashMap<String, Float> teleportPos = map.getTeleportObjects()[0];
        GridPoint2 fixedPos = new GridPoint2(teleportPos.get("x").intValue(),
                (map.getDimensions().get(TILES_HEIGHT) - teleportPos.get("y").intValue()) - 2);
        this.spawnEntityAt(teleport, fixedPos, true, true);
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

    private void spawnEnemy() {
        Entity enemy = NPCFactory.createMeleeElf(player);
        spawnEntityAt(enemy, new Vector2(50f, 37.5f), true, true);
    }

    private void setInitialDialogue() {
        PlayerSave.write();
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        RandomDialogueSet dialogueSet = RandomDialogueSet.TUTORIAL;

        textBox.setRandomFirstEncounter(dialogueSet);
    }
}