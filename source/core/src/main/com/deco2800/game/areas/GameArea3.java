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
import com.deco2800.game.ui.textbox.TextBox;
import com.deco2800.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Override
    public void create() {
        map = FileLoader.readClass(Map.class, "maps/lvl_2.json");
        tileTextures = map.TileRefsArray();

        super.create();
        loadAssets();
        displayUI();

        spawnTerrain();
        spawnPlayer();

        spawnOutdoorArcherObject();
        spawnOutdoorWarriorObject();
        spawnAsgardWarriorObject();
        spawnMovementCutscenes();
        spawnDialogueCutscenes();
        setInitialDialogue();

        spawnOdin();
        spawnObstacles();
        spawnLights();

        spawnSpikeTraps();
        spawnLavaTraps();

        spawnTraps();
        spawnPTraps();
        spawnHealthCrateObject();
        playMusic();
        spawnTeleport();

        player.getComponent(CombatStatsComponent.class).setHealth(playerHealth);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Level 4"));
        spawnEntity(ui);
    }

    private void spawnOdin() {
        HashMap<String, Float>[] bossObjects = map.getBossObjects();
        for (HashMap<String, Float> boss : bossObjects) {
            int x = boss.get("x").intValue();
            int y = boss.get("y").intValue();

            spawnEntityAt(
                    NPCFactory.createOdin(player),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
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

    private void spawnDialogueCutscenes() {
        RandomDialogueSet dialogueSet = RandomDialogueSet.ODIN_ENCOUNTER;
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
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - y),
                    false,
                    false);
        }
    }

    /**
     * Use for teleport, track the current map player in
     */
    @Override
    public int getLevel() {
        return 3;
    }
}