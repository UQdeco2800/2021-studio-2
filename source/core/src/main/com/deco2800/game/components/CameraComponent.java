package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;

import java.util.ArrayList;

public class CameraComponent extends Component {
  private final Camera camera;
  private Vector2 lastPosition;
  private Entity player;
  private ArrayList<Vector2> lastPositions;

  public CameraComponent() {
    this(new OrthographicCamera());
  }

  public CameraComponent(Camera camera) {
    this.camera = camera;
    lastPosition = Vector2.Zero.cpy();
    lastPositions = new ArrayList<>();
  }

  @Override
  public void update() {
    Vector2 position;
    if (player == null) {
      position = entity.getPosition();
      if (!lastPosition.epsilonEquals(entity.getPosition())) {
        camera.position.set(position.x, position.y, 0f);
        lastPosition = position;
      }
    } else {
      position = player.getPosition();
      if (!player.getPosition().epsilonEquals(averagePositions())) {
        camera.position.set(averagePositions().x, averagePositions().y, 0f);
        lastPosition = position;
      }
    }
    lastPositions.add(lastPosition);
    if (lastPositions.size() > 50) {
      lastPositions.remove(0);
    }
    camera.update();
  }

  private Vector2 averagePositions() {
    float x = 0, y = 0;
    for (Vector2 vector : lastPositions) {
      x+= vector.x;
      y+= vector.y;
    }
    return new Vector2(x/lastPositions.size(), y/lastPositions.size());
  }

  public Matrix4 getProjectionMatrix() {
    return camera.combined;
  }

  public Camera getCamera() {
    return camera;
  }

  public void resize(int screenWidth, int screenHeight, float gameWidth) {
    float ratio = (float) screenHeight / screenWidth;
    camera.viewportWidth = gameWidth;
    camera.viewportHeight = gameWidth * ratio;
    camera.update();
  }

  public void setPlayer(Entity player) {
    this.player = player;
  }
}
