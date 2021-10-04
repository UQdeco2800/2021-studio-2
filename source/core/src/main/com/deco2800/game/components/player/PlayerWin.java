package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;

public class PlayerWin extends Component {
    private boolean hasWin = false;

    @Override
    public void create() {
        super.create();
        this.entity.getEvents().addListener("gameWin", this::triggerWin);
    }

    public boolean getHasWin() {
        return hasWin;
    }

    private void triggerWin() {
        hasWin = true;
    }
}
