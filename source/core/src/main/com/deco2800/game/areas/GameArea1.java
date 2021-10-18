package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

/**
 * Level based on Helhiem (lava level) with Loki as boss.
 */
public class GameArea1 extends GameArea {

    public GameArea1(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    public GameArea1(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    public GameArea create() {
        levelInt = 1;
        super.create("maps/lvl_3.json", "Level 2");

        spawnHellWarriorObject();
        spawnBoss();

        spawnMovementCutscenes();
        spawnDialogueCutscenes(RandomDialogueSet.LOKI_ENCOUNTER);
        setInitialDialogue();

        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
        return this;
    }

    /**
     * Sets the dialogue for when the game first loads.
     */
    private void setInitialDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        RandomDialogueSet dialogueSet = RandomDialogueSet.LOKI_INTRODUCTION;

        PlayerSave.Save.setHasPlayed(true);
        if (PlayerSave.Save.getLokiEnc() == 0) {
            textBox.setRandomFirstEncounter(dialogueSet);
        } else {
            if (PlayerSave.Save.getLokiWins() == 0) {
                //If getWins() returns 0, that means the most recent game has resulted in a loss
                textBox.setRandomDefeatDialogueSet(dialogueSet);
            } else {
                // When it returns 1, then the player has beaten the boss before
                textBox.setRandomBeatenDialogueSet(dialogueSet);
            }
        }
        PlayerSave.Save.setElfWins(1);
        PlayerSave.Save.setLokiWins(0);
        PlayerSave.write();
    }
}