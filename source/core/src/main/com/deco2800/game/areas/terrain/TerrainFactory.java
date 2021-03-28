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
        Texture orthoTexture = new Texture("terrain_ortho.png");
        TextureRegion orthoGrass = new TextureRegion(orthoTexture, 0, 0, 16, 16);
        return createForestDemoTerrain(1f, new GridPoint2(16, 16), orthoGrass);
      case FOREST_DEMO_ISO:
        TextureRegion isoGrass = new TextureAtlas("terrain_iso_grass.atlas").findRegion("grass");
        return createForestDemoTerrain(1f, new GridPoint2(132, 132), isoGrass);
      case FOREST_DEMO_HEX:
        Texture hexGrassTex = new Texture("terrain_hex.png");
        TextureRegion hexGrass = new TextureRegion(hexGrassTex, 224, 66, 32, 32);
        return createForestDemoTerrain(1f, new GridPoint2(32, 32), hexGrass);
      default:
        return null;
    }
  }

  private TerrainComponent createForestDemoTerrain(
      float tileWorldSize, GridPoint2 tilePixelSize, TextureRegion tex) {
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, tex);
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

  private TiledMap createForestDemoTiles(GridPoint2 tileSize, TextureRegion textureRegion) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile mapTile = new TerrainTile(textureRegion);

    TiledMapTileLayer layer = new TiledMapTileLayer(20, 20, tileSize.x, tileSize.y);
    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        Cell cell = new Cell();
        cell.setTile(mapTile);
        layer.setCell(x, y, cell);
      }
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
