package com.deco2800.game.components.tasks;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 * todo: may need to offset the target by the sprites center size to stop it approaching with the corner
 */
public class ProjectileMovementTask extends MovementTask implements PriorityTask {

  public ProjectileMovementTask(Vector2 target, Vector2 moveSpeed) {
    super(target, moveSpeed);
  }


  //Arrow have sound effect when they disappear
  private void playArrow(){
    Sound arrowEffect = ServiceLocator.getResourceService().getAsset("sounds/arrow_disappear.mp3", Sound.class);
    arrowEffect.play();
  }


  /**
   * Update the arrow position on the screen
   */

  public void update() {
    super.update();
    Vector2 bodyOffset = owner.getEntity().getCenterPosition().cpy().sub(owner.getEntity().getPosition());
    ServiceLocator.getRenderService().getDebug().drawLine(owner.getEntity().getCenterPosition(), target.cpy().add(bodyOffset));
  }

  /**
   * return the priority of the arrow
   * If arrow is in moving, return 10, else return -1 and dispose the arrow
   * @return int 10 if arrow is moving, -1 if arrow is not
   */
  public int getPriority() {
    if (stoppedMoving()) {
      //Arrows disappears when at destination to stop it from looping in the same place
      owner.getEntity().prepareDispose();
      return (-1);
    } else {
      return (10);
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
    //ForestGameArea.playArrow();

    //Arrows disappear when at destination to stop it from looping in the same place

    owner.getEntity().prepareDispose();

  }

  /**
   * check if the arrow is at target or if it stuck by an object (tree)
   * @return true if stop move, false otherwise
   */
  public boolean stoppedMoving() {
    return (isAtTarget() || checkIfStuck());
  }
}
