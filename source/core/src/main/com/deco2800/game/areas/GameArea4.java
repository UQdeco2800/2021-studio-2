package com.deco2800.game.areas;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

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

        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
        return this;
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