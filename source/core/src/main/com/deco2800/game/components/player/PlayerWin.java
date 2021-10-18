package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerWin extends UIComponent {
    private boolean hasWin = false;
    private Image whiteScreen;

    @Override
    public void create() {
        super.create();
        addActors();
        this.entity.getEvents().addListener("gameWin", this::triggerWin);
        stage.addActor(whiteScreen);
    }

    private void addActors() {
        whiteScreen = new Image(ServiceLocator.getResourceService().getAsset("images/textBoxDisplay" +
                        "/black_bars.png",
                Texture.class));
        // setup
        whiteScreen.setScaling(Scaling.stretch);
        whiteScreen.setVisible(false);
        whiteScreen.setFillParent(true);
        whiteScreen.setSize(1920, 1080);
        whiteScreen.setColor(0, 0, 0, 0);
        whiteScreen.setTouchable(Touchable.disabled);
    }

    private void winFade(float opacity) {
        if (opacity < 1) {
            whiteScreen.setColor(0, 0, 0, opacity);
            java.util.Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    winFade(opacity + 0.05f);
                    timer.cancel();
                }
            }, 30);
        } else {
            whiteScreen.setColor(0, 0, 0, 1);
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    public boolean getHasWin() {
        return hasWin;
    }

    public float getZIndex() {
        return 10f;
    }

    private void triggerWin() {
        hasWin = true;
        whiteScreen.setVisible(true);
        winFade(0);
    }

    @Override
    public void dispose() {
        super.dispose();
        whiteScreen.remove();
    }

}
