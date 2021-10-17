package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.files.FileLoader;

public class TestGameArea1 extends GameArea {
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
        displayUI("Map Test");

        spawnTerrain();
        spawnPlayer();

        spawnTeleport();
        player.getComponent(CombatStatsComponent.class).setHealth(this.playerHealth);
    }

    @Override
    public int getLevel() {
        return 2;
    }
}
