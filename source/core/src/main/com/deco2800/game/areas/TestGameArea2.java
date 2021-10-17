package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
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

public class TestGameArea2 extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(TutorialGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(20, 370);
    private static Map map;

    public TestGameArea2(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    @Override
    public void create() {
        map = FileLoader.readClass(Map.class, "maps/testMap2.json");
        tileTextures = map.TileRefsArray();

        super.create();
        loadAssets();
        displayUI("Map Test");

        spawnTerrain();
        spawnPlayer();

        spawnTeleport();
    }

    @Override
    public int getLevel() {
        return 3;
    }
}
