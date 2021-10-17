package com.deco2800.game.components.tasks;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.services.ServiceLocator;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 */
public class ProjectileMovementTask extends MovementTask implements PriorityTask {

    private int updateAngle = 0;

    private Entity targetEntity;

    public ProjectileMovementTask(Vector2 targetLoc, Vector2 moveSpeed) {
        super(targetLoc, moveSpeed);
    }

    public ProjectileMovementTask(Entity targetEntity, Vector2 moveSpeed) {
        super(targetEntity.getPosition(), moveSpeed);
        this.targetEntity = targetEntity;
    }

    //Arrow have sound effect when they disappear
    private void playArrow() {
        ServiceLocator.getResourceService().getAsset("sounds/arrow_disappear.mp3", Sound.class).play();
    }

    /**
     * Update the arrow position on the screen
     */
    @Override
    public void update() {
        //Change this if statement if there is too much lag
        if (updateAngle > 0) {//UserSettings.get().fps/10) {
            if (targetEntity != null) {
                RaycastHit hit = new RaycastHit();
                if (!ServiceLocator.getPhysicsService().getPhysics().raycast(owner.getEntity().getPosition(), targetEntity.getPosition(), PhysicsLayer.OBSTACLE, hit)) {
                    float turningAngle = 0.4f;//UserSettings.get().fps;
                    Vector2 relativeLocationTarget = target.cpy().sub(owner.getEntity().getPosition());
                    Vector2 relativeLocationEntity = targetEntity.getPosition().cpy().sub(owner.getEntity().getPosition());
                    if (relativeLocationTarget.angleDeg(relativeLocationEntity) > turningAngle && relativeLocationEntity.angleDeg(relativeLocationTarget) > turningAngle) {
                        if (relativeLocationTarget.angleDeg(relativeLocationEntity) > relativeLocationEntity.angleDeg(relativeLocationTarget)) {
                            //left
                            this.target = relativeLocationTarget.rotateAroundDeg(new Vector2(0, 0), turningAngle).setLength(1).add(owner.getEntity().getPosition());
                        } else {
                            //right
                            this.target = relativeLocationTarget.rotateAroundDeg(new Vector2(0, 0), -turningAngle).setLength(1).add(owner.getEntity().getPosition());
                        }
                    } else {
                        this.target = targetEntity.getPosition();
                    }
                } else {
                    ServiceLocator.getRenderService().getDebug().drawLine(owner.getEntity().getPosition(), hit.point, Color.RED, 1);
                }
            }
            //Can be very cpu intensive at times
            owner.getEntity().setAngle(owner.getEntity().getPosition().cpy().sub(target).angleDeg());
            updateAngle = 0;
        }
        updateAngle++;
        super.update();
    }

    /**
     * return the priority of the arrow
     * If arrow is in moving, return 10, else return -1 and dispose the arrow
     *
     * @return int 10 if arrow is moving, -1 if arrow is not
     */
    public int getPriority() {
        if (!owner.getEntity().data.containsKey("fireBallMovement")) {
            if (stoppedMoving()) {
                //Arrows disappears when at destination to stop it from looping in the same place
                owner.getEntity().prepareDispose();
                return (-1);
            } else {
                return (10);
            }
        } else {
            //fireball priority based off entity data
            if (owner.getEntity().data.get("fireBallMovement").equals(false)) {
                return (-1);
            } else if (stoppedMoving()) {
                owner.getEntity().prepareDispose();
                return (-1);
            } else {
                return (10);
            }
        }
    }

    /**
     * stop the arrow movement - dispose the arrow
     */
    @Override
    public void stop() {
        super.stop();
        //Arrows disappears when at destination to stop it from looping in the same place
        playArrow();
//        owner.getEntity().prepareDispose();
    }

    /**
     * check if the arrow is at target or if it stuck by an object (tree)
     *
     * @return true if stop move, false otherwise
     */
    public boolean stoppedMoving() {
        return (isAtTarget() || checkIfStuck());
    }
}
