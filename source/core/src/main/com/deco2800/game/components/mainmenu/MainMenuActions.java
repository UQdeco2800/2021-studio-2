package com.deco2800.game.components.mainmenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private GdxGame game;

  public MainMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::start);
    entity.getEvents().addListener("load", this::load);
    entity.getEvents().addListener("exit", this::exit);
  }

  void start() {
    logger.info("Start game");
    game.setScreen(GdxGame.ScreenType.MainGame);
  }

  void load() {
    logger.info("Load game");
  }

  void exit() {
    logger.info("Exit game");
    game.exit();
  }
}
