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
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class TestGameArea1 extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(TutorialGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(20, 370);

    public TestGameArea1(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    public TestGameArea1(TerrainFactory terrainFactory, int currentHealth) {
        super();
        this.terrainFactory = terrainFactory;
        this.playerHealth = currentHealth;
    }

    @Override
    public void create() {
        map = FileLoader.readClass(Map.class, "maps/testMap1.json");
        tileTextures = map.TileRefsArray();

        super.create();
        loadAssets();
        displayUI();

        spawnTerrain();
        spawnPlayer();

        spawnTeleport();
        player.getComponent(CombatStatsComponent.class).setHealth(this.playerHealth);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Map Test"));
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.TEST, map);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);

        //Walls imported from JSON
        HashMap<String, Float>[] walls = map.getWallObjects();
        for (HashMap<String, Float> wall : walls) {
            int x = wall.get("x").intValue();
            int y = wall.get("y").intValue();
            float width = wall.get("width");
            float height = wall.get("height");

            int unitHeight = (int) ((height / 32f));
            spawnEntityAt(
                    ObstacleFactory.createWall((width / 32f) * 0.5f, (height / 32f) * 0.5f),
                    new GridPoint2(x, map.getDimensions().get("n_tiles_height") - (y + unitHeight)),
                    false,
                    false);
        }
    }

    private void spawnTeleport() {
        Entity teleport = ObstacleFactory.createTeleport();
        GridPoint2 fixedPos = new GridPoint2(15, 10);
        spawnEntityAt(teleport, fixedPos, true, true);
        //boss= 1;
    }

    private void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer("Hammer");
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        player = newPlayer;
        player.setPosition(new Vector2(15, 8)); //TESTING FOR TELEPORT
    }

    @Override
    public int getLevel() {
        return 2;
    }
}
