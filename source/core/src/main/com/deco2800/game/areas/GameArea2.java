package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

/**
 * Level based on a harsher Helhiem (even more lava) with Thor as the boss
 */
public class GameArea2 extends GameArea {

    /**
     * Gamer area 2
     * @param terrainFactory terrain factory
     */
    public GameArea2(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /**
     * Gamer area 2 with teleport save health
     * @param terrainFactory terrain factory
     * @param currentHealth player health from last map
     */
    public GameArea2(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    public GameArea create() {
        playerWeaponType = "Scepter";
        music = "sounds/area3.mp3";

        levelInt = 2;
        super.create("maps/lvl_4.json", "Level 3");

        spawnHellWarriorObject();
        spawnThor();

        spawnMovementCutscenes();
        spawnDialogueCutscenes(RandomDialogueSet.LOKI2_ENCOUNTER);
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

        RandomDialogueSet dialogueSet = RandomDialogueSet.LOKI2_INTRODUCTION;

        PlayerSave.Save.setHasPlayed(true);
        if (PlayerSave.Save.getLoki2Enc() == 0) {
            textBox.setRandomFirstEncounter(dialogueSet);
        } else {
            if (PlayerSave.Save.getLoki2Wins() == 0) {
                //If getWins() returns 0, that means the most recent game has resulted in a loss
                textBox.setRandomDefeatDialogueSet(dialogueSet);
            } else {
                // When it returns 1, then the player has beaten the boss before
                textBox.setRandomBeatenDialogueSet(dialogueSet);
            }
        }
        PlayerSave.Save.setLokiWins(1);
        PlayerSave.Save.setLoki2Wins(0);
        PlayerSave.write();
    }
}
