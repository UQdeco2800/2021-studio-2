package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class CameraComponent extends Component {
  private final Camera camera;
  private Vector2 lastPosition;

  public CameraComponent() {
    this(new OrthographicCamera());
  }

  public CameraComponent(Camera camera) {
    this.camera = camera;
    lastPosition = Vector2.Zero.cpy();
  }

  @Override
  public void create() {
    // When the entity moves, update the camera to move too
//    entity.getEvents().addListener("setPosition", this::onPositionChanged);
  }

  @Override
  public void update() {
    Vector2 position = entity.getPosition();
    if (!lastPosition.epsilonEquals(entity.getPosition())) {
      camera.position.set(position, 0f);
    }
    camera.update();
  }

  public Matrix4 getProjectionMatrix() {
    return camera.combined;
  }

  public Camera getCamera() {
    return camera;
  }

  public void resize(int width, int height) {
    float ratio = (float) height / width;
    camera.viewportWidth = width;
    camera.viewportHeight = width * ratio;
    camera.update();
  }

  private void onPositionChanged(Vector2 position) {
    camera.position.set(position, 0f);
  }
}
