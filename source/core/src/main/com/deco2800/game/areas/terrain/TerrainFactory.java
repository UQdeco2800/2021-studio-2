package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Factory for creating game terrains.
 */
public class TerrainFactory {
    public static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);
    private static final int TUFT_TILE_COUNT = 30;
    private static final int ROCK_TILE_COUNT = 30;

    private final OrthographicCamera camera;
    private final TerrainOrientation orientation;

    static Logger logger = LoggerFactory.getLogger(FileLoader.class);

    /**
     * Create a terrain factory with Orthogonal orientation
     *
     * @param cameraComponent Camera to render terrains to. Must be ortographic.
     */
    public TerrainFactory(CameraComponent cameraComponent) {
        this(cameraComponent, TerrainOrientation.ORTHOGONAL);
    }

    /**
     * Create a terrain factory
     *
     * @param cameraComponent Camera to render terrains to. Must be orthographic.
     * @param orientation     orientation to render terrain at
     */
    public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
        this.camera = (OrthographicCamera) cameraComponent.getCamera();
        this.orientation = orientation;
    }

    /**
     * Create a terrain of the given type, using the orientation of the factory. This can be extended
     * to add additional game terrains.
     *
     * @param terrainType Terrain to create
     * @return Terrain component which renders the terrain
     */
    public TerrainComponent createTerrain(TerrainType terrainType) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        switch (terrainType) {
            case FOREST_DEMO:
                TextureRegion orthoGrass =
                        new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
                TextureRegion orthoTuft =
                        new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
                TextureRegion orthoRocks =
                        new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
                return createForestDemoTerrain(0.5f, orthoGrass, orthoTuft, orthoRocks);
            case FOREST_DEMO_ISO:
                TextureRegion isoGrass =
                        new TextureRegion(resourceService.getAsset("images/iso_grass_1.png", Texture.class));
                TextureRegion isoTuft =
                        new TextureRegion(resourceService.getAsset("images/iso_grass_2.png", Texture.class));
                TextureRegion isoRocks =
                        new TextureRegion(resourceService.getAsset("images/iso_grass_3.png", Texture.class));
                return createForestDemoTerrain(1f, isoGrass, isoTuft, isoRocks);
            case FOREST_DEMO_HEX:
                TextureRegion hexGrass =
                        new TextureRegion(resourceService.getAsset("images/hex_grass_1.png", Texture.class));
                TextureRegion hexTuft =
                        new TextureRegion(resourceService.getAsset("images/hex_grass_2.png", Texture.class));
                TextureRegion hexRocks =
                        new TextureRegion(resourceService.getAsset("images/hex_grass_3.png", Texture.class));
                return createForestDemoTerrain(1f, hexGrass, hexTuft, hexRocks);

            default:
                return null;
        }
    }

    /**
     * A version of createTerrain that takes a map object as an input to render a specific map
     *
     * @param terrainType
     * @param map
     * @return
     */
    public TerrainComponent createTerrain(TerrainType terrainType, Map map) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (terrainType == TerrainType.TEST) {
            String[] tileRefs = map.TileRefsArray();
            ArrayList<TextureRegion> textures = new ArrayList<>();

            for (String s : tileRefs) {
                textures.add(new TextureRegion(resourceService.getAsset(s, Texture.class)));
            }

            return createWorldTerrain(textures, map.getMapTiles(), map.getDimensions());
        }
        return null;
    }

    private TerrainComponent createForestDemoTerrain(
            float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
        GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
        TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);
        TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
        return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
    }


    private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
        switch (orientation) {
            case ORTHOGONAL:
                return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
            case ISOMETRIC:
                return new IsometricTiledMapRenderer(tiledMap, tileScale);
            case HEXAGONAL:
                return new HexagonalTiledMapRenderer(tiledMap, tileScale);
            default:
                return null;
        }
    }

    private TiledMap createForestDemoTiles(
            GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
        TiledMap tiledMap = new TiledMap();
        TerrainTile grassTile = new TerrainTile(grass);
        TerrainTile grassTuftTile = new TerrainTile(grassTuft);
        TerrainTile rockTile = new TerrainTile(rocks);
        TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);
        // Create base grass
        fillTiles(layer, grassTile);

        // Add some grass and rocks
        fillTilesAtRandom(layer, grassTuftTile, TUFT_TILE_COUNT);
        fillTilesAtRandom(layer, rockTile, ROCK_TILE_COUNT);

        tiledMap.getLayers().add(layer);
        return tiledMap;
    }

    private TerrainComponent createWorldTerrain(
            ArrayList<TextureRegion> textures, int[][] map, HashMap dimensions) {

        GridPoint2 tilePixelSize = new GridPoint2(textures.get(1).getRegionWidth(), textures.get(1).getRegionHeight());

        TiledMap tiledMap = createTiles(tilePixelSize, textures, map, dimensions);

        TiledMapRenderer renderer = createRenderer(tiledMap, (float) 0.5 / tilePixelSize.x);

        return new TerrainComponent(camera, tiledMap, renderer, orientation, (float) 0.5);
    }

    private TiledMap createTiles(
            GridPoint2 tileSize, ArrayList<TextureRegion> textures, int[][] map, HashMap<String, Integer> dimensions) {
        TiledMap tiledMap = new TiledMap();

        TiledMapTileLayer layer = new TiledMapTileLayer(dimensions.get("n_tiles_width"),
                dimensions.get("n_tiles_height"), tileSize.x, tileSize.y);

        // Create Tiles
        ArrayList<TerrainTile> tiles = new ArrayList<>();
        for (TextureRegion t : textures
        ) {
            tiles.add(new TerrainTile(t));
        }

        // Create the map
        GridPoint2 mapSize = new GridPoint2(dimensions.get("n_tiles_width"), dimensions.get("n_tiles_height"));
        //fillTiles(layer, mapSize, tiles.get(0));

        placeTiles(layer, mapSize, tiles, map);

        tiledMap.getLayers().add(layer);
        return tiledMap;
    }

    private static void placeTiles(
            TiledMapTileLayer layer, GridPoint2 mapSize, ArrayList<TerrainTile> tiles, int[][] map) {
        GridPoint2 min = new GridPoint2(0, 0);
        GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

        for (int y = min.y; y <= max.y; y++) {
            for (int x = min.y; x <= max.x; x++) {
                Cell cell = new Cell();

                cell.setTile(tiles.get(map[y][x] - 1));
                layer.setCell(x, max.y - y, cell);
            }
        }
    }

    private static void fillTilesAtRandom(
            TiledMapTileLayer layer, TerrainTile tile, int amount) {
        GridPoint2 min = new GridPoint2(0, 0);
        GridPoint2 max = new GridPoint2(TerrainFactory.MAP_SIZE.x - 1, TerrainFactory.MAP_SIZE.y - 1);

        for (int i = 0; i < amount; i++) {
            GridPoint2 tilePos = RandomUtils.random(min, max);
            Cell cell = layer.getCell(tilePos.x, tilePos.y);
            cell.setTile(tile);
        }
    }

    private static void fillTiles(TiledMapTileLayer layer, TerrainTile tile) {
        for (int x = 0; x < TerrainFactory.MAP_SIZE.x; x++) {
            for (int y = 0; y < TerrainFactory.MAP_SIZE.y; y++) {
                Cell cell = new Cell();
                cell.setTile(tile);
                layer.setCell(x, y, cell);
            }
        }
    }

    /**
     * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
     * the same oerientation. But for demonstration purposes, the base code has the same level in 3
     * different orientations.
     */
    public enum TerrainType {
        FOREST_DEMO,
        FOREST_DEMO_ISO,
        FOREST_DEMO_HEX,
        TEST
    }
}