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
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Factory for creating game terrains.
 */
public class TerrainFactory {
    public static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);

    private final OrthographicCamera camera;
    private final TerrainOrientation orientation;
    protected static final String TILES_HEIGHT = "n_tiles_height";
    protected static final String TILES_WIDTH = "n_tiles_width";

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
     * A version of createTerrain that takes a map object as an input to render a specific map
     *
     * @param map Map of tiles
     * @return TerrainComponent
     */
    public TerrainComponent createTerrain(Map map) {
        ResourceService resourceService = ServiceLocator.getResourceService();
        String[] tileRefs = map.tileRefsArray();
        ArrayList<TextureRegion> textures = new ArrayList<>();

        for (String s : tileRefs) {
            textures.add(new TextureRegion(resourceService.getAsset(s, Texture.class)));
        }

        return createWorldTerrain(textures, map.getMapTiles(), map.getDimensions());
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

    private TerrainComponent createWorldTerrain(
            ArrayList<TextureRegion> textures, int[][] map, HashMap<String, Integer> dimensions) {

        GridPoint2 tilePixelSize = new GridPoint2(textures.get(1).getRegionWidth(), textures.get(1).getRegionHeight());

        TiledMap tiledMap = createTiles(tilePixelSize, textures, map, dimensions);

        TiledMapRenderer renderer = createRenderer(tiledMap, (float) 0.5 / tilePixelSize.x);

        return new TerrainComponent(camera, tiledMap, renderer, orientation, (float) 0.5);
    }

    private TiledMap createTiles(
            GridPoint2 tileSize, ArrayList<TextureRegion> textures, int[][] map, HashMap<String, Integer> dimensions) {
        TiledMap tiledMap = new TiledMap();

        TiledMapTileLayer layer = new TiledMapTileLayer(dimensions.get(TILES_WIDTH),
                dimensions.get(TILES_HEIGHT), tileSize.x, tileSize.y);

        // Create Tiles
        ArrayList<TerrainTile> tiles = new ArrayList<>();
        for (TextureRegion t : textures
        ) {
            tiles.add(new TerrainTile(t));
        }

        // Create the map
        GridPoint2 mapSize = new GridPoint2(dimensions.get(TILES_WIDTH), dimensions.get(TILES_HEIGHT));

        placeTiles(layer, mapSize, tiles, map);

        tiledMap.getLayers().add(layer);
        return tiledMap;
    }

    private static void placeTiles(
            TiledMapTileLayer layer, GridPoint2 mapSize, ArrayList<TerrainTile> tiles, int[][] map) {
        GridPoint2 min = new GridPoint2(0, 0);
        GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

        for (int y = min.y; y <= max.y; y++) {
            for (@SuppressWarnings("SuspiciousNameCombination") int x = min.y; x <= max.x; x++) {
                Cell cell = new Cell();

                cell.setTile(tiles.get(map[y][x] - 1));
                layer.setCell(x, max.y - y, cell);
            }
        }
    }
}
