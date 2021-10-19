package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

/**
 * Dungeon Level with an Elf Mage as the boss
 */
public class GameArea1 extends GameArea {

    /**
     * Gamer area 0
     *
     * @param terrainFactory terrain factory
     */
    public GameArea1(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /**
     * Gamer area 0 with teleport save health
     *
     * @param terrainFactory terrain factory
     * @param currentHealth  player health from last map
     */
    public GameArea1(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    public GameArea create() {
        playerWeaponType = "Axe";
        music = "sounds/area1.mp3";

        levelInt = 1;
        super.create("maps/lvl_1.json", "Level 1");

        spawnMeleeElf();
        spawnElfGuard();
        spawnRangedElf();
        spawnAssassinElf();
        spawnAnchoredElf();
        spawnBoss();

        spawnMovementCutscenes();
        spawnDialogueCutscenes(RandomDialogueSet.ELF_ENCOUNTER);
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
