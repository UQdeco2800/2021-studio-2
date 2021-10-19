package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.files.PlayerSave;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

import java.util.HashMap;

/**
 * Level based on the insides of a Palace with the boss being Odin
 */
public class GameArea3 extends GameArea {
    public GameArea3(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    public GameArea3(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    public GameArea create() {
        playerWeaponType = "Hammer";
        levelInt = 3;
        super.create("maps/lvl_2.json", "Level 4");

        spawnOutdoorArcherObject();
        spawnOutdoorWarriorObject();
        spawnAsgardWarriorObject();
        spawnOdin();

        spawnMovementCutscenes();
        spawnDialogueCutscenes(RandomDialogueSet.ODIN_ENCOUNTER);
        setInitialDialogue();

        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
        return this;
    }

    private void spawnOdin() {
        HashMap<String, Float>[] bossObjects = map.getBossObjects();
        for (HashMap<String, Float> boss : bossObjects) {
            int x = boss.get("x").intValue();
            int y = boss.get("y").intValue();
            incBossNum();
            spawnEntityAt(
                    NPCFactory.createOdin(player),
                    new GridPoint2(x, map.getDimensions().get(TILES_HEIGHT) - y),
                    false,
                    false);
        }
    }

    private void setInitialDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);

        RandomDialogueSet dialogueSet = RandomDialogueSet.ODIN_INTRODUCTION;

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
        PlayerSave.Save.setThorWins(1);
        PlayerSave.Save.setOdinWins(0);
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
