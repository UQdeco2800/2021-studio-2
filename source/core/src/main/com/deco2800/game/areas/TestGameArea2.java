package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.files.FileLoader;

public class TestGameArea2 extends GameArea {
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
