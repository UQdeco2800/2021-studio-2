package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;

import java.util.ArrayList;

public class CameraComponent extends Component {
    private final Camera camera;
    private Entity player;
    private final ArrayList<Vector2> lastPositions;
    private Vector2 lastPosition;

    public CameraComponent() {
        this(new OrthographicCamera());
    }

    public CameraComponent(Camera camera) {
        this.camera = camera;
        this.camera.position.set(4.75f, 4.75f, 0f);
        lastPosition = new Vector2(4.75f, 4.75f);
        lastPositions = new ArrayList<>();
    }

    /**
     * Updates the position of the camera to a point within the map. If the camera is already
     * a player, it will attempt to change the position of the camera to match the player position.
     * <p>
     * To smooth the camera movement, the current camera position is an average of the last positions
     * the main character was.
     */
    @Override
    public void update() {
        Vector2 position;
        if (player == null) {
            camera.position.set(4.75f, 4.75f, 0f);
        } else {
            position = player.getPosition();
            if (!player.getPosition().epsilonEquals(averagePositions())) {
                camera.position.set(averagePositions().x, averagePositions().y, 0f);
                lastPosition = position;
            }
            lastPositions.add(lastPosition);
            if (lastPositions.size() > 50) {
                lastPositions.remove(0);
            }
        }
        camera.update();
    }

    /**
     * Averages the last 50 positions the player was before and returns the average.
     * This is used to smooth the camera movement.
     *
     * @return average of the last 50 positions of the camera
     */
    private Vector2 averagePositions() {
        float x = 0;
        float y = 0;
        for (Vector2 vector : lastPositions) {
            x += vector.x;
            y += vector.y;
        }
        return new Vector2(x / lastPositions.size(), y / lastPositions.size());
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

    /**
     * Sets the player entity the camera will track.
     *
     * @param player entity that is controlled by the user
     */
    public void setPlayer(Entity player) {
        this.player = player;
    }
}
