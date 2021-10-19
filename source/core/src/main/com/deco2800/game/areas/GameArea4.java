package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

/**
 * Level based on the insides of a Palace with the boss being Odin
 */
public class GameArea4 extends GameArea {
    /**
     * Gamer area 3
     *
     * @param terrainFactory terrain factory
     */
    public GameArea4(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /**
     * Gamer area 3 with teleport save health
     *
     * @param terrainFactory terrain factory
     * @param currentHealth  player health from last map
     */
    public GameArea4(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    public GameArea create() {
        playerWeaponType = "Hammer";
        music = "sounds/area4.mp3";

        levelInt = 4;
        super.create("maps/lvl_5.json", "Level 4");
        spawnOutdoorArcherObject();
        spawnOutdoorWarriorObject();
        spawnThor();

        spawnMovementCutscenes();
        spawnDialogueCutscenes(RandomDialogueSet.THOR_ENCOUNTER);
        setInitialDialogue();

        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
        return this;
    }

    private void setInitialDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        RandomDialogueSet dialogueSet = RandomDialogueSet.THOR_INTRODUCTION;

        PlayerSave.Save.setHasPlayed(true);
        if (PlayerSave.Save.getOdinEnc() == 0) {
            textBox.setRandomFirstEncounter(dialogueSet);
        } else {
            if (PlayerSave.Save.getOdinWins() == 0) {
                //If getWins() returns 0, that means the most recent game has resulted in a loss
                textBox.setRandomDefeatDialogueSet(dialogueSet);
            } else {
                // When it returns 1, then the player has beaten the boss before
                textBox.setRandomBeatenDialogueSet(dialogueSet);
            }
        }
        PlayerSave.Save.setLoki2Wins(1);
        PlayerSave.Save.setThorWins(0);
        PlayerSave.write();
    }

    /**
     * Use for teleport, track the current map player in
     */
    @Override
    public int getLevel() {
        return 3;
    }
}
