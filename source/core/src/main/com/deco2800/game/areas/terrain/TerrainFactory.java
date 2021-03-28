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
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;

/**
 * Factory for creating game terrains.
 */
public class TerrainFactory {
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  public TerrainFactory(OrthographicCamera camera) {
    this.camera = camera;
    this.orientation = TerrainOrientation.Orthogonal;
  }

  public TerrainFactory(OrthographicCamera camera, TerrainOrientation orientation) {
    this.camera = camera;
    this.orientation = orientation;
  }

  public TerrainComponent createTerrain(TerrainType terrainType) {
    switch (terrainType) {
      case FOREST_DEMO:
        float tileWorldSize = 1f;
        int tilePixelSize = 16;
        Texture texture = new Texture("Overworld.png");
        TextureRegion grass = new TextureRegion(texture, 0, 0, tilePixelSize, tilePixelSize);
        TiledMap tiledMap = makeForestDemoTiledMap(tilePixelSize, grass);
        TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize);
        return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
      case FOREST_DEMO_ISO:
        return createTerrainForestDemoIso();
      default:
        return null;
    }
  }

  private TerrainComponent createTerrainForestDemoIso() {
    float tileWorldSize = 1f;
    int tilePixelSize = 132;
    TextureRegion grass = new TextureAtlas("terrain_iso.atlas").findRegion("landscapeTiles");
    TiledMap tiledMap = makeForestDemoTiledMap(tilePixelSize, grass);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize);
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

  private TiledMap makeForestDemoTiledMap(int tilePixelSize, TextureRegion textureRegion) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile mapTile = new TerrainTile(textureRegion);

    TiledMapTileLayer layer = new TiledMapTileLayer(100, 100, tilePixelSize, tilePixelSize);
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
    FOREST_DEMO, FOREST_DEMO_ISO
  }
}
