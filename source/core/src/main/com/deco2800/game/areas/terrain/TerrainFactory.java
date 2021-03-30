package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.math.RandomUtils;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param camera Camera to render terrains to
   */
  public TerrainFactory(OrthographicCamera camera) {
    this.camera = camera;
    this.orientation = TerrainOrientation.Orthogonal;
  }

  /**
   * Create a terrain factory
   *
   * @param camera Camera to render terrains to
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(OrthographicCamera camera, TerrainOrientation orientation) {
    this.camera = camera;
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
    switch (terrainType) {
      case FOREST_DEMO:
        TextureRegion orthoGrass = new TextureRegion(new Texture("grass_1.png"));
        TextureRegion orthoTuft = new TextureRegion(new Texture("grass_2.png"));
        TextureRegion orthoRocks = new TextureRegion(new Texture("grass_3.png"));
        return createForestDemoTerrain(0.5f, orthoGrass, orthoTuft, orthoRocks);
      case FOREST_DEMO_ISO:
        TextureRegion isoGrass = new TextureAtlas("terrain_iso_grass.atlas").findRegion("grass");
        return createForestDemoTerrain(1f, isoGrass, isoGrass, isoGrass);
      case FOREST_DEMO_HEX:
        Texture hexGrassTex = new Texture("terrain_hex.png");
        TextureRegion hexGrass = new TextureRegion(hexGrassTex, 224, 66, 32, 32);
        return createForestDemoTerrain(1f, hexGrass, hexGrass, hexGrass);
      default:
        return null;
    }
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
      case Orthogonal:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case Isometric:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case Hexagonal:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    TiledMap tiledMap = new TiledMap();
    GridPoint2 mapSize = new GridPoint2(20, 20);
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
    TiledMapTileLayer layer = new TiledMapTileLayer(mapSize.x, mapSize.y, tileSize.x, tileSize.y);

    // Create base grass
    for (int x = 0; x < 20; x++) {
      for (int y = 0; y < 20; y++) {
        Cell cell = new Cell();
        cell.setTile(grassTile);
        layer.setCell(x, y, cell);
      }
    }

    // Add some tufts
    for (int i = 0; i < 20; i++) {
      GridPoint2 tilePos = RandomUtils.random(new GridPoint2(0, 0), new GridPoint2(19, 19));
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(grassTuftTile);
    }
    // Add some rocks
    for (int i = 0; i < 20; i++) {
      GridPoint2 tilePos = RandomUtils.random(new GridPoint2(0, 0), new GridPoint2(19, 19));
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(rockTile);
    }

    tiledMap.getLayers().add(layer);

    return tiledMap;
  }

  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX
  }
}
