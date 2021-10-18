package com.deco2800.game.components.endGame;

import com.deco2800.game.components.Component;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class EndScreenAnimationController extends Component {
    AnimationRenderComponent endAnimation;

    @Override
    public void create() {
        super.create();
        endAnimation = this.entity.getComponent(AnimationRenderComponent.class);
        addEvents();
    }

    public void addEvents() {
        EventHandler crateHandler = this.entity.getEvents();
        crateHandler.addListener("advanceFrame", this::advanceFrame);
        crateHandler.addListener("startEndScreen", this::startFrame);
        crateHandler.addListener("endFrame", this::endFrame);
    }

    /**
     * selects the frame to play based on the frame number. this allows multiple frames or no
     * need to change .atlas file names
     *
     * @param frameNum the number of the frame
     */
    public void advanceFrame(String frameNum) {
        String frame = "frame" + frameNum;
        endAnimation.startAnimation(frame);
    }

    public void startFrame() {
        endAnimation.startAnimation("startFrame");
    }

    public void endFrame() {
        endAnimation.startAnimation("ending");
    }
}
